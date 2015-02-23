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
	private int opponent;

	private SuccessorGenerator scg;
	private Timer timer;

	int cacheHits = 0;

	private final byte ABSOLUTEDEPTH = 10;

	private static int ALPHA = Integer.MIN_VALUE;
	private static int BETA = Integer.MAX_VALUE;
	
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
	 * Takes a state and depth to perform a limited alpha-beta search. We start from a max node, generate it's children
	 * and call the alpha-beta search to examine the min nodes next, alternating each level of the search.
	 * 
	 * @param board - Current state of the game
	 * @param player - The player colour our agent is
	 * @return - The move combination to get to the next best state
	 */
	@Override
	public byte[] maxSearch(Board board, int player){

		timer.startTiming();
		
		int max = Integer.MIN_VALUE;
		byte[] move = null;
		
		// Setup alpha beta bounds
		ALPHA = Integer.MIN_VALUE;
		BETA = Integer.MAX_VALUE;

		ourPlayer = (byte)player;
		
		if (ourPlayer == 1){
			opponent = 2;
		} else {
			opponent = 1;
		}
		
		if (transitionTable.size() > 1000000){
			System.out.println("Flushing transition table.");
			transitionTable.clear();
		}

		DEPTH = 2;
		// Get list of possible actions that can be made from the state
		List<byte[]> potentialActions = scg.getRelevantActions(board, player);
						
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

				ALPHA = Math.max(ALPHA, alphaBeta(child, 1, false));

				if (ALPHA > max){
					potentialActions = moveToFront(potentialActions, action);

					max = ALPHA;
					move = action;
					ties.clear();
				} else if (ALPHA == max){
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

		if (ties.size() > 1){
			move = tieBreaker();
		}

		ties.clear();

		System.out.println("[" + ALPHA +"," + BETA + "]");
		System.out.println("Best estimate: " + max);
		System.out.println("Got to depth: " + DEPTH);
		return move;

	}
	
	public int alphaBeta(Board board, int searchDepth, boolean maxNode) {
				
		// Terminal nodes in search tree or at max depth we evaluate the board
		if (searchDepth == DEPTH){
			int hashValue = java.util.Arrays.deepHashCode(board.getBoard());
			if (transitionTable.containsKey(hashValue)){
				cacheHits++;
				return transitionTable.get(hashValue);
			}

			int value = evaluator.evaluate(board, ourPlayer);
			transitionTable.put(hashValue, value);
			return value;	
		}
		
		// Max node
		if (maxNode){
			// Generate all possible moves for our player
			List<byte[]> potentialActions = scg.getRelevantActions(board, ourPlayer);
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
