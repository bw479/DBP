package BPlusTree;

import java.util.List;

public class IndexNode extends TreeNode {
	private List<Integer> keys;
	private List<Integer> address;
	private List<TreeNode> children;
	
	public IndexNode(List<Integer> keys, List<Integer> address, List<TreeNode> children) {
		this.keys = keys;
		this.address = address;
		this.children = children;
	}
	
	public List<Integer> getKeys() {
		return this.keys;
	}
	
	public List<Integer> getAdresses() {
		return this.address;
	}
	
	public List<TreeNode> getChildren() {
		return this.children;
	}
	
	public List<DataEntry> getEntry() {
		return null;
	}
	
	public int getMinKey() {
		return this.children.get(0).getMinKey();
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("IndexNode with keys ");
		s.append(keys.toString());
		s.append(" and child addresses ");
		s.append(address.toString());
		s.append("\n\n");
		return s.toString();
	}
}
