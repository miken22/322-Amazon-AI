package ai.search;

import ai.Board;

/**
 * Interface to build different minimax game tree searches on
 * @author Mike Nowicki
 *
 */
public interface Minimax {

	/**
	 * Takes a state and depth to perform a limited minimax search
	 * 
	 * @param board - Current state of the game
	 * @param player - The player colour our agent is
	 * @return - The move combination to get to the next best state
	 */
	public byte[] alphaBetaSearch(Board board, int player);
	
	public byte[] tieBreaker(Board board);
	
}
