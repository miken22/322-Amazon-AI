package ai.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import ai.Board;
import ai.Pair;
import ai.Timer;

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
	private int opponent;

	private SuccessorGenerator scg;
	private Timer timer;

	private final byte ABSOLUTEDEPTH = 12;

	private static int ALPHA = Integer.MIN_VALUE;
	private static int BETA = Integer.MAX_VALUE;

	// Since there are so many collisions we can store a list, check each pair
	private HashMap<Long, ArrayList<Pair<byte[][], Integer> > > transitionTable;
	private int cacheHits = 0;

	List<byte[]> ties;

	public HMinimaxSearch(EvaluationFunction evaluator){
		this.evaluator = evaluator;
		scg = new SuccessorGenerator();
		ties = new ArrayList<>();
		transitionTable = new HashMap<>();		
	}

	/**
	 * Takes a state and depth to perform a limited alpha-beta search. We start from a max node, generate it's children
	 * and call the alpha-beta search to examine the min nodes next, alternating each level of the search.
	 * 
	 * @param board - Current state of the game
	 * @param player - The player colour our agent is
	 * @return - The move combination to get to the next best state
	 */
	@Override
	public byte[] maxSearch(Board board, int player){

		timer = new Timer();
		timer.startTiming();

		int max = Integer.MIN_VALUE;
		byte[] move = null;
		cacheHits = 0;

		// Setup alpha beta bounds
		ALPHA = Integer.MIN_VALUE;
		BETA = Integer.MAX_VALUE;

		ourPlayer = (byte)player;

		if (ourPlayer == 1){
			opponent = 2;
		} else {
			opponent = 1;
		}

		// TODO: Figure out a way to store boards, would make a huge difference...
		if (transitionTable.size() > 1000000){
			System.out.println("Flushing transition table.");
			transitionTable.clear();
		}

		DEPTH = 1;
		int searchDepth = DEPTH;
		// Get list of possible actions that can be made from the state
		List<byte[]> potentialActions = scg.getRelevantActions(board, player);

		// Timer controlled search, level 0 of the search
		while (!timer.almostExpired()){

			if (potentialActions.size() == 0){
				break;
			}

			// Generate the child of the root state, performing depth first alpha-beta search
			for (int i = 0; i < potentialActions.size(); i++){

				if (timer.almostExpired()) {
					break;
				}

				byte[] action = potentialActions.get(i);
				Board child = scg.generateSuccessor(board, action, (byte)player);
				// Since root is a max node we want the maximum possible value given our opponent
				// makes a move to minimize our potential gains.
				ALPHA = Math.max(ALPHA, alphaBeta(child, 1, false));

				if (ALPHA > max){
					// Update move ordering to put this action at the head of the list
					potentialActions = moveToFront(potentialActions, action);
					// Store best value and action
					max = ALPHA;
					move = action;
					// Reset set of actions that lead to equally promising states
					ties.clear();
					ties.add(action);
				} else if (ALPHA == max){
					potentialActions = moveToFront(potentialActions, action);
					ties.add(action);
				}
			}		
			if (timer.almostExpired()) {
				break;
			}
			searchDepth = DEPTH;
			// Increase bounds on the search
			DEPTH++;
			// Attempt to enforce unnecessary search late in the game
			if (DEPTH > ABSOLUTEDEPTH){
				break;
			}
		}

		if (potentialActions.size() == 0){
			System.out.println("No possible moves from this state, player loses.");
		}

		System.out.println("Number of cache hits: " + cacheHits);
		cacheHits = 0;

		ties.clear();

		System.out.println("Alpha-Beta result: [" + ALPHA +"," + BETA + "]");
		System.out.println("Got to depth: " + searchDepth);
		return move;

	}

	public int alphaBeta(Board board, int searchDepth, boolean maxNode) {

		// Terminal nodes in search tree or at max depth we evaluate the board
		if (searchDepth == DEPTH || timer.almostExpired()){

			// Hash board, see if code has been generated before
			long hash = hashCode(board.getBoard());
			if (transitionTable.containsKey(hash)) {
				// Get the bucket representing all board/value tuples that hash to same value
				ArrayList<Pair<byte[][], Integer> > bucket = transitionTable.get(hash);
				byte[][] game = board.getBoard();
				// Iterate over bucket, check if any are EXACTLY equal
				for (Pair<byte[][], Integer> tuple : bucket) {
					// If they are, return the cost
					if (java.util.Arrays.deepEquals(game, tuple.getLeft())){
						cacheHits++;
						return tuple.getRight();
					}
				}
				// Otherwise new board state collides, evaluate and add to bucket
				int value = evaluator.evaluate(board, ourPlayer);
				bucket.add(new Pair<byte[][], Integer>(game, value));
				return value;
			}

			// Otherwise, first time we have seen the board, evaluate it.
			int value = evaluator.evaluate(board, ourPlayer);

			// Create a bucket and put into the hashmap
			ArrayList<Pair<byte[][], Integer> > bucket = new ArrayList<>();
			bucket.add(new Pair<byte[][], Integer>(board.getBoard(), value));
			transitionTable.put(hash, bucket);

			return value;	
		}

		// Max node
		if (maxNode){
			// Generate all possible moves for our player
			List<byte[]> potentialActions = scg.getRelevantActions(board, ourPlayer);
			if (potentialActions.size() == 0) {
				// Means we have no possible moves and should avoid this!
				return Integer.MIN_VALUE;
			}
			for (byte[] action : potentialActions){
				Board child = scg.generateSuccessor(board, action, (byte)ourPlayer);
				// Search to next depth (min node)
				int result = alphaBeta(child, searchDepth+1, false);

				if (timer.almostExpired()){
					return Math.max(ALPHA, result);
				}

				// Alpha-beta pruning
				if (result >= BETA){
					return result;
				}
				ALPHA = Math.max(ALPHA, result);
			}
			return ALPHA;
		}
		else {
			// Same logic as max nodes, but for min states instead, generate children for opponents possible moves
			List<byte[]> potentialActions = scg.getRelevantActions(board, opponent);
			if (potentialActions.size() == 0) {
				// Means opponent has no possible moves, we want this!
				return Integer.MAX_VALUE;
			}
			for (byte[] action : potentialActions){

				Board child = scg.generateSuccessor(board, action, (byte) opponent);
				int result = alphaBeta(child, searchDepth+1, true);		

				if (timer.almostExpired()){
					return Math.min(BETA, result);
				}

				if (result <= ALPHA){
					return result;
				}

				BETA = Math.min(BETA, result);
			}
			return BETA;
		}
	}

	private long hashCode(byte[][] board) {
		return java.util.Arrays.deepHashCode(board);
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

	/**
	 * When switching heuristic the old values become useless, reset the table
	 */
	public void clearTable() {
		transitionTable.clear();
	}
}
