package ai.search;

import ai.Board;

public interface Search {

	public final int WQUEEN = 1;
	public final int BQUEEN = 2;
	public final int ARROW = 3;
	public final int FREE = -1;	 

	public int[] selectMove(Board currentBoard);
	
}
