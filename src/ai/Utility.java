package ai;

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
	
	public static char getColumn(int c){
		switch(c){
		case(0):
			return 'a';
		case(1):
			return 'b';
		case(2):
			return 'c';
		case(3):
			return 'd';
		case(4):
			return 'e';
		case(5):
			return 'f';
		case(6):
			return 'g';
		case(7):
			return 'h';
		case(8):
			return 'i';
		case(9):
			return 'j';
	}
	
	return ' ';
	}
	
}
