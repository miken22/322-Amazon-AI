package ai.search;

import ai.Actions;
import ai.Board;

/**
 * Class that contains all the common methods for successor generation during gametree search.
 * 
 * @author Mike Nowicki
 *
 */
public class GameTreeSearch {

	public Actions actions = new Actions();

	public final int WQUEEN = 1;
	public final int BQUEEN = 2;
	public final int ARROW = 3;
	public final int FREE = -1;

	public boolean moveIsValid(Board board, int sX, int sY, int dX, int dY, int player, boolean isArrow){

		if (dX < 0 || dX > 9){
			return false;
		}
		if (dY < 0 || dY > 9){
			return false;
		}

		// Check horizontal move, make sure no obstacles
		if (sX == dX){

			if (sY == dY){
				// No move, just throw arrow, not allowed to throw into your own tile
				if (isArrow){
					return false;
				} else {
					// Otherwise you are not moving, still valid I guess?
					return true;
				}
			}

			// Get change in column, find if positive/negative, use to increment to new position checking each tile on the way.
			int deltaY = dY - sY;
			deltaY = deltaY / Math.abs(deltaY);

			while(sY != dY){
				sY += deltaY;
				if (board.isMarked(dX,sY)){
					return false;
				}
			}
			return true;
		}
		// Check vertical move, same thing as above
		if (sY == dY){

			int deltaX = dX - sX;
			deltaX = deltaX / Math.abs(deltaX);

			while (sX != dX){
				sX += deltaX;
				if (board.isMarked(sX, dY)){
					return false;
				}
			}
			return true;
		}

		// Must have the same rise as run for a legal diagonal move. Might be redundant with preset moves now
		if (Math.abs(sX - dX) != Math.abs(sY - dY)){
			return false;
		}

		// Diagonal checks
		if(sX > dX && sY > dY){	
			return checkDownLeftDiagonal(board, sX, sY, dX, dY, 1);
		} else if (sX < dX && sY < dY){
			// Case where we move from a square to one to its upper right
			return checkUpRightDiagonal(board, sX, sY, dX, dY, 1);
		} else {
			// The other two diagonal directions
			return oppositeDiagonal(board, sX, sY, dX, dY);
		}
	}
	
	/**
	 * Simple rise/run calculation to check that the diagonal is valid. We must take as many steps
	 * left/right as we do up/down to have a valid diagonal move. Must be sure to swap start, end
	 * nodes if sX and sY > dX and dY for the algorithm to hold.
	 * 
	 * @param sX - Starting x value.
	 * @param sY - Starting y value.
	 * @param dX - Ending x value.
	 * @param dY - Ending y value.
	 * @return - {@code TRUE} if valid, {@code FALSE} otherwise.
	 */
	private boolean checkDownLeftDiagonal(Board board, int sX, int sY, int dX, int dY, int i) {

		while (sX > dX || sY > dY){
			sX--;
			sY--;
			if (board.isMarked(sX, sY)){
				return false;
			}
		}
		return true;

	}

	/**
	 * Simple rise/run calculation to check that the diagonal is valid. We must take as many steps
	 * left/right as we do up/down to have a valid diagonal move. Must be sure to swap start, end
	 * nodes if sX and sY > dX and dY for the algorithm to hold.
	 * 
	 * @param sX - Starting x value.
	 * @param sY - Starting y value.
	 * @param dX - Ending x value.
	 * @param dY - Ending y value.
	 * @return - {@code TRUE} if valid, {@code FALSE} otherwise.
	 */
	public boolean checkUpRightDiagonal(Board board, int sX, int sY, int dX, int dY, int player){		
		while (sX < dX || sY < dY){
			sX++;
			sY++;
			if (board.isMarked(sX, sY)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Simple calculation to check the other two diagonal directions. We determine if we are moving up left
	 * or down right, in either case deltaX = -(deltaY) so we compute them and iteratively move along the diagonal
	 * like the other check does.
	 * 
	 * 
	 * @param sX - Starting x value.
	 * @param sY - Starting y value.
	 * @param dX - Ending x value.
	 * @param dY - Ending y value.
	 * @return - {@code TRUE} if valid, {@code FALSE} otherwise.
	 */
	public boolean oppositeDiagonal(Board board, int sX, int sY, int dX, int dY){

		int deltaX = dX - sX;
		deltaX = deltaX/Math.abs(deltaX);

		int deltaY = deltaX/(-1);

		while (sX != dX || sY != dY){
			sX += deltaX;
			sY += deltaY;
			if (board.isMarked(sX, sY)){
				return false;
			}
		}
		return true;
	}
}
