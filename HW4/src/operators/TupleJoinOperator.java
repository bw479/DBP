package operators;

import net.sf.jsqlparser.expression.Expression;

/**
 * This class is used to a new physical tuple nested join operator with 
 * two children and the join expression, and also contains some related 
 * methods. 
 * 
 */
public class TupleJoinOperator extends JoinOperator{

	/**
	 * Constructor to create a new tuple join operator with the left and right
	 * operator and the expression
	 * @param left the left operator
	 * @param right the right operator
	 * @param exp the join expression
	 */
	public TupleJoinOperator(Operator left, Operator right, Expression exp) {
		super(left, right, exp);
		this.leftTuple = left.getNextTuple();
		this.rightTuple = right.getNextTuple();
	}

	/**
	 * Set the next left tuple and next right tuple.
	 */
	public void setNext() {
		// set leftTuple and rightTuple
		rightTuple = right.getNextTuple();
		if (rightTuple == null) {
			leftTuple = left.getNextTuple();
			right.reset();
			rightTuple = right.getNextTuple();
		}
	}
	
}
