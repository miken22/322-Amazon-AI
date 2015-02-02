package ai.search;

import java.util.ArrayList;
import java.util.Stack;

import ai.Board;
import ai.Pair;

public class TrivialFunction extends EvaluationFunction {
	
	private final int ROWS = 10;
	private final int COLS = 10;

	public TrivialFunction(int role){
		super(role);
	}
	
	@Override
	public int evaluate(Board board, int player) {
		
		ArrayList<Pair<Integer, Integer> > wPositions = board.getWhitePositions();
		ArrayList<Pair<Integer, Integer> > bPositions = board.getBlackPositions();

		int[][] hasChecked = new int[ROWS][COLS];

		for (Pair<Integer, Integer> pair : wPositions) {
			countReachableTiles(board, pair, WQUEEN, hasChecked);
		}

		for (Pair<Integer, Integer> pair : bPositions) {
			countReachableTiles(board, pair, BQUEEN, hasChecked);
		}

		int whiteTiles = 0;
		int blackTiles = 0;
		int bothCanReach = 0;

		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				switch (hasChecked[i][j]) {
				case (1):
					whiteTiles++;
				break;
				case (2):
					blackTiles++;
				break;
				case (3):
					bothCanReach++;
				break;
				}
			}
		}
		
		if (player == 1){
			return whiteTiles + bothCanReach;
		} else {
			return blackTiles + bothCanReach;
		}
	
	}
	
	private void countReachableTiles(Board board, Pair<Integer, Integer> source, int player,
			int[][] hasChecked) {

		int opponent;
		switch (player) {
		case (WQUEEN):
			opponent = BQUEEN;
			break;
		default:
			opponent = WQUEEN;
		}

		Stack<Pair<Integer, Integer>> stack = new Stack<>();

		// Used to indicate spots where queens are located.
		hasChecked[source.getLeft()][source.getRight()] = 4;

		stack.push(source);

		while (!stack.empty()) {
			// Check 8 diagonal positions.
			Pair<Integer, Integer> top = stack.pop();
			int xPos = top.getLeft();
			int yPos = top.getRight();

			// Check boundary
			if (xPos - 1 >= 0) {
				// If it is free
				if (!board.isMarked((xPos - 1), yPos)) {
					// If we haven't looked at it yet
					if (hasChecked[xPos - 1][yPos] == 0) {
						stack.push(new Pair<>(xPos - 1, yPos));
						hasChecked[xPos - 1][yPos] = player;
					} else if (hasChecked[xPos - 1][yPos] == opponent) {
						stack.push(new Pair<>(xPos - 1, yPos));
						hasChecked[xPos - 1][yPos] = ARROW;
					}
				}
			}
			if (xPos + 1 < ROWS) {
				if (!board.isMarked((xPos + 1), yPos)) {
					if (hasChecked[xPos + 1][yPos] == 0) {
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos + 1, yPos));
						hasChecked[xPos + 1][yPos] = player;
					} else if (hasChecked[xPos + 1][yPos] == opponent) {
						stack.push(new Pair<>(xPos + 1, yPos));
						hasChecked[xPos + 1][yPos] = ARROW;
					}
				}
			}
			if (yPos - 1 >= 0) {
				if (!board.isMarked((xPos), yPos - 1)) {
					if (hasChecked[xPos][yPos - 1] == 0) {
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos, yPos - 1));
						hasChecked[xPos][yPos - 1] = player;
					} else if (hasChecked[xPos][yPos - 1] == opponent) {
						stack.push(new Pair<>(xPos, yPos - 1));
						hasChecked[xPos][yPos - 1] = ARROW;
					}
				}
			}
			if (yPos + 1 < COLS) {
				if (!board.isMarked(xPos, yPos + 1)) {
					if (hasChecked[xPos][yPos + 1] == 0) {
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos, yPos + 1));
						hasChecked[xPos][yPos + 1] = player;
					} else if (hasChecked[xPos][yPos + 1] == opponent) {
						stack.push(new Pair<>(xPos, yPos + 1));
						hasChecked[xPos][yPos + 1] = ARROW;
					}
				}
			}
			if ((xPos + 1 < ROWS) && (yPos + 1 < COLS)) {
				if (!board.isMarked((xPos + 1), yPos + 1)) {
					if (hasChecked[xPos + 1][yPos + 1] == 0) {
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos + 1, yPos + 1));
						hasChecked[xPos + 1][yPos + 1] = player;
					} else if (hasChecked[xPos + 1][yPos + 1] == opponent) {
						hasChecked[xPos + 1][yPos + 1] = ARROW;
						stack.push(new Pair<>(xPos + 1, yPos + 1));
					}
				}
			}
			if ((xPos + 1 < ROWS) && (yPos - 1 >= 0)) {
				if (!board.isMarked((xPos + 1), yPos - 1)) {
					if (hasChecked[xPos + 1][yPos - 1] == 0) {
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos + 1, yPos - 1));
						hasChecked[xPos + 1][yPos - 1] = player;
					} else if (hasChecked[xPos + 1][yPos - 1] == opponent) {
						stack.push(new Pair<>(xPos + 1, yPos - 1));
						hasChecked[xPos + 1][yPos - 1] = ARROW;
					}
				}
			}
			if ((xPos - 1 >= 0) && (yPos + 1 < COLS)) {
				if (!board.isMarked((xPos - 1), yPos + 1)) {
					if (hasChecked[xPos - 1][yPos + 1] == 0) {
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos - 1, yPos + 1));
						hasChecked[xPos - 1][yPos + 1] = player;

					} else if (hasChecked[xPos - 1][yPos + 1] == opponent) {
						stack.push(new Pair<>(xPos - 1, yPos + 1));
						hasChecked[xPos - 1][yPos + 1] = ARROW;
					}
				}
			}
			if ((xPos - 1 >= 0) && (yPos - 1 >= 0)) {
				if (!board.isMarked((xPos - 1), yPos - 1)) {
					if (hasChecked[xPos - 1][yPos - 1] == 0) {
						// If we haven't looked at it yet
						stack.push(new Pair<>(xPos - 1, yPos - 1));
						hasChecked[xPos - 1][yPos - 1] = player;
					} else if (hasChecked[xPos - 1][yPos - 1] == opponent) {
						stack.push(new Pair<>(xPos - 1, yPos - 1));
						hasChecked[xPos - 1][yPos - 1] = ARROW;
					}
				}
			}
		}
	}	
}
