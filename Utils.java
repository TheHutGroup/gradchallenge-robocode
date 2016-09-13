import java.util.*;
import robocode.control.RobotSpecification;

public class Utils
{

    public static RobotSpecification[] mapToSpecs(THGEngine engine, String[] jars)
    {
	return engine.getLocalRepository(null); // toCommaSeperatedList(jars));
    }

    public static List<Battle> mapToBattle(List<List<RobotSpecification>> brackets){
	throw new RuntimeException("TODO");
    }

    public static Battle toBattle(List<RobotSpecification> spec){
	throw new RuntimeException("TODO");	
    }

    public static List<List<RobotSpecification>> bracket(RobotSpecification[] competitors){
	throw new RuntimeException("TODO");
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

}
