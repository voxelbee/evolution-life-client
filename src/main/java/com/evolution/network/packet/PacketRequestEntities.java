package com.evolution.network.packet;

import com.evolution.network.BufferUtils;

import io.netty.buffer.ByteBuf;

public class PacketRequestEntities implements AIPacket
{
  public int numberOfEntities;

  public PacketRequestEntities()
  {

  }

  public PacketRequestEntities( int inNum )
  {
    this.numberOfEntities = inNum;
  }

  @Override
  public void readPacket( ByteBuf buf )
  {
    this.numberOfEntities = BufferUtils.readVarInt( buf );
  }

  @Override
  public void writePacket( ByteBuf buf )
  {
    BufferUtils.writeVarInt( buf, this.numberOfEntities );
  }

  @Override
  public void handlePacket()
  {

  }
}
