package ai.search;

import ai.Board;

public class TrivialFunction extends EvaluationFunction {

	public TrivialFunction(int role){
		super(role);
	}
	
	@Override
	public int evaluate(Board board, int player) {
		
		
		return 0;
	}
}
