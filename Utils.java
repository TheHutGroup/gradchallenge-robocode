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
	for(List<RobotSpecification> bracket : brackets)
	{
	    result.add(toBattle(bracket, width, height, numRounds));
	}
	return result;
    }

    private static RobotSpecification[] toArray(List<RobotSpecification> inp){
	throw new RuntimeException("Unimplemented");
    }


    public static Battle toBattle(List<RobotSpecification> spec, int width, int height, int numRounds){
	
	return new Battle(toArray(spec), width, height, numRounds);
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
						   

    public static RobotSpecification extractWinner(BattleResults[] results, RobotSpecification[] spec){
	throw new RuntimeException("Unimplemented");
    }


}
