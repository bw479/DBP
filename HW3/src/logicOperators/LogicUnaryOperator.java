package logicOperators;

import java.util.List;

/**
 * This class constructs a logic unary operator which has one child.
 *
 */
public abstract class LogicUnaryOperator extends LogicOperator{
	public LogicOperator child;
	
	/**
	 * Constructor to create a new logic unary operator
	 * @param child the child of this new logic unary operator
	 */
	public LogicUnaryOperator(LogicOperator child) {
		this.child = child;
	}
	
}
