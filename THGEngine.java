import robocode.BattleResults;
import java.sql.*;
import java.util.*;
import robocode.control.*;
import robocode.control.snapshot.*;
import robocode.control.events.*;

public class THGEngine extends RobocodeEngine {
    SimpleListener listener;
    Connection conn;
    boolean logToDB;
    public THGEngine(String connString, String user, String password){
	setVisible(true);
	listener = new SimpleListener();	
	addBattleListener(listener);
	try {
	    conn = DriverManager.getConnection(connString, user, password);
	    logToDb = true;
	}
	catch(Exception e){
	    logToDb = false;
	}
    }
    
    public RobotSpecification runBattle(Battle b){

	runBattle(b.getBattleSpecification(), false);
	try { listener.wait(); } catch (Exception e) { throw new RuntimeException("Crash"); }

	List<List<RoundResult>> roundResults = listener.getRoundResults();
	List<Tuple<Integer, Integer>> scores = Utils.score(roundResults, b.getCompetitors());

	if(logToDb){
	    executeStatements(toBattleInfoStatements(b.battleId, scores));
	    executeStatements(toRoundInfoStatements(b.battleId, roundResults));
	}
	
	return Utils.extractWinner(roundResults, b.getCompetitors());

    
    }

    private String toRoundInfoStatement(int battleId, RoundResult result){x
	    return String.format("insert into roundinfo (battleid, roundid, playerid, energyLeft, ramDamage, gunDamage) values (%d, %d, %d, %d, %d, %d)", battleId, result.roundId, result.playerId, result.energyLeft, result.ramDamage, result.gunDamage);
    }

    private List<String> toRoundInfoStatements(int battleId, List<List<RoundResult>> roundResults){

	List<String> result = new ArrayList<String>();

	for(List<RoundResult> roundResult : roundResults){
	    for(RoundResult result : roundResult){
		result.add(toRoundInfoStatement(battleId, result));
	    }
	}
	return result;
    }


    private String toBattleInfoStatement(int battleId, int playerId, int playerRank){
	return String.format("insert into battleinfo (battleid, playerid, rank) values (%d, %d, %d)", battleId, playerId, playerRank);
    }

    private List<String> toBattleInfoStatements(int battleId, List<Tuple<Integer, Integer>> scores){
	scores.sort(scores, new Comparator<Tuple<Integer, Integer>>(){
		public int compareTo(Tuple<Integer, Integer> t1, Tuple<Integer, Integer> t2){
		    return t2.snd() - t1.snd();
		}
	    });
	List<String> result = new LinkedList<String>();
	for(int rank = 1; rank <= scores.size(); rank++){
	    result.add(toBattleInfoStatement(battleId, scores.get(rank-1).fst(), rank));
	}
	return result;
    }

    


    public void executeStatements(List<String> sqlStatements){
	Statement stmt;
	for(String sqlStatement : sqlStatements){
	    try {
		stmt = conn.createStatement();
		stmt.executeQuery(sqlStatement);
	    } catch (Exception e) {System.err.println("Failed to log: " + sqlStatement);}
	}
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
		    newElem.add(toRoundResult(i, snapshot));
		}
		result.add(newElem);
	    }
	    return result;
	}

	private RoundResult toRoundResult(int roundId, IRobotSnapshot robotSnapshot){
	    
	    double energyLeft = robotSnapshot.getEnergy();
	    int playerId = robotSnapshot.getTeamIndex();
	    IScoreSnapshot snapshot = robotSnapshot.getScoreSnapshot();
	    double gunDamage = snapshot.getCurrentBulletDamageScore() + snapshot.getCurrentBulletKillBonus();
	    double ramDamage = snapshot.getCurrentRammingDamageScore() + snapshot.getCurrentRammingKillBonus();
	    return new RoundResult(roundId, playerId, energyLeft, ramDamage, gunDamage);

	}


    }

}
