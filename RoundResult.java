public class RoundResult {

    double energyLeft;
    double ramDamage;
    double gunDamage;

    public RoundResult(double energyLeft, double ramDamage, double gunDamage){
	this.energyLeft = energyLeft;
	this.ramDamage = ramDamage;
	this.gunDamage = gunDamage;
    }
    

    public double getEnergyLeft(){
	return energyLeft;
    }

    public double getRamDamage(){
	return ramDamage;
    }

    public double getGunDamage(){
	return gunDamage;
    }
       

}
