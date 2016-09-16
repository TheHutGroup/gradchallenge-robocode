package com.hutgroup.robocode.server;

import java.io.*;
import com.hutgroup.robocode.utils.Tuple;
import robocode.BattleResults;
import java.sql.*;
import java.util.*;
import robocode.control.*;
import robocode.control.snapshot.*;
import robocode.control.events.*;

public class THGEngine extends RobocodeEngine
{

    /**
     * Custom robocode engine for The Hut Group.
     */

    private static final int ACCURACY = 1000000;

    boolean        logToDB;
    Connection     conn;
    SimpleListener listener;
    File           robocodeHome;

    public THGEngine(File robocodeHome, String connString, String user, String password)
    {
	/**
	 * Creates the robocode screen and sets up a connection to the database
	 */

	super(robocodeHome);

	setVisible(true);

	listener = new SimpleListener();	
	addBattleListener(listener);

	try {
	    conn = DriverManager.getConnection(connString, user, password);
	    logToDB = true;
	}
	catch(Exception e){ logToDB = false; }
    }
    
    public RobotSpecification runBattle(Battle b)
    {
	/**
	 * Runs the battle and logs the results to the database
	 */

	runBattle(b.getBattleSpecification(), false);

	List<List<RoundResult>>      roundResults = listener.getRoundResults();
	List<Tuple<Integer, Double>> scores       = Utils.score(roundResults,
								Utils.roundResultToCumulativeResult(roundResults));

	if(logToDB)
	{
	    executeStatements(toBattleInfoStatements(b.battleId, scores));
	    executeStatements(toRoundInfoStatements(b.battleId, roundResults));
	}
	
	return Utils.extractWinner(scores, b.getCompetitors());
    
    }

    public void executeStatements(List<String> sqlStatements)
    {
	/**
	 * Executes the sql statements on the current database connection
	 */
	Statement stmt;

	for(String sqlStatement : sqlStatements){
	    try {
		stmt = conn.createStatement();
		stmt.executeQuery(sqlStatement);
	    } catch (Exception e) {System.err.println("Failed to log: " + sqlStatement);}
	}

    }

    public void displayBattleScreen (List<RobotSpecification> winners)
    {
	for(RobotSpecification r : winners){
	    System.out.println(r.getTeamId());
	}
    }
    
    public void displayWinner (RobotSpecification winner)
    {
	System.out.println(winner.getTeamId());
    }



    private String toRoundInfoStatement(int battleId, RoundResult result)
    {
	    return String.format("insert into roundinfo (battleid, roundid, playerid, energyLeft, ramDamage, gunDamage) values (%d, %d, %d, %d, %d, %d)",
				 battleId,
				 result.roundId,
				 result.playerId,
				 result.energyLeft,
				 result.ramDamage,
				 result.gunDamage);
    }

    private List<String> toRoundInfoStatements(int battleId, List<List<RoundResult>> roundResults)
    {

	List<String> result = new ArrayList<String>();

	for(List<RoundResult> roundResult : roundResults)
	{
	    for(RoundResult rResult : roundResult)
	    {
		result.add(toRoundInfoStatement(battleId, rResult));
	    }
	}
	
	return result;
    }


    private String toBattleInfoStatement(int battleId, int playerId, int playerRank)
    {
	return String.format("insert into battleinfo (battleid, playerid, rank) values (%d, %d, %d)",
			     battleId,
			     playerId,
			     playerRank);
    }

    private List<String> toBattleInfoStatements(int battleId, List<Tuple<Integer, Double>> scores)
    {
	Collections.sort(scores, new Comparator<Tuple<Integer, Double>>(){
		public int compare(Tuple<Integer, Double> t1, Tuple<Integer, Double> t2)
		{
		    return (int)((t2.snd() - t1.snd())*ACCURACY);
		}
	    });

	List<String> result = new LinkedList<String>();

	for(int rank = 1; rank <= scores.size(); rank++)
	{
	    result.add(toBattleInfoStatement(battleId, scores.get(rank-1).fst(), rank));
	}

	return result;

    }

    private class SimpleListener extends BattleAdaptor
    {

	List<List<IRobotSnapshot>> roundResults;
	List<IRobotSnapshot> newRoundResult;

	public SimpleListener(){
	    roundResults = new ArrayList<List<IRobotSnapshot>>();
	}

	@Override
	public synchronized void onTurnEnded(final TurnEndedEvent event)
	{
	    newRoundResult = Arrays.asList(event.getTurnSnapshot().getRobots());
	}

	@Override
	public synchronized void onRoundEnded(final RoundEndedEvent event) { roundResults.add(newRoundResult); }

	@Override
	public synchronized void onBattleCompleted(BattleCompletedEvent e) {
	    System.out.println(e);
	    System.out.println(roundResults);
	    notifyAll();
	}
	
	public synchronized List<List<RoundResult>> getRoundResults()
	{
	    System.out.println("Application Thread is waiting--");
	    try { wait(); } catch (Exception e) { throw new RuntimeException(e.toString()); }
	    System.out.println("Application thread has woken up");
	    List<List<RoundResult>> result = new ArrayList<List<RoundResult>>();
	    for(int i = 0; i < roundResults.size(); i++){
		List<RoundResult> newElem = new ArrayList<RoundResult>();
		for(IRobotSnapshot snapshot : roundResults.get(i)) {
		    newElem.add(toRoundResult(i, snapshot));
		}
		result.add(newElem);
	    }
	    roundResults.clear();
	    return result;
	}

	private RoundResult toRoundResult(int roundId, IRobotSnapshot robotSnapshot)
	{
	    
	    double energyLeft = robotSnapshot.getEnergy();
	    int playerId = robotSnapshot.getTeamIndex();
	    IScoreSnapshot snapshot = robotSnapshot.getScoreSnapshot();
	    double gunDamage = snapshot.getCurrentBulletDamageScore() + snapshot.getCurrentBulletKillBonus();
	    double ramDamage = snapshot.getCurrentRammingDamageScore() + snapshot.getCurrentRammingKillBonus();
	    return new RoundResult(roundId, playerId, energyLeft, ramDamage, gunDamage);

	}


    }

}
