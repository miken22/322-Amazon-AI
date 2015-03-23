package ai;

public class Timer {

	private long startTime;
		
	public void startTiming(){
		startTime = System.currentTimeMillis();
	}
	
	public boolean almostExpired(){	
		long currentTime = ((System.currentTimeMillis() - startTime) / 1000) % 60;
		if (currentTime > 25){
			return true;
		}
		return false;
	}
}
