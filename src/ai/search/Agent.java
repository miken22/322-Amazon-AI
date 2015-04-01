package ai.search;

import java.util.ArrayList;
import java.util.Random;

import ai.Board;

/**
 * Search agent
 * 
 * @author Mike Nowicki
 *
 */
public class Agent implements Search {

	private Minimax hMinimax;

	private byte role;
	private int move = 0;
	int rows = 10;
	int columns = 10;

	private Board board;

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

		byte[] moveChoice = hMinimax.maxSearch(board, role);

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
}
