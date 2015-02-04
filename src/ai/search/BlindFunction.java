package ai.search;

import ai.Board;

public class BlindFunction extends EvaluationFunction {

	public BlindFunction(int role) {
		super(role);
		
	}

	@Override
	public int evaluate(Board board, int player) {
		return 1;
	}

}
