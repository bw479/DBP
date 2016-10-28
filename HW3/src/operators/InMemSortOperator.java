package operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import utils.Tuple;

public class InMemSortOperator extends SortOperator{
	private List<Tuple> all;
	private int index;

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

	public Tuple getNextTuple() {
		if (index < all.size()) {
			Tuple r = all.get(index);
			index++;
			return r;

		}
		return null;
	}

	@Override
	public void reset() {
		index = 0;
	}
	
	@Override
	public void reset(long index) {
//		try {
//			tr.reset();
//		} catch (IOException e) {
//			System.out.println("Nothing to reset");
//		}
	}

}
