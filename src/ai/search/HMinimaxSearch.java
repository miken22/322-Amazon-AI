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
	private int MAXDEPTH;
	private int ourPlayer;
	
	private SuccessorGenerator scg;
	private Timer timer;
	
	private final int ABSOLUTEDEPTH = 10;
	
	
	List<int[]> ties;
	
	public HMinimaxSearch(EvaluationFunction evaluator){
		this.evaluator = evaluator;
		scg = new SuccessorGenerator();
		ties = new ArrayList<>();
		timer = new Timer();
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
		
		MAXDEPTH = 1;
		
		while (!timer.hasStarted() || timer.isStillValid()){

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

			}
			MAXDEPTH++;
			if (MAXDEPTH > ABSOLUTEDEPTH){
				break;
			}
		}


		
		if (potentialActions.size() == 0){
			System.out.println("No possible moves detected.");
		}
		
		if (ties.size() > 0){
			move = tieBreaker();
		}

		ties.clear();
		timer.resetTimer();
		
		System.out.println("Best estimate: " + max);
		System.out.println("Got to depth: " + MAXDEPTH);
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
			
			int result = minVal(child, depth+1, player);

			max = Math.max(max, result);

			if (timer.almostExpired()){
				return max;
			}
			
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

			int result = maxVal(child, depth+1, player);
			
			min = Math.min(min, result);
			
			if (timer.almostExpired()){
				return min;
			}
			
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
	
	public void startTimer(){
		timer.startTiming();
	}
	
}
