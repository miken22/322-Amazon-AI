package ai;

import java.util.ArrayList;
import java.util.List;

public class Actions {

	public byte[] stayStill = { 0, 0 };

	public byte[] leftOne = { -1, 0 };
	public byte[] leftTwo = { -2, 0 };
	public byte[] leftThree = { -3, 0 };
	public byte[] leftFour = { -4, 0 };
	public byte[] leftFive = { -5, 0 };
	public byte[] leftSix = { -6, 0 };
	public byte[] leftSeven = { -7, 0 };
	public byte[] leftEight = { -8, 0 };
	public byte[] leftNine = { -9, 0 };

	public byte[] rightOne = { 1, 0 };
	public byte[] rightTwo = { 2, 0 };
	public byte[] rightThree = { 3, 0 };
	public byte[] rightFour = { 4, 0 };
	public byte[] rightFive = { 5, 0 };
	public byte[] rightSix = { 6, 0 };
	public byte[] rightSeven = { 7, 0 };
	public byte[] rightEight = { 8, 0 };
	public byte[] rightNine = { 9, 0 };

	public byte[] upOne = { 0, -1 };
	public byte[] upTwo = { 0, -2 };
	public byte[] upThree = { 0, -3 };
	public byte[] upFour = { 0, -4 };
	public byte[] upFive = { 0, -5 };
	public byte[] upSix = { 0, -6 };
	public byte[] upSeven = { 0, -7 };
	public byte[] upEight = { 0, -8 };
	public byte[] upNine = { 0, -9 };

	public byte[] downOne = { 0, 1 };
	public byte[] downTwo = { 0, 2 };
	public byte[] downThree = { 0, 3 };
	public byte[] downFour = { 0, 4 };
	public byte[] downFive = { 0, 5 };
	public byte[] downSix = { 0, 6 };
	public byte[] downSeven = { 0, 7 };
	public byte[] downEight = { 0, 8 };
	public byte[] downNine = { 0, 9 };
	
	public byte[] downLeftOne = { -1, -1 };
	public byte[] downLeftTwo = { -2, -2 };
	public byte[] downLeftThree = { -3, -3 };
	public byte[] downLeftFour = { -4, -4 };
	public byte[] downLeftFive = { -5, -5 };
	public byte[] downLeftSix = { -6, -6 };
	public byte[] downLeftSeven = { -7, -7 };
	public byte[] downLeftEight = { -8, -8 };
	public byte[] downLeftNine = { -9, -9 };
	
	public byte[] downRightOne = { -1, 1 };
	public byte[] downRightTwo = { -2, 2 };
	public byte[] downRightThree = { -3, 3 };
	public byte[] downRightFour = { -4, 4 };
	public byte[] downRightFive = { -5, 5 };
	public byte[] downRightSix = { -6, 6 };
	public byte[] downRightSeven = { -7, 7 };
	public byte[] downRightEight = { -8, 8 };
	public byte[] downRightNine = { -9, 9 };

	public byte[] upRightOne = { 1, 1 };
	public byte[] upRightTwo = { 2, 2 };
	public byte[] upRightThree = { 3, 3 };
	public byte[] upRightFour = { 4, 4 };
	public byte[] upRightFive = { 5, 5 };
	public byte[] upRightSix = { 6, 6 };
	public byte[] upRightSeven = { 7, 7 };
	public byte[] upRightEight = { 8, 8 };
	public byte[] upRightNine = { 9, 9 };

	public byte[] upLeftOne = { 1, -1 };
	public byte[] upLeftTwo = { 2, -2 };
	public byte[] upLeftThree = { 3, -3 };
	public byte[] upLeftFour = { 4, -4 };
	public byte[] upLeftFive = { 5, -5 };
	public byte[] upLeftSix = { 6, -6 };
	public byte[] upLeftSeven = { 7, -7 };
	public byte[] upLeftEight = { 8, -8 };
	public byte[] upLeftNine = { 9, -9 };
	
	List<byte[]> actions;
	List<byte[]> arrowThrows;
	
	public Actions(){
		
		actions = new ArrayList<>();
		arrowThrows = new ArrayList<>();
			
		actions.add(leftOne);
		actions.add(leftTwo);
		actions.add(leftThree);
		actions.add(leftFour);
		actions.add(leftFive);
		actions.add(leftSix);
		actions.add(leftSeven);
		actions.add(leftEight);
		actions.add(leftNine);
		
		actions.add(rightOne);
		actions.add(rightTwo);
		actions.add(rightThree);
		actions.add(rightFour);
		actions.add(rightFive);
		actions.add(rightSix);
		actions.add(rightSeven);
		actions.add(rightEight);
		actions.add(rightNine);
		
		actions.add(upOne);
		actions.add(upTwo);
		actions.add(upThree);
		actions.add(upFour);
		actions.add(upFive);
		actions.add(upSix);
		actions.add(upSeven);
		actions.add(upEight);
		actions.add(upNine);
		
		actions.add(downOne);
		actions.add(downTwo);
		actions.add(downThree);
		actions.add(downFour);
		actions.add(downFive);
		actions.add(downSix);
		actions.add(downSeven);
		actions.add(downEight);
		actions.add(downNine);
		
		actions.add(downLeftOne);
		actions.add(downLeftTwo);
		actions.add(downLeftThree);
		actions.add(downLeftFour);
		actions.add(downLeftFive);
		actions.add(downLeftSix);
		actions.add(downLeftSeven);
		actions.add(downLeftEight);
		actions.add(downLeftNine);
		
		actions.add(downRightOne);
		actions.add(downRightTwo);
		actions.add(downRightThree);
		actions.add(downRightFour);
		actions.add(downRightFive);
		actions.add(downRightSix);
		actions.add(downRightSeven);
		actions.add(downRightEight);
		actions.add(downRightNine);
		
		actions.add(upLeftOne);
		actions.add(upLeftTwo);
		actions.add(upLeftThree);
		actions.add(upLeftFour);
		actions.add(upLeftFive);
		actions.add(upLeftSix);
		actions.add(upLeftSeven);
		actions.add(upLeftEight);
		actions.add(upLeftNine);
		
		actions.add(upRightOne);
		actions.add(upRightTwo);
		actions.add(upRightThree);
		actions.add(upRightFour);
		actions.add(upRightFive);
		actions.add(upRightSix);
		actions.add(upRightSeven);
		actions.add(upRightEight);
		actions.add(upRightNine);
		
		actions.add(stayStill);
		
		arrowThrows.add(leftNine);
		arrowThrows.add(leftEight);
		arrowThrows.add(leftSeven);
		arrowThrows.add(leftSix);
		arrowThrows.add(leftFive);
		arrowThrows.add(leftFour);
		arrowThrows.add(leftThree);
		arrowThrows.add(leftTwo);
		arrowThrows.add(leftOne);

		arrowThrows.add(rightNine);
		arrowThrows.add(rightEight);
		arrowThrows.add(rightSeven);
		arrowThrows.add(rightSix);
		arrowThrows.add(rightFive);
		arrowThrows.add(rightFour);
		arrowThrows.add(rightThree);
		arrowThrows.add(rightTwo);
		arrowThrows.add(rightOne);

		arrowThrows.add(upNine);
		arrowThrows.add(upEight);
		arrowThrows.add(upSeven);
		arrowThrows.add(upSix);
		arrowThrows.add(upFive);
		arrowThrows.add(upFour);
		arrowThrows.add(upThree);
		arrowThrows.add(upTwo);
		arrowThrows.add(upOne);

		arrowThrows.add(downNine);
		arrowThrows.add(downEight);
		arrowThrows.add(downSeven);
		arrowThrows.add(downSix);
		arrowThrows.add(downFive);
		arrowThrows.add(downFour);
		arrowThrows.add(downThree);
		arrowThrows.add(downTwo);
		arrowThrows.add(downOne);

		arrowThrows.add(downLeftNine);
		arrowThrows.add(downLeftEight);
		arrowThrows.add(downLeftSeven);
		arrowThrows.add(downLeftSix);
		arrowThrows.add(downLeftFive);
		arrowThrows.add(downLeftFour);
		arrowThrows.add(downLeftThree);
		arrowThrows.add(downLeftTwo);
		arrowThrows.add(downLeftOne);

		arrowThrows.add(downRightNine);
		arrowThrows.add(downRightEight);
		arrowThrows.add(downRightSeven);
		arrowThrows.add(downRightSix);
		arrowThrows.add(downRightFive);
		arrowThrows.add(downRightFour);
		arrowThrows.add(downRightThree);
		arrowThrows.add(downRightTwo);
		arrowThrows.add(downRightOne);

		arrowThrows.add(upLeftNine);
		arrowThrows.add(upLeftEight);
		arrowThrows.add(upLeftSeven);
		arrowThrows.add(upLeftSix);
		arrowThrows.add(upLeftFive);
		arrowThrows.add(upLeftFour);
		arrowThrows.add(upLeftThree);
		arrowThrows.add(upLeftTwo);
		arrowThrows.add(upLeftOne);
		
		arrowThrows.add(upRightNine);
		arrowThrows.add(upRightEight);
		arrowThrows.add(upRightSeven);
		arrowThrows.add(upRightSix);
		arrowThrows.add(upRightFive);
		arrowThrows.add(upRightFour);
		arrowThrows.add(upRightThree);
		arrowThrows.add(upRightTwo);
		arrowThrows.add(upRightOne);

		
	}

	public List<byte[]> getActions(){	
		return actions;
	}
	
	public List<byte[]> getArrowThrows(){
		return arrowThrows;
	}
		
}
