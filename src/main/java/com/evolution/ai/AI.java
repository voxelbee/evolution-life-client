package com.evolution.ai;

public class AI implements Runnable
{
  public byte[] dna;

  public AI( byte[] dna )
  {
    this.dna = dna;
    System.out.println( "New AI" );
  }

  @Override
  public void run()
  {

  }

  public void stop()
  {
    System.out.println( "Stopped AI" );
  }
}
