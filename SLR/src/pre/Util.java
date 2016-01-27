package pre;

import java.util.Vector;

public class Util {
	static public Vector<String> opration = new Vector<String>();
	public static void init(){
		opration.add("lit");//0
		opration.add("lod");//1
		opration.add("sto");//2
		opration.add("cal");//3
		opration.add("int");//4
		opration.add("jmp");//5
		opration.add("jpc");//6
		opration.add("opr");//7
	}
	static String changeChar(String str, int i, int j) {
		String result = str;
		if (j < str.length() && i > 0) {
			String back = str.substring(j + 1, str.length());
			String front = str.substring(0, i);
			char x = str.charAt(i);
			char y = str.charAt(j);
			result = front + y + x + back;
		} else {
			if (i == 0 && j == str.length()) {
				char x = str.charAt(i);
				char y = str.charAt(j);
				result = "" + y + x;
			} else {
				if (j == str.length()) {
					String front = str.substring(0, i - 1);
					char x = str.charAt(i);
					char y = str.charAt(j);
					result = front + y + x;
				} else {
					String back = str.substring(j + 1, str.length());
					char x = str.charAt(i);
					char y = str.charAt(j);
					result = "" + y + x + back;
				}
			}
		}
		return result;
	}
	static int sym2int(char ch) {
		switch (ch) {
		case 'a':
			return 0;
		case 'b':
			return 1;
		case 'c':
			return 2;
		case 'd':
			return 3;
		case 'e':
			return 4;
		case 'f':
			return 5;
		case 'g':
			return 6;
		case 'h':
			return 7;
		case 'i':
			return 8;
		case 'j':
			return 9;
		case 'm':
			return 10;
		case 'n':
			return 11;
		case 'o':
			return 12;
		case 'p':
			return 13;
		case 'q':
			return 14;
		case 'r':
			return 15;
		case 's':
			return 16;
		case 't':
			return 17;
		case '+':
			return 18;
		case '-':
			return 19;
		case '*':
			return 20;
		case '/':
			return 21;
		case '=':
			return 22;
		case '#':
			return 23;
		case '<':
			return 24;
		case '>':
			return 25;
		case '(':
			return 26;
		case ')':
			return 27;
		case ',':
			return 28;
		case ';':
			return 29;
		case '.':
			return 30;
		case '$':
			return 31;
		case 'A':
			return 32;
		case 'B':
			return 33;
		case 'C':
			return 34;
		case 'D':
			return 35;
		case 'E':
			return 36;
		case 'F':
			return 37;
		case 'G':
			return 38;
		case 'H':
			return 39;
		case 'I':
			return 40;
		case 'J':
			return 41;
		case 'K':
			return 42;
		case 'L':
			return 43;
		case 'M':
			return 44;
		case 'N':
			return 45;
		case 'O':
			return 46;
		case 'P':
			return 47;
		case 'Q':
			return 48;
		case 'R':
			return 49;
		case 'S':
			return 50;
		case 'T':
			return 51;
		case 'U':
			return 52;
		case 'V':
			return 53;
		case 'W':
			return 54;
		case 'X':
			return 55;
		case 'Y':
			return 56;
		case 'Z':
			return 57;
		default:
			return -1;
		}
	}
}
