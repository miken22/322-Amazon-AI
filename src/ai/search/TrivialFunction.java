package ai.search;

import java.util.ArrayList;
import java.util.Stack;

import ai.Actions;
import ai.Board;
import ai.Pair;

/**
 * Trivial heuristic function, trying to convert to the min-distance function. It's close but not quite there
 * 
 * @author Mike Nowicki
 *
 */
public class TrivialFunction extends EvaluationFunction {

	private final int ROWS = 10;
	private final int COLS = 10;

	Actions actions = new Actions();
	GameTreeSearch search = new GameTreeSearch();
	
	public TrivialFunction(int role){
		super(role);
	}

	@Override
	public int evaluate(Board board, int player){

		int wUtility = 0;
		int bUtility = 0;
		ArrayList<Pair<Integer, Integer> > wPositions = board.getWhitePositions();
		ArrayList<Pair<Integer, Integer> > bPositions = board.getBlackPositions();

		int[][] wDistanceTable = new int[ROWS][COLS];
		int[][] bDistanceTable = new int[ROWS][COLS];
		
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				wDistanceTable[i][j] = Integer.MAX_VALUE;
				bDistanceTable[i][j] = Integer.MAX_VALUE;
			}
		}

		for (Pair<Integer, Integer> pair : wPositions) {
			scoreDistance(board, pair, WQUEEN, wDistanceTable);
		}

		for (Pair<Integer, Integer> pair : bPositions) {
			scoreDistance(board, pair, BQUEEN, bDistanceTable);
		}

		for (int i = 9; i  >= 0; i--){
			for (int j = 0; j < 10; j++){
				if (wDistanceTable[i][j] == Integer.MAX_VALUE){
					System.out.print(0 + " | ");
				} else {
					System.out.print(wDistanceTable[i][j] + " | ");
				}
			}
			System.out.println("");
		}
		System.out.println("--------------------------");
		
		
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				// Want to consider it "owned" if it is easier for one side to reach
				if (wDistanceTable[i][j] > bDistanceTable[i][j]){
					bUtility++;
				} else if (wDistanceTable[i][j] < bDistanceTable[i][j]){
					wUtility++;
				}
			}
		}
		if (player == 1){
			return wUtility;
		} else {
			return bUtility;
		}
	}

	public void scoreDistance(Board board, Pair<Integer, Integer> source, int player, int[][] distanceTable){
	
		Stack<Pair<Integer, Integer>> stack = new Stack<>();

		// Used to indicate spots where queens are located.
		distanceTable[source.getLeft()][source.getRight()] = -1;

		stack.push(source);
		
		while (!stack.empty()) {
			// Check 8 diagonal positions.
			Pair<Integer, Integer> top = stack.pop();
			int xPos = top.getLeft();
			int yPos = top.getRight();
			
			
			for (int[] action : actions.getActions()){

				int currentDistance = distanceTable[xPos][yPos];
				if (currentDistance == -1){
					currentDistance = 0;
				}
				
				int newX = xPos + action[0];
				int newY = yPos + action[1];
				
				// Bound checks
				if ((newX > 9 || newX < 0) || (newY > 9 || newY < 0)){
					continue;
				}
				
				currentDistance++;
				
				if (!board.isMarked(newX, newY)){
					if (search.moveIsValid(board, xPos, yPos, newX, newY, player, false)){
						if (distanceTable[newX][newY] > currentDistance){
							distanceTable[newX][newY] = currentDistance;
							stack.push(new Pair<Integer, Integer>(newX, newY));
						}
					}
				}
		
			}
		}
	}
}
