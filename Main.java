import net.sf.robocode.repository.IRepositoryManagerBase;
import robocode.control.*;
import java.util.*;
import robocode.control.events.*;
import net.sf.robocode.core.Container;
import net.sf.robocode.repository.root.handlers.RootHandler;
import net.sf.robocode.repository.IRepositoryManager;
 
 //
 // Application that demonstrates how to run two sample robots in Robocode using the
 // RobocodeEngine from the robocode.control package.
 //
 // @author Flemming N. Larsen
 //
import java.net.URL;
import java.net.URLClassLoader;

 public class Main {

     public static void main(String[] args) {

	 Queue<Battle> battleQueue = new LinkedList<Battle>();
	 Battle b1 = new Battle("josh.RamFire", "sample.RamFire", 5);
	 Battle b2 = new Battle("josh.Corners", "sample.Corners", 5);
	 battleQueue.add(b1);
	 battleQueue.add(b2);
	 BattleRunner bRunner = new BattleRunner(battleQueue, args.length > 1 && args[0] == "-v");
	 bRunner.runNextBattle();
	 bRunner.runNextBattle();
	 
     }
 }
