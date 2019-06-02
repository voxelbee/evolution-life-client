package com.evolution.network.packet;

import io.netty.buffer.ByteBuf;

public class ClientCountPacket implements AIPacket
{
  public int AICount;
  public static final int ID = 0;

  public ClientCountPacket( int count )
  {
    this.AICount = count;
  }

  @Override
  public void readPacket( ByteBuf buf )
  {
    this.AICount = buf.readInt();
  }

  @Override
  public void writePacket( ByteBuf buf )
  {
    buf.writeInt( this.AICount );
  }

  @Override
  public void handlePacket()
  {

  }
}