package ai.singleplayer;

public class Timer {

	private long startTime;
		
	public void startTiming(){
		startTime = System.currentTimeMillis();
	}
	
	public boolean almostExpired(){
		long currentTime = ((System.currentTimeMillis() - startTime) / 1000) % 60;
		if (currentTime > 5){
			return true;
		}
		return false;
	}
	
	public boolean isStillValid(){
		
		if (startTime == 0){
			return true;
		}
		
		return ((System.currentTimeMillis() - startTime) / 1000) % 60 < 5;
	}
	
	public boolean hasStarted(){
		if (startTime == 0){
			return false;
		}
		return true;
	}
	
	public void resetTimer(){
		startTime = 0;
	}
	
}
