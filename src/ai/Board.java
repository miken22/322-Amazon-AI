package ai;

import java.util.ArrayList;

public class Board {

	private int[][] board;
	private int rows;
	private int columns;

	public final int WQUEEN = 1;
	public final int BQUEEN = 2;
	public final int ARROW = 3;
	public final int FREE = -1;
	
	ArrayList<Pair<Integer, Integer> > whitePositions;
	ArrayList<Pair<Integer, Integer> > blackPositions;
	
	public Board(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		
		board = new int[rows][columns];
		whitePositions = new ArrayList<>();
		blackPositions = new ArrayList<>();
		
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

		whitePositions.add(new Pair<Integer, Integer>(0,3));
		whitePositions.add(new Pair<Integer, Integer>(0,6));
		whitePositions.add(new Pair<Integer, Integer>(3,0));
		whitePositions.add(new Pair<Integer, Integer>(3,9));

		board[6][0] = BQUEEN;
		board[6][9] = BQUEEN;
		board[9][3] = BQUEEN;
		board[9][6] = BQUEEN;	

		blackPositions.add(new Pair<Integer, Integer>(6, 9));
		blackPositions.add(new Pair<Integer, Integer>(9, 3));
		blackPositions.add(new Pair<Integer, Integer>(6, 0));
		blackPositions.add(new Pair<Integer, Integer>(9, 6));
		
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
	/**
	 * Cycle through black pieces, find the one that matches the starting configuration, remove it and make a new pairing.
	 * 
	 * @param oldX
	 * @param oldY
	 * @param newX
	 * @param newY
	 */
	public void updateBlackPositions(int oldX, int oldY, int newX, int newY){
		
		for (Pair<Integer, Integer> p : blackPositions){
			
			if (p.getLeft() == oldX && p.getRight() == oldY){
				blackPositions.remove(p);
				blackPositions.add(new Pair<Integer, Integer>(newX, newY));
				return;
			}	
		}
	}
	
	public ArrayList<Pair<Integer, Integer> > getBlackPositions(){
		return blackPositions;
	}
	
	public void updateWhitePositions(int oldX, int oldY, int newX, int newY){

		for (Pair<Integer, Integer> p : whitePositions){
			
			if (p.getLeft() == oldX && p.getRight() == oldY){
				whitePositions.remove(p);
				whitePositions.add(new Pair<Integer, Integer>(newX, newY));
				return;
			}	
		}
	}
	
	public ArrayList<Pair<Integer, Integer> > getWhitePositions(){
		return whitePositions;
	}
}
