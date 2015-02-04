package ai;

import java.util.List;

import ai.gui.GUI;
import ai.search.SuccessorGenerator;


/**
 * Using this to test the implementation of the minimax search algorithm and trivial heuristic. Want to demonstrate
 * the ways that the classes and interfaces can be used to make a generic search.
 * 
 * @author mike-nowicki
 *
 */
public class TestClass {
	
	public static void main(String[] args){
		
		Board board = new Board(10, 10);
		board.initialize();
		
		GUI gui = new GUI(board, 10, 10);
		gui.init();
		
//		board.placeMarker(0, 2, 3);
//		board.placeMarker(0, 4, 3);
//		board.placeMarker(1, 3, 3);
//		board.placeMarker(1, 4, 3);
//		gui.updateGUI(0, 3, 0, 3, 0, 2, 1);
//		gui.updateGUI(0, 3, 0, 3, 0, 4, 1);
//		gui.updateGUI(0, 3, 0, 3, 1, 3, 1);
//		gui.updateGUI(0, 3, 0, 3, 1, 4, 1);
		
		SuccessorGenerator scg = new SuccessorGenerator();
		List<int[]> actions = scg.getRelevantActions(board, 1);

		for (int[] move : actions){
			
			System.out.println(move[0] + "" + move[1] + "-" + move[2] + "" + move[3] + "-" + move[4] + "" + move[5]);
			
			// See move
			board.freeSquare(move[0], move[1]);
			board.placeMarker(move[2], move[3], 1);
			board.placeMarker(move[4], move[5], 3);
			
			gui.updateGUI(move[0], move[1], move[2], move[3], move[4], move[5], 1);
			
			String s = "";
			for (int i = 0; i < 1000; i++){
				s += "";
			}
			// Undo move
			board.freeSquare(move[2], move[3]);
			board.freeSquare(move[4], move[5]);
			board.placeMarker(move[0], move[1], 1);
			gui.updateGUI(move[2], move[3], move[0], move[1], move[4], move[5], 1);
			gui.removeArrow(move[4], move[5]);

			
		}
		
				
	}

}
