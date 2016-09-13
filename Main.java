import java.util.*;
import robocode.control.RobotSpecification;
import robocode.control.RobocodeEngine;

public class Main
{

    public static void runner(String[]jars)
    {

	THGEngine                      engine  = new THGEngine();
	RobotSpecification[]           specs   = Utils.mapToSpecs(engine, jars);
	List<List<RobotSpecification>> groups  = Utils.bracket(specs);
	List<Battle>                   battles = Utils.mapToBattle(groups);
	List<RobotSpecification>       winners = new ArrayList<RobotSpecification>();
	
	for(Battle b : battles)	{ winners.add(engine.runBattle(b)); }

	engine.displayBattleScreen(winners);

	Battle             finalBattle = Utils.toBattle(winners);
        RobotSpecification champion    = engine.runBattle(finalBattle);

	engine.displayWinner(champion);

    }


}
