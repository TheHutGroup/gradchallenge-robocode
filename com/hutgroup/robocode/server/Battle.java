package com.hutgroup.robocode.server;

import java.util.List;
import robocode.control.*;


public class Battle
{
    /**
     * Wrapper around a battle information.
     */

    RobotSpecification[]     competitors;
    BattlefieldSpecification spec;
    BattleSpecification      bSpec;
    int                      battleId;

    public Battle(int battleId, RobotSpecification[] competitors, int width, int height, int numRounds){
	this.battleId    = battleId;
	this.competitors = competitors;
	this.spec        = new BattlefieldSpecification(width, height);
	this.bSpec       = new BattleSpecification(numRounds, spec, competitors);
    }

    public int                  getBattleId()            { return battleId;    }
    public RobotSpecification[] getCompetitors()         { return competitors; }
    public BattleSpecification  getBattleSpecification() { return bSpec;       }

}
