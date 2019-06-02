package com.evolution.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.evolution.ai.AI;
import com.evolution.network.packet.AIPacket;
import com.evolution.network.packet.EnumPacketTypes;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ClientManager
{
  private Socket socket;
  private boolean isListening;

  private InputStream in;
  private OutputStream out;

  private Map< UUID, AI > ais = new HashMap< UUID, AI >();

  public ClientManager( String ipAddress, int port ) throws UnknownHostException, IOException
  {
    this.socket = new Socket( ipAddress, port );

    this.in = this.socket.getInputStream();
    this.out = this.socket.getOutputStream();

    this.isListening = true;

    ( new Thread( new Runnable()
    {

      @Override
      public void run()
      {
        while ( isListening )
        {
          try
          {
            listen();
          }
          catch ( IOException e )
          {
            e.printStackTrace();
            isListening = false;
          }
        }
      }
    } ) ).start();
  }

  private void listen() throws IOException
  {
    byte[] number = new byte[ 4 ];
    this.in.read( number );
    int dataSize = Utils.byteArrayToInt( number, 0 );

    byte[] buffer = new byte[ dataSize ];
    this.in.read( buffer );

    ByteBuf buf = Unpooled.wrappedBuffer( buffer );

    AIPacket packet = EnumPacketTypes.PacketTypes.getPacketFromID( buf.readInt() );
    packet.readPacket( buf );
    packet.handlePacket();
  }

  public void close()
  {
    this.isListening = false;

    // Close the socket
    try
    {
      socket.close();
    }
    catch ( IOException e )
    {
      e.printStackTrace();
    }
  }

  /**
   * Sends a byte array to the server
   *
   * @param buffer
   */
  private void sendBytes( ByteBuf buffer )
  {
    try
    {
      this.out.write( buffer.readableBytes() );
      buffer.getBytes( 0, this.out, buffer.readableBytes() );
      this.out.flush();
    }
    catch ( IOException e )
    {
      e.printStackTrace();
    }
  }

  /**
   * Sends the packet to the server
   *
   * @param packet
   */
  public void sendPacket( AIPacket packet )
  {
    ByteBuf buf = Unpooled.directBuffer( 8 );
    buf.writeInt( EnumPacketTypes.PacketTypes.getIdFromPacket( packet ) ); // Writes the id to the buffer
    packet.writePacket( buf );
    this.sendBytes( buf );
    buf.release();
  }

  public void handleDNAPacket( byte[] dna, UUID aiId )
  {
    this.ais.put( aiId, new AI( dna ) );
  }
}
