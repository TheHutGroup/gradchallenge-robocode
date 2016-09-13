import robocode.BattleResults;
import java.util.*;
import robocode.control.*;
import robocode.control.events.*;

public class THGEngine extends RobocodeEngine {
    SimpleListener listener;


    public THGEngine(){
	setVisible(true);
	listener = new SimpleListener();	
	addBattleListener(listener);
    }
    
    public RobotSpecification runBattle(Battle b){
	runBattle(b.getBattleSpecification(), false);
	try { listener.wait(); } catch (Exception e) { throw new RuntimeException("Crash"); }
	return Utils.extractWinner(listener.getResult(), b.getCompetitors());
    }

    public void displayBattleScreen(List<RobotSpecification> winners){
	throw new RuntimeException("TODO");	
    }

    public void displayWinner(RobotSpecification winner){
	throw new RuntimeException("TODO");
    }


    private class SimpleListener extends BattleAdaptor {
	BattleResults[] results;

	public void onBattleCompleted(BattleCompletedEvent e) {
	    results = e.getIndexedResults();
	}
	
	public BattleResults[] getResult(){
	    return results;
	}	    
    }

}
