package operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import utils.Tuple;
/**
 * Operator that applies in memory sort
 */
public class InMemSortOperator extends SortOperator{
	private List<Tuple> all;
	private int index;

	/**
	 * construct a in-memory operator
	 * @param child the child operator
	 * @param orderBy specified order
	 */
	public InMemSortOperator(Operator child, List<String> orderBy) {
		super(child, orderBy);
		all = new ArrayList<Tuple>();


		Tuple next = child.getNextTuple();
		while (next != null) {
			all.add(next);
			next = child.getNextTuple();
		}
		Collections.sort(all, new CompareTuple());
		index = 0;

	}

	/**
	 * return the next available tuple
	 */
	public Tuple getNextTuple() {
		if (index < all.size()) {
			Tuple r = all.get(index);
			index++;
			return r;

		}
		return null;
	}
	
	/**
	 * reset back to the first tuple.
	 */
	@Override
	public void reset() {
		index = 0;
	}
	
	/**
	 * reset to a specified index of tuple.
	 */
	@Override
	public void reset(int index) {
		this.index = index;
	}

}
