package operators;

import java.util.List;

/**
 * This class constructs a physical unary operator which has one child, 
 * and also contains some related methods.
 *
 */

public abstract class UnaryOperator extends Operator{
	public Operator child;
	
	/**
	 * Constructor to create a new unary operator
	 * @param child the child of this operator
	 */
	public UnaryOperator(Operator child) {
		this.child = child;
	}
	
	/**
	 * Reset this operator
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
		if (this.schema != null)
			return this.schema;
		return child.schema;
	}
	
}
