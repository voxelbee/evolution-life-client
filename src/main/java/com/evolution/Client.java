package com.evolution;

import com.evolution.network.ServerNetworkManager;
import com.evolution.network.packet.PacketClientSettings;
import com.evolution.network.packet.PacketConnectionSuccess;

public class Client
{
  public ServerNetworkManager networkManager;

  public Client()
  {
    this.networkManager = ServerNetworkManager.createClientConnection( Main.ADDRESS, Main.PORT );
  }

  public void handleConnectionSuccess( PacketConnectionSuccess packet )
  {
    System.out.println( "Connected to server..." );
    this.networkManager.sendPacket( new PacketClientSettings( 20 ) );
  }

  public boolean isConnected()
  {
    return this.networkManager.isConnected;
  }
}
