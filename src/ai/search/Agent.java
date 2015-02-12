package ai.search;

import java.util.Random;
import java.util.concurrent.Callable;

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
	
	private boolean thinking = false;
		
	public Agent(int ourColour){
		this.role = ourColour;
	}
	
	public void setupHeuristic(EvaluationFunction function){
		hMinimax = new HMinimaxSearch(function);
	}

	/**
	 * Method to return the move that the search agent has selected. Six entries must be in the move
	 * array at the time of return: FromX, FromY, ToX, ToY, aRow, aCol.  
	 */
	public int[] selectMove(Board currentBoard){
		
		if (move == 0){
			move = 1;
			if (role == 1){
				return selectOpeningMove();
			}
		}
		
		move++;
		
		thinking = true;
		
		int[] moveChoice = hMinimax.maxSearch(currentBoard, role);

		thinking = false;
		
		// Checks that we never pick a move standing stil and shooting at self
		for (int i = 0; i < moveChoice.length; i++){
			if(moveChoice[i] != 0){
				return moveChoice;
			}
		}
		
		return null;
		
	}

	// TODO: Figure out opening move strategies
	private int[] selectOpeningMove() {
		int[] openingMove1 = { 0, 3, 7, 3, 5, 1 };
		int[] openingMove2 = { 0, 6, 7, 6, 5, 8 };
		
		int random = new Random().nextInt() % 2;
		if (random == 0) {
			return openingMove1;
		}
		return openingMove2;
	}
	
	public void startTimer(){
		hMinimax.startTimer();
	}

	public boolean isThinking(){
		return thinking;
	}

}
