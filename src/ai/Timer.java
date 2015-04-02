package ai;
/**
 * Simple timer class to control Minimax search time
 * 
 * @author Mike Nowicki
 *
 */
public class Timer {

	private long startTime;
	private final byte MAXSEARCHTIME = 25;
		
	public void startTiming(){
		startTime = System.currentTimeMillis();
	}
	
	public boolean almostExpired(){	
		long currentTime = ((System.currentTimeMillis() - startTime) / 1000) % 60;
		if (currentTime > MAXSEARCHTIME){
			return true;
		}
		return false;
	}
}
