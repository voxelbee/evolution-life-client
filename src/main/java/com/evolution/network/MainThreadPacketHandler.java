package com.evolution.network;

import java.util.PriorityQueue;
import java.util.Queue;

import com.evolution.network.packet.AIPacket;

public class MainThreadPacketHandler
{
  private static Queue< AIPacket > packetQueue = new PriorityQueue< AIPacket >();
  private static int maxPacketProcessing = 2000;

  public static void handlePacket( AIPacket packet )
  {
    packetQueue.add( packet );
  }

  public static void tick()
  {
    for ( int i = 0; i < maxPacketProcessing; i++ )
    {
      AIPacket packet = packetQueue.poll();
      if ( packet != null )
      {
        packet.handlePacket();
      }
      else
      {
        break;
      }
    }
  }
}
