package ai.search;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
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
public class MinDistanceHeuristic extends EvaluationFunction {

	private final int ROWS = 10;
	private final int COLS = 10;

	Actions actions = new Actions();
	GameTreeSearch search = new GameTreeSearch();
	
	public MinDistanceHeuristic(int role){
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
			wDistanceTable[pair.getLeft()][pair.getRight()] = -1;
		}

		for (Pair<Integer, Integer> pair : bPositions) {
			bDistanceTable[pair.getLeft()][pair.getRight()] = -1;
		}
		
		findDistances(board, WQUEEN, wDistanceTable);
		findDistances(board, BQUEEN, bDistanceTable);

//		for (int i = 9; i  >= 0; i--){
//			for (int j = 0; j < 10; j++){
//				if (wDistanceTable[i][j] == Integer.MAX_VALUE){
//					System.out.print(0 + " | ");
//				} else {
//					System.out.print(wDistanceTable[i][j] + " | ");
//				}
//			}
//			System.out.println("");
//		}
//		System.out.println("--------------------------");
		
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
		
//		System.out.println(wUtility);
		
		if (player == 1){
			return wUtility;
		} else {
			return bUtility;
		}
	}
	
	/**
	 * Different way to compute distances, acts like a DFS outwards from each queen, supposed to be faster.
	 * 
	 * @param board - State being evaluated.
	 * @param player - Colour evaluating for.
	 * @param distanceTable - The matrix representing distances.
	 */
	public void findDistances(Board board, int player, int[][] distanceTable){

		ArrayList<Pair<Integer, Integer> > positions = null;
		Queue<Pair<Integer, Integer> > queue = new ArrayDeque<>();
		
		if (player == 1){
			positions = board.getWhitePositions();
		} else {
			positions = board.getBlackPositions();
		}
		
		for (Pair<Integer, Integer> pair : positions) {
			queue.add(pair);
		}
		
		while (!queue.isEmpty()){
			
			Pair<Integer, Integer> top = queue.poll();
	
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
				// We set each tile in the set of actions equal to 1 + cost to source tile
				if (!board.isMarked(newX, newY)){
					// If we can move to that tile, and the cost of the movement is less than the current shortest path
					// score the distance and place the tile on the stack
					if (search.moveIsValid(board, xPos, yPos, newX, newY, player, false)){
						if (distanceTable[newX][newY] > currentDistance){
							distanceTable[newX][newY] = currentDistance;
							queue.add(new Pair<Integer, Integer>(newX, newY));
						}
					}
				}		
			}	
		}
	}
}