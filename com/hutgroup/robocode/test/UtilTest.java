package com.hutgroup.robocode.test;

import java.util.*;
import com.hutgroup.robocode.utils.*;
import com.hutgroup.robocode.server.*;
import robocode.control.RobotSpecification;
import robocode.control.*;

public class UtilTest {


    public static void main(String []args){

	int numCompetitors = 10;
	int numRounds = 100;

	if(args.length == 2)
	{
	    numRounds = Integer.parseInt(args[0]);
	    numCompetitors = Integer.parseInt(args[1]);
	}

	runTests(numRounds, numCompetitors);

    }

    private static RobotSpecification roboSpec(int i){
	String id = i + "";
	return RobotSpecificationTest.createRobotSpecification(null,
							   id,
							   id,
							   "",
							   "",
							   "",
							   "josh.jar",
							   "josh.Corners",
							   "");
    }

    

    public static RobotSpecification[] initCompetitors(int n){
	RobotSpecification [] rs = new RobotSpecification[n];
	for(int i = 0; i < n; i++){
	    rs[i] = roboSpec(i);
	}
	return rs;
    }

    private static RoundResult robotResult(int round, int robot){
	double e = Math.random()*100;
	double r = Math.random()*100;
	double g = Math.random()*100;
	return new RoundResult(round, robot, e, r, g);
    }

    public static List<List<RoundResult>> initRoundResults(int rounds, int numRobots){

	List<List<RoundResult>> result = new LinkedList<List<RoundResult>>();

	for(int round = 0; round < rounds; round++){
	    List<RoundResult> newRound = new ArrayList<RoundResult>();

	    for(int robot = 0; robot < numRobots; robot++){
		newRound.add(robotResult(round, robot));
	    }
	    result.add(newRound);
	}

	return result;

    }

    public static void runTests(int rounds, int numCompetitors)
    {
	RobotSpecification[] competitors = initCompetitors(numCompetitors);
	
	testBracket(competitors, 10, 2, 5, 20);
	System.out.println("testBracket passes");


	List<List<RobotSpecification>> brackets = Utils.bracket(competitors, 2, 10, 5, 20);
	
	
	for(int i = 10; i <= 200; i++){
	    testDetermineGroupInfo(i, 10, 2, 5, 20);
	}
	System.out.println("testDetermineGroupInfo passes");
	
	testMapToBattle(brackets, 100, 100, 10);
	System.out.println("testMapToBattle passes");


	List<List<RoundResult>> roundResults = initRoundResults(rounds, numCompetitors);
	testRoundResultToCumulativeResult(roundResults);

    }

    public static void testMapToBattle(List<List<RobotSpecification>> brackets, int width, int height, int numRounds){
	// test that no battles have the same battle id
	List<Battle> battles = Utils.mapToBattle(brackets, width, height, numRounds);
	Set<Integer> battleIds = new HashSet<Integer>();
	for(Battle b : battles){
	    assert(!battleIds.contains(b.battleId));
	    battleIds.add(b.battleId);
	}
    }

    public static void testDetermineGroupInfo(int numCompetitors, int groupMaxBound, int groupMinBound, int minGroupSize, int maxGroupSize){
	// test that the resulting size and number are within the expected bounds
	Tuple<Integer, Integer> result = Utils.determineGroupInfo(numCompetitors, groupMaxBound, groupMinBound, minGroupSize, maxGroupSize);
	assert(result.fst() <= groupMaxBound && result.fst() >= groupMinBound);
	assert(result.fst() <= maxGroupSize && result.fst() >= minGroupSize);	
    }

    public static void testBracket(RobotSpecification[] competitors, int groupMaxBound, int groupMinBound, int minGroupSize, int maxGroupSize){
	// test that all robots are spread across the resultant brackets
	// test that no robot is present in multiple brackets
	// test that the number of robots is the same in both
	// test that the difference between the maximum and the minimum number of robots in a group <= 1
	List<List<RobotSpecification>> result = Utils.bracket(competitors, groupMaxBound, groupMinBound, minGroupSize, maxGroupSize);
	List<RobotSpecification> flattenedResult = flatten(result);
	Set<RobotSpecification> resultSet = new HashSet<RobotSpecification>(flattenedResult);
	for(RobotSpecification c : competitors){
	    assert(containsRobotSpec(flattenedResult, c));
	}

	for(int i = 0; i < result.size(); i++){
	    for(int j = i+1; j < result.size(); j++){
		for(RobotSpecification r : result.get(i)){
		    assert(!containsRobotSpec(result.get(j), r));
		}
	    }
	}
	
	assert(flattenedResult.size() == competitors.length);

	int maxRobotsInBracket = result.get(0).size();
	int minRobotsInBracket = result.get(0).size();
	
	for(List<RobotSpecification> l : result){
	   maxRobotsInBracket = Math.max(maxRobotsInBracket, l.size());
	   minRobotsInBracket = Math.min(minRobotsInBracket, l.size());
	}

	assert(maxRobotsInBracket - minRobotsInBracket <= 1);

    }

    
    private static boolean containsRobotSpec(List<RobotSpecification> l, RobotSpecification r){
	for(RobotSpecification c : l){
	    if(c.getTeamId().equals(r.getTeamId())){
		return true;
	    }
	}
	return false;
    }

    private static <X> List<X> flatten(List<List<X>> inp){
	List<X> result = new LinkedList<X>();
	for(List<X> i : inp){
	    for(X x : i){
		result.add(x);
	    }
	}
	
	return result;
    }


    public static void testRoundResultToCumulativeResult(List<List<RoundResult>> rr){
	// test that the number of rounds is the same
	List<CumulativeRoundResult> roundResult = Utils.roundResultToCumulativeResult(rr);
	assert(roundResult.size() == rr.size());
    }

    public static void testSplitIntoRoundResultsPerBot(List<List<RoundResult>> inp){
	// test that the number of keys is the number of robots per round
	// test that the number of rounds for each robot is the same
	// test that total number of rounds is preserved
	// test that all rounds for a key belong to that robot

	Map<Integer, List<RoundResult>> result = Utils.splitIntoRoundResultsPerBot(inp);

	assert(result.keySet().size() == inp.get(0).size());

	for(int robot : result.keySet()){
	    assert(result.get(robot).size() == inp.size());
	}

	for(int robot : result.keySet()){
	    for(RoundResult rr : result.get(robot)){
		assert(rr.playerId == robot);
	    }
	}

    }

    

}
