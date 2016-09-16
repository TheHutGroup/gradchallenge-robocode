package com.hutgroup.robocode.server;

import java.util.*;
import robocode.control.RobotSpecification;
import robocode.control.RobocodeEngine;

public class Main
{
    /**
     * Main interface to the THGEngine
     */

    public static void main(String []args)
    {
	Scanner sc = new Scanner(System.in);
	int n = sc.nextInt();
	String jars [] = new String[n];
	for(int i = 0; i < n; i++){
	    jars[i] = sc.next();
	}
	runner(jars, 10, 2, 5, 15, 500, 500, 100);
    }


    public static void runner(String[]jars,
			      int groupMaxBound,
			      int groupMinBound,
			      int minGroupSize,
			      int maxGroupSize,
			      int width,
			      int height,
			      int numRounds)
    {
	/**
	 * Runs the engine on the given jars.
	 *
	 * @param jars: Array of jar names relative to '<robocode home>/robots'
	 * @param groupMaxBound, groupMinBound: Bounds for the number of brackets in the knockout round
	 * @param minGroupSize, maxGroupSize: Bounds for the size of a bracket in the knockout round
	 * @param width, height, numRounds: Parameters for the battles
	 */

	assert groupMinBound <= groupMaxBound;
	assert minGroupSize <= maxGroupSize : String.format("Min: %d, Max: %d", minGroupSize, maxGroupSize);
	assert jars.length >= groupMinBound*minGroupSize;
	assert jars.length <= groupMaxBound*maxGroupSize;
	assert width >= 400 && width <= 5000;
	assert height >= 400 && height <= 5000;
	assert numRounds <= 1000;

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
