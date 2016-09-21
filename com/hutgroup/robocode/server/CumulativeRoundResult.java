package com.hutgroup.robocode.server;

public class CumulativeRoundResult extends RoundResult
{
    /**
     * Represents the cumulative results of a round (cumulative energy, ram damage ..) 
     * across all bots particapting in the round.
     */

    public CumulativeRoundResult(double energyLeft, double ramDamage, double gunDamage)
    {
	super(0, "", energyLeft,  ramDamage,  gunDamage);
    }

}
