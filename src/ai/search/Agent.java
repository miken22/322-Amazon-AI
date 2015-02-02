package ai.search;

import ai.Board;

/**
 * Search agent
 * 
 * @author Mike Nowicki
 *
 */
public class Agent implements Search {
	
	private HMinimaxSearch hMinimax;

	private int role;
	
	private int DEPTH = 4;
		
	public Agent(int rows, int columns, int ourColour){
		this.role = ourColour;
	}
	
	public Agent(int rows, int columns, int ourColour, int depth){
		this.role = ourColour;
		DEPTH = depth;
	}
	
	public void setupHeuristic(EvaluationFunction function){
		hMinimax = new HMinimaxSearch(function, DEPTH);
	}
	
	/**
	 * Method to return the move that the search agent has selected. Six entries must be in the move
	 * array at the time of return: FromX, FromY, ToX, ToY, aRow, aCol.  
	 */
	public int[] selectMove(Board currentBoard){
		
		int[] moveChoice = hMinimax.maxSearch(currentBoard, role);
		
		int[] move = new int[6];
		
		move[0] = 3;
		move[1] = 0;
		move[2] = 3;
		move[3] = 3;
		move[4] = 8;
		move[5] = 3;
		// Just to make a trivial move for now
		moveChoice = move;
		
		return moveChoice;
				
	}
}
