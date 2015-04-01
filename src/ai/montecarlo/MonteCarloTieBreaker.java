package ai.montecarlo;

import java.util.List;
import java.util.Random;

import ai.Actions;
import ai.Board;
import ai.Utility;
import ai.search.SuccessorGenerator;

/**
 * Test attempt to break ties, run Monte Carlo simulation
 * from each tie successor to pick most promising node.
 * 
 * @author Mike Nowicki
 *
 */
public class MonteCarloTieBreaker {

	Actions actions = new Actions();
	SuccessorGenerator scg = new SuccessorGenerator();
	Random rng = new Random();
	
	private byte OURCOLOUR;
	private byte OPPONENT;
	
	public MonteCarloTieBreaker(byte role) {	
		OURCOLOUR = role;
		if (OURCOLOUR == 1) {
			OPPONENT = 2;
		} else {
			OPPONENT = 1;
		}
	}
	
	public int simulateFromSuccessor(Board board, byte[] move) {
		
		int winNumber = 0;
		int counter = 0;
		
		while (counter < 1000) {
			Board successor = scg.generateSuccessor(board, move, OURCOLOUR);
			if (runSimulation(successor, false)) {
				winNumber++;
			}
			counter++;
		}
		
		return winNumber;
		
	}
	
	private boolean runSimulation(Board board, boolean ourTurn) {
		
		boolean finished = false;
		
		while (true && !finished) {
			if (ourTurn) {
			
				List<byte[]> possibleMoves = scg.getRelevantActions(board, OURCOLOUR);
			
				if (possibleMoves.size() == 0) {
					return false;
				}
				
				byte[] move = possibleMoves.get(rng.nextInt(possibleMoves.size()));
				makeMove(board, move, OURCOLOUR);	
				ourTurn = false;
				
				finished = Utility.checkIfFinished(board);
				if (finished) {
					return true;	// We won
				}
			} else {

				List<byte[]> possibleMoves = scg.getRelevantActions(board, OPPONENT);
				
				if (possibleMoves.size() == 0) {
					return true;
				}
				
				byte[] move = possibleMoves.get(rng.nextInt(possibleMoves.size()));
				makeMove(board, move, OPPONENT);	
				ourTurn = true;
				
				finished = Utility.checkIfFinished(board);
				if (finished) {
					return false;	// We lost
				}
			}
		}
		return false;
	}
	
	private void makeMove(Board board, byte[] move, int player){
		board.freeSquare(move[0], move[1]);
		board.placeMarker(move[2], move[3], (byte) player);
		board.placeMarker(move[4], move[5], (byte) 3);	
		
		if (player == 1){
			board.updateWhitePositions(move[0], move[1], move[2], move[3]);
		} else {
			board.updateBlackPositions(move[0], move[1], move[2], move[3]);
		}
		

	}

}
