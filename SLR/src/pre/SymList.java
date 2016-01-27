package pre;

import java.util.ArrayList;

public class SymList {
	ArrayList<Item> itemlist = new ArrayList<Item>();

	public SymList() {

	}

	public void add(SymList list) {
		for (int i = 0; i < list.getSize(); i++) {
			if (this.contain(list.index(i)))
				list.remove(i);
		}
		itemlist.addAll(list.getAll());
	}

	public void add(Item item) {
		itemlist.add(item);
	}

	public void add(char head, String content, int status) {
		itemlist.add(new Item(head, content, status));
	}

	public int getSize() {
		return itemlist.size();
	}

	public Item getStrat() {
		return itemlist.get(0);
	}

	public ArrayList<Item> getAll() {
		return itemlist;
	}

	public Item index(int i) {
		return itemlist.get(i);
	}

	public SymList getClolist(char c) {
		SymList result = new SymList();
		for (int i = 0; i < itemlist.size(); i++) {
			if (itemlist.get(i).head == c
					&& itemlist.get(i).content.charAt(0) == '|')
				result.add(itemlist.get(i));
		}
		return result;
	}

	public Item remove(int i) {
		return itemlist.remove(i);
	}

	public void setItemstatus(Item item, int status) {
		itemlist.get(itemlist.indexOf(item)).status = status;
	}

	public void setItemstatus(SymList items, int status) {
		for (int i = 0; i < items.getSize(); i++) {
			for (int j = 0; j < itemlist.size(); j++) {
				if (itemlist.get(j).head == items.index(i).head
						&& itemlist.get(j).content
								.equals(items.index(i).content)) {
					itemlist.get(j).status = status;
				}
			}
		}
	}

	public SymList getAction() {
		SymList result = new SymList();
		Item item;
		for (int i = 0; i < itemlist.size(); i++) {
			item = itemlist.get(i);
			if (item.content.charAt(item.content.length() - 1) == '|')
				result.add(item);
		}
		return result;
	}

	public int findStatus(SymList findlist) {
		int result_status = -2;
		for (int i = 0; i < findlist.getSize(); i++) {
			for (int j = 0; j < itemlist.size(); j++) {
				if (itemlist.get(j).head == findlist.index(i).head
						&& itemlist.get(j).content
								.equals(findlist.index(i).content)) {
					if (result_status == -2)
						result_status = itemlist.get(j).status;
					else if (result_status != itemlist.get(j).status)
						return -1;
				}
			}
		}
		return result_status;
	}

	public SymList getGoto(char ch, int status) {
		SymList result = new SymList();
		String content;
		int n;
		for (int i = 0; i < itemlist.size(); i++) {
			content = itemlist.get(i).content;
			n = content.indexOf('|');
			if (n < content.length() - 1) {
				if (content.charAt(n + 1) == ch) {
					Item item = new Item();
					item.head = itemlist.get(i).head;
					item.content = Util.changeChar(content, n, n + 1);
					if (status != -1)
						item.status = status;
					result.add(item);
				}
			}
		}
		return result;
	}

	public int getMaxstatus() {
		int max = -1;
		for (int i = 0; i < itemlist.size(); i++) {
			if (itemlist.get(i).status > max)
				max = itemlist.get(i).status;
		}
		return max;
	}

	public boolean contain(Item item) {
		for (int i = 0; i < itemlist.size(); i++) {
			if (itemlist.get(i).head == item.head
					&& itemlist.get(i).content.equals(item.content))
				return true;
		}
		return false;
	}
}
