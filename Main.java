import net.sf.robocode.repository.IRepositoryManagerBase;
import robocode.control.*;
import java.util.*;
import robocode.control.events.*;
import net.sf.robocode.core.Container;
import net.sf.robocode.repository.root.handlers.RootHandler;
import net.sf.robocode.repository.IRepositoryManager;

import java.net.URL;
import java.util.*;
import java.net.URLClassLoader;

 public class Main {
     // Move the code in this class to a Bracket abstraction

     public static List<String> collectJarNames(){
	 Scanner sc = new Scanner(System.in);
	 System.out.println("Enter a series of lines with the fully qualified package name of the classes followed by '--' to signal the end of the input:");
	 List<String> result = new LinkedList<String>();
	 String jarName;
	 while(!(jarName = sc.nextLine()).equals("--")){
	     result.add(jarName);
	 }
	 return result;
	 
     }

     public static List<List<String>> genBrackets(List<String> inputJars){
	 List<List<String>> result = new LinkedList<List<String>>();
	 int numJars = inputJars.size();
	 int maxGroups = 10;
	 int minGroups = 2;
	 int maxPlayersPerGroup = 15;
	 int minPlayersPerGroup = 5;

	 int numberOfGroups = 2;
	 int ming = 0;
	 int maxg = 0;
	 for(int g = maxGroups; g >= minGroups; g--){
	     if(numJars/g >= minPlayersPerGroup && numJars/g <= maxPlayersPerGroup){
		 if(maxg <= 0){
		     maxg = g;
		 }
		 ming = g;
	     }
	 }
	 numberOfGroups = (ming + maxg)/2;
	 int count = 0;
	 int playersPerGroup = numJars/numberOfGroups;
	 System.out.printf("numJars: %d, numberOfGroups: %d, playersPerGroup: %d\n", numJars, numberOfGroups, playersPerGroup);
	 List<String> bracket = new LinkedList<String>();
	 int i;
	 for(i = 0; i < playersPerGroup*numberOfGroups; i++){
	     String jar = inputJars.get(i);
	     bracket.add(jar);
	     count++;
	     if(count == playersPerGroup){
		 result.add(bracket);
		 bracket = new LinkedList<String>();
		 count = 0;
	     }
	 }
	 int ix = 0;
	 while(i < numJars){
	     result.get(ix).add(inputJars.get(i));
	     i++;
	     ix++;
	 }

	 return result;
     }

     public Queue<Battle> createBattleQueue(List<List<String>> brackets){
	 return null;
     }
     
     public static void main(String[] args) {

	 List<String> inputJars = collectJarNames();
	 List<List<String>> brackets = genBrackets(inputJars);
	 
	 Queue<Battle> knockoutRounds = createBattleQueue(brackets);

	 BattleRunner bRunner = new BattleRunner(args.length > 1 && args[0].equals("-v"));
	 bRunner.init();
	 // Over here add the provision that it should be run for a thousand rounds
	 // and the streaming should only happen on the final two rounds
	 // Also add a timeout for the battle making sure it only runs for some number of rounds
	 Battle finalRound = new Battle(1);
	 for(Battle b : knockoutRounds){
	     String winner = bRunner.runBattle(b);
	     finalRound.addBot(winner);
	 }
	 bRunner.runBattle(finalRound);
	 bRunner.shutdown();
	 
     }
 }
