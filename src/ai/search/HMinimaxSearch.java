package ai.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ai.Board;

/**
 * The general minimax heuristic search algorithm
 * 
 * @author Mike Nowicki
 *
 */
public class HMinimaxSearch implements Minimax {
	
	/**
	 * The heuristic being used
	 */
	EvaluationFunction evaluator;

	private int ALPHA;
	private int BETA;
	public int MAXDEPTH = 4;
	
	public SuccessorGenerator scg;

	private long startTime;
	
	/**
	 * Doesn't work.
	 */
	HashMap<int[][], Integer> stateValues;
	List<int[]> ties;
	
	public HMinimaxSearch(EvaluationFunction evaluator){
		this.evaluator = evaluator;
		scg = new SuccessorGenerator();
		stateValues = new HashMap<>();
		ties = new ArrayList<>();
	}
	public HMinimaxSearch(EvaluationFunction evaluator, int depth){
		this.evaluator = evaluator;
		MAXDEPTH = depth;
		scg = new SuccessorGenerator();
		stateValues = new HashMap<>();
		ties = new ArrayList<>();
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
	@Override
	public int[] maxSearch(Board board, int player){
		
		int max = Integer.MIN_VALUE;
		int[] move = new int[6];
		
		ALPHA = Integer.MIN_VALUE;
		BETA = Integer.MAX_VALUE;
		
		if (stateValues.size() > 250000){
			stateValues.clear();
		}
				
		List<int[]> potentialActions = scg.getSuccessors(board, player);
		
		startTime = System.currentTimeMillis();
		
		for (int[] action : potentialActions){
			
			Board child = scg.generateBoard(board, action, player);
			
			int result = minVal(child, 1, player);
			
			if (result > max){
				max = result;
				move = action;
				ties.clear();
			} else if (result == max){
				ties.add(action);
			}
			
			// Want to find the maximum value that we can achieve after the opponent tries to minimize us optimally
			// Need some kind of tiebreaking, use of ordering of top solutions
			
			if (((System.currentTimeMillis() - startTime) / 1000) % 60  >= 28){
				break;
			}
		}
		
		if (ties.size() > 0){
			move = tieBreaker();
		}
		
		System.out.println("Best estimate: " + max);
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
	@Override
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
	@Override
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
	
	@Override
	public int[] tieBreaker() {
		
		int[] topSelection = new int[6];
		int mostGround = 0;
		
		
		for (int[] move : ties){
		
			int dQ = Math.abs(move[2] - move[0]) + Math.abs(move[3] - move[1]);
			int dA = Math.abs(move[5] - move[3]) + Math.abs(move[4] - move[1]);
			
			if (dQ + dA > mostGround){
				topSelection = move;
			}
			
		}
		return topSelection;
	}
}
