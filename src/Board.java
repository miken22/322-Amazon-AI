import ubco.ai.games.BoardGameModel;


public class Board extends BoardGameModel {

	
	public Board(int rows, int columns) {
		super(rows, columns);
	}
	
	public void initialize(){
		positionMarked(0, 3, 0, 0, 0, 0, false);
		positionMarked(0, 6, 0, 0, 0, 0, false);
		positionMarked(3, 0, 0, 0, 0, 0, false);
		positionMarked(6, 0, 0, 0, 0, 0, false);
		positionMarked(9, 3, 0, 0, 0, 0, false);
		positionMarked(9, 6, 0, 0, 0, 0, false);
		
	}
	
	

}
