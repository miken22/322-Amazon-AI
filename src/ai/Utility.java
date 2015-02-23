package ai;

import java.util.Stack;

public class Utility {
	
	public static final int WQUEEN = 1;
	public static final int BQUEEN = 2;
	public static final int ARROW = 3;

	private static final int ROWS = 10;
	private static final int COLS = 10;
	
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
	public static int[][] countReachableTiles(Board board, Pair<Byte, Byte> source, int player, int[][] hasChecked){

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
						hasChecked[xPos-1][yPos] = ARROW;
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
}
