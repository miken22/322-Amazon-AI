package ai;

import ai.search.Agent;
import ai.search.TrivialFunction;


/**
 * Using this to test the implementation of the minimax search algorithm and trivial heuristic. Want to demonstrate
 * the ways that the classes and interfaces can be used to make a generic search.
 * 
 * @author mike-nowicki
 *
 */
public class TestClass {
	
	public static void main(String[] args){
		
		Agent agent = new Agent(10, 10, 1);
		
		agent.setupHeuristic(new TrivialFunction(1));

		Board board = new Board(10, 10);
		board.initialize();
		
		agent.selectMove(board);
				
	}

}
