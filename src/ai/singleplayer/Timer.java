package ai.singleplayer;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;

public class Timer extends JLabel {

	private static final long serialVersionUID = 2555383820456291745L;

	private double startTime;
	private double endTime;
	private double duration;
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
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		setBackground(Color.RED);
		
		double time = (System.currentTimeMillis() - startTime)/1000;
		
		
		if (time < 60){
			int seconds = (int) Math.round(time);
			setText("0 : " + seconds);
		} else {
			int minutes = (int)time % 60;
			int seconds = (int)time/(minutes*60);
			setText(minutes + " : " + seconds);
			
		}
		
	}
	
	
}
