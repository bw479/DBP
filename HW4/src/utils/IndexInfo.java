package utils;

public class IndexInfo {

	public String attr;
	public boolean isClustered;
	public int order;
	
	public IndexInfo(String attr, boolean isClustered, int order) {

		this.attr = attr;
		this.isClustered = isClustered;
		this.order = order;
	}
}
