package ai.search;

import java.util.ArrayList;
import java.util.List;

import ai.Board;
import ai.Pair;

public class SuccessorGenerator extends GameTreeSearch {

	public List<int[]> getSuccessors(Board board, int player){

		ArrayList<int[]> moveList = new ArrayList<>();

		// Get the starting positions of the queens
		ArrayList<Pair<Integer, Integer> > queens;
		if (player == 1){
			queens = board.getWhitePositions();
		} else {
			queens = board.getBlackPositions();
		}

		// Iterate through queens
		for (Pair<Integer, Integer> queen : queens){

			int fromX = queen.getLeft();
			int fromY = queen.getRight();

			// Try all possible moves from the queens position
			for (int[] queenMove : actions.getActions()){
				
				Board tempBoard = new Board(board);

				int toX = fromX + queenMove[0];
				int toY = fromY + queenMove[1];

				// Check that the move is valid, if so try placing arrows
				if (moveIsValid(tempBoard, fromX, fromY, toX, toY, player, false)){
					
					tempBoard.freeSquare(fromX, fromY);
					tempBoard.placeMarker(toX, toY, player);
					
					
					for (int[] arrowSpot : actions.getActions()){
						
						int arrowX = toX + arrowSpot[0];
						int arrowY = toY + arrowSpot[1];
						
						if (fromX == toX && toX == arrowX && fromY == toY && toY == arrowY){
							continue;
						}
						
						// If queen and arrow placement is valid record the actions and push onto the list
						if (moveIsValid(tempBoard, toX, toY, arrowX, arrowY, player, true)){
							int[] move = new int[6];
							move[0] = fromX;
							move[1] = fromY;
							move[2] = toX;
							move[3] = toY;
							move[4] = arrowX;
							move[5] = arrowY;
							
							moveList.add(move);
													
						}
						
						// Undo the action for the next round
						tempBoard.freeSquare(toX, toY);
						tempBoard.placeMarker(fromX, fromY, player);
						
					}
				}
			}
		}
		return moveList;
	}

	// Applies the move sequence for the given player and returns a new board for the successor state.
	public Board generateBoard(Board parent, int[] move, int player){
		
		Board child = new Board(parent);
		
		// Update the logical positions on the board
		child.freeSquare(move[0], move[1]);
		child.placeMarker(move[2], move[3], player);
		child.placeMarker(move[4], move[5], ARROW);
		
		// Update the references to the queens
		if (player == 1){
			child.updateWhitePositions(move[0], move[1], move[2], move[3]);
		} else {
			child.updateBlackPositions(move[0], move[1], move[2], move[3]);
		}
		
		return child;	
	}
}
