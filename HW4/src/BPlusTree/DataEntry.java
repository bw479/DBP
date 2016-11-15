package BPlusTree;

import java.util.ArrayList;
import java.util.List;

public class DataEntry {
	private int key;
	private List<Rid> ridList;
	
	public DataEntry(int key, List<Rid> ridList) {
		this.key = key;
		this.ridList = ridList;
	}
	
	public List<Rid> getRids() {
		return ridList;
	}
	
	public int getKey() {
		return key;
	}
	
	@Override
	public String toString() {
		
		StringBuilder s = new StringBuilder();
		s.append("<[" + key + ":");
		for (int i = 0; i < ridList.size(); i++) {
			s.append(ridList.get(i).toString());
		}
		s.append("]>");
		return s.toString();
	}
}
