package operators;

import net.sf.jsqlparser.expression.Expression;
import utils.Tuple;

import java.util.*;

/**
 * This class constructs a physical binary operator which will have two children and
 * an expression, and also contains some related methods.
 *
 */

public abstract class BinaryOperator extends Operator{
	public Operator left, right;
	public Expression exp;
	
	/**
	 * Constructor to create a new binary operator
	 * @param left the left operator 
	 * @param right the right operator
	 * @param exp the expression
	 */
	public BinaryOperator(Operator left, Operator right, Expression exp) {
		this.left = left;
		this.right = right;
		this.exp = exp;
	}
	
	/**
	 * Reset the operator
	 */
	@Override
	public void reset() {
		return;
	}
	
	/**
	 * Get the schema of this operator
	 */
	@Override
	public List<String> getSchema() {
		return this.schema;
	}
	
}
