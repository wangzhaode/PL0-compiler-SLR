package pre;

import java.io.*;

/**
 * 词法分析
 */
public class Worlanalysis {
	BufferedReader in;
	String str,str1; // 读取一行
	int Length;// 存放每行的字符数
	int lineNum = 0;// 存放行号
	int i;// 存放当前读取的字符位置
	public char Cha; // 存放当前读取的字符
	public boolean isEnd = false;
	static Word KEY[]; // 关键字表
	public Error error;
	public int errorNum = 0; // 词法分析错误数目

	int len = 10; // 定义标识符的最大长度
	int nmax = 4;// 定义最大整数位数
	boolean print;
	/**
	 * 词法分析
	 * 
	 * @param filename
	 */
	public Worlanalysis(String filename,boolean print) {
		this.print = print;
		try {
			FileReader fr = new FileReader(filename);
			in = new BufferedReader(fr); // BufferedReader比FileReader效率高
			str = in.readLine().trim(); // 忽略前导空白和尾部空白
			Length = str.length();
			lineNum = 1;
			// 把文本开头的空行都去掉
			while (Length == 0) {
				str = in.readLine().trim();
				Length = str.length();
				lineNum++;
			}
			i = 0;
			getchar();
		} catch (FileNotFoundException a) {
			System.out.println("没有找到文件！");
		} catch (IOException b) {
			System.out.println("文件读取错误！");
		}

		// 关键字表
		KEY = new Word[13];
		KEY[0] = new Word("begin", 'f');
		KEY[1] = new Word("call", 'm');
		KEY[2] = new Word("const", 'c');
		KEY[3] = new Word("do", 'o');
		KEY[4] = new Word("end", 'g');
		KEY[5] = new Word("if", 'p');
		KEY[6] = new Word("odd", 'h');
		KEY[7] = new Word("procedure", 'e');
		KEY[8] = new Word("read", 'i');
		KEY[9] = new Word("then", 'q');
		KEY[10] = new Word("var", 'd');
		KEY[11] = new Word("while", 'n');
		KEY[12] = new Word("write", 'j');
	}

	/**
	 * 读取一个字符
	 */
	public void getchar() {
		if (isEnd)
			return;
		try {
			if (i == Length)// 如果到达行尾,则重新读取一行源码
			{
				str = in.readLine();
				if (str == null) {
					isEnd = true;
					return;
				}
				str = str.trim();
				Length = str.length();
				lineNum++;
				// 略去空行
				while (Length == 0) {
					str1 = in.readLine();
					if (str1 == null) {
						isEnd = true;
						return;
					}
					str = str1.trim();
					Length = str.length();
					lineNum++;
				}
				i = 0;
				Cha = str.charAt(i);
				i++;
			} else {
				Cha = str.charAt(i);
				i++;
			}
		} catch (IOException c) {
			System.out.println("字符读取错误！");
		}
	}

	/**
	 * @return 返回错误数
	 */
	public int getErrorNumber() {
		return errorNum;
	}

	/**
	 * @return 返回word对象
	 */
	public Word getWord() {
		if (isEnd)
			return null;
		int charNum1;// 记录当前单词的开始索引
		int charNum2;// 记录当前单词的结束索引
		String tokenName;// 当前单词
		// 滤掉单词间的空格
		while (Cha == ' ') {
			getchar();
		}
		// 识别标识符和关键字
		if (Cha >= 'a' && Cha <= 'z') {// 以字母开头
			charNum1 = i - 1; // 标识符的起始位置,因为在getchar()中i++,所以这里i-1
			charNum2 = i; // 标识符的最后一个字符的后一个位置,Cha对应的索引为i-1
			while ((Character.isLetter(Cha) || Character.isDigit(Cha))
					&& i < Length) {
				charNum2 = i;
				getchar();
			}
			/*
			 * 判断为什么跳出while循环。 如果Cha还是字母或者数字，则是因为此行读完而停止的，此时chatNum2=Length-1,
			 * Cha=str.charAt(Length-1)， 则从charNum1开始的字符串就都是标识符。
			 * 如果Cha不是字母或数字，则表示因为读到非字母或数字而停止
			 */
			if (Character.isLetter(Cha) || Character.isDigit(Cha)) {
				tokenName = str.substring(charNum1);
				getchar();
			} else
				// charNum2-1 即是标识符最后一个字符的后一个位置
				// substring(a,b),索引从a到b-1。
				tokenName = str.substring(charNum1, charNum2);
			// 若长度超出标识符的最大长度len则报错
			if ((charNum2 - charNum1) > len) {
				//error.error(lineNum, 19);
				//errorNum++;
				tokenName = str.substring(charNum1, charNum1 + len);
			}

			int n = 0; // 用于记录保留字数组的下标
			// 判断是否为保留字
			while (n < 13 && (!KEY[n].getName().equals(tokenName))) {
				n++;
			}
			if (n == 13) // 如果保留字表中没有找到,则为标识符
			{
				return new Word(tokenName, 'b', lineNum);
			} else {
				KEY[n].setLineNum(lineNum);
				return KEY[n];
			}
		}
		// 识别数字
		else if (Character.isDigit(Cha)) {
			charNum1 = i - 1; // 数字的起始位置
			charNum2 = i; // 数字的最后一个字符的后一个位置
			while (Character.isDigit(Cha) && i < Length) {
				charNum2 = i;
				getchar();
			}
			// 判断为什么跳出while循环。如果Cha还是数字，则表明是因为此行读完而停止的；如果Cha不是数字，表示因为读到非数字而停止
			if (Character.isDigit(Cha)) {
				tokenName = str.substring(charNum1);
				getchar();
			} else
				tokenName = str.substring(charNum1, charNum2);
			// 若长度超出最大位数nmax则报错
			if ((charNum2 - charNum1) > nmax) {
				//error.error(lineNum, 8);
				//errorNum++;
				tokenName = tokenName.substring(charNum1, charNum1 + nmax);
			}
			return new Word(tokenName, 'a', lineNum);
		}
		// 识别算符和界符
		else if (Cha == ':') {
			getchar();
			if (Cha == '=') {
				getchar();
				return new Word(":=", 't', lineNum);

			} else {
				System.err.println("：后面缺少=!");
				return null;
			}

		} else if (Cha == '<') {
			getchar();
			if (Cha == '=') {
				getchar();
				return new Word("<=", 'r', lineNum);

			} else {
				return new Word("<", '<', lineNum);
			}

		} else if (Cha == '>') {
			getchar();
			if (Cha == '=') {
				getchar();
				return new Word(">=", 's', lineNum);
			} else {
				return new Word(">", '>', lineNum);
			}

		} else {
			char sym = '0';
			String sr = Character.toString(Cha);
			if (Cha == '+')
				sym = '+';
			else if (Cha == '-')
				sym = '-';
			else if (Cha == '*')
				sym = '*';
			else if (Cha == '/')
				sym = '/';
			else if (Cha == '(')
				sym = '(';
			else if (Cha == ')')
				sym = ')';
			else if (Cha == '=')
				sym = '=';
			else if (Cha == ',')
				sym = ',';
			else if (Cha == '.')
				sym = '.';
			else if (Cha == ';')
				sym = ';';
			else if (Cha == '#')
				sym = '#';
			getchar();
			return new Word(sr, sym, lineNum);
		}
	}
}
