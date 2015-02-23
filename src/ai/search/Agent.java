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
	public byte[] selectMove(Board currentBoard){

		this.board = currentBoard;

		if (move == 0){
			move++;
			if (role == 1){
				return selectOpeningMove();
			}
		}

		move++;

		byte[] moveChoice = hMinimax.maxSearch(currentBoard, role);

		// Checks that we never pick a move standing still and shooting at self
		for (int i = 0; i < moveChoice.length; i++){
			if(moveChoice[i] != 0){
				return moveChoice;
			}
		}

		return moveChoice;

	}

	// TODO: Figure out opening move strategies
	private byte[] selectOpeningMove() {
		
		ArrayList<byte[]> openingMoves = new ArrayList<>();
				
		byte[] openingMove1 = { 0, 3, 7, 3, 5, 1 };
		byte[] openingMove2 = { 0, 6, 7, 6, 5, 8 };
		
		byte[] openingMove3 = { 0, 3, 6, 3, 6, 1 };
		byte[] openingMove4 = { 0, 6, 6, 6, 6, 8 };
		
		openingMoves.add(openingMove1);
		openingMoves.add(openingMove2);
		openingMoves.add(openingMove3);
		openingMoves.add(openingMove4);
		
		int random = new Random().nextInt(4);
		
		return openingMoves.get(random);
	}

	/**
	 * Search board starting from each of the players pieces. If one colour owns more
	 * territory than the other amazons, plus neutral tiles, then we are in a "winning"
	 * position. We should then 
	 * 
	 */
	public boolean checkIfFinished() {	

		if (winningState){
			return true;
		}

		ArrayList<Pair<Byte, Byte> > wPositions = board.getWhitePositions();
		ArrayList<Pair<Byte, Byte> > bPositions = board.getBlackPositions();


		int[][] hasChecked = new int[rows][columns];

		for (Pair<Byte, Byte> pair : wPositions){
			hasChecked = Utility.countReachableTiles(board, pair, WQUEEN, hasChecked);		
		}

		for (Pair<Byte, Byte> pair : bPositions){
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
			// Switch to a heuristic that maximizes total area controlled.
			System.out.println("Switching to max reachable tiles heuristic for endgame.");
			
			setupHeuristic(new CountReachableTilesHeuristic(role));
			return true;
		}


		return false;
	}
}
