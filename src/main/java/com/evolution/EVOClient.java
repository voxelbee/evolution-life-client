package com.evolution;

import java.io.IOException;

import com.evolution.network.ClientManager;
import com.evolution.network.packet.ClientCountPacket;

public class EVOClient
{
  public static void main( String[] args ) throws IOException
  {
    ClientManager client = new ClientManager( "localhost", 5000 );
    client.sendPacket( new ClientCountPacket( 70 ) );
    client.close();
  }
}
