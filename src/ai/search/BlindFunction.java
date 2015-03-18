package ai.search;

import ai.Board;

/**
 * Blind heuristic function for testing purposes
 * 
 * @author Michael Nowicki
 *
 */
public class BlindFunction extends EvaluationFunction {

	public BlindFunction(int role) {
		super(role);
		
	}

	@Override
	public int evaluate(Board board, int player) {
		return 0;
	}

}
