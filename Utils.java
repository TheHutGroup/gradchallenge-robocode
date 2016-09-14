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
	return (RobotSpecification[])(inp.toArray());
    }


    public static Battle toBattle(int battleId, List<RobotSpecification> spec, int width, int height, int numRounds){
	
	return new Battle(battleId, toArray(spec), width, height, numRounds);
    }
						   
    private static Tuple<Integer, Integer> determineGroupInfo(int numCompetitors, int groupMaxBound, int groupMinBound, int minGroupSize, int maxGroupSize)
    {

	int minGSize = 0;
	int maxGSize = 0;

	for(int g = groupMaxBound; g >= groupMinBound; g--){
	    if(numCompetitors/g <= maxGroupSize && numCompetitors/g >= minGroupSize){
		minGSize = Math.min(minGSize, g);
		maxGSize = Math.max(maxGSize, g);
	    }
	}

	int groupSize = (minGSize + maxGSize)/2;
	int numGroups = numCompetitors/groupSize;

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
	for(int size = 0, i = 0; i < numGroups; i++)
	{
	    newGroup.add(competitors[i]);
	    size++;
	    if(size == groupSize){
		result.add(newGroup);
		newGroup = new ArrayList<RobotSpecification>();
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

    private static Map<Integer, List<RoundResult>> splitIntoRoundResultsPerBot(List<List<RoundResult>> res){

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
