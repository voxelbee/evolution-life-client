package com.evolution.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import io.netty.buffer.ByteBuf;

public class ClientManager
{
  private Socket socket;
  private boolean isListening;

  private InputStream in;
  private OutputStream out;

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

    this.handleInPacket( buffer );
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

  private void handleInPacket( byte[] buf )
  {

  }

  /**
   * Sends a byte array to the server
   *
   * @param buffer
   */
  public void sendBytes( ByteBuf buffer )
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
}
