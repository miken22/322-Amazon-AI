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
	private int move = 0;
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
		
		if (move == 0 && role == 1){
			move++;
			hMinimax.setMaxDepth(2);
			return selectOpeningMove();
		}
		
		if (move > 4){
			hMinimax.setMaxDepth(2);
		} else if (move > 6){
			hMinimax.setMaxDepth(3);
		} else if (move > 8){
			hMinimax.setMaxDepth(4);
		} else if (move > 11){
			hMinimax.setMaxDepth(6);
		}

		move++;
		int[] moveChoice = hMinimax.maxSearch(currentBoard, role);
		
		for (int i = 0; i < moveChoice.length; i++){
			if(moveChoice[i] != 0){
				return moveChoice;
			}
			// If we reach this state we hit a goal condition, needs to be properly handled.
		}
		
		return null;
		
	}

	// TODO: Figure out opening move strategies
	private int[] selectOpeningMove() {
		int[] openingMove = { 0, 3, 7, 3, 5, 1 };
		return openingMove;
	}
}
