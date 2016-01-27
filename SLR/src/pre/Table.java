package pre;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Table {
	private String filename;
public Table(String filename){
	this.filename = filename;
	for (int i = 0; i < 150; i++) {
		for (int j = 0; j < 57; j++) {
			Main.table[i][j] = -1;
		}
	}
}
public void initTable(boolean print){
	try {
		FileReader reader = new FileReader(filename);
		BufferedReader br = new BufferedReader(reader);
		String line = null;
		char head;
		int line_number = 0,right_len;
		while ((line = br.readLine()) != null) {
			Main.all_laug.add(line);
			head = line.charAt(0);
			right_len = line.length()-2;
			Main.laug[line_number][0]=head;
			Main.laug[line_number++][1]=(char) right_len;
			line = line.substring(1, line.length());
			line = line.replace('¡ú', '|');
			Main.all_symlist.add(head, line, -1);
			for (int i = 0; i < line.length() - 1; i++) {
				line = Util.changeChar(line, i, i + 1);
				Main.all_symlist.add(head, line, -1);
			}
		}
		new Status(Main.all_symlist.getStrat(), 0, Main.symMap);
		if(print){
			System.out.println("SLR·ÖÎö±íÎª£º");
			for (int i = 0; i <= Main.all_symlist.getMaxstatus(); i++) {
				for (int j = 0; j < 57; j++) {
					System.out.print(Main.table[i][j]+" ");
				}
				System.out.println();
		}
		}
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
}
}
