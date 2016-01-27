package pre;

/**
 * 对每个过程的说明对象造Table表
 */
public class Row {
	public String name;
	char kind;
	// val：值 ， lev：层次， adr：地址
	public int val, lev, adr;

	public Row() {
		this.name = null;
		this.kind = ' ';
		this.val = -1;
		this.lev = 0;
		this.adr = 0;
	}

	public Row(Row t) {
		this.name = t.name;
		this.kind = t.kind;
		this.val = t.val;
		this.lev = t.lev;
		this.adr = t.adr;
	}

	public Row(String name, char kind, int val) {
		this.name = name;
		this.kind = kind;
		this.val = val;
		this.lev = 0;
		this.adr = 0;
	}

	public Row(String name, char kind, int lev, int adr) {
		this.name = name;
		this.kind = kind;
		this.val = -1;
		this.lev = lev;
		this.adr = adr;
	}

	public void setAdr(int a) {
		adr = a;
	}

	public int getAdr() {
		return adr;
	}

	public char getKind() {
		return kind;
	}
	
	public String getSym() {
		switch(kind){
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
	
	public int getVal() {
		return val;
	}

	public String getName() {
		return name;
	}

	public int getLev() {
		return lev;
	}

	/**
	 * 常量说明
	 */
	public void showConst() {
		System.out.println("NAME: " + name + "\t" + "KIND: " + getSym() + "\t"
				+ "VAL: " + val);
	}

	/**
	 * 变量和过程说明
	 */
	public void show() {
		System.out.println("NAME: " + name + "\t" + "KIND: " + getSym() + "\t"
				+ "LEVEL: " + lev + "\t" + "ADR: " + adr);
	}

	@Override
	public String toString() {
		if (val == -1)
			return "NAME: " + name + "\t" + "KIND: " + getSym() + "\t" + "LEVEL: "
					+ lev + "\t" + "ADR: " + adr;
		else
			return "NAME: " + name + "\t" + "KIND: " + getSym() + "\t" + "VAL: "
					+ val;
	}

}
