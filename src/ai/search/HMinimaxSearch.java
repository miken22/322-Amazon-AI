package ai.search;

import java.util.HashMap;
import java.util.List;

import ai.Board;

/**
 * The general minimax heuristic search algorithm
 * 
 * @author Mike Nowicki
 *
 */
public class HMinimaxSearch {
	
	/**
	 * The heuristic being used
	 */
	EvaluationFunction evaluator;

	public int MAXDEPTH = 4;
	
	public SuccessorGenerator scg;
	
	HashMap<int[][], Integer> stateValues;
	
	public HMinimaxSearch(EvaluationFunction evaluator){
		this.evaluator = evaluator;
		scg = new SuccessorGenerator();
		stateValues = new HashMap<>();
	}
	public HMinimaxSearch(EvaluationFunction evaluator, int depth){
		this.evaluator = evaluator;
		MAXDEPTH = depth;
		scg = new SuccessorGenerator();
		stateValues = new HashMap<>();
	}

	/**
	 * Takes a state and depth to perform a limited minimax search
	 * 
	 * @param board - Current state of the game
	 * @param player - The player colour our agent is
	 * @return - The move combination to get to the next best state
	 */
	public int[] maxSearch(Board board, int player){
		
		int max = Integer.MIN_VALUE;
		int[] move = new int[6];
				
		List<int[]> potentialActions = scg.getSuccessors(board, player);
		
		for (int[] action : potentialActions){
			
			Board child = scg.generateBoard(board, action, player);
			
			int result = maxVal(child, 1, player);
			
			if (result > max){
				max = result;
				move = action;
			}
		}
		return move;
	}
	
	/**
	 * Returns the evaluation of the board for the player
	 * 
	 * @param board - State of the amazons game being evaluated
	 * @param depth - The current depth of the search
	 * @param player - The player being evaluated, {@value 1} for max, {@value 2} for min
	 * 
	 * @return - The heuristic value of the state
	 */
	public int maxVal(Board board,int depth, int player){
		
		int min = Integer.MIN_VALUE;

		// Switch roles for next generation
		if (player == 1){
			player = 2;
		} else {
			player = 1;
		}
		
		if (depth == MAXDEPTH){
			
			if (stateValues.containsKey(board.getBoard())){
				return stateValues.get(board.getBoard());
			} 
		
			int value = evaluator.evaluate(board, player);
			stateValues.put(board.getBoard(), value);
			
			return  value;	
		}
		
		List<int[]> potentialActions = scg.getSuccessors(board, player);
		
		for (int[] action : potentialActions){

			Board child = scg.generateBoard(board, action, player);
			int result = minVal(child, depth+1, player);

			min = Math.max(min, result);		
		}
		return min;
	}
	/**
	 * Returns the evaluation of the board for the player
	 * 
	 * @param board - State of the amazons game being evaluated
	 * @param depth - The current depth of the search
	 * @param player - The player being evaluated, {@value 1} for max, {@value 2} for min
	 * 
	 * @return - The heuristic value of the state
	 */
	public int minVal(Board board,int depth, int player){
		
		int max = Integer.MAX_VALUE;

		// Switch roles for next generation
		if (player == 1){
			player = 2;
		} else {
			player = 1;
		}
		
		if (depth == MAXDEPTH){
			
			if (stateValues.containsKey(board.getBoard())){
				return stateValues.get(board.getBoard());
			} 
		
			int value = evaluator.evaluate(board, player);
			stateValues.put(board.getBoard(), value);
			
			return  value;	
		}
		
		List<int[]> potentialActions = scg.getSuccessors(board, player);
		
		for (int[] action : potentialActions){

			Board child = scg.generateBoard(board, action, player);
			int result = maxVal(child, depth+1, player);
			
			max = Math.min(max, result);
		}
		return max;
	}
}
