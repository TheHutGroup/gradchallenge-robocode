import net.sf.robocode.repository.IRepositoryManagerBase;
import robocode.control.*;
import java.util.*;
import robocode.control.events.*;
import net.sf.robocode.core.Container;
import net.sf.robocode.repository.root.handlers.RootHandler;
import net.sf.robocode.repository.IRepositoryManager;
 
 //
 // Our private battle listener for handling the battle event we are interested in.
 //
 class BattleObserver extends BattleAdaptor {
 
     // Called when the battle is completed successfully with battle results
     public void onBattleCompleted(BattleCompletedEvent e) {
         System.out.println("-- Battle has completed --");
         
         // Print out the sorted results with the robot names
         System.out.println("Battle results:");
         for (robocode.BattleResults result : e.getSortedResults()) {
             System.out.println("  " + result.getTeamLeaderName() + ": " + result.getScore());
         }
     }
 
     // Called when the game sends out an information message during the battle
     public void onBattleMessage(BattleMessageEvent e) {
         System.out.println("Msg> " + e.getMessage());
     }
 
     // Called when the game sends out an error message during the battle
     public void onBattleError(BattleErrorEvent e) {
         System.out.println("Err> " + e.getError());
     }
 }
