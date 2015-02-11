package ai.singleplayer;

public class Timer {

	private long startTime;
		
	public void startTiming(){
		startTime = System.currentTimeMillis();
	}
	
	public boolean almostExpired(){
		long currentTime = ((System.currentTimeMillis() - startTime) / 1000) % 60;
		if (currentTime > 29){
			return true;
		}
		return false;
	}
	
	public boolean isStillValid(){
		return ((System.currentTimeMillis() - startTime) / 1000) % 60 < 30;
	}
}
