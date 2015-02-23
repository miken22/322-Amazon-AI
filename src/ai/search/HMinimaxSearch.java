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
	private int DEPTH;
	private byte ourPlayer;

	private SuccessorGenerator scg;
	private Timer timer;

	int cacheHits = 0;

	private final byte ABSOLUTEDEPTH = 10;

	private HashMap<Integer, Integer> transitionTable;

	List<byte[]> ties;

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
	public byte[] maxSearch(Board board, int player){

		int max = Integer.MIN_VALUE;
		byte[] move = null;

		// Setup alpha beta bounds
		int ALPHA = Integer.MIN_VALUE;
		int BETA = Integer.MAX_VALUE;

		ourPlayer = (byte)player;

		// Get list of possible actions that can be made from the state
		List<byte[]> potentialActions = scg.getRelevantActions(board, player);

		if (transitionTable.size() > 1000000){
			System.out.println("Flushing transition table.");
			transitionTable.clear();
		}

		timer.startTiming();

		DEPTH = 2;

		// Timer controlled search, level 0 of the search
		while (timer.isStillValid()){

			if (potentialActions.size() == 0){
				break;
			}

			// Generate the child of the root state, performing depth first alpha-beta search
			for (int i = 0; i < potentialActions.size(); i++){

				byte[] action = potentialActions.get(i);
				Board child = scg.generateSuccessor(board, action, (byte)player);
				// Opponent wants to minimize our possible moves from the root

				int result = minVal(child, 1, ALPHA, BETA, player);

				if (result > max){
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
		
		System.out.println("Cache size: " + transitionTable.size());
		System.out.println("Number of cache hits: " + cacheHits);
		cacheHits = 0;
		
		if (potentialActions.size() == 0){
			System.out.println("No possible moves detected.");
		}

		if (ties.size() > 1){
			move = tieBreaker();
		}

		ties.clear();

		System.out.println("[" + ALPHA +"," + BETA + "]");
		System.out.println("Best estimate: " + max);
		System.out.println("Got to depth: " + DEPTH);
		return move;

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
	public int maxVal(Board board, int searchDepth, int ALPHA, int BETA, int player){

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
				cacheHits++;
				return transitionTable.get(hashValue);
			}

			int value = evaluator.evaluate(board, ourPlayer);

			transitionTable.put(hashValue, value);

			return  value;	
		}

		List<byte[]> potentialActions = scg.getRelevantActions(board, player);

		for (byte[] action : potentialActions){

			Board child = scg.generateSuccessor(board, action, (byte)player);

			int result = minVal(child, searchDepth+1, ALPHA, BETA, player);

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
	public int minVal(Board board, int searchDepth, int ALPHA, int BETA, int player){

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
				cacheHits++;
				return transitionTable.get(hashValue);
			}

			int value = evaluator.evaluate(board, ourPlayer);

			transitionTable.put(hashValue, value);

			return  value;	
		}

		List<byte[]> potentialActions = scg.getRelevantActions(board, player);

		for (byte[] action : potentialActions){

			Board child = scg.generateSuccessor(board, action, (byte)player);

			int result = maxVal(child, searchDepth+1, ALPHA, BETA, player);

			min = Math.min(min, result);

			if (timer.almostExpired()){
				return min;
			}

			if (min <= ALPHA){
				return min;
			}

			BETA = Math.min(BETA, min);

		}
		// No moves, goal state so we win, skew the results!
		if (potentialActions.size() == 0){
			min = Integer.MAX_VALUE;
		}
		return min;
	}

	/**
	 * Swaps the location of the newest best potential action by moving it to the front of the array for
	 * the next round of alpha-beta search.
	 * 
	 * @param potentialActions - The arraylist of actions.
	 * @param action - The action moving to the front.
	 * @return The updated array with the best action at the front.
	 */
	private List<byte[]> moveToFront(List<byte[]> potentialActions, byte[] action) {

		potentialActions.remove(action);

		List<byte[]> tempList = new ArrayList<>();
		tempList.add(action);
		tempList.addAll(potentialActions);

		potentialActions = tempList;

		return potentialActions;
	}

	/**
	 * Tie breaker that selects the operator at random.
	 */
	@Override
	public byte[] tieBreaker() {

		// TODO: Try to find an algorithm to pick the state that x1blocks opponent best (arrow closest or something)

		System.out.println("Number of ties: " + ties.size());

		int choice = new Random().nextInt(ties.size());
		return ties.get(choice);
	}
}
