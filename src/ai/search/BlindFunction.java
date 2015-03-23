package ai.search;

import ai.Board;

/**
 * Blind heuristic function for testing purposes
 * 
 * @author Michael Nowicki
 *
 */
public class BlindFunction extends EvaluationFunction {

	public BlindFunction(byte role) {
		super(role);
		
	}

	@Override
	public int evaluate(Board board, byte player) {
		return 0;
	}

}
