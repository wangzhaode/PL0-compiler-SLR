package pre;

import java.util.ArrayList;
import java.util.Vector;

public class SYM {
	private char name;
	private ArrayList<SYM> prefirstList = new ArrayList<SYM>();
	private ArrayList<SYM> prefollowList = new ArrayList<SYM>();
	private Vector<Character> firstSet = new Vector<Character>();
	private Vector<Character> followSet = new Vector<Character>();

	public SYM(char name) {
		this.name = name;
	}

	public void setPrefirst(SYM presym) {
		if (!prefirstList.contains(presym))
			prefirstList.add(presym);
	}

	public void setFirst(Vector<Character> firstset) {
		firstSet.addAll(firstset);
		for (int i = 0; i < prefirstList.size(); i++) {
			prefirstList.get(i).setFirst(firstset);
		}
	}

	public void setFirst(char first) {
		if (!firstSet.contains(first)) {
			firstSet.add(first);
		}
		for (int i = 0; i < prefirstList.size(); i++) {
			prefirstList.get(i).setFirst(first);
		}
	}

	public void setPrefollow(SYM presym) {
		if (!prefollowList.contains(presym))
			prefollowList.add(presym);
	}

	public void setFollow(Vector<Character> followset) {
		followSet.addAll(followset);
		for (int i = 0; i < prefollowList.size(); i++) {
			if (prefollowList.get(i) != this)
				prefollowList.get(i).setFollow(followset);
		}
	}

	public void setFollow(char follow) {
		if (!followSet.contains(follow))
			followSet.add(follow);
		for (int i = 0; i < prefollowList.size(); i++) {
			if (prefollowList.get(i) != this)
				prefollowList.get(i).setFollow(follow);
		}
	}

	public void unique() {
		Vector newVector = new Vector();
		for (int i = 0; i < followSet.size(); i++) {
			Object obj = followSet.get(i);
			if (!newVector.contains(obj))
				newVector.add(obj);
		}
		followSet = newVector;
	}

	public Vector<Character> getFirst() {
		return firstSet;
	}

	public Vector<Character> getFollow() {
		return followSet;
	}

	public void printFirst() {
		System.out.print("FIRST(" + name + ")={");
		for (int i = 0; i < firstSet.size(); i++)
			System.out.print(firstSet.get(i));
		System.out.println("}");
	}

	public void printFollow() {
		System.out.print("FOLLOW(" + name + ")={");
		for (int i = 0; i < followSet.size(); i++)
			System.out.print(followSet.get(i));
		System.out.println("}");
	}

	public void printName() {
		System.out.println(name);
	}
}
