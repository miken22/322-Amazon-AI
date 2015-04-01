package ai.search;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import ai.Board;
import ai.Pair;

/**
 * Min-distance function as defined in the paper on the project
 * page.
 * 
 * @author Mike Nowicki
 *
 */
public class MinDistanceHeuristic extends EvaluationFunction {

	private final int ROWS = 10;
	private final int COLS = 10;

	GameTreeSearch search = new GameTreeSearch();
	
	public MinDistanceHeuristic(byte role){
		super(role);
	}

	@Override
	public int evaluate(Board board, byte player){

		int wUtility = 0;
		int bUtility = 0;
		ArrayList<Pair<Byte, Byte> > wPositions = board.getWhitePositions();
		ArrayList<Pair<Byte, Byte> > bPositions = board.getBlackPositions();

		int[][] wDistanceTable = new int[ROWS][COLS];
		int[][] bDistanceTable = new int[ROWS][COLS];
		
		// Initialize matrix representing board with infinite values
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++){
				wDistanceTable[i][j] = Byte.MAX_VALUE;
				bDistanceTable[i][j] = Byte.MAX_VALUE;
			}
		}
		
		// Mark each queen position as -1 so that it cannot be evaluated for control
		for (Pair<Byte, Byte> pair : wPositions) {
			wDistanceTable[pair.getLeft()][pair.getRight()] = -1;
		}

		// Same as above
		for (Pair<Byte, Byte> pair : bPositions) {
			bDistanceTable[pair.getLeft()][pair.getRight()] = -1;
		}
		
		// Scan the board from the white pieces and record the number of steps to
		// each reachable tile. Then goes back over for black pieces and updates only
		// if black can get there in fewer moves. Any tie states are recorded as neutral.
		findDistances(board, WQUEEN, wDistanceTable);
		findDistances(board, BQUEEN, bDistanceTable);

//		for (int i = 9; i  >= 0; i--){
//			for (int j = 0; j < 10; j++){
//				if (wDistanceTable[i][j] == Byte.MAX_VALUE){
//					System.out.print(0 + " | ");
//				} else {
//					System.out.print(wDistanceTable[i][j] + " | ");
//				}
//			}
//			System.out.println("");
//		}
//		System.out.println("--------------------------");
		
		// Scan the board and count the tiles controlled by each side
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
		
		// Not used atm
		int adjustment = 0;// adjustForIsolatedPieces(board);
		
		if (player == 1){
			return wUtility + adjustment;
		} else {
			return bUtility + adjustment;
		}
	}
	
	/**
	 * Different way to compute distances, acts like a DFS outwards from each queen.
	 * 
	 * @param board - State being evaluated.
	 * @param player - Colour evaluating for.
	 * @param distanceTable - The matrix representing distances.
	 */
	public void findDistances(Board board, int player, int[][] distanceTable){

		ArrayList<Pair<Byte, Byte> > positions = null;
		Deque<Pair<Byte, Byte> > queue = new ArrayDeque<>();
		
		if (player == WQUEEN){
			positions = board.getWhitePositions();
		} else {
			positions = board.getBlackPositions();
		}
		
		// Fill the queue to start from the initial positions
		for (Pair<Byte, Byte> pair : positions) {
			queue.add(pair);
		}
		
		// Iteratively remove from the queue, try a move and add to the queue
		// if it is reached in fewer numbers.
		while (!queue.isEmpty()){
			
			Pair<Byte, Byte> top = queue.poll();
	
			byte xPos = top.getLeft();
			byte yPos = top.getRight();
			
			for (byte[] action : actions.getActions()){
				
				int currentDistance = distanceTable[xPos][yPos];
				// So that we start from the 0th spot
				if (currentDistance == -1){
					currentDistance = 0;
				}
				
				byte newX = (byte) (xPos + action[0]);
				byte newY = (byte) (yPos + action[1]);
				
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
							queue.addFirst(new Pair<Byte, Byte>(newX, newY));
						}
					}
				}		
			} // end for	
		}// end while
	}
}// end class
