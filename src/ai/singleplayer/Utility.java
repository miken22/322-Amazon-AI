package ai.singleplayer;

public class Utility {
	
	public static int getColumn(char c){
	
		switch(c){
			case('a'):
				return 0;
			case('b'):
				return 1;
			case('c'):
				return 2;
			case('d'):
				return 3;
			case('e'):
				return 4;
			case('f'):
				return 5;
			case('g'):
				return 6;
			case('h'):
				return 7;
			case('i'):
				return 8;
			case('j'):
				return 9;
		}
		
		return -1;
	}
	
}
