package com.hutgroup.robocode.server;

import java.util.*;
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

    public Map<String, RobotSpecification> getCompetitorsMap()
    {
	Map<String, RobotSpecification> result = new HashMap<String, RobotSpecification>();
	
	for(RobotSpecification spec : competitors) { result.put(spec.getName(), spec); }

	return result;

    }

    public int                  getBattleId()            { return battleId;    }
    public RobotSpecification[] getCompetitors()         { return competitors; }
    public BattleSpecification  getBattleSpecification() { return bSpec;       }

}
