package ai.search;

import java.util.ArrayList;
import java.util.List;

import ai.Board;
import ai.Pair;

/**
 * Generic successor generator for Amazons. Close to generating all possible moves in a partially better than brute force
 * way. Currently generates 80 more than literature says possible for the first move. We search in one direction until 
 * we hit an obstacle preventing the queen from moving, then we know we cannot move farther in that direction so we skip 
 * and go to the next one. Arrows are generated from farthest to closest so this pruning does not work, however some 
 * technique can be created to overcome this.
 * 
 * @author Mike Nowicki
 *
 */
public class SuccessorGenerator extends GameTreeSearch {

	public List<int[]> getRelevantActions(Board board, int player){

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
			for (int i = 0; i < actions.getActions().size(); i++){
				
				int[] queenMove = actions.getActions().get(i);
				
				Board tempBoard = new Board(board);

				int toX = fromX + queenMove[0];
				int toY = fromY + queenMove[1];
				
				if (player == 1){
					tempBoard.updateWhitePositions(fromX, fromY, toX, toY);
				} else {
					tempBoard.updateBlackPositions(fromX, fromY, toX, toY);				
				}

				// Check that the move is valid, if so try placing arrows
				if (moveIsValid(tempBoard, fromX, fromY, toX, toY, player, false)){
					
					tempBoard.freeSquare(fromX, fromY);
					tempBoard.placeMarker(toX, toY, player);
					
					// Use a different ordering for the arrows, start looking farthest away and then work in
					for (int[] arrowSpot : actions.getArrowThrows()){
						
						int arrowX = toX + arrowSpot[0];
						int arrowY = toY + arrowSpot[1];
											
						// Means we stand still and shoot at ourselves, does not mean we encounter an obstacle
						if (fromX == toX && toX == arrowX && fromY == toY && toY == arrowY){
							continue;
						}
						
						// If queen and arrow placement is valid record the actions and push onto the list
						if (moveIsValid(tempBoard, toX, toY, arrowX, arrowY, player, true)){
							int[] move = { fromX, fromY, toX, toY, arrowX, arrowY };						
							moveList.add(move);
						}
					}
				} else {
					// This relies on current ordering of actions. We proceed in one direction from the old queen, as soon as we hit an
					// obstacle we know we cannot proceed further in that direction so we skip the remaining operators
					// that allow moving in the obstructed direction.
					if (queenMove[0] == 0){
						i += 9 - Math.abs(queenMove[1]);
					} else {
						i += 9 - Math.abs(queenMove[0]);
					}
				}
				
			}
		}		
		return moveList;
	}

	// Applies the move sequence for the given player and returns a new board for the successor state.
	public Board generateSuccessor(Board parent, int[] move, int player){
		
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
