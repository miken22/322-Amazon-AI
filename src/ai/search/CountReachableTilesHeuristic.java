package ai.search;

import java.util.ArrayList;

import ai.Board;
import ai.Pair;
import ai.Utility;

/**
 * Trying for an endgame heuristic. This counts the number of tiles that the
 * amazon can hold, hopefully guiding the AI to consider moves that maximize
 * its area it owns in the late stages.
 * 
 * 
 * @author Mike-SSD
 *
 */
public class CountReachableTilesHeuristic extends EvaluationFunction {
	
	private final int ROWS = 10;
	private final int COLS = 10;
		
	public CountReachableTilesHeuristic(int role) {
		super(role);
	}

	@Override
	public int evaluate(Board board, int player) {
		
		ArrayList<Pair<Integer, Integer> > wPositions = board.getWhitePositions();
		ArrayList<Pair<Integer, Integer> > bPositions = board.getBlackPositions();

		int[][] hasChecked = new int[ROWS][COLS];
	
		// Iterate over every queen and score the tiles it could reach. 
		for (Pair<Integer, Integer> pair : wPositions){
			hasChecked = Utility.countReachableTiles(board, pair, WQUEEN, hasChecked);		
		}
		
		for (Pair<Integer, Integer> pair : bPositions){
			hasChecked = Utility.countReachableTiles(board, pair, BQUEEN, hasChecked);
		}
		
		int whiteTiles = 4;
		int blackTiles = 4; 
		
		for (int i = 0; i < ROWS; i++){
			for (int j = 0; j < COLS; j++){
				switch(hasChecked[i][j]){
					case(1):
						whiteTiles++;
						break;
					case(2):
						blackTiles++;
						break;
				}
			}
		}

		// Return the appropriate score, we want the difference between the area controlled.
		if (OURCOLOUR == 1){
			return whiteTiles - blackTiles;
		} else {
			return blackTiles - whiteTiles;
		}
		
	}
	
}
