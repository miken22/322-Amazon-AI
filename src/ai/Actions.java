package ai;

import java.util.ArrayList;
import java.util.List;

public class Actions {

	public int[] stayStill = { 0, 0 };

	public int[] leftOne = { -1, 0 };
	public int[] leftTwo = { -2, 0 };
	public int[] leftThree = { -3, 0 };
	public int[] leftFour = { -4, 0 };
	public int[] leftFive = { -5, 0 };
	public int[] leftSix = { -6, 0 };
	public int[] leftSeven = { -7, 0 };
	public int[] leftEight = { -8, 0 };
	public int[] leftNine = { -9, 0 };

	public int[] rightOne = { 1, 0 };
	public int[] rightTwo = { 2, 0 };
	public int[] rightThree = { 3, 0 };
	public int[] rightFour = { 4, 0 };
	public int[] rightFive = { 5, 0 };
	public int[] rightSix = { 6, 0 };
	public int[] rightSeven = { 7, 0 };
	public int[] rightEight = { 8, 0 };
	public int[] rightNine = { 9, 0 };

	public int[] upOne = { 0, -1 };
	public int[] upTwo = { 0, -2 };
	public int[] upThree = { 0, -3 };
	public int[] upFour = { 0, -4 };
	public int[] upFive = { 0, -5 };
	public int[] upSix = { 0, -6 };
	public int[] upSeven = { 0, -7 };
	public int[] upEight = { 0, -8 };
	public int[] upNine = { 0, -9 };

	public int[] downOne = { 0, 1 };
	public int[] downTwo = { 0, 2 };
	public int[] downThree = { 0, 3 };
	public int[] downFour = { 0, 4 };
	public int[] downFive = { 0, 5 };
	public int[] downSix = { 0, 6 };
	public int[] downSeven = { 0, 7 };
	public int[] downEight = { 0, 8 };
	public int[] downNine = { 0, 9 };
	
	public int[] downLeftOne = { -1, -1 };
	public int[] downLeftTwo = { -2, -2 };
	public int[] downLeftThree = { -3, -3 };
	public int[] downLeftFour = { -4, -4 };
	public int[] downLeftFive = { -5, -5 };
	public int[] downLeftSix = { -6, -6 };
	public int[] downLeftSeven = { -7, -7 };
	public int[] downLeftEight = { -8, -8 };
	public int[] downLeftNine = { -9, -9 };
	
	public int[] downRightOne = { -1, 1 };
	public int[] downRightTwo = { -2, 2 };
	public int[] downRightThree = { -3, 3 };
	public int[] downRightFour = { -4, 4 };
	public int[] downRightFive = { -5, 5 };
	public int[] downRightSix = { -6, 6 };
	public int[] downRightSeven = { -7, 7 };
	public int[] downRightEight = { -8, 8 };
	public int[] downRightNine = { -9, 9 };

	public int[] upRightOne = { 1, 1 };
	public int[] upRightTwo = { 2, 2 };
	public int[] upRightThree = { 3, 3 };
	public int[] upRightFour = { 4, 4 };
	public int[] upRightFive = { 5, 5 };
	public int[] upRightSix = { 6, 6 };
	public int[] upRightSeven = { 7, 7 };
	public int[] upRightEight = { 8, 8 };
	public int[] upRightNine = { 9, 9 };

	public int[] upLeftOne = { 1, -1 };
	public int[] upLeftTwo = { 2, -2 };
	public int[] upLeftThree = { 3, -3 };
	public int[] upLeftFour = { 4, -4 };
	public int[] upLeftFive = { 5, -5 };
	public int[] upLeftSix = { 6, -6 };
	public int[] upLeftSeven = { 7, -7 };
	public int[] upLeftEight = { 8, -8 };
	public int[] upLeftNine = { 9, -9 };
	
	List<int[]> actions;
	List<int[]> arrowThrows;
	
	public Actions(){
		
		actions = new ArrayList<>();
		arrowThrows = new ArrayList<>();
	
		actions.add(stayStill);
		
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

	public List<int[]> getActions(){	
		return actions;
	}
	
	public List<int[]> getArrowThrows(){
		return arrowThrows;
	}
		
}
