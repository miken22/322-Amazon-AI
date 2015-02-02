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
	
	private int DEPTH = 2;
		
	public Agent(int ourColour){
		this.role = ourColour;
	}
	
	public Agent(int ourColour, int depth){
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
		return moveChoice;
	}
}
