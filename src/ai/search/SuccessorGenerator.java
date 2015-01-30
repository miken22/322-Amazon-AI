package ai.search;

import java.util.ArrayList;
import java.util.List;

import ai.Actions;
import ai.Board;
import ai.Pair;

public class SuccessorGenerator {

	
	private Actions actions;

	private int OURCOLOUR;
	private final int ARROW = 3;
	private final int FREE = -1;
	
	public SuccessorGenerator(int ourColour){
		this.OURCOLOUR = ourColour;
	}
	
	public List<Board> getSuccessors(Board board){
		
		List<Board> successors = new ArrayList<>();
		List<Pair<Integer, Integer> > queenPositions;
		
		if (OURCOLOUR == 1){
			queenPositions = board.getWhitePositions();
		} else {
			queenPositions = board.getBlackPositions();
		}
		
		for (Pair p : queenPositions){
			
			for (int i = 0; i < 10; i++){
				
				
				
			}
			
		}
		
		
		return successors;
	}
	
	
}
