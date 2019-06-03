package com.evolution.network;

import com.evolution.network.packet.AIPacket;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class ServerNetworkManager extends SimpleChannelInboundHandler< AIPacket >
{
  public static final LazyLoadBase< NioEventLoopGroup > CLIENT_NIO_EVENTLOOP = new LazyLoadBase<>( () ->
  {
    return new NioEventLoopGroup( 0, ( new ThreadFactoryBuilder() ).setNameFormat( "AI Client IO #%d" ).setDaemon( true ).build() );
  } );
  public static final LazyLoadBase< EpollEventLoopGroup > CLIENT_EPOLL_EVENTLOOP = new LazyLoadBase<>( () ->
  {
    return new EpollEventLoopGroup( 0, ( new ThreadFactoryBuilder() ).setNameFormat( "AI Client IO #%d" ).setDaemon( true ).build() );
  } );

  private Channel channel;

  private boolean socketConnected = false;
  public boolean isConnected = false;

  /**
   * Creates a new connection to the server returning the NetworkManager that has been created.
   * You can then use this to send and receive packets from the server. The function blocks
   * until the client is connected to the server socket.
   *
   * @param address - The network address of the server to connect too
   * @param port - The port of the server
   * @return
   */
  public static ServerNetworkManager createClientConnection( String address, int port )
  {
    ServerNetworkManager manager = new ServerNetworkManager();

    Class< ? extends SocketChannel > sClass;
    LazyLoadBase< ? extends EventLoopGroup > lazyloadbase;
    if ( Epoll.isAvailable() )
    {
      sClass = EpollSocketChannel.class;
      lazyloadbase = CLIENT_EPOLL_EVENTLOOP;
    }
    else
    {
      sClass = NioSocketChannel.class;
      lazyloadbase = CLIENT_NIO_EVENTLOOP;
    }

    ( new Bootstrap() ).group( lazyloadbase.getValue() ).handler( new ChannelInitializer< Channel >()
    {
      @Override
      protected void initChannel( Channel p_initChannel_1_ ) throws Exception
      {
        try
        {
          p_initChannel_1_.config().setOption( ChannelOption.TCP_NODELAY, true );
        }
        catch ( ChannelException var3 )
        {
          ;
        }

        p_initChannel_1_.pipeline()
            .addLast( "timeout", new ReadTimeoutHandler( 30 ) )
            .addLast( "splitter", new FrameDecoder() )
            .addLast( "decoder", new PacketDecoder() )
            .addLast( "prepender", new FrameEncoder() )
            .addLast( "encoder", new PacketEncoder() )
            .addLast( "packet_handler", manager );
      }
    } ).channel( sClass ).connect( address, port ).syncUninterruptibly();
    while ( !manager.socketConnected )
    {
      try
      {
        Thread.sleep( 100 );
      }
      catch ( InterruptedException e )
      {
        e.printStackTrace();
      }
    }
    return manager;
  }

  @Override
  protected void channelRead0( ChannelHandlerContext ctx, AIPacket msg ) throws Exception
  {
    MainThreadPacketHandler.handlePacket( msg );
  }

  /**
   * Sends a packet to the server
   *
   * @param packetIn - The packet to send
   */
  public void sendPacket( AIPacket packetIn )
  {
    if ( this.channel.eventLoop().inEventLoop() )
    {

      ChannelFuture channelfuture = this.channel.writeAndFlush( packetIn );
      channelfuture.addListener( ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE );
    }
    else
    {
      this.channel.eventLoop().execute( () ->
      {
        ChannelFuture channelfuture1 = this.channel.writeAndFlush( packetIn );
        channelfuture1.addListener( ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE );
      } );
    }
  }

  @Override
  public void channelActive( ChannelHandlerContext p_channelActive_1_ ) throws Exception
  {
    super.channelActive( p_channelActive_1_ );
    this.channel = p_channelActive_1_.channel();
    this.socketConnected = true;
  }
}
