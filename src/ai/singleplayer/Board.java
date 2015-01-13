package ai.singleplayer;

public class Board {

	private int[][] board;
	private int rows;
	private int columns;

	public final int WQUEEN = 1;
	public final int BQUEEN = 2;
	public final int ARROW = 3;
	public final int FREE = -1;
	
	
	public Board(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		
		board = new int[rows][columns];
		
		initialize();		
	}
	
	private void initialize(){
		
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < columns; j++){
				board[i][j] = FREE;
			}
		}
		
		board[0][3] = WQUEEN;
		board[3][0] = WQUEEN;
		board[9][3] = WQUEEN;
		board[6][0] = WQUEEN;
		
		board[0][6] = BQUEEN;
		board[3][9] = BQUEEN;
		board[9][9] = BQUEEN;
		board[6][9] = BQUEEN;	
		
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

}