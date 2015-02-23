package ai.search;

import java.util.ArrayList;
import java.util.List;

import ai.Board;
import ai.Pair;

/**
 * Generic successor generator for Amazons.
 * 
 * @author Mike Nowicki
 *
 */
public class SuccessorGenerator extends GameTreeSearch {

	public List<byte[]> getRelevantActions(Board board, int player){

		ArrayList<byte[]> moveList = new ArrayList<>();

		// Get the starting positions of the queens
		ArrayList<Pair<Byte, Byte> > amazons;
		if (player == 1){
			amazons = board.getWhitePositions();
		} else {
			amazons = board.getBlackPositions();
		}

		// Iterate through queens
		for (Pair<Byte, Byte> amazon : amazons){

			byte fromX = amazon.getLeft();
			byte fromY = amazon.getRight();

			// Try all possible moves from the queens position
			for (byte i = 0; i < actions.getActions().size(); i++){
				
				byte[] amazonMove = actions.getActions().get(i);
				
				Board tempBoard = new Board(board);

				byte toX = (byte)(fromX + amazonMove[0]);
				byte toY = (byte)(fromY + amazonMove[1]);
				
				if (player == 1){
					tempBoard.updateWhitePositions(fromX, fromY, toX, toY);
				} else {
					tempBoard.updateBlackPositions(fromX, fromY, toX, toY);				
				}

				// Check that the move is valid, if so try placing arrows
				if (moveIsValid(tempBoard, fromX, fromY, toX, toY, player, false)){
					
					tempBoard.freeSquare(fromX, fromY);
					tempBoard.placeMarker(toX, toY, (byte)player);
					
					// Skip last action in list (stays still)
					for (int j = 0; j < actions.getActions().size() - 1; j++){
						
						byte[] arrowSpot = actions.getActions().get(j);
						
						byte arrowX = (byte)(toX + arrowSpot[0]);
						byte arrowY = (byte)(toY + arrowSpot[1]);
						
						// If queen and arrow placement is valid record the actions and push onto the list
						if (moveIsValid(tempBoard, toX, toY, arrowX, arrowY, player, true)){
							byte[] move = { fromX, fromY, toX, toY, arrowX, arrowY };						
							moveList.add(move);
						} else {
							// This relies on current ordering of actions. We proceed in one direction from the new queen, as soon as we hit an
							// obstacle we know we cannot proceed further in that direction so we skip the remaining operators
							// that allow moving in the obstructed direction.
							if (arrowSpot[0] == 0){
								j += 9 - Math.abs(arrowSpot[1]);
							} else {
								j += 9 - Math.abs(arrowSpot[0]);
							}
						}
					}
				} else {
					// This relies on current ordering of actions. We proceed in one direction from the old queen, as soon as we hit an
					// obstacle we know we cannot proceed further in that direction so we skip the remaining operators
					// that allow moving in the obstructed direction.
					if (amazonMove[0] == 0){
						i += 9 - Math.abs(amazonMove[1]);
					} else {
						i += 9 - Math.abs(amazonMove[0]);
					}
				}
				
			}
		}		
		return moveList;
	}

	// Applies the move sequence for the given player and returns a new board for the successor state.
	public Board generateSuccessor(Board parent, byte[] move, byte player){
		
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
