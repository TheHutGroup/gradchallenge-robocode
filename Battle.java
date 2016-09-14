import java.util.List;
import robocode.control.*;


public class Battle
{
    RobotSpecification[] competitors;
    BattlefieldSpecification spec;
    BattleSpecification bSpec;
    int battleId;

    public Battle(int battleId, RobotSpecification[] competitors, int width, int height, int numRounds){
	this.battleId = battleId;
	this.competitors = competitors;
	spec = new BattlefieldSpecification(width, height);
	bSpec = new BattleSpecification(numRounds, spec, competitors);

    }

    public RobotSpecification[] getCompetitors(){
	return competitors;
    }

    public BattleSpecification getBattleSpecification(){
	return bSpec;
    }

}
