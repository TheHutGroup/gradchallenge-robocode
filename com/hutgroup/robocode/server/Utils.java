package com.hutgroup.robocode.server;

import com.hutgroup.robocode.utils.Tuple;
import robocode.BattleResults;
import java.util.*;
import robocode.control.*;
import robocode.control.events.*;

public class Utils
{

    public static RobotSpecification[] mapToSpecs(THGEngine engine, String[] jars)
    {
	return engine.getLocalRepository(toCommaSeperatedList(jars));
    }

    public static List<Battle> mapToBattle(List<List<RobotSpecification>> brackets, int width, int height, int numRounds){
	List<Battle> result = new ArrayList<Battle>();
	int battleId = 0;
	for(List<RobotSpecification> bracket : brackets)
	{
	    result.add(toBattle(battleId, bracket, width, height, numRounds));
	    battleId++;
	}
	return result;
    }

    private static RobotSpecification[] toArray(List<RobotSpecification> inp){
	RobotSpecification[] result = new RobotSpecification[inp.size()];
	int counter = 0;
	for(RobotSpecification r : inp){
	    result[counter] = r;
	    counter++;
	}
	return result;
    }


    public static Battle toBattle(int battleId, List<RobotSpecification> spec, int width, int height, int numRounds){
	return new Battle(battleId, toArray(spec), width, height, numRounds);
    }
						   
    public static Tuple<Integer, Integer> determineGroupInfo(int numCompetitors, int groupMaxBound, int groupMinBound, int minGroupSize, int maxGroupSize)
    {
	assert(groupMaxBound >= groupMinBound);
	assert(minGroupSize <= maxGroupSize);

	int minNumGroups = groupMaxBound;
	int maxNumGroups = groupMinBound;

	for(int g = groupMinBound; g <= groupMaxBound; g++){
	    if(numCompetitors/g <= maxGroupSize && numCompetitors/g >= minGroupSize){
		minNumGroups = Math.min(minNumGroups, g);
		maxNumGroups = Math.max(maxNumGroups, g);
	    }
	}

	int numGroups = (minNumGroups + maxNumGroups)/2;
	int groupSize = numCompetitors/numGroups;

	assert(groupSize <= maxGroupSize);
	assert(groupSize >= minGroupSize);
	assert(numGroups <= groupMaxBound);
	assert(numGroups >= groupMinBound);

	return new Tuple<Integer, Integer>(groupSize, numGroups);

    }
						   
    public static List<List<RobotSpecification>> bracket(RobotSpecification[] competitors, int groupMaxBound, int groupMinBound, int minGroupSize, int maxGroupSize)
    {
	Tuple<Integer, Integer> groupInfo
	    = determineGroupInfo(competitors.length,
				 groupMaxBound,
				 groupMinBound,
				 minGroupSize,
				 maxGroupSize);
	int groupSize = groupInfo.fst();
	int numGroups = groupInfo.snd();
	List<List<RobotSpecification>> result = new ArrayList<List<RobotSpecification>>();
	List<RobotSpecification> newGroup = new ArrayList<RobotSpecification>();
	for(int i = 0; i < competitors.length; i++)
	{
	    newGroup.add(competitors[i]);
	    if(newGroup.size() == groupSize){
		result.add(newGroup);
		newGroup = new ArrayList<RobotSpecification>();
		
	    }
	}

	if(!newGroup.isEmpty()){
	    for(int i = 0;i < newGroup.size(); i++){
		result.get(i).add(newGroup.get(i));
	    }
	    
	}

	return result;

    }

    
						   
    private static String toCommaSeperatedList(String[] inp){

	String result = "";

	for(int i = 0; i < inp.length-1; i++)
	{
	    result += inp[i] + ",";
	}
	result += inp[inp.length-1];

	return result;
    }
						   
    public static List<CumulativeRoundResult> roundResultToCumulativeResult(List<List<RoundResult>> roundResults){
	List<CumulativeRoundResult> res = new ArrayList<CumulativeRoundResult>();
	for(List<RoundResult> round : roundResults){
	    res.add(toCumulativeResult(round));
	}
	return res;
    }

    private static CumulativeRoundResult toCumulativeResult(List<RoundResult> roundResults){
	double totalEnergyLeft = 0;
	double totalRamDamage  = 0;
	double totalGunDamage  = 0;

	for(RoundResult r : roundResults){
	    totalEnergyLeft += r.energyLeft;
	    totalRamDamage  += r.ramDamage;
	    totalGunDamage  += r.gunDamage;
	    
	}

	return new CumulativeRoundResult(totalEnergyLeft, totalRamDamage, totalGunDamage);
    }


    private static Tuple<Integer, Double> maxPlayerScorePair(List<Tuple<Integer, Double>> tuples){

	Tuple<Integer, Double> tuplePair = tuples.get(0);

	for(Tuple<Integer, Double> t : tuples){
	    if(t.snd() > tuplePair.snd()){
		t = tuplePair;
	    }
	}

	return tuplePair;
	
    }

    public static RobotSpecification extractWinner(List<Tuple<Integer, Double>> results, RobotSpecification[] spec){
	//  a list of results 
	
	return spec[maxPlayerScorePair(results).fst()];
    }

    private static double S(RoundResult round, CumulativeRoundResult cumResult){
	return round.getGunDamage()/cumResult.getGunDamage() + round.getRamDamage()/cumResult.getRamDamage()*1.5 + round.getEnergyLeft()/cumResult.getEnergyLeft();
    }

    public static Map<Integer, List<RoundResult>> splitIntoRoundResultsPerBot(List<List<RoundResult>> res){

	Map<Integer, List<RoundResult>> mp = new HashMap<Integer, List<RoundResult>>();

	for(List<RoundResult> lrr : res){
	    for(RoundResult rr : lrr){
		if(mp.get(rr.playerId) == null){
		    mp.put(rr.playerId, new LinkedList<RoundResult>());
		}
		mp.get(rr.playerId).add(rr);
	    }
	}
	return mp;
    }

    public static List<Tuple<Integer, Double>> score(List<List<RoundResult>> roundResults, List<CumulativeRoundResult> cumRes){
	List<Tuple<Integer, Double>> result = new ArrayList<Tuple<Integer, Double>>();
	Map<Integer, List<RoundResult>> mp = splitIntoRoundResultsPerBot(roundResults);
	for(int key : mp.keySet()){
	    result.add(new Tuple<Integer, Double>(key, scoreOne(mp.get(key), cumRes)));
	}
	return result;
    }

    public static double scoreOne(List<RoundResult> rounds, List<CumulativeRoundResult> cumResult){
	double matchScore = 0;

	for(int i = 1; i <= rounds.size(); i++){
	    matchScore += 2.1 * 10e-4 *i * S(rounds.get(i), cumResult.get(i));
	}

	for(int k = rounds.size()-3; k < rounds.size(); k++){
	    matchScore += S(rounds.get(k), cumResult.get(k));
	}

	return matchScore;

    }


}
