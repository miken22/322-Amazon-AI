package ai;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Utility class to help format moves, convert columns to integers
 * and test for endgame states.
 * 
 * @author Michael Nowicki
 *
 */

public class Utility {
	
	public static final byte WQUEEN = 1;
	public static final byte BQUEEN = 2;
	public static final byte ARROW = 3;

	private static final int ROWS = 10;
	private static final int COLS = 10;
	

	/**
	 * Convert letter representation for column to integer
	 * 
	 * @param c - Char for the column being represented
	 * @return - The integer value for the column
	 */
	public static int getColumn(char c){
	
		switch(c){
			case('a'):
				return 0;
			case('b'):
				return 1;
			case('c'):
				return 2;
			case('d'):
				return 3;
			case('e'):
				return 4;
			case('f'):
				return 5;
			case('g'):
				return 6;
			case('h'):
				return 7;
			case('i'):
				return 8;
			case('j'):
				return 9;
		}
		
		return -1;
	}
	/**
	 * Convert integer to letter representation
	 * 
	 * @param c - Int for the column being represented
	 * @return - The character rep for the column
	 */
	public static char getColumnLetter(int c){
		switch(c){
		case(0):
			return 'a';
		case(1):
			return 'b';
		case(2):
			return 'c';
		case(3):
			return 'd';
		case(4):
			return 'e';
		case(5):
			return 'f';
		case(6):
			return 'g';
		case(7):
			return 'h';
		case(8):
			return 'i';
		case(9):
			return 'j';
	}
	
	return ' ';
	}
	
	
	/**
	 * This is a stack based search of the game board to count the number of tiles an amazon
	 * owns, or that is neutral. We score the board 1 for white, 2 for black, and 3 for neutral
	 * with 4 representing where queens are positioned.
	 * 
	 * 
	 * @param board - Current state
	 * @param source - The amazon we are scanning out from
	 * @param player - The colour we are
	 * @param hasChecked - A 2D int representing owned and neutral tiles
	 * @return The updated 2D array indicating what could be reached from that queen
	 */
	public static byte[][] countReachableTiles(Board board, Pair<Byte, Byte> source, byte player, byte[][] hasChecked){

		int opponent;
		switch(player){
		case(WQUEEN):
			opponent = BQUEEN;
			break;
		default:
			opponent = WQUEEN;
		}
		
		Stack<Pair<Byte, Byte>> stack = new Stack<>();
		
		hasChecked[source.getLeft()][source.getRight()] = 4;
				
		stack.push(source);

		while (!stack.empty()){
			// Check 8 diagonal positions.
			Pair<Byte, Byte> top = stack.pop();
			byte xPos = top.getLeft();
			byte yPos = top.getRight();
			
			// Check boundary
			if (xPos - 1 >= 0){
				// If it is free
				if (!board.isMarked((xPos-1), yPos)){
					// If we haven't looked at it yet
					if (hasChecked[xPos-1][yPos] == 0){
						stack.push(new Pair<>((byte)(xPos-1), (byte)yPos)); 
						hasChecked[xPos-1][yPos] = player;
					// If the other team can reach it the tile is neutral
					} else if (hasChecked[xPos - 1][yPos] == opponent) {
						stack.push(new Pair<>((byte)(xPos-1), yPos)); 
						hasChecked[xPos-1][yPos] = ARROW;	// Represent "neutral"
					}
				}
			}
			// Repeat x7
			if (xPos + 1 < ROWS){
				if (!board.isMarked((xPos+1), yPos)){
					if (hasChecked[xPos+1][yPos] == 0){
						// If we haven't looked at it yet
						stack.push(new Pair<>((byte)(xPos+1), yPos));
						hasChecked[xPos+1][yPos] = player;
					} else if (hasChecked[xPos+1][yPos] == opponent) {
						stack.push(new Pair<>((byte)(xPos+1), yPos));
						hasChecked[xPos+1][yPos] = ARROW;
					}
				}
			}
			if (yPos - 1 >= 0){
				if (!board.isMarked((xPos), yPos-1)){
					if (hasChecked[xPos][yPos-1] == 0){
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos, (byte)(yPos-1)));
						hasChecked[xPos][yPos-1] = player;
					} else if (hasChecked[xPos][yPos-1] == opponent){
						stack.push(new Pair<>(xPos, (byte)(yPos-1)));
						hasChecked[xPos][yPos-1] = ARROW;
					}
				}
			}
			if (yPos + 1 < COLS){
				if (!board.isMarked(xPos, yPos+1)){
					if (hasChecked[xPos][yPos+1] == 0){
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos, (byte)(yPos+1)));
						hasChecked[xPos][yPos+1] = player;
					} else if (hasChecked[xPos][yPos+1] == opponent){
						stack.push(new Pair<>(xPos, (byte)(yPos+1)));
						hasChecked[xPos][yPos+1] = ARROW;
					}
				}
			}
			if ((xPos + 1 < ROWS) && (yPos + 1 < COLS)){
				if (!board.isMarked((xPos+1), yPos+1)){
					if (hasChecked[xPos+1][yPos+1] == 0){
						// If we haven't looked at it yet
						stack.push(new Pair<>((byte)(xPos+1), (byte)(yPos+1)));
						hasChecked[xPos+1][yPos+1] = player;
					} else if (hasChecked[xPos+1][yPos+1] == opponent) {
						hasChecked[xPos+1][yPos+1] = ARROW;
						stack.push(new Pair<>((byte)(xPos+1), (byte)(yPos+1)));
					}
				}
			}
			if ((xPos + 1 < ROWS) && (yPos - 1 >= 0)){
				if (!board.isMarked((xPos+1), yPos-1)){
					if (hasChecked[xPos+1][yPos-1] == 0){
						// If we haven't looked at it yet
						stack.push(new Pair<>((byte)(xPos+1), (byte)(yPos-1)));
						hasChecked[xPos+1][yPos-1] = player;
					} else if (hasChecked[xPos+1][yPos-1] == opponent) {
						stack.push(new Pair<>((byte)(xPos+1), (byte)(yPos-1)));
						hasChecked[xPos+1][yPos-1] = ARROW;
					}
				}
			}
			if ((xPos - 1 >= 0) && (yPos + 1 < COLS)){
				if (!board.isMarked((xPos-1), yPos+1)){
					if (hasChecked[xPos-1][yPos+1] == 0){
						// If we haven't looked at it yet
						stack.push(new Pair<>((byte)(xPos-1), (byte)(yPos+1)));
						hasChecked[xPos-1][yPos+1] = player;
						
					} else if (hasChecked[xPos-1][yPos+1] == opponent) {
						stack.push(new Pair<>((byte)(xPos-1), (byte)(yPos+1)));
						hasChecked[xPos-1][yPos+1] = ARROW;
					}
				}
			}
			if ((xPos - 1 >= 0) && (yPos - 1 >= 0)){
				if (!board.isMarked((xPos-1), yPos-1)){
					if (hasChecked[xPos-1][yPos-1] == 0){
						// If we haven't looked at it yet
						stack.push(new Pair<>((byte)(xPos-1), (byte)(yPos-1)));
						hasChecked[xPos-1][yPos-1] = player;
					} else if (hasChecked[xPos-1][yPos-1] == opponent) {
						stack.push(new Pair<>((byte)(xPos-1), (byte)(yPos-1)));
						hasChecked[xPos-1][yPos-1] = ARROW;
					}
				}
			}
		}	
		return hasChecked;
	}	
	
	/**
	 * Search board starting from each of the players pieces. If one colour owns more
	 * territory than the other amazons, plus neutral tiles, then we are in a "winning"
	 * position. We should then 
	 * 
	 */
	public static boolean checkIfFinished(Board board) {	

		ArrayList<Pair<Byte, Byte> > wPositions = board.getWhitePositions();
		ArrayList<Pair<Byte, Byte> > bPositions = board.getBlackPositions();

		byte[][] hasChecked = new byte[10][10];

		// Scan from each amazon, mark each tile an amazon can reach
		for (Pair<Byte, Byte> pair : wPositions){
			hasChecked = countReachableTiles(board, pair, (byte) WQUEEN, hasChecked);		
		}

		for (Pair<Byte, Byte> pair : bPositions){
			hasChecked = countReachableTiles(board, pair, (byte) BQUEEN, hasChecked);
		}

		int whiteTiles = 4;
		int blackTiles = 4; 
		int bothCanReach = 0;

		// Scan the abstracted board
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				switch(hasChecked[i][j]){
				case(1):
					whiteTiles++;	// White controlled
					break;
				case(2):
					blackTiles++;	// Black controlled
					break;
				case(3):
					bothCanReach++;	// Neutral tile
					break;
				}
			}
		}
		// Test for winning or losing winning state
		if ((whiteTiles > blackTiles + bothCanReach) || (blackTiles > whiteTiles + bothCanReach)) {
			return true;
		}
		return false;
	}

}
