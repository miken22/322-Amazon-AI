package ai;

import ai.singleplayer.Board;

public class SuccessorGenerator {

	
	private Board board;

	private int OURCOLOUR;
	private final int ARROW = 3;
	private final int FREE = -1;
	
	public SuccessorGenerator(Board board, int ourColour){
		this.board = board;
		this.OURCOLOUR = ourColour;
	}
	
	
	
	public boolean isMoveValid(int fRow, int fCol, int tRow, int tCol, int aRow, int aCol){
		
		// Check starting from an owned piece
		if (board.getPiece(fRow, fCol) != OURCOLOUR){
			return false;
		}

		// Verify move is valid (straight/diagonal, no obstructions)
		if (!moveIsValid(fRow,fCol,tRow,tCol,false)){
			return false;
		}
		
		// Amazon piece is valid, we update the logic part of the board
		board.freeSquare(fRow,fCol);
		board.placeMarker(tRow,tCol, OURCOLOUR);
		
		// If the arrow throw is invalid, we must put the queen back and ask for a new move
		if (!moveIsValid(tRow,tCol,aRow,aCol,true)){
			board.freeSquare(tRow,tCol);
			board.placeMarker(fRow,fCol, OURCOLOUR);
			return false;
		}

		// Otherwise, place the arrow on the board, update the records and GUI
		board.placeMarker(aRow, aCol, ARROW);

		board.updateBlackPositions(fRow, fCol, tRow, tCol);
		
		return true;
	}
	
	private boolean moveIsValid(int sX, int sY, int dX, int dY, boolean isArrow){

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
			return checkFirstDiagonal(sX, sY, dX, dY);
		} else if (sX < dX && sY < dY){
			// Case where we move from a square to one to its upper right
			return checkFirstDiagonal(sX, sY, dX, dY);
		} else {
			// The other two diagonal directions
			return oppositeDiagonal(sX, sY, dX, dY);
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
	private boolean checkFirstDiagonal(int sX, int sY, int dX, int dY){		
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
	private boolean oppositeDiagonal(int sX, int sY, int dX, int dY){

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
