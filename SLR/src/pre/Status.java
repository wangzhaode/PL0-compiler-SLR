package pre;

import java.util.HashMap;
import java.util.Vector;

public class Status {
	private SymList symlist = new SymList();
	private Item start_sym;
	private int status;

	public Status(Item start_sym, int status, HashMap<Character, SYM> symMap) {
		Main.all_symlist.setItemstatus(start_sym, status);
		this.start_sym = start_sym;
		this.status = status;
		symlist.add(start_sym);
		work(symMap);
	}

	public Status(SymList start_symlist, int status,
			HashMap<Character, SYM> symMap) {
		this.symlist = start_symlist;
		this.status = status;
		Main.all_symlist.setItemstatus(symlist, status);
		work(symMap);
	}

	public void work(HashMap<Character, SYM> symMap) {
		Clouser();
		Action(symMap);
		Goto(symMap);
		//print();
	}

	public void Clouser() {
		SymList tmplist = new SymList();
		SymList appearlist = new SymList();
		tmplist.add(symlist);
		Vector<Character> appear = new Vector<Character>();
		char next;
		while (tmplist.getSize() != 0) {
			for (int i = 0; i < tmplist.getSize(); i++) {
				int n = tmplist.index(i).content.indexOf('|');
				if (n + 1 < tmplist.index(i).content.length())
					next = tmplist.index(i).content.charAt(n + 1);
				else
					next = '0';
				appearlist.add(tmplist.remove(i));
				if (next >= 'A' && next <= 'Z' && !appear.contains(next)) {
					appear.add(next);
					symlist.add(Main.all_symlist.getClolist(next));
					tmplist.add(Main.all_symlist.getClolist(next));
				}
			}
		}
	}

	public void Action(HashMap<Character, SYM> symMap) {
		SymList action_list = symlist.getAction();
		Vector<Character> action_vect;
		char action_sym;
		int action_index;
		String tmp_str;
		if (action_list.getSize() != 0) {
			for (int i = 0; i < action_list.getSize(); i++) {
				action_sym =  action_list.index(i).head;
				tmp_str = action_list.index(i).toString();
				tmp_str = tmp_str.substring(0,tmp_str.length()-1);
				action_index = Main.all_laug.indexOf(tmp_str);
				action_vect = symMap.get(action_sym).getFollow();
				for (int j = 0; j < action_vect.size(); j++) {
					Main.table[status][Util.sym2int(action_vect.get(j))] = 1000 + action_index;
				}
			}
		}
	}

	public void Goto(HashMap<Character, SYM> symMap) {
		char head;
		String content;
		int n, tmp_status = -1;
		char curr_ch;
		SymList gotosym;
		Vector<Character> appear = new Vector<Character>();
		for (int i = 0; i < symlist.getSize(); i++) {
			head = symlist.index(i).head;
			content = symlist.index(i).content;
			n = content.indexOf('|');
			if (n < content.length() - 1) {
				curr_ch = content.charAt(n + 1);
				if (!appear.contains(curr_ch)) {
					gotosym = symlist.getGoto(curr_ch, tmp_status);
					tmp_status = Main.all_symlist.findStatus(gotosym);
					if (tmp_status != -1) {
						Main.table[status][Util.sym2int(curr_ch)] = tmp_status;
					} else {
						appear.add(curr_ch);
						tmp_status = Main.all_symlist.getMaxstatus() + 1;
						Main.table[status][Util.sym2int(curr_ch)] = tmp_status;
						gotosym = symlist.getGoto(curr_ch, tmp_status);
						new Status(gotosym, tmp_status, symMap);
					}
				}
			}
		}
	}

	public void print() {
		if(status==2||status==1){
		System.out.println(status + "×´Ì¬¸öÊý£º" + symlist.getSize());
		for (int i = 0; i < symlist.getSize(); i++)
			System.out.println(symlist.index(i));
		System.out.println();
		//for (int j = 0; j < Main.table[65].length; j++)
			System.out.println(Main.table[59][Util.sym2int('g')] + " ");
		}
	}
}
