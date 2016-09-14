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


    public static RobotSpecification extractWinner(List<List<RoundResult>> results, RobotSpecification[] spec){
	RobotSpecification winner = null;
	double maxScore = 0;
	for(int i = 0; i < spec.length; i++){
	    if(maxScore < Utils.score(results.get(i), toCumulativeResult(results.get(i)))){
		winner = spec[i];
		maxScore = Utils.score(results.get(i), toCumulativeResult(results.get(i)));
	    }
	}

	return winner;
    }

    private static double S(RoundResult round, CumulativeRoundResult cumResult){
	return round.getGunDamage()/cumResult.getGunDamage() + round.getRamDamage()/cumResult.getRamDamage()*1.5 + round.getEnergyLeft()/cumResult.getEnergyLeft();
    }

    public static double score(List<RoundResult> rounds, CumulativeRoundResult cumResult){
	double matchScore = 0;

	for(int i = 1; i <= rounds.size(); i++){
	    matchScore += 2.1 * 10e-4 *i * S(rounds.get(i), cumResult);
	}

	for(int k = rounds.size()-3; k < rounds.size(); k++){
	    matchScore += S(rounds.get(k), cumResult);
	}

	return matchScore;

    }


}
