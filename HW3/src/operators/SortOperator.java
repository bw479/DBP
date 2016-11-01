package operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.jsqlparser.statement.select.OrderByElement;
import utils.Catalog;
import utils.Tuple;

public abstract class SortOperator extends UnaryOperator{
	private List<String> orderBy;
	/**
	 * create a duplicate operator that can sort tuples with a desired order
	 * @para child the child operator that gives it tuples
	 * @para the attributes that determine the order
	 */
	
 	public SortOperator(Operator child, List<String> orderBy) {
		super(child);
		schema = child.getSchema();
		this.orderBy = orderBy;
		
		
	}
	
	/**
	 * output the next tuple 
	 * @return return the next available tuple
	 */
	@Override
	public abstract Tuple getNextTuple();
	
	public abstract void reset();
	
	public abstract void reset(int index);
	
	
	/**
	 * Comparator that compares two tuples.
	 *
	 */
	protected class CompareTuple implements Comparator<Tuple> {
		public int compare(Tuple t1, Tuple t2) {
			List<Integer> orderIndex = new ArrayList<Integer>();
			for (int i = 0; i < orderBy.size(); i++) {
				
				String element = orderBy.get(i);
				String[] temp = element.split("\\.");
				if (!Catalog.selfJoinMap.containsKey(temp[0]) && Catalog.alias.containsKey(temp[0])) 
					element = Catalog.alias.get(temp[0]) + '.' + temp[1];
				orderIndex.add(child.getSchema().indexOf(element));//consider aliasing later
			}
			
			for (int i = 0; i < schema.size(); i++) {
				if (orderIndex.indexOf(i) == -1) {
					orderIndex.add(i);
				}
			}
			
			for (int i = 0; i < orderIndex.size(); i++) {
				int temp = t1.getColumn().get(orderIndex.get(i)).compareTo(t2.getColumn().get(orderIndex.get(i)));
				if (temp != 0) {
					return temp;
				}
			}
			return 0;
		}
	}
 }
