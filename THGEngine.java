import robocode.BattleResults;
import java.util.*;
import robocode.control.*;
import robocode.control.snapshot.*;
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
	return Utils.extractWinner(listener.getRoundResults(), b.getCompetitors());
    }

    public void displayBattleScreen(List<RobotSpecification> winners){
	throw new RuntimeException("TODO");	
    }

    public void displayWinner(RobotSpecification winner){
	throw new RuntimeException("TODO");
    }


    private class SimpleListener extends BattleAdaptor {

	List<List<IRobotSnapshot>> roundResults;
	List<IRobotSnapshot> newRoundResult;
	

	@Override
	public void onTurnEnded(final TurnEndedEvent event){
	    newRoundResult = Arrays.asList(event.getTurnSnapshot().getRobots());
	}

	@Override
	public void onRoundEnded(final RoundEndedEvent event){
	    roundResults.add(newRoundResult);
	}

	@Override
	public void onBattleCompleted(BattleCompletedEvent e) {
	    notifyAll();
	}
	
	public List<List<RoundResult>> getRoundResults(){
	    List<List<RoundResult>> result = new ArrayList<List<RoundResult>>();
	    for(int i = 0; i < roundResults.size(); i++){
		List<RoundResult> newElem = new ArrayList<RoundResult>();
		for(IRobotSnapshot snapshot : roundResults.get(i)) {
		    newElem.add(toRoundResult(snapshot));
		}
		result.add(newElem);
	    }
	    return result;
	}

	private RoundResult toRoundResult(IRobotSnapshot robotSnapshot){
	    
	    double energyLeft = robotSnapshot.getEnergy();
	    
	    IScoreSnapshot snapshot = robotSnapshot.getScoreSnapshot();
	    double gunDamage = snapshot.getCurrentBulletDamageScore() + snapshot.getCurrentBulletKillBonus();
	    double ramDamage = snapshot.getCurrentRammingDamageScore() + snapshot.getCurrentRammingKillBonus();
	    return new RoundResult(energyLeft, ramDamage, gunDamage);

	}


    }

}
