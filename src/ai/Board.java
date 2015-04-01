package ai;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * This class represents the state of the game during search and gameplay.
 * 
 * @author Mike Nowicki
 *
 */
public class Board {

	private byte[][] board;
	private int rows;
	private int columns;

	public final byte WQUEEN = 1;
	public final byte BQUEEN = 2;
	public final byte ARROW = 3;
	public final byte FREE = 0;

	// Used to keep track of where each amazon is for easier lookup
	ArrayList<Pair<Byte, Byte>> whitePositions;
	ArrayList<Pair<Byte, Byte>> blackPositions;
	
	// Record of parents trapped pieces, updated after the state is evaluated.
	private HashSet<Pair<Byte, Byte> > whiteTraps;
	private HashSet<Pair<Byte, Byte> > blackTraps;
	
	private int heuristicValue;

	public Board(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		whiteTraps = new HashSet<>();
		blackTraps = new HashSet<>();
		board = new byte[rows][columns];
		whitePositions = new ArrayList<>();
		blackPositions = new ArrayList<>();
	}
	
	// Makes a clone of the parent board
	public Board(Board parent) {
		rows = 10;
		columns = 10;

		board = new byte[rows][columns];
		whitePositions = new ArrayList<>();
		blackPositions = new ArrayList<>();

		for (Pair<Byte, Byte> pair : parent.getWhitePositions()) {
			whitePositions.add(pair);
		}
		for (Pair<Byte, Byte> pair : parent.getBlackPositions()) {
			blackPositions.add(pair);
		}
		
		this.whiteTraps = parent.getWhiteTrappedPieces();
		this.blackTraps = parent.getBlackTrappedPieces();

		byte[][] parentBoard = parent.getBoard();

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				board[i][j] = parentBoard[i][j];
			}
		}
	}

	/**
	 * Use only for the initial board to set up the initial position
	 */
	public void initialize() {

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				board[i][j] = FREE;
			}
		}

		board[0][3] = WQUEEN;
		board[0][6] = WQUEEN;
		board[3][0] = WQUEEN;
		board[3][9] = WQUEEN;

		whitePositions.add(new Pair<Byte, Byte>((byte)0, (byte)3));
		whitePositions.add(new Pair<Byte, Byte>((byte)0, (byte)6));
		whitePositions.add(new Pair<Byte, Byte>((byte)3, (byte)0));
		whitePositions.add(new Pair<Byte, Byte>((byte)3, (byte)9));

		board[6][0] = BQUEEN;
		board[6][9] = BQUEEN;
		board[9][3] = BQUEEN;
		board[9][6] = BQUEEN;

		blackPositions.add(new Pair<Byte, Byte>((byte)6, (byte)9));
		blackPositions.add(new Pair<Byte, Byte>((byte)9, (byte)3));
		blackPositions.add(new Pair<Byte, Byte>((byte)6, (byte)0));
		blackPositions.add(new Pair<Byte, Byte>((byte)9, (byte)6));

	}

	public void freeSquare(int x, int y) {
		board[x][y] = FREE;
	}

	public void placeMarker(int x, int y, byte piece) {
		board[x][y] = piece;
	}

	public boolean isMarked(int x, int y) {
		if (board[x][y] == FREE) {
			return false;
		}
		return true;
	}

	public int getPiece(int x, int y) {
		return board[x][y];
	}

	/**
	 * Takes a pair representing a white amazon position, checks if it is
	 * stored within the hashset of trapped pieces
	 * 
	 * @param position - A pair of bytes representing the x,y coordinate 
	 * 					of the piece to check of a white amazon
	 * @return - True if the piece was already stored in the trapped positions
	 * 			False otherwise
	 */
	public boolean whitePieceTrapped(Pair<Byte, Byte> position){
		
		if (whiteTraps.contains(position)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Takes a pair representing a black amazon position, checks if it is
	 * stored within the hashset of trapped pieces
	 * 
	 * @param position - A pair of bytes representing the x,y coordinate 
	 * 					of the piece to check of a black amazon
	 * @return - True if the piece was already stored in the trapped positions
	 * 			False otherwise
	 */
	public boolean blackPieceTrapped(Pair<Byte, Byte> position){
		
		if (blackTraps.contains(position)) {
			return true;
		}
		return false;
	}

	public void addWhiteTrappedPiece(Pair<Byte, Byte> amazon) {
		whiteTraps.add(amazon);
	}	
	
	public void addBlackTrappedPiece(Pair<Byte, Byte> amazon) {
		blackTraps.add(amazon);
	}
	
	public void clearWhiteTrapMap() {
		whiteTraps.clear();
	}

	public void clearBlackTrapMap() {
		blackTraps.clear();
	}
	/**
	 * Cycle through black pieces, find the one that matches the starting
	 * configuration, remove it and make a new pairing.
	 * 
	 * @param oldX
	 * @param oldY
	 * @param newX
	 * @param newY
	 */
	public void updateBlackPositions(int oldX, int oldY, byte newX, byte newY) {

		for (Pair<Byte, Byte> p : blackPositions) {
			if (p.getLeft() == oldX && p.getRight() == oldY) {
				blackPositions.remove(p);
				blackPositions.add(new Pair<Byte, Byte>(newX, newY));
				return;
			}
		}
	}

	public void updateWhitePositions(int oldX, int oldY, byte newX, byte newY) {

		for (Pair<Byte, Byte> p : whitePositions) {
			if (p.getLeft() == oldX && p.getRight() == oldY) {
				whitePositions.remove(p);
				whitePositions.add(new Pair<Byte, Byte>(newX, newY));
				return;
			}
		}
	}

	public ArrayList<Pair<Byte, Byte>> getWhitePositions() {
		return whitePositions;
	}

	public ArrayList<Pair<Byte, Byte>> getBlackPositions() {
		return blackPositions;
	}

	public byte[][] getBoard() {
		return board;
	}

	public HashSet<Pair<Byte, Byte>> getBlackTrappedPieces() {
		return blackTraps;
	}

	public HashSet<Pair<Byte, Byte>> getWhiteTrappedPieces() {
		return whiteTraps;
	}
	
	public void setHeuristicValue(int value) {
		this.heuristicValue = value;
	}
	
	public int getHeuristicValue() {
		return heuristicValue;
	}
	
	@Override
	public int hashCode() {
		return java.util.Arrays.deepHashCode(board);
	}
	
	public boolean equals(Board otherBoard){
		return java.util.Arrays.deepEquals(board, otherBoard.getBoard());
	}
}
