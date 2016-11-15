package BPlusTree;

public class Rid {
	
	int pageId;
	int tupleId;
	
	public Rid(int pageId, int tupleId) {
		this.pageId = pageId;
		this.tupleId = tupleId;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("(" + pageId + "," + tupleId + ")");
		return s.toString();
	}
}
