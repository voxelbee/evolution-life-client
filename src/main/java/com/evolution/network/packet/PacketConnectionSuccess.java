package com.evolution.network.packet;

import com.evolution.Main;

import io.netty.buffer.ByteBuf;

public class PacketConnectionSuccess implements AIPacket
{

  @Override
  public void readPacket( ByteBuf buf )
  {

  }

  @Override
  public void writePacket( ByteBuf buf )
  {

  }

  @Override
  public void handlePacket()
  {
    Main.client.handleConnectionSuccess( this );
  }
}
