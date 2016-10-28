package operators;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import utils.Tuple;

/**
 * This class is used to a new physical block nested join operator with 
 * two children, the join expression and the number of buffer pages, and 
 * also contains some related methods. 
 * 
 */
public class BlockJoinOperator extends JoinOperator{

	private int outerTupleNum;
	private List<Tuple> outerBuf;
	private int curOurterIdx;
	
	private static final int pageSize = 4096;

	/**
	 * Constructor to create a new block join operator with the left and right
	 * operator, the join expression and the number of buffer pages. 
	 * @param left the left operator
	 * @param right the right operator
	 * @param exp the join expression
	 * @param bufNum the number of buffer pages for the outer block
	 */
	public BlockJoinOperator(Operator left, Operator right, Expression exp, 
			int bufNum) {
		super(left, right, exp);
		int tupleSize = 4 * left.schema.size();
		outerTupleNum = bufNum * (pageSize / tupleSize);
		outerBuf = new ArrayList<Tuple>();
		setNext();
	}

	/**
	 * Set the next left and right tuples
	 */
	@Override
	public void setNext() {
		// Right tuple is not null, compare the right tuple with 
		// each tuple in the left block
		if (rightTuple != null) {

			leftTuple = setLeft(curOurterIdx++);

			// Left tuple is not null, return
			if (leftTuple != null) {
				return;
			}
			// Left tuple is null, reset the left block, turn to
			// the next right tuple
			rightTuple = right.getNextTuple();
			if (rightTuple != null) {
				curOurterIdx = 0;
				leftTuple = setLeft(curOurterIdx++);
				return;
			}

		}

		getOuterBlock();
		leftTuple = setLeft(curOurterIdx++);
		right.reset();
		rightTuple = right.getNextTuple();
	}

	/**
	 * Set the left tuple with the given index
	 * @param index the index of the tuple in the outerBuf list
	 * @return
	 */
	private Tuple setLeft(int index) {
		if (index < outerTupleNum && index < outerBuf.size())
			return outerBuf.get(index++);
		return null;
	}

	/**
	 * Fill the outer block 
	 */
	private void getOuterBlock() {
		outerBuf.clear();
		int count = 0;
		Tuple tuple;
		while (count < outerTupleNum && 
				(tuple = left.getNextTuple()) != null) {
			outerBuf.add(tuple);
			count++;
		}
		curOurterIdx = 0;
	}
}
