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
		
		if (move == 0){
			move = 1;
			hMinimax.setMaxDepth(1);
			if (role == 1){
				return selectOpeningMove();
			}
		}
		

		if (move == 16){
			hMinimax.setMaxDepth(3);
		} 
//		else if (move == 8){
//			hMinimax.setMaxDepth(6);
//		}

		move++;
		int[] moveChoice = hMinimax.maxSearch(currentBoard, role);
		
		for (int i = 0; i < moveChoice.length; i++){
			if(moveChoice[i] != 0){
				return moveChoice;
			}
		}
		
		// If we reach end of this loop the operator says to stand still and shoot an arrow
		// at yourself, not possible so we must be in a goal state.
		return null;
		
	}

	// TODO: Figure out opening move strategies
	private int[] selectOpeningMove() {
		int[] openingMove = { 0, 3, 7, 3, 5, 1 };
		return openingMove;
	}
}
