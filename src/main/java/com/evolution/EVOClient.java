package com.evolution;

import java.io.IOException;

import com.evolution.network.ClientManager;
import com.evolution.network.packet.ClientCountPacket;

public class EVOClient
{
  public static ClientManager client;

  public static void main( String[] args ) throws IOException, InterruptedException
  {
    client = new ClientManager( "localhost", 5000 );
    client.sendPacket( new ClientCountPacket( 10 ) );

    while ( true )
    {
      Thread.sleep( 100 );
    }
  }
}
