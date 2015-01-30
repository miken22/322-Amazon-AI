package ai.search;

import java.util.ArrayList;
import java.util.List;

import ai.Actions;
import ai.Board;
import ai.Pair;

public class SuccessorGenerator {

	
	private Actions actions;

	private int OURCOLOUR;
	private final int ARROW = 3;
	private final int FREE = -1;
	
	public SuccessorGenerator(int ourColour){
		this.OURCOLOUR = ourColour;
	}
	
	public List<Board> getSuccessors(Board board){
		
		List<Board> successors = new ArrayList<>();
		List<Pair<Integer, Integer> > queenPositions;
		
		if (OURCOLOUR == 1){
			queenPositions = board.getWhitePositions();
		} else {
			queenPositions = board.getBlackPositions();
		}
		
		for (Pair<Integer, Integer> p : queenPositions){
			
			int currentX = p.getLeft();
			int currentY = p.getRight();
			
			for (int i = 0; i < 10; i++){
				
				
				
			}
			
		}
		
		
		return successors;
	}
		
	private boolean moveIsValid(Board board, int sX, int sY, int dX, int dY, boolean isArrow){

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
		
		// Must have the same rise as run for a legal diagonal move.
		if (Math.abs(sX - dX) != Math.abs(sY - dY)){
			return false;
		}

		// Diagonal checks
		if(sX > dX && sY > dY){	
			// This is the moving from a square to one to its lower left
			int temp = sX;
			sX = dX;
			dX = temp;	

			temp = sY;
			sY = dY;
			dY = temp;	
			return checkFirstDiagonal(board, sX, sY, dX, dY);
		} else if (sX < dX && sY < dY){
			// Case where we move from a square to one to its upper right
			return checkFirstDiagonal(board, sX, sY, dX, dY);
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
	 * @param sX - Smaller x value.
	 * @param sY - Smaller y value.
	 * @param dX - Larger x value.
	 * @param dY - Larger y value.
	 * @return
	 */
	private boolean checkFirstDiagonal(Board board, int sX, int sY, int dX, int dY){		
		while (sX <= dX || sY <= dY){
			if (board.getPiece(sX, sY) != FREE && board.getPiece(sX, sY) != OURCOLOUR){
				return false;
			}
			sX = sX + 1;
			sY = sY + 1;
		}
		return true;
	}

	/**
	 * Simple calculation to check the other two diagonal directions. We determine if we are moving up left
	 * or down right, in either case deltaX = -(deltaY) so we compute them and iteratively move along the diagonal
	 * like the other check does.
	 * 
	 * @param sX - Starting x value.
	 * @param sY - Starting y value.
	 * @param dX - Ending x value.
	 * @param dY - Ending y value.
	 * @return
	 */
	private boolean oppositeDiagonal(Board board, int sX, int sY, int dX, int dY){

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
