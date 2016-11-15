package logicOperators;

import net.sf.jsqlparser.expression.Expression;
import utils.Tuple;

import java.util.*;

/**
 * This class constructs a logic binary operator which will have two children and
 * an expression.
 *
 */

public abstract class LogicBinaryOperator extends LogicOperator{
	public LogicOperator left, right;
	public Expression exp;
	
	/**
	 * Constructor to create a new logic binary operator
	 * @param left the left logic operator of this binary operator
	 * @param right the right logic operator of this binary operator
	 * @param exp the expression
	 */
	public LogicBinaryOperator(LogicOperator left, LogicOperator right, Expression exp) {
		this.left = left;
		this.right = right;
		this.exp = exp;
	}
}
