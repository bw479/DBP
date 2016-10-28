package operators;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import utils.Tuple;
import visitors.JoinVisitor;

/**
 * This class is used to create a new physical join operator with two children
 * and its expression, and also contains some related methods. 
 *
 */

public abstract class JoinOperator extends BinaryOperator{
	protected Tuple leftTuple;
	protected Tuple rightTuple;
	private JoinVisitor jv;

	/**
	 * Join constructor to create a new JoinOperator object with the given
	 * left and right child operators and the expression
	 * @param left left child operator
	 * @param right right child operator
	 * @param exp join expression
	 */
	public JoinOperator(Operator left, Operator right, Expression exp) {
		super(left, right, exp);
		schema = new ArrayList<String>();
		schema.addAll(left.getSchema());
		schema.addAll(right.getSchema());
	}


	/**
	 * get the current schema of this join operator
	 * @return the schema
	 */
	@Override
	public List<String> getSchema() {
		return schema;
	}

	/**
	 * Concatenate the left tuple and the right tuple to generate a new join tuple
	 * @param leftTuple the left tuple
	 * @param rightTuple the right tuple
	 * @return the new concatenated tuple
	 */
	public Tuple joinTuples(Tuple leftTuple, Tuple rightTuple) {
		if (leftTuple == null) return null;
		List<Integer> column = new ArrayList<Integer>();
		for (Integer i : leftTuple.getColumn()) {
			column.add(i);
		}
		for (Integer i : rightTuple.getColumn()) {
			column.add(i);
		}
		return new Tuple(column);
	}

	/**
	 * Get the next tuple of the join operator
	 * @return the next tuple
	 */
	@Override
	public Tuple getNextTuple() {
		Tuple nextTuple = null;
		// generate new tuples
		while (leftTuple != null  &&  rightTuple != null) {
			
			jv = new JoinVisitor(leftTuple, rightTuple,left.getSchema(), right.getSchema());
			exp.accept(jv);
			if (exp == null  ||  jv.getResult())
				nextTuple = joinTuples(leftTuple, rightTuple);
			setNext();
			
			if (nextTuple != null) return nextTuple;
		}
		return null;
	}
	
	/**
	 * Set the next left tuple and next right tuple.
	 */
	protected abstract void setNext();

}
