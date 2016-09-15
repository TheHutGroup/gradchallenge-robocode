package com.hutgroup.robocode.server;

import java.util.*;
import robocode.control.RobotSpecification;
import robocode.control.RobocodeEngine;

public class Main
{

    public static void runner(String[]jars, int groupMaxBound, int groupMinBound, int minGroupSize, int maxGroupSize, int width, int height, int numRounds)
    {

	THGEngine                      engine  = new THGEngine("jdbc:mysql://localhost:3306/robocode", "root", "");
	RobotSpecification[]           specs   = Utils.mapToSpecs(engine, jars);
	List<List<RobotSpecification>> groups  = Utils.bracket(specs, groupMaxBound, groupMinBound, minGroupSize, maxGroupSize);
	List<Battle>                   battles = Utils.mapToBattle(groups, width, height, numRounds);
	List<RobotSpecification>       winners = new ArrayList<RobotSpecification>();
	
	for(Battle b : battles)	{ winners.add(engine.runBattle(b)); }

	engine.displayBattleScreen(winners);

	Battle             finalBattle = Utils.toBattle(battles.size()+1, winners, width, height, numRounds);
        RobotSpecification champion    = engine.runBattle(finalBattle);

	engine.displayWinner(champion);

    }


}
