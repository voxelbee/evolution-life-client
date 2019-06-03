package com.evolution;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.evolution.ai.AI;
import com.evolution.network.ServerNetworkManager;
import com.evolution.network.packet.PacketConnectionSuccess;
import com.evolution.network.packet.PacketReciveEntities;
import com.evolution.network.packet.PacketRequestEntities;

public class Client
{
  public ServerNetworkManager networkManager;

  public Map< UUID, AI > ais = new HashMap< UUID, AI >();

  public Client()
  {
    this.networkManager = ServerNetworkManager.createClientConnection( Main.ADDRESS, Main.PORT );
  }

  public void handleConnectionSuccess( PacketConnectionSuccess packet )
  {
    System.out.println( "Connected to server..." );
    this.networkManager.sendPacket( new PacketRequestEntities( 10 ) );
  }

  public void handleReciveEntities( PacketReciveEntities packet )
  {
    for ( UUID id : packet.entityIds )
    {
      ais.put( id, new AI( null ) );
    }
  }

  public boolean isConnected()
  {
    return this.networkManager.isConnected;
  }
}
