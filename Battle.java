import net.sf.robocode.repository.IRepositoryManagerBase;
import robocode.control.*;
import java.util.*;
import robocode.control.events.*;
import net.sf.robocode.core.Container;
import net.sf.robocode.repository.root.handlers.RootHandler;
import net.sf.robocode.repository.IRepositoryManager;
import java.util.*;


public class Battle {

    int numberOfRounds;
    String contestant1;
    String contestant2;
    GraphicsStream stream;
    BattlefieldSpecification battleFieldSpecs;

    public Battle(String c1, String c2, int numRounds){
	contestant1 = c1;
	contestant2 = c2;
	this.numberOfRounds = numRounds;
	battleFieldSpecs = new BattlefieldSpecification(800, 600);
    }

    public String getContestant1(){
	return contestant1;
    }

    public String getContestant2(){
	return contestant2;
    }
    
    public BattlefieldSpecification getBattleFieldSpecs(){
	return battleFieldSpecs;
    }
    
    public GraphicsStream getGraphicsStream(){
	// Do that finalize thing over here
	return stream;
    }

    @Override
    public String toString(){
	return "Battle between: " + contestant1 + " and " + contestant2;
    }

}
