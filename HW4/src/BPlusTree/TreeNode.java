package BPlusTree;

import java.util.List;

public abstract class TreeNode {
	private int minKey;
	
	abstract public int getMinKey();
	
	abstract public List<DataEntry> getEntry();
}
