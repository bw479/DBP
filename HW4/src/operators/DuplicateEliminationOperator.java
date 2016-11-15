package operators;

import java.util.List;

import utils.Tuple;

/**
 * This class constructs a physical duplicate elimination operator which will have a child, 
 * and also contains some related methods.
 *
 */

public class DuplicateEliminationOperator extends UnaryOperator{
	private Tuple last;
	
	/**
	 * create a duplicate operator that can remove duplicate tuples
	 * @para child the child operator that gives it tuples
	 */
	public DuplicateEliminationOperator(Operator child) {
		super(child);
		last = null;
	}
	
	/**
	 * output the next tuple 
	 * @return return the next available tuple
	 */
	@Override
	public Tuple getNextTuple() {
		Tuple cur = child.getNextTuple();
		if (cur == null) {
			return null;
		}
		if (last == null) {
			last = cur;
			return cur;
		}
		while (true) {
			if (cur == null) {
				return null;
			}

			if (areEqual(cur, last)) {
				cur = child.getNextTuple();
			} else {
				last = cur;
				return cur;
			}
		}
	}
	/**
	 * test if two tuples are the same
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 * @return true or false
	 */
	private boolean areEqual(Tuple t1, Tuple t2) {
		for (int i = 0; i < t1.getColumn().size(); i++) {
			if (!t1.getColumn().get(i).equals(t2.getColumn().get(i))) {
				return false;
			}
		}
		return true;
	}
	/**
	 * return the current schema
	 */
	@Override
	public List<String> getSchema() {
		return this.schema;
		
	}
	
}
