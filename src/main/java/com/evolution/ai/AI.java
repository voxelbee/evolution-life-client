package com.evolution.ai;

public class AI implements Runnable
{
  public byte[] dna;

  public AI( byte[] dna )
  {
    this.dna = dna;
    System.out.println( new String( this.dna ) );
  }

  @Override
  public void run()
  {

  }
}
