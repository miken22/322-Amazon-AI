package ai.search;

import ai.Board;

/**
 * The interface that a search agent should use during gameplay
 * 
 * @author Mike Nowicki
 *
 */
public interface Search {

	public final int WQUEEN = 1;
	public final int BQUEEN = 2;
	public final int ARROW = 3;
	public final int FREE = -1;	 

	public byte[] selectMove(Board currentBoard);
	
}
