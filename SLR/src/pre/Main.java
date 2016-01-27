package pre;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

public class Main {
	static HashMap<Character, SYM> symMap = new HashMap<Character, SYM>();
	static SymList all_symlist = new SymList();
	static int[][] table = new int[150][58];
	static char[][] laug = new char[57][2];
	static Vector<String> all_laug = new Vector<String>();
	static String filename = "wenfa2.txt";
	static String resoucefile = "resouce.txt";
	public static void main(String[] args) {
		Util.init();
		FirstFollow initff = new FirstFollow(symMap, filename);
		initff.initFirst(true);
		initff.initFollow(true);
		Table initt = new Table(filename);
		initt.initTable(true);
		Worlanalysis wa = new Worlanalysis(resoucefile,true);
		Compiler com = new  Compiler(wa,true);
		com.printTable();
		com.printCode();
		com.run_code();
	}

}