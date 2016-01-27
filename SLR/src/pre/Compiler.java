package pre;

import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

public class Compiler {
	private Stack<Word> input = new Stack<Word>();
	private Stack<Word> work = new Stack<Word>();
	private Stack<Integer> status = new Stack<Integer>();
	private Worlanalysis wa;
	private Word in_word, act_word;
	private int cur_status, row, col, op;
	boolean print;
	private Vector<Word> action_list;
	private Vector<Row> Table = new Vector<Row>();
	private int lev = 0, addr = 3;
	private Word act_w;
	private int[][] codelist = new int[100][3];
	private int total_code = 0;
	private int while_if_line = -1;
	private int lev0 = 0;
	private Vector<String> erro_list = new Vector<String>();

	public Compiler(Worlanalysis wa, boolean printaction) {
		this.wa = wa;
		this.print = printaction;
		work();
	}

	public void work() {
		Word tmp = new Word("0");
		Stack<Word> tmp_s = new Stack<Word>();
		if (wa.print)
			System.out.println("name\tsymbol\tline");
		tmp = wa.getWord();
		do {
			if (wa.print)
				System.out.println(tmp);
			tmp_s.push(tmp);
			tmp = wa.getWord();
		} while (tmp != null);
		tmp_s.push(new Word("$", '$', 0));// 添加终结符
		while (!tmp_s.isEmpty()) {
			input.push(tmp_s.pop());
		}
		status.push(0);
		gen(Util.opration.indexOf("jmp"), 0, 2);
		while (true) {
			in_word = input.peek();
			cur_status = status.peek();
			col = find_col(in_word.SYM);
			op = Main.table[cur_status][col];
			if (op == 1000) {
				if (print) {
					System.out.println("按" + Main.all_laug.get(0) + "归约\n分析结束，无语法错误！");
				}
				action(0);
				break;
			} else if (op > 1000) {
				op -= 1000;
				if (print)
					System.out.print("按" + Main.all_laug.get(op) + "归约");
				action_list = new Vector<Word>();
				act_word = work.peek();
				action_list.add(act_word);
				for (int n = 0; n < Main.laug[op][1] - 1; n++) {
					work.pop();
					status.pop();
					act_word = work.peek();
					action_list.add(act_word);
				}
				work.pop();
				status.pop();
				act_w = new Word("" + Main.laug[op][0], Main.laug[op][0], 0);
				action(op);
				work.push(act_w);
				col = find_col(Main.laug[op][0]);
				cur_status = status.peek();
				if (print)
					System.out.print(" 退回状态：" + cur_status);
				op = Main.table[cur_status][col];
				status.push(op);
				if (print)
					System.out.println(" 下一状态：" + status.peek());
			} else if (op > 0) {// 移进
				work.push(in_word);
				status.push(op);
				input.pop();
				if (print)
					System.out.println(in_word.NAME + "移进" + "，下一状态："
							+ status.peek());
			} else {
				System.err.println("编译终止！关键错误: 行：" + in_word.lineNum + ",符号：\""
						+ in_word.NAME + "\"");
				break;
			}
		}
	}

	private int find_col(char sym) {
		return Util.sym2int(sym);
	}

	public void action(int op) {
		switch (op) {
		case 0:
			gen(Util.opration.indexOf("opr"), 0, 0);
			break;
		case 10:
			Table.add(new Row(action_list.get(2).NAME, 'c',
					action_list.get(0).val));
			break;
		case 11:
			if (lev == 0)
				lev0 = 3 + action_list.get(1).list.size();
			else
				gen(Util.opration.indexOf("int"), 0,
						3 + action_list.get(1).list.size());
			for (int i = 0; i < action_list.get(1).list.size(); i++) {
				Table.add(new Row(action_list.get(1).list.get(i).NAME, 'd',
						lev, addr++));
			}
			break;
		case 12:
			addr = action_list.get(2).val;
			lev--;
			gen(Util.opration.indexOf("opr"), 0, 0);
			gen(Util.opration.indexOf("int"), 0, lev0);
			codelist[1][2] = total_code;
			break;
		case 13:
			gen(Util.opration.indexOf("jmp"), 0, total_code + 2);
			Row ro = new Row(action_list.get(1).NAME, 'e', lev++, addr++);
			ro.val = total_code;
			Table.add(ro);
			act_w.val = addr;
			addr = 3;
			break;
		case 21:
			int xl = get_lev(action_list.get(2));
			int xa = get_addr(action_list.get(2));
			if (xl == -2)
				erro_list.add("错误！未定义变量：行：" + action_list.get(2).lineNum
						+ "，变量名：\"" + action_list.get(2).NAME + "\"");
			gen(Util.opration.indexOf("sto"), lev - xl, xa);
			break;
		case 23:
			int opcode = -1;
			switch (action_list.get(1).SYM) {
			case '=':
				opcode = 8;
				break;
			case '#':
				opcode = 9;
				break;
			case '<':
				opcode = 10;
				break;
			case 'r':
				opcode = 13;
				break;
			case '>':
				opcode = 11;
				break;
			case 's':
				opcode = 12;
				break;
			}
			if (opcode == -1)
				erro_list.add("错误！比较符号错误：行：" + action_list.get(1).lineNum
						+ " 符号：\"" + action_list.get(1).NAME + "\"");
			gen(Util.opration.indexOf("opr"), 0, opcode);
			gen(Util.opration.indexOf("jpc"), 0, 100);
			while_if_line = total_code;
			break;
		case 25:
			if (action_list.get(1).NAME.equals("+")) {
				act_w.val = action_list.get(2).val + action_list.get(0).val;
				gen(Util.opration.indexOf("opr"), 0, 2);
			} else {
				act_w.val = action_list.get(2).val - action_list.get(0).val;
				gen(Util.opration.indexOf("opr"), 0, 3);
			}
			break;
		case 26:
			act_w.val = action_list.get(0).val;
			act_w.NAME = action_list.get(0).NAME;
			break;
		case 27:
			act_w.val = -action_list.get(0).val;
			break;
		case 28:
			act_w.val = action_list.get(0).val;
			break;
		case 29:
			if (action_list.get(1).NAME.equals("*")) {
				act_w.val = action_list.get(2).val * action_list.get(0).val;
				gen(Util.opration.indexOf("opr"), 0, 4);
			} else {
				if (action_list.get(0).val == 0)
					System.err.println("关键错误！除以0的操作：行："
							+ action_list.get(0).lineNum);
				else {
					act_w.val = action_list.get(2).val / action_list.get(0).val;
					gen(Util.opration.indexOf("opr"), 0, 5);
				}
			}
			break;
		case 30:
			act_w.val = action_list.get(0).val;
			act_w.NAME = action_list.get(0).NAME;
			break;
		case 31:
			int value = get_val(action_list.get(0));
			xl = get_lev(action_list.get(0));
			xa = get_addr(action_list.get(0));
			if (value == -2)
				erro_list.add("错误！未定义变量：行：" + action_list.get(0).lineNum
						+ "，变量名：\"" + action_list.get(0).NAME + "\"");
			else {
				if (xa == 0)
					gen(Util.opration.indexOf("lit"), 0, value);
				else
					gen(Util.opration.indexOf("lod"), lev - xl, xa);
				act_w.val = value;
				act_w.NAME = action_list.get(0).NAME;
			}
			break;
		case 32:
			act_w.val = action_list.get(0).val;
			gen(Util.opration.indexOf("lit"), 0, action_list.get(0).val);
			break;
		case 33:
			act_w.val = action_list.get(1).val;
			break;
		case 34:
			act_w.NAME = action_list.get(0).NAME;
			act_w.SYM = action_list.get(0).SYM;
			break;
		case 35:
			act_w.NAME = action_list.get(0).NAME;
			act_w.SYM = action_list.get(0).SYM;
			break;
		case 36:
			act_w.NAME = action_list.get(0).NAME;
			act_w.SYM = action_list.get(0).SYM;
			break;
		case 37:
			act_w.NAME = action_list.get(0).NAME;
			act_w.SYM = action_list.get(0).SYM;
			break;
		case 38:
			act_w.NAME = action_list.get(0).NAME;
			act_w.SYM = action_list.get(0).SYM;
			break;
		case 39:
			act_w.NAME = action_list.get(0).NAME;
			act_w.SYM = action_list.get(0).SYM;
			break;
		case 40:
			act_w.NAME = action_list.get(0).NAME;
			act_w.SYM = action_list.get(0).SYM;
			break;
		case 41:
			act_w.NAME = action_list.get(0).NAME;
			act_w.SYM = action_list.get(0).SYM;
			break;
		case 42:
			act_w.NAME = action_list.get(0).NAME;
			act_w.SYM = action_list.get(0).SYM;
			break;
		case 43:
			act_w.NAME = action_list.get(0).NAME;
			act_w.SYM = action_list.get(0).SYM;
			break;
		case 44:
			codelist[while_if_line][2] = total_code + 1;// 回填
			break;
		case 45:
			int line = get_val(action_list.get(0));
			if (line == -2)
				erro_list.add("错误！未定义过程：行：" + action_list.get(0).lineNum
						+ "，过程名：\"" + action_list.get(0).NAME + "\"");
			gen(Util.opration.indexOf("cal"), 0, line);
			break;
		case 46:
			gen(Util.opration.indexOf("jmp"), 0, while_if_line - 3);
			codelist[while_if_line][2] = total_code + 1;// 回填
			break;
		case 47:
			xl = codelist[total_code][1];
			xa = codelist[total_code][2];
			total_code--;
			gen(Util.opration.indexOf("opr"), 0, 16);
			gen(Util.opration.indexOf("sto"), lev - xl, xa);
			break;
		case 48:
			gen(Util.opration.indexOf("opr"), 0, 14);
			gen(Util.opration.indexOf("opr"), 0, 15);
			break;
		case 51:
			act_w.list.addAll(action_list.get(2).list);
			act_w.list.add(action_list.get(0));
			break;
		case 52:
			act_w.list.add(action_list.get(0));
			xl = get_lev(action_list.get(0));
			xa = get_addr(action_list.get(0));
			if (xl != -2) {
				if (xa == 0)
					erro_list.add("错误！不能改变常量：行：" + action_list.get(0).lineNum
							+ ",常量名：\"" + action_list.get(0).NAME + "\"");
				else
					gen(Util.opration.indexOf("lod"), lev - xl, xa);
			}
			break;
		default:
			break;
		}
	}

	public int get_val(Word w) {
		for (int i = 0; i < Table.size(); i++) {
			if (Table.get(i).name.equals(w.NAME)) {
				return Table.get(i).val;
			}
		}
		return -2;
	}

	public int get_lev(Word w) {
		for (int i = 0; i < Table.size(); i++) {
			if (Table.get(i).name.equals(w.NAME)) {
				return Table.get(i).lev;
			}
		}
		return -2;
	}

	public int get_addr(Word w) {
		for (int i = 0; i < Table.size(); i++) {
			if (Table.get(i).name.equals(w.NAME)) {
				return Table.get(i).adr;
			}
		}
		return -2;
	}

	public char get_kind(Word w) {
		for (int i = 0; i < Table.size(); i++) {
			if (Table.get(i).name.equals(w.NAME)) {
				return Table.get(i).kind;
			}
		}
		return '0';
	}

	public void printTable() {
		System.out.println("变量表为：");
		for (int i = 0; i < Table.size(); i++)
			System.out.println(Table.get(i));
	}

	public int get_num(int lev) {
		int result = 0;
		for (int i = 0; i < Table.size(); i++)
			if (Table.get(i).lev == lev)
				result++;
		return result;
	}

	public void printCode() {
		if (erro_list.size() != 0) {
			System.err.println("错误数：" + erro_list.size());
			for (int i = 0; i < erro_list.size(); i++)
				System.err.println(erro_list.get(i));
		} else {
			System.out.println("目标代码为：");
			for (int i = 1; i <= total_code; i++)
				System.out.println(i + "-> "
						+ Util.opration.get(codelist[i][0]) + " "
						+ codelist[i][1] + " " + codelist[i][2]);
		}
	}

	private void gen(int op, int l, int a) {
		total_code++;
		codelist[total_code][0] = op;
		codelist[total_code][1] = l;
		codelist[total_code][2] = a;
	}

	public void run_code() {
		Stack<Integer> run_stack = new Stack<Integer>();
		int [][] varlist = new int [3][20];
		int var,level = 0;
		int tmp_x, tmp_y;
		varlist[0][0]=0;									  
		System.out.println("PL\\0解释执行程序：\n>>>>>>>>>>>>开始<<<<<<<<<<<<");
		for (int i = 1; i <= total_code; i++) {
			int op = codelist[i][0];
			int lev = codelist[i][1];
			int addr = codelist[i][2];
			switch (op) {
			case 0:
				run_stack.push(addr);
				break;
			case 1:
				var = varlist[level-lev][addr];
				run_stack.push(var);
				break;
			case 2:
				varlist[level-lev][addr] = run_stack.peek();
				break;
			case 3:
				varlist[++level][0] = i; 
				i = addr - 1;
				break;
			case 4:
				break;
			case 5:
				i = addr - 1;
				break;
			case 6:
				if (run_stack.peek() == 0)
					i = addr - 1;
				break;
			case 7:
				switch (addr) {
				case 0:
					if(varlist[level][0]==0)
						System.out.println(">>>>>>>>>>>>结束<<<<<<<<<<<<");
					else{
						i = varlist[level][0];
						level--;
					}
					break;
				case 1:
					run_stack.push(1 - run_stack.pop());
					break;
				case 2:
					tmp_y = run_stack.pop();
					tmp_x = run_stack.pop();
					run_stack.push(tmp_x + tmp_y);
					break;
				case 3:
					tmp_y = run_stack.pop();
					tmp_x = run_stack.pop();
					run_stack.push(tmp_x - tmp_y);
					break;
				case 4:
					tmp_y = run_stack.pop();
					tmp_x = run_stack.pop();
					run_stack.push(tmp_x * tmp_y);
					break;
				case 5:
					tmp_y = run_stack.pop();
					tmp_x = run_stack.pop();
					run_stack.push(tmp_x / tmp_y);
					break;
				case 6:
					run_stack.push(run_stack.peek() % 2);
					break;
				case 7:
					break;
				case 8:
					tmp_y = run_stack.pop();
					tmp_x = run_stack.pop();
					if (tmp_x == tmp_y)
						run_stack.push(1);
					else
						run_stack.push(0);
					break;
				case 9:
					tmp_y = run_stack.pop();
					tmp_x = run_stack.pop();
					if (tmp_x != tmp_y)
						run_stack.push(1);
					else
						run_stack.push(0);
					break;
				case 10:
					tmp_y = run_stack.pop();
					tmp_x = run_stack.pop();
					if (tmp_x < tmp_y)
						run_stack.push(1);
					else
						run_stack.push(0);
					break;
				case 11:
					tmp_y = run_stack.pop();
					tmp_x = run_stack.pop();
					if (tmp_x > tmp_y)
						run_stack.push(1);
					else
						run_stack.push(0);
					break;
				case 12:
					tmp_y = run_stack.pop();
					tmp_x = run_stack.pop();
					if (tmp_x >= tmp_y)
						run_stack.push(1);
					else
						run_stack.push(0);
					break;
				case 13:
					tmp_y = run_stack.pop();
					tmp_x = run_stack.pop();
					if (tmp_x <= tmp_y)
						run_stack.push(1);
					else
						run_stack.push(0);
					break;
				case 14:
					System.out.print(run_stack.peek());
					break;
				case 15:
					System.out.println();
					break;
				case 16:
					Scanner scan = new Scanner(System.in);
					try{
						var = scan.nextInt();
						run_stack.push(var);
					}catch (Exception e) {
						System.out.println(">>>>>>>>>>>>结束<<<<<<<<<<<<");
						System.err.println("错误！必须输入数字！");
						return;
					}
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}
	}
}
