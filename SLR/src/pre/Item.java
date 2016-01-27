package pre;

public class Item {
	public char head;
	public String content;
	public int status;

	public Item(){
		
	}
	public Item(char head, String content, int status) {
		this.head = head;
		this.content = content;
		this.status = status;
	}

	@Override
	public String toString() {
		return ""+head+'¡ú'+content;
	}
	
}
