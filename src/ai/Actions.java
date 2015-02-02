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

	public int[] upLeftOne = { -1, 1 };
	public int[] upLeftTwo = { -2, 2 };
	public int[] upLeftThree = { -3, 3 };
	public int[] upLeftFour = { -4, 4 };
	public int[] upLeftFive = { -5, 5 };
	public int[] upLeftSix = { -6, 6 };
	public int[] upLeftSeven = { -7, 7 };
	public int[] upLeftEight = { -8, 8 };
	public int[] upLeftNine = { -9, 9 };
	
	List<int[]> actions;
	
	public Actions(){
		
		actions = new ArrayList<>();
	
		actions.add(leftNine);
		actions.add(leftEight);
		actions.add(leftSeven);
		actions.add(leftSix);
		actions.add(leftFive);
		actions.add(leftFour);
		actions.add(leftThree);
		actions.add(leftTwo);
		actions.add(leftOne);

		actions.add(rightNine);
		actions.add(rightEight);
		actions.add(rightSeven);
		actions.add(rightSix);
		actions.add(rightFive);
		actions.add(rightFour);
		actions.add(rightThree);
		actions.add(rightTwo);
		actions.add(rightOne);

		actions.add(upNine);
		actions.add(upEight);
		actions.add(upSeven);
		actions.add(upSix);
		actions.add(upFive);
		actions.add(upFour);
		actions.add(upThree);
		actions.add(upTwo);
		actions.add(upOne);

		actions.add(downNine);
		actions.add(downEight);
		actions.add(downSeven);
		actions.add(downSix);
		actions.add(downFive);
		actions.add(downFour);
		actions.add(downThree);
		actions.add(downTwo);
		actions.add(downOne);

		actions.add(downLeftNine);
		actions.add(downLeftEight);
		actions.add(downLeftSeven);
		actions.add(downLeftSix);
		actions.add(downLeftFive);
		actions.add(downLeftFour);
		actions.add(downLeftThree);
		actions.add(downLeftTwo);
		actions.add(downLeftOne);

		actions.add(downRightNine);
		actions.add(downRightEight);
		actions.add(downRightSeven);
		actions.add(downRightSix);
		actions.add(downRightFive);
		actions.add(downRightFour);
		actions.add(downRightThree);
		actions.add(downRightTwo);
		actions.add(downRightOne);

		actions.add(upLeftNine);
		actions.add(upLeftEight);
		actions.add(upLeftSeven);
		actions.add(upLeftSix);
		actions.add(upLeftFive);
		actions.add(upLeftFour);
		actions.add(upLeftThree);
		actions.add(upLeftTwo);
		actions.add(upLeftOne);
		
		actions.add(upRightNine);
		actions.add(upRightEight);
		actions.add(upRightSeven);
		actions.add(upRightSix);
		actions.add(upRightFive);
		actions.add(upRightFour);
		actions.add(upRightThree);
		actions.add(upRightTwo);
		actions.add(upRightOne);

		actions.add(stayStill);
		
	}

	public List<int[]> getActions(){	
		return actions;
	}
		
}
