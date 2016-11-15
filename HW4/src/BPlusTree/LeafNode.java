package BPlusTree;

import java.util.List;

public class LeafNode extends TreeNode{
	
	private List<DataEntry> entries;
	
	public LeafNode(List<DataEntry> entries) {
		this.entries = entries;
	}
	
	public List<DataEntry> getEntry() {
		return this.entries;
	}
	
	public int getMinKey() {
		return entries.get(0).getKey();
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("\n\nLeafNode[\n");
		for (int i = 0; i < entries.size(); i++) {
			s.append(entries.get(i).toString() + "\n");
		}
		s.append("]");
		return s.toString();
	}

}
