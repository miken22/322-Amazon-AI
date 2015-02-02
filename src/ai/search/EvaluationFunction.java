package ai.search;

import ai.Board;

/**
 * Interface to be implemented by heuristic functions for search
 * 
 * @author Mike Nowicki
 *
 */
public abstract class EvaluationFunction {

	public int OURCOLOUR;
	public int OPPONENT;
	
	public final int WQUEEN = 1;
	public final int BQUEEN = 2;
	public final int ARROW = 3;
	public final int FREE = -1;
	
	
	public EvaluationFunction(int role){
		
		OURCOLOUR = role;
		
		if (OURCOLOUR == WQUEEN){
			OPPONENT = BQUEEN;
		} else {
			OPPONENT = WQUEEN;
		}
		
	}
	
	/**
	 * Returns the move for the most promising state
	 * 
	 * @param board - State of the amazons game being evaluated
	 * @param player - The player being evaluated, {@value 1} white, {@value 2} for black
	 * 
	 * @return - The heuristic value of the state
	 */
	public abstract int evaluate(Board board, int player);

}
