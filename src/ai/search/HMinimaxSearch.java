package ai.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ai.Board;
import ai.singleplayer.Timer;

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
	private EvaluationFunction evaluator;

	private int ALPHA;
	private int BETA;
	public int MAXDEPTH = 2;
	private int ourPlayer;
	
	private SuccessorGenerator scg;
	private Timer timer;
	
	List<int[]> ties;
	
	public HMinimaxSearch(EvaluationFunction evaluator){
		this.evaluator = evaluator;
		scg = new SuccessorGenerator();
		ties = new ArrayList<>();
		timer = new Timer();
	}
	public HMinimaxSearch(EvaluationFunction evaluator, int depth){
		this.evaluator = evaluator;
		MAXDEPTH = depth;
		scg = new SuccessorGenerator();
		ties = new ArrayList<>();
		timer = new Timer();
	}
	
	public void setMaxDepth(int newDepth){
		this.MAXDEPTH = newDepth;
	}

	/**
	 * Takes a state and depth to perform a limited alpha-beta search
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
				
		ourPlayer = player;
				
		List<int[]> potentialActions = scg.getRelevantActions(board, player);
		
		timer.startTiming();
		
		while (timer.isStillValid()){

			for (int[] action : potentialActions){
				
				Board child = scg.generateSuccessor(board, action, player);
				
				int result = minVal(child, 1, player);
				
				if (result > max){
					max = result;
					move = action;
					ties.clear();
				} else if (result == max){
					ties.add(action);
				}
				
				// Want to find the maximum value that we can achieve after the opponent tries to minimize us optimally
				// Need some kind of tie breaking, use of ordering of top solutions
				
//				if (timer.almostExpired()){
//					break;
//				}
			}
		}
		
		if (potentialActions.size() == 0){
			System.out.println("No possible moves detected.");
		}
		
		if (ties.size() > 0){
			move = tieBreaker();
		}

		ties.clear();
		System.out.println("Best estimate: " + max);
		return move;
	
	}
	
	/**
	 * Returns the evaluation of the board for the player
	 * 
	 * @param board - State of the amazons game being evaluated
	 * @param depth - The current depth of the search
	 * @param player - The player being evaluated, 1 for max, 2 for min
	 * 
	 * @return - The heuristic value of the state
	 */
	@Override
	public int maxVal(Board board,int depth, int player){

		int max = Integer.MIN_VALUE;
		
		// Switch roles for next generation
		if (player == 1){
			player = 2;
		} else {
			player = 1;
		}
		
		if (depth == MAXDEPTH){
			int value = evaluator.evaluate(board, ourPlayer);
			return  value;	
		}
		
		List<int[]> potentialActions = scg.getRelevantActions(board, player);
		
		for (int[] action : potentialActions){

			Board child = scg.generateSuccessor(board, action, player);
			
			if (timer.almostExpired()){
				return Math.max(Integer.MIN_VALUE, max);
			}
			
			int result = minVal(child, depth+1, player);

			max = Math.max(max, result);
			
			if (max >= BETA){
				return max;
			}
			
			ALPHA = Math.max(ALPHA, max);
						
		}
		// We ran out of moves, no good!
		if (potentialActions.size() == 0){
			max = Integer.MIN_VALUE;
		}
		
		return max;
	}
	/**
	 * 
	 * Returns the evaluation of the board for the player
	 * 
	 * @param board - State of the amazons game being evaluated
	 * @param depth - The current depth of the search
	 * @param player - The player being evaluated, 1 for max, 2 for min
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
			int value = evaluator.evaluate(board, ourPlayer);
			return  value;	
		}
		
		List<int[]> potentialActions = scg.getRelevantActions(board, player);
		
		for (int[] action : potentialActions){

			Board child = scg.generateSuccessor(board, action, player);

			if (timer.almostExpired()){
				return Math.max(Integer.MAX_VALUE, min);
			}
			
			int result = maxVal(child, depth+1, player);
			
			min = Math.min(min, result);
			
			if (min <= ALPHA){
				return min;
			}
			
			BETA = Math.min(BETA, min);
						
		}
		// No moves, goal state so we win!
		if (potentialActions.size() == 0){
			min = Integer.MAX_VALUE;
		}
		return min;
	}
	/**
	 * Tie breaker that selects the operator at random.
	 */
	@Override
	public int[] tieBreaker() {
		int choice = new Random().nextInt(ties.size());
		return ties.get(choice);
	}
}
