package com.hutgroup.robocode.server;

public class RoundResult
{

    /**
     * Wrapper for information around the result of a round
     */

    double energyLeft;
    double ramDamage;
    double gunDamage;
    int    playerId;
    int    roundId;

    public RoundResult(int roundId, int playerId, double energyLeft, double ramDamage, double gunDamage)
    {
	this.energyLeft = energyLeft;
	this.ramDamage  = ramDamage;
	this.gunDamage  = gunDamage;
	this.playerId   = playerId;
	this.roundId    = roundId;
    }
    

    public double getEnergyLeft() { return energyLeft; }
    public double getRamDamage () { return ramDamage;  }
    public double getGunDamage () { return gunDamage;  }
    public int    getPlayerId  () { return playerId;   }

}
