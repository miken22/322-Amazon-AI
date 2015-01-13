package ai;

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
		
		for (int i = rows-1; i >= 0; i--){
			for (int j = columns-1; j >= 0; j--){
				
				if (i == 9){
					if (j == 3 || j == 6){
						board[i][j] = BQUEEN;
						continue;
					}
				} else if (i == 6){
					if (j == 0 || j == 9){
						board[i][j] = BQUEEN;
						continue;
					}
				}
				
				if (i == 0){
					if (j == 3 || j == 6){
						board[i][j] = WQUEEN;
						continue;
					}
				} else if (i == 3){
					if (j == 0 || j == 9){
						board[i][j] = WQUEEN;
						continue;
					}
				}
				
				board[i][j] = FREE;
				
			}
		}
		
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