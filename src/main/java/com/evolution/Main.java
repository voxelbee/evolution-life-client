package com.evolution;

import java.io.IOException;

import com.evolution.network.MainThreadPacketHandler;

public class Main
{
  public static Client client;

  public static final String ADDRESS = "localhost";
  public static final int PORT = 5000;

  public static void main( String[] args ) throws IOException, InterruptedException
  {
    client = new Client();
    while ( !client.isConnected() )
    {
      MainThreadPacketHandler.tick();
      Thread.sleep( 10 );
    }

    while ( client.isConnected() )
    {
      MainThreadPacketHandler.tick();
      Thread.sleep( 10 );
    }
  }
}
