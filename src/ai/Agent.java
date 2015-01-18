package ai;

import ai.singleplayer.Board;

/**
 * Search agent
 * 
 * @author Mike Nowicki
 *
 */
public class Agent {

	public final int WQUEEN = 1;
	public final int BQUEEN = 2;
	public final int ARROW = 3;
	public final int FREE = -1;	 
	
	private Board board;
	private SuccessorGenerator scg;

	private int rows;
	private int columns;
		
	public Agent(Board board, int rows, int columns, int ourColour){
		this.board = board;
		this.rows = rows;
		this.columns = columns;
		scg = new SuccessorGenerator(board, ourColour);
	}
	
	public void selectMove(){
		
	}
	
}
