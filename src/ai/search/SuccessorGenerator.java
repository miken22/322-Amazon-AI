package ai.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

	/**
	 * This function takes a state and player number and returns the set of all possible
	 * actions that the player can make. The moves are ordered from fewest a piece can
	 * make to the most.
	 * 
	 * @param board - Current game state
	 * @param player - 1 for white, 2 for black
	 * @return ArrayList of byte arrays that indicate the moves possible
	 */
	public List<byte[]> getRelevantActions(Board board, int player){

		// Collect each set of moves for each amazon
		ArrayList<ArrayList<byte[]>> moveList = new ArrayList<>();
		
		for (int i = 0; i < 4; i++) {
			moveList.add(new ArrayList<byte[]>());
		}
		// Index piece we are looking at.
		int piece = 0;

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
				// Create a new board to examine
				Board tempBoard = new Board(board);

				byte toX = (byte)(fromX + amazonMove[0]);
				byte toY = (byte)(fromY + amazonMove[1]);
				
				// Update references to amazon positions for the corresponding player
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
					for (int j = 0; j < actions.getArrowThrows().size(); j++){
						
						byte[] arrowSpot = actions.getArrowThrows().get(j);
						
						byte arrowX = (byte)(toX + arrowSpot[0]);
						byte arrowY = (byte)(toY + arrowSpot[1]);
						
						// If queen and arrow placement is valid record the actions and push onto the list
						if (moveIsValid(tempBoard, toX, toY, arrowX, arrowY, player, true)){
							byte[] move = { fromX, fromY, toX, toY, arrowX, arrowY };
							moveList.get(piece).add(move);
						}
					}
				} 
			}
			// Increment index for move list.
			piece++;
		}		
			
		// Order the set of moves		
		Collections.sort(moveList, new CountComparator());
		
		ArrayList<byte[]> orderedMoves = new ArrayList<>();
		
		// Try all possible moves -> Tests ordering by trying the move for each queen, not an improvement so far
//		for (byte i = 0; i < actions.getActions().size(); i++){
//			
//			byte[] amazonMove = actions.getActions().get(i);
//
//			for (Pair<Byte, Byte> amazon : amazons) {
//				
//				Board tempBoard = new Board(board);
//				byte fromX = amazon.getLeft();
//				byte fromY = amazon.getRight();
//				
//				byte toX = (byte)(fromX + amazonMove[0]);
//				byte toY = (byte)(fromY + amazonMove[1]);
//				
//				// Update references to amazon positions for the corresponding player
//				if (player == 1){
//					tempBoard.updateWhitePositions(fromX, fromY, toX, toY);
//				} else {
//					tempBoard.updateBlackPositions(fromX, fromY, toX, toY);				
//				}
//				// Check that the move is valid, if so try placing arrows
//				if (moveIsValid(tempBoard, fromX, fromY, toX, toY, player, false)){
//
//					tempBoard.freeSquare(fromX, fromY);
//					tempBoard.placeMarker(toX, toY, (byte)player);
//
//					// Skip last action in list (stays still)
//					for (int j = 0; j < actions.getArrowThrows().size(); j++){
//
//						byte[] arrowSpot = actions.getArrowThrows().get(j);
//
//						byte arrowX = (byte)(toX + arrowSpot[0]);
//						byte arrowY = (byte)(toY + arrowSpot[1]);
//
//						// If queen and arrow placement is valid record the actions and push onto the list
//						if (moveIsValid(tempBoard, toX, toY, arrowX, arrowY, player, true)){
//							byte[] move = { fromX, fromY, toX, toY, arrowX, arrowY };
//							orderedMoves.add(move);
//						}
//					}
//				}
//			}
//		}
		
		
		
		// Add all the moves into one main list
		for (int i = 0; i < 4; i++) {
			orderedMoves.addAll(moveList.get(i));
		}		
		// Return valid actions.
		return orderedMoves;
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
	
	// Simple comparator to order the number of moves possible for each amazon
	private class CountComparator implements Comparator<ArrayList<byte[]>> {

		@Override
		public int compare(ArrayList<byte[]> o1, ArrayList<byte[]> o2) {
			if (o1.size() > o2.size()) {
				return 1;
			} else if (o1.size() < o2.size()) {
				return -1;
			}
			return 0;
		}
	}
}
