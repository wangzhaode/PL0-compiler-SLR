package pre;

import java.util.Vector;

/**
 * µ¥´Ê
 */
public class Word {
	public char SYM;
	public String NAME;
	public int lineNum;
	public int val;
	public Vector<Word> list = new Vector<Word>();

	public Word() {
		this.NAME = null;
		this.lineNum = -1;
	}

	public Word(Word w) {
		this.SYM = w.SYM;
		this.NAME = w.NAME;
		this.lineNum = w.lineNum;
		if (SYM == 'a')
			val = Integer.parseInt(NAME);
	}

	public Word(String name) {
		this.NAME = name;
	}

	public Word(String name, char sym) {
		this.SYM = sym;
		this.NAME = name;
		if (sym == 'a')
			val = Integer.parseInt(NAME);
	}

	public Word(String name, char sym, int linenum) {
		this.SYM = sym;
		this.NAME = name;
		this.lineNum = linenum;
		if (sym == 'a')
			val = Integer.parseInt(NAME);
	}

	public void setLineNum(int l) {
		lineNum = l;
	}

	public int getLineNum() {
		return lineNum;
	}

	public String getName() {
		return NAME;
	}

	public String getSym() {
		switch(SYM){
		case 'c':
			return "const";
		case 'd':
			return "var";
		case 'e':
			return "procedure";
		default:
			return "";
		}
	}

	@Override
	public String toString() {
		return NAME + "\t" + getSym() + "\t" + lineNum;
	}

}
