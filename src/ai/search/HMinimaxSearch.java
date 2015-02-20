package ai.search;

import java.util.ArrayList;
import java.util.HashMap;
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
	private int DEPTH;
	private int ourPlayer;
	
	private SuccessorGenerator scg;
	private Timer timer;
	
	private final int ABSOLUTEDEPTH = 10;
	
	private HashMap<Integer, Integer> transitionTable;
	
	List<int[]> ties;
	
	public HMinimaxSearch(EvaluationFunction evaluator){
		this.evaluator = evaluator;
		scg = new SuccessorGenerator();
		ties = new ArrayList<>();
		timer = new Timer();
		transitionTable = new HashMap<>();
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
		int[] move = null;
		
		// Setup alpha beta bounds
		ALPHA = Integer.MIN_VALUE;
		BETA = Integer.MAX_VALUE;
				
		ourPlayer = player;
				
		// Get list of possible actions that can be made from the state
		List<int[]> potentialActions = scg.getRelevantActions(board, player);
		
		if (transitionTable.size() > 500000){
			transitionTable.clear();
		}
		
		timer.startTiming();
		
		DEPTH = 1;
		
		// Timer controlled search, level 0 of the search
		while (timer.isStillValid()){

			if (potentialActions.size() == 0){
				break;
			}
			
			// Generate the child of the root state, performing depth first alpha-beta search
			for (int i = 0; i < potentialActions.size(); i++){

				int[] action = potentialActions.get(i);
				
				Board child = scg.generateSuccessor(board, action, player);

				// Oppoenent wants to minimize our possible moves from the root
				int result = minVal(child, 1, player);

				if (result > max){
					// TODO: Trying a move ordering technique
					potentialActions = moveToFront(potentialActions, action);
					
					max = result;
					move = action;
					ties.clear();
				} else if (result == max){
					potentialActions = moveToFront(potentialActions, action);
					ties.add(action);
				}

			}
			// Increase bounds on the search
			DEPTH++;
			// Attempt to enforce unnecessary search late in the game
			if (DEPTH > ABSOLUTEDEPTH){
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
		
		System.out.println("Best estimate: " + max);
		System.out.println("Got to depth: " + DEPTH);
		return move;
	
	}
	
	private List<int[]> moveToFront(List<int[]> potentialActions, int[] action) {
		
		potentialActions.remove(action);
		
		List<int[]> tempList = new ArrayList<>();
		tempList.add(action);
		tempList.addAll(potentialActions);
		
		potentialActions = tempList;
		
		return potentialActions;
	}

	/**
	 * Returns the evaluation of the board for the player
	 * 
	 * @param board - State of the amazons game being evaluated
	 * @param DEPTH - The current depth of the search
	 * @param player - The player being evaluated, 1 for max, 2 for min
	 * 
	 * @return - The heuristic value of the state
	 */
	@Override
	public int maxVal(Board board,int searchDepth, int player){

		int max = Integer.MIN_VALUE;
		
		// Switch roles for next generation
		if (player == 1){
			player = 2;
		} else {
			player = 1;
		}
		
		if (searchDepth == DEPTH){
			
			int hashValue = java.util.Arrays.deepHashCode(board.getBoard());
			
			if (transitionTable.containsKey(hashValue)){
				return transitionTable.get(hashValue);
			}
			
			int value = evaluator.evaluate(board, ourPlayer);
			
			transitionTable.put(hashValue, value);
			
			return  value;	
		}
		
		List<int[]> potentialActions = scg.getRelevantActions(board, player);
		
		for (int[] action : potentialActions){

			Board child = scg.generateSuccessor(board, action, player);
			
			int result = minVal(child, searchDepth+1, player);

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
	 * @param DEPTH - The current depth of the search
	 * @param player - The player being evaluated, 1 for max, 2 for min
	 * 
	 * @return - The heuristic value of the state
	 */
	@Override
	public int minVal(Board board, int searchDepth, int player){
		
		int min = Integer.MAX_VALUE;

		// Switch roles for next generation
		if (player == 1){
			player = 2;
		} else {
			player = 1;
		}
		
		if (searchDepth == DEPTH){

			int hashValue = java.util.Arrays.deepHashCode(board.getBoard());
			
			if (transitionTable.containsKey(hashValue)){
				return transitionTable.get(hashValue);
			}
			
			int value = evaluator.evaluate(board, ourPlayer);
			
			transitionTable.put(hashValue, value);
			
			return  value;	
		}
		
		List<int[]> potentialActions = scg.getRelevantActions(board, player);
		
		for (int[] action : potentialActions){

			Board child = scg.generateSuccessor(board, action, player);

			int result = maxVal(child, searchDepth+1, player);
			
			min = Math.min(min, result);
			
			if (timer.almostExpired()){
				return min;
			}
			
			if (min <= ALPHA){
				return min;
			}
			
			BETA = Math.min(BETA, min);
						
		}
		// No moves, goal state so we win, scew the results!
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
		
		// TODO: Try to find an algorithm to pick the state that blocks opponent best (arrow closest or something)
		
		System.out.println("Number of ties: " + ties.size());
		
		int choice = new Random().nextInt(ties.size());
		return ties.get(choice);
	}
}
