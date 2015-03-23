package ai.search;

import java.util.ArrayList;
import java.util.List;

import ai.Actions;
import ai.Board;
import ai.Pair;

/**
 * Interface to be implemented by heuristic functions for search
 * 
 * @author Mike Nowicki
 *
 */
public abstract class EvaluationFunction {

	public int OURCOLOUR;
	public int OPPONENT;
	
	public final byte WQUEEN = 1;
	public final byte BQUEEN = 2;
	public final byte ARROW = 3;
	public final byte FREE = -1;
	
	Actions actions = new Actions();
	
	public EvaluationFunction(byte role){
		OURCOLOUR = role;
		if (OURCOLOUR == WQUEEN){
			OPPONENT = BQUEEN;
		} else {
			OPPONENT = WQUEEN;
		}
	}
	
	/**
	 * Returns the move for the most promising state.
	 * 
	 * @param board - State of the amazons game being evaluated.
	 * @param player - The player being evaluated, {@value 1} white, {@value 2} for black.
	 * @return - The heuristic value of the state
	 */
	public abstract int evaluate(Board board, byte player);
	
	/**
	 * Call this function after evaluation to test if either play has isolated pieces.
	 * 
	 * @param board - State of the amazons game being evaluated.
	 * @return - The adjustment that should be made to the heuristic value of the state.
	 */
	public int adjustForIsolatedPieces(Board board) {
		
		int adjustment = 0;
		
		ArrayList<Pair<Byte, Byte> > wPositions = board.getWhitePositions();
		ArrayList<Pair<Byte, Byte> > bPositions = board.getBlackPositions();
		
		// Use flags to indicate if pieces can move
		boolean[] whiteMoves = new boolean[4];
		boolean[] blackMoves = new boolean[4];
		// Track amazons
		int index = 0;
		for (Pair<Byte, Byte> amazon : wPositions) {
			whiteMoves[index] = canMove(board, amazon);
			index++;
		}
		
		index = 0;
		for (Pair<Byte, Byte> amazon : bPositions) {
			blackMoves[index] = canMove(board, amazon);
			index++;
		}
		
		if (OURCOLOUR == WQUEEN) {
			// Scan all our pieces, for each one that cannot move subtract one
			for (boolean b : whiteMoves) {
				if (!b) {
					adjustment--;
				}
			}
			// Scan opponents pieces, for each trapped amazon add one
			for (boolean b : blackMoves) {
				if (!b) {
					adjustment++;
				}
			}	
		} else {
			// Same logic as above, roles switched though
			for (boolean b : whiteMoves) {
				if (!b) {
					adjustment += 2;
				}
			}
			
			for (boolean b : blackMoves) {
				if (!b) {
					adjustment -= 2;
				}
			}
		}
		return adjustment;
	}
	
	/**
	 * Test if the amazon can take a step in one of the 8 possible directions.
	 * @param board - Current board configuration.
	 * @param amazon - The piece being tested.
	 * @return - True if the piece can move, false otherwise.
	 */
	public boolean canMove(Board board, Pair<Byte, Byte> amazon) {
		
		List<byte[]> moves = actions.getSimpleMoves();
		// Try step in each possible direction
		for (byte[] step : moves) {
			// Add step to amazon, if the square is not marked the amazon has at least one move
			// so can return true;
			int testX = amazon.getLeft() + step[0];
			int testY = amazon.getRight() + step[1];
			
			if ((testX >= 0 && testX < 10) && (testY >= 0 && testY < 10)){
				// Piece can take at least one step, return true
				if (!board.isMarked(testX, testY)) {
					return true;
				}
			}
		}
		// Have tried taking one step in each direction, could not so
		// the piece must be trapped.
		return false;
	}

}
