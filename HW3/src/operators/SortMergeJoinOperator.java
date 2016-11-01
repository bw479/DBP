package operators;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import utils.Catalog;
import utils.Tuple;
import visitors.JoinVisitor;

/**
 * This class is used to create a new physical sort mergejoin operator 
 * with two children, the join expression, and the orders to sort the left tuple
 * and the right tuple. 
 *
 */
public class SortMergeJoinOperator extends JoinOperator{
	List<String> leftOrder;
	List<String> rightOrder;
	JoinVisitor jv;
	int partitionIndex;

	/**
	 * Sort Merge Join constructor to create a new SortMergeJoinOperator object 
	 * with the given left and right child operators, the join expression, 
	 * and the orders to sort the left tuple and the right tuple. 
	 * @param left the left operator
	 * @param right the right operator
	 * @param exp the join expression
	 * @param leftOrder the order to sort the left tuple
	 * @param rightOrder the order to sort the right tuple
	 */
	public SortMergeJoinOperator(
			Operator left, Operator right, Expression exp, 
			List<String> leftOrder, List<String> rightOrder) {
		super(left, right, exp);
		this.leftOrder = leftOrder;
		this.rightOrder = rightOrder;
		this.leftTuple = left.getNextTuple();
		this.rightTuple = right.getNextTuple();
		this.partitionIndex = 0;
	}


	/**
	 * Get the next tuple of the sort merge join operator
	 * @return the next tuple
	 */
	@Override
	public Tuple getNextTuple() {
		while (leftTuple != null  &&  rightTuple != null) {
			int compareResult = compare(leftTuple, rightTuple);
			if (compareResult < 0) {
				leftTuple = left.getNextTuple();
				continue;
			}
			
			if (compareResult > 0) {
				rightTuple = right.getNextTuple();
				partitionIndex++;
				continue;
			}

			jv = new JoinVisitor(leftTuple, rightTuple, 
					left.getSchema(), right.getSchema());
			exp.accept(jv);
			Tuple nextTuple = null;
			if (exp == null  ||  jv.getResult())
				nextTuple = joinTuples(leftTuple, rightTuple);
			
			rightTuple = right.getNextTuple();
			if (rightTuple == null  ||  compare(leftTuple, rightTuple) != 0) {
				((SortOperator)right).reset(partitionIndex);
				rightTuple = right.getNextTuple();
				leftTuple = left.getNextTuple();
			}
			
			if (nextTuple != null) return nextTuple;
		}
		return null;
	}

	/**
	 * Compare two tuples based on the sort orders
	 * @param leftTuple the left tuple
	 * @param rightTuple the right tuple
	 * @return 0 if two tuples are equal, 1 if the leftTuple is bigger 
	 * than the rightTuple, -1 if the left Tuple is smaller than the rightTuple
	 */
	public int compare(Tuple leftTuple, Tuple rightTuple) {
		
		for (int i = 0; i < leftOrder.size(); i++) {
			String orderEle1 = leftOrder.get(i);
			String orderEle2 = rightOrder.get(i);
			int leftEle = leftTuple.getColumn().get(left.schema.indexOf(orderEle1));
			int rightEle = 
					rightTuple.getColumn().get(right.schema.indexOf(orderEle2));
			int r = Integer.compare(leftEle, rightEle);
			if (r != 0) return r;
		}
		return 0;
	}

	/**
	 * Set the next left and right tuples.
	 */
	@Override
	public void setNext() {
		return;
	}
}
