package ai.search;

import ai.Board;

/**
 * Search agent
 * 
 * @author Mike Nowicki
 *
 */
public class Agent implements Search {
	
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
	
	/**
	 * Method to return the move that the search agent has selected. Six entries must be in the move
	 * array at the time of return: FromX, FromY, ToX, ToY, aRow, aCol.  
	 */
	public int[] selectMove(){
		int[] move = new int[6];
		
		move[0] = 3;
		move[1] = 0;
		move[2] = 3;
		move[3] = 3;
		move[4] = 8;
		move[5] = 3;
		
		return move;		
	}
}
