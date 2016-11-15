package operators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sf.jsqlparser.statement.select.SelectItem;
import utils.Catalog;
import utils.Tuple;



/**
 * Operator that runs projection
 */
public class ProjectOperator extends UnaryOperator {
	
	private List<Integer> addIndex;
	
	/**
	 * create a project operator that can output tuples with desired attributes
	 * @para child the child operator that gives it tuples
	 * @para selectList the desired attributes
	 */
	public ProjectOperator (Operator child, List<SelectItem> selectList) {
		super(child);
		List<String> selectItems = new ArrayList<String>();
		addIndex = new ArrayList<Integer>();

		for (SelectItem s : selectList) {
			
			String added = s.toString();
			if (added.equals("*")) {
				selectItems.addAll(child.getSchema());
				break;
			}
			
			String[] temp = added.split("\\.");
			if (!Catalog.selfJoinMap.containsKey(temp[0]) && Catalog.alias.containsKey(temp[0])) 
				added = Catalog.alias.get(temp[0]) + '.' + temp[1];
			selectItems.add(added);
		}
		for (int i = 0; i < selectItems.size(); i++) {
			addIndex.add(child.getSchema().indexOf(selectItems.get(i)));
		}
		this.schema = selectItems;

	}
	
	/**
	 * output the next tuple 
	 * @return return the next available tuple
	 */
	public Tuple getNextTuple() {
		Tuple cur = child.getNextTuple();
		if (cur == null) {
			return null;
		}

		List<Integer> curList = cur.getColumn();
		List<Integer> newList = new ArrayList<Integer>();
		for (int i = 0; i < addIndex.size(); i++) {
			newList.add(curList.get(addIndex.get(i)));
		}
		return new Tuple(newList);
	}
	
	
	

}
