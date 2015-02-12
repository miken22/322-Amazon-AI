package ai.search;

import java.util.ArrayList;
import java.util.Random;

import ai.Board;
import ai.Pair;
import ai.Utility;

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
	int rows = 10;
	int columns = 10;

	private Board board;

	private boolean winningState = false;


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

		this.board = currentBoard;

		if (move == 0){
			move = 1;
			if (role == 1){
				return selectOpeningMove();
			}
		}

		move++;

		int[] moveChoice = hMinimax.maxSearch(currentBoard, role);

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

	/**
	 * Search board starting from each of the players pieces. If one colour owns more
	 * territory than the other amazons, plus neutral tiles, then we are in a "winning"
	 * position. We should then 
	 * 
	 */
	public boolean isFinished() {	

		if (winningState){
			return true;
		}

		ArrayList<Pair<Integer, Integer> > wPositions = board.getWhitePositions();
		ArrayList<Pair<Integer, Integer> > bPositions = board.getBlackPositions();


		int[][] hasChecked = new int[rows][columns];

		for (Pair<Integer, Integer> pair : wPositions){
			hasChecked = Utility.countReachableTiles(board, pair, WQUEEN, hasChecked);		
		}

		for (Pair<Integer, Integer> pair : bPositions){
			hasChecked = Utility.countReachableTiles(board, pair, BQUEEN, hasChecked);
		}

		int whiteTiles = 4;
		int blackTiles = 4; 
		int bothCanReach = 0;

		for (int i = 0; i < rows; i++){
			for (int j = 0; j < columns; j++){
				switch(hasChecked[i][j]){
				case(1):
					whiteTiles++;
				break;
				case(2):
					blackTiles++;
				break;
				case(3):
					bothCanReach++;
				break;
				}
			}
		}


		// Test for winning or losing winning state
		if ((whiteTiles > blackTiles + bothCanReach) || (blackTiles > whiteTiles + bothCanReach)) {
			winningState = true;
			return true;
		}


		return false;
	}
}
