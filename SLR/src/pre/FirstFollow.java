package pre;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class FirstFollow {
	private HashMap<Character, SYM> symMap;
	private HashMap<Character, SYM> rela = new HashMap<Character, SYM>();
	private Vector<String> appear = new Vector<String>();
	private String filename;
	private FileReader reader;
	private BufferedReader br;
	private String line = null;

	public FirstFollow(HashMap<Character, SYM> symMap, String filename) {
		this.symMap = symMap;
		this.filename = filename;
	}

	public void initFirst(boolean print) {
		try {
			reader = new FileReader(filename);
			br = new BufferedReader(reader);
			char first, next;
			SYM firstsym;
			while ((line = br.readLine()) != null) {
				first = line.charAt(0);
				next = line.charAt(2);

				if (!symMap.containsKey(first))
					firstsym = new SYM(first);
				else
					firstsym = symMap.get(first);

				if (rela.containsKey(first)) {
					firstsym.setPrefirst(rela.get(first));
					rela.remove(first);
				}
				if (next >= 'A' && next <= 'Z') {
					if (!rela.containsKey(next) && next != first)
						rela.put(next, firstsym);
				} else {
					firstsym.setFirst(next);
				}
				if (!symMap.containsKey(first))
					symMap.put(first, firstsym);
			}
			br.close();
			reader.close();
			for (char i = 'A'; !rela.isEmpty() && i < 'Z'; i++) {
				if (rela.containsKey(i)) {
					rela.get(i).setFirst(symMap.get(i).getFirst());
					rela.remove(i);
				}
			}
			if (print) {
				for (char i = 'A'; i < 'Z'; i++) {
					if (symMap.containsKey(i))
						symMap.get(i).printFirst();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initFollow(boolean print) {
		try {
			reader = new FileReader(filename);
			br = new BufferedReader(reader);
			char first, end, temp, next;
			while ((line = br.readLine()) != null) {
				first = line.charAt(0);
				end = line.charAt(line.length() - 1);
				if (end >= 'A' && end <= 'Z' && end != first
						&& !appear.contains("" + first + end)) {
					appear.add("" + first + end);
					appear.add("" + end + first);
					symMap.get(first).setPrefollow(symMap.get(end));
				}
			}
			reader = new FileReader(filename);
			br = new BufferedReader(reader);
			while ((line = br.readLine()) != null) {
				for (int i = 2; i < line.length() - 1; i++) {
					temp = line.charAt(i);
					next = line.charAt(i + 1);
					if (temp >= 'A' && temp <= 'Z') {
						if (next >= 'A' && next <= 'Z') {
							symMap.get(temp).setFollow(
									symMap.get(next).getFirst());
						} else {
							symMap.get(temp).setFollow(next);
						}
					}
				}
			}
			symMap.get('A').setFollow('$');
			for (char i = 'A'; i < 'Z'; i++) {
				if (symMap.containsKey(i)) {
					symMap.get(i).unique();
					if (print)
						symMap.get(i).printFollow();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
