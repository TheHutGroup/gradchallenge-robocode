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
    public List<String> contestants;
    GraphicsStream stream;
    BattlefieldSpecification battleFieldSpecs;

    public Battle(List<String> contestants, int numRounds){
	this.contestants = contestants;
	this.numberOfRounds = numRounds;
	battleFieldSpecs = new BattlefieldSpecification(800, 600);
	// TODO: Need to make this configurable
    }

    public Battle(int numRounds){
	this.contestants = new LinkedList<String>();
	this.numberOfRounds = numRounds;
	battleFieldSpecs = new BattlefieldSpecification(800, 600);
	// TODO: Need to make this configurable
    }

    public BattlefieldSpecification getBattleFieldSpecs(){
	return battleFieldSpecs;
    }
    
    public GraphicsStream getGraphicsStream(){
	// Do that finalize thing over here
	return stream;
    }

    public void addBot(String bot){
	contestants.add(bot);
    }

    @Override
    public String toString(){
	return "Battle between: " + contestants;
    }

}
