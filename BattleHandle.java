public class BattleHandle {
    
    int BID;
    private static int nextBID = 0;

    public BattleHandle(){
	BID = nextID();
    }

    private static int nextID(){
	return ++nextBID;
    }
    

}
