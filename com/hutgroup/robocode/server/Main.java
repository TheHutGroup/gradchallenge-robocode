package com.hutgroup.robocode.server;

import java.io.*;
import java.util.*;
import robocode.control.RobotSpecification;
import robocode.control.RobocodeEngine;

public class Main
{
    /**
     * Main interface to the THGEngine
     */

    private static final String JDBC_CONN_STRING = "jdbc:mysql://localhost:3306/robocode";
    public  static final String ROBOCODE_HOME    = "C:/robocode";
    private static final String DB_USERNAME      = "root";
    private static final String DB_PASSWORD      = "";

    public static void main(String []args)
    {
	Scanner  sc    = new Scanner(System.in);
	int      n     = sc.nextInt();
	String[] jars  = new String[n];

	for(int i = 0; i < n; i++){  jars[i] = sc.next(); }

	runner(jars, 10, 2, 5, 15, 500, 500, 1);

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

	THGEngine                      engine  = new THGEngine(new File(ROBOCODE_HOME), JDBC_CONN_STRING, DB_USERNAME, DB_PASSWORD);
	RobotSpecification[]           specs   = Utils.mapToSpecs(engine, jars);
	assert specs.length == jars.length: String.format("Number of jars loaded != number requested: Loaded: %d, Expected: %d", specs.length, jars.length);
	
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
