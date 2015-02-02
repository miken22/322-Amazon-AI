package ai.search;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
	
	private int ALPHA;
	private int BETA;
	
	private long startTime;
	
	/**
	 * Doesn't work.
	 */
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
	
	public void setMaxDepth(int newDepth){
		MAXDEPTH = newDepth;
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
		
		ALPHA = Integer.MIN_VALUE;
		BETA = Integer.MAX_VALUE;
		
		if (stateValues.size() > 250000){
			stateValues.clear();
		}
				
		List<int[]> potentialActions = scg.getSuccessors(board, player);
		
		int modifiedMax = Integer.MIN_VALUE;
		
		
		startTime = System.currentTimeMillis();
		
		for (int[] action : potentialActions){
			
			Board child = scg.generateBoard(board, action, player);
			
			int result = minVal(child, 1, player);
			
			if (result == max){
				// Skew the always even results to pick a random move
				result = (int) (result * (1 + new Random().nextDouble()));
				if (result > modifiedMax){
					modifiedMax = result;
					move = action;
				}
			} else if (result > max){
				modifiedMax = result;
				max = result;
			}
			
			// Want to find the maximum value that we can achieve after the opponent tries to minimize us optimally
			// Need some kind of tiebreaking, use of ordering of top solutions
			
			if (((System.currentTimeMillis() - startTime) / 1000) % 60  >= 28){
				break;
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
			//stateValues.put(board.getBoard(), value);
			
			return  value;	
		}
		
		int max = Integer.MIN_VALUE;

		List<int[]> potentialActions = scg.getSuccessors(board, player);
		
		for (int[] action : potentialActions){

			Board child = scg.generateBoard(board, action, player);
			int result = minVal(child, depth+1, player);

			max = Math.max(max, result);
			
			if (((System.currentTimeMillis() - startTime) / 1000) % 60  >= 27){
				return max;
			}
			
			if (max >= BETA){
				return max;
			}
			
			ALPHA = Math.max(ALPHA, max);
						
		}
		
		if (potentialActions.size() == 0){
			int value = evaluator.evaluate(board, player);
			max = Math.max(max, value);
		}
		
		return max;
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
	public int minVal(Board board, int depth, int player){
		
		int min = Integer.MAX_VALUE;

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
			//stateValues.put(board.getBoard(), value);
			
			return  value;	
		}
		
		List<int[]> potentialActions = scg.getSuccessors(board, player);
		
		for (int[] action : potentialActions){

			Board child = scg.generateBoard(board, action, player);
			int result = maxVal(child, depth+1, player);
			
			min = Math.min(min, result);
			
			if (((System.currentTimeMillis() - startTime) / 1000) % 60  >= 27){
				return min;
			}
			
			if (min <= ALPHA){
				return min;
			}
			
			BETA = Math.min(BETA, min);
						
			
		}
		if (potentialActions.size() == 0){
			int value = evaluator.evaluate(board, player);
			min = Math.min(min, value);
		}
		return min;
	}
}
