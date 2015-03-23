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

	private byte role;
	private int move = 0;
	int rows = 10;
	int columns = 10;

	private Board board;

	private boolean winningState = false;


	public Agent(byte ourColour){
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
		byte[] openingMove2 = { 0, 3, 8, 3, 6, 1 };
		byte[] openingMove3 = { 0, 6, 7, 6, 5, 8 };
		
		byte[] openingMove4 = { 0, 3, 6, 3, 6, 1 };
		byte[] openingMove5 = { 0, 6, 6, 6, 6, 8 };
		byte[] openingMove6 = { 0, 6, 8, 6, 6, 8 };
		
		openingMoves.add(openingMove1);
		openingMoves.add(openingMove2);
		openingMoves.add(openingMove3);
		openingMoves.add(openingMove4);
		openingMoves.add(openingMove5);
		openingMoves.add(openingMove6);
		
		int random = new Random().nextInt(6);
		
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

		byte[][] hasChecked = new byte[rows][columns];

		// Scan from each amazon, mark each tile an amazon can reach
		for (Pair<Byte, Byte> pair : wPositions){
			hasChecked = Utility.countReachableTiles(board, pair, (byte) WQUEEN, hasChecked);		
		}

		for (Pair<Byte, Byte> pair : bPositions){
			hasChecked = Utility.countReachableTiles(board, pair, (byte) BQUEEN, hasChecked);
		}

		int whiteTiles = 4;
		int blackTiles = 4; 
		int bothCanReach = 0;

		// Scan the abstracted board
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < columns; j++){
				switch(hasChecked[i][j]){
				case(1):
					whiteTiles++;	// White controlled
					break;
				case(2):
					blackTiles++;	// Black controlled
					break;
				case(3):
					bothCanReach++;	// Neutral tile
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
			System.out.println("Cleared the transition table.");
			hMinimax.clearTable();
			return true;
		}


		return false;
	}
}
