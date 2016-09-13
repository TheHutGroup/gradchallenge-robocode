import java.util.*;
import robocode.control.RobotSpecification;
import robocode.control.RobocodeEngine;

public class THGEngine extends RobocodeEngine {

    public RobocodeEngine(){
	engine.setVisible(true);
	addListener(new SimpleListener());
    }
    
    public RobotSpecification runBattle(Battle b){
	runBattle(specification, false);
	listener.wait();
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
	
	public getResult(){
	    return results;
	}	    
    }

}
