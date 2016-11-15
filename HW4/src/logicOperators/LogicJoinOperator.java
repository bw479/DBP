package logicOperators;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import utils.Tuple;
import visitors.JoinVisitor;
import visitors.PhysicalPlanBuilder;

/**
 * This class is used to create a new logic join operator with two children
 * and its expression, and the accept method. 
 *
 */

public class LogicJoinOperator extends LogicBinaryOperator{

	/**
	 * Join constructor to create a new JoinOperator object with the given
	 * left and right child operators and the expression
	 * @param left left child operator
	 * @param right right child operator
	 * @param exp join expression
	 */
	public LogicJoinOperator(LogicOperator left, LogicOperator right, Expression exp) {
		super(left, right, exp);
	}
	
	/**
	 * An override accept method which will accept phyPlanBuilder as a parameter.
	 */
	@Override
	public void accept(PhysicalPlanBuilder phyPlanBuilder) {
		phyPlanBuilder.visit(this);
	}
}
