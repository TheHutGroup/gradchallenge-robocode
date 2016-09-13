import java.util.List;
import robocode.control.*;


public class Battle
{
    RobotSpecification[] competitors;
    BattlefieldSpecification spec;
    BattleSpecification bSpec;

    public Battle(RobotSpecification[] competitors, int width, int height, int numRounds){
	this.competitors = competitors;
	spec = new BattlefieldSpecification(width, height);
	bSpec = new BattleSpecification(numRounds, spec, competitors);
	engine.setVisible(true);

    }

    public BattleSpecification getBattleSpec(){
	return bSpec;
    }

}
