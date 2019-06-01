package com.evolution;

import java.io.IOException;

import com.evolution.network.ClientManager;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class EVOClient
{
  public static void main( String[] args ) throws IOException
  {
    ClientManager client = new ClientManager( "localhost", 5000 );

    ByteBuf buf = Unpooled.directBuffer( 12 );
    buf.writeInt( 0 );
    buf.writeInt( 30 );
    client.sendBytes( buf );

    client.close();
  }
}
