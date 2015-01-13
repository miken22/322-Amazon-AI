package ai.singleplayer;

import java.util.HashMap;

public class Board {

	private int[][] board;
	private int rows;
	private int columns;

	public final int WQUEEN = 1;
	public final int BQUEEN = 2;
	public final int ARROW = 3;
	public final int FREE = -1;
	
	HashMap<Integer, Integer> whitePositions;
	HashMap<Integer, Integer> blackPositions;
	
	// TODO: Keep track of black/white amazon positions for quick reference
	
	public Board(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		
		board = new int[rows][columns];
		
		whitePositions = new HashMap<>();
		blackPositions = new HashMap<>();
		
		initialize();		
	}
	
	private void initialize(){
		
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < columns; j++){
				board[i][j] = FREE;
			}
		}
		
		board[0][3] = WQUEEN;
		board[0][6] = WQUEEN;
		board[3][0] = WQUEEN;
		board[3][9] = WQUEEN;

		whitePositions.put(0, 3);
		whitePositions.put(0, 6);
		whitePositions.put(3, 0);
		whitePositions.put(3, 9);

		board[6][0] = BQUEEN;
		board[6][9] = BQUEEN;
		board[9][3] = BQUEEN;
		board[9][6] = BQUEEN;	

		blackPositions.put(6, 9);
		blackPositions.put(9, 3);
		blackPositions.put(6, 0);
		blackPositions.put(9, 6);
		
	}
	
	public void freeSquare(int x, int y){
		
		board[x][y] = FREE;
		
	}
	
	public void placeMarker(int x, int y, int piece){
		
		board[x][y] = piece;
		
	}
		
	public boolean isMarked(int x, int y){
		
		if (board[x][y] == -1){
			return false;
		}
		return true;
		
	}
	
	public int getPiece(int x, int y){
		
		return board[x][y];
		
	}
	
	public void updateBlackPositions(int oldX, int oldY, int newX, int newY){
		
	}
	
	public HashMap<Integer, Integer> getBlackPositions(){
		return blackPositions;
	}
	
	public void updateWhitePositions(int oldX, int oldY, int newX, int newY){
		
	}
	
	public HashMap<Integer, Integer> getWhitePositions(){
		return whitePositions;
	}
}


/**
oard[0][3] = WQUEEN;
board[0][6] = WQUEEN;
board[3][0] = WQUEEN;
board[6][0] = WQUEEN;

whitePositions.put(0, 3);
whitePositions.put(3, 0);
whitePositions.put(0, 6);
whitePositions.put(6, 0);

board[0][6] = BQUEEN;
board[3][9] = BQUEEN;
board[9][3] = BQUEEN;
board[6][9] = BQUEEN;	

blackPositions.put(0, 6);
blackPositions.put(3, 9);
blackPositions.put(9, 3);
blackPositions.put(6, 0);
*/