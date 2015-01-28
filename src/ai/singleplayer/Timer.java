package ai.singleplayer;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;

public class Timer extends JLabel {

	private static final long serialVersionUID = 2555383820456291745L;

	private double startTime;
	private boolean notDone;
	
	
	public Timer(){
		notDone = true;
		setText("Starting new game");
	}
	
	public void startTiming(){
		startTime = System.currentTimeMillis();
		
		while(notDone){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
		
	}
	
	public void stopTiming(){
		notDone = false;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		setBackground(Color.RED);
		
		double elapsedTime = System.currentTimeMillis() - startTime;
		double seconds = elapsedTime / 1000;
		double displaySecs = seconds % 60;
		double minutes = seconds / 60;
		
		int m = (int) minutes;
		int s = (int)Math.round(displaySecs);
				
		String secs = Integer.toString(s);
		
		if (s < 10){
			secs = "0" + secs;
		}
		setText("0: 0" + m + " : " + secs);
		
	}
}
