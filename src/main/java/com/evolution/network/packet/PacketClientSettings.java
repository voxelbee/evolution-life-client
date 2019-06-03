package com.evolution.network.packet;

import com.evolution.network.BufferUtils;

import io.netty.buffer.ByteBuf;

public class PacketClientSettings implements AIPacket
{
  public int entitySimulationCount;

  public PacketClientSettings( int inEntitySimulationCount )
  {
    this.entitySimulationCount = inEntitySimulationCount;
  }

  public PacketClientSettings()
  {

  }

  @Override
  public void readPacket( ByteBuf buf )
  {
    this.entitySimulationCount = BufferUtils.readVarInt( buf );
  }

  @Override
  public void writePacket( ByteBuf buf )
  {
    BufferUtils.writeVarInt( buf, this.entitySimulationCount );
  }

  @Override
  public void handlePacket()
  {

  }
}
