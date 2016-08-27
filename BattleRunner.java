import net.sf.robocode.repository.IRepositoryManagerBase;
import robocode.control.*;
import java.util.*;
import robocode.control.events.*;
import net.sf.robocode.core.Container;
import net.sf.robocode.repository.root.handlers.RootHandler;
import net.sf.robocode.repository.IRepositoryManager;
 

import java.net.URL;
import java.net.URLClassLoader;

public class BattleRunner implements IBattleRunner {

    Map<BattleHandle, Battle> handleToBattleMapper;

    Queue<Battle> battles;

    RobocodeEngine engine;

    public static final String ROBOCODE_DIR = "C:/robocode";

    public BattleRunner(Queue<Battle> battles, boolean debug){
	this.battles = battles;
	handleToBattleMapper = new HashMap<BattleHandle, Battle>();
         // Disable log messages from Robocode
        RobocodeEngine.setLogMessagesEnabled(debug);
        engine = new RobocodeEngine(new java.io.File(ROBOCODE_DIR)); // Run from C:/Robocode
}

    public void init(){

         engine.addBattleListener(new BattleObserver());
 

         engine.setVisible(true);
    }

    public void shutdown() {

         engine.close();
 
	 System.exit(0);

    }

    @Override
    public void runNextBattle(){
	if(!battles.isEmpty()){
	    Battle b = battles.poll();
	    handleToBattleMapper.put(new BattleHandle(), b);
	    startBattle(b);
	}
    }

    public void startBattle(Battle b){
         int numberOfRounds = b.numberOfRounds;
         BattlefieldSpecification battlefield = b.getBattleFieldSpecs(); // 800x600
	 // Cache these values somewhere
         RobotSpecification[] selectedRobots = engine.getLocalRepository(b.getContestant1() + "," + b.getContestant2());

         BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots);
 
         // Run our specified battle and let it run till it is over
	 System.out.println("Running a battle: " + b);
         engine.runBattle(battleSpec, true); // waits till the battle finishes

    }

    @Override
    public GraphicsStream streamBattle(BattleHandle handle){
	return handleToBattleMapper.get(handle).getGraphicsStream();
    }

}
 
