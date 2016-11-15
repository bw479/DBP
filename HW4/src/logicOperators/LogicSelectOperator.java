package logicOperators;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import visitors.PhysicalPlanBuilder;
import visitors.SelectVisitor;
import utils.Tuple;

/**
 * This class is used to create a new logic select operator with one child
 * and an expression, and the accept method. 
 *
 */

public class LogicSelectOperator extends LogicUnaryOperator{
	public Expression ex;

	/**
	 * create a select operator that can output tuples with desired conditions
	 * @para child the child operator that gives it tuples
	 * @para the desired conditions
	 */
	public LogicSelectOperator(LogicOperator child, Expression ex) {
		super(child);
		this.ex = ex;
	}

	/**
	 * An override accept method which will accept phyPlanBuilder as a parameter.
	 */
	@Override
	public void accept(PhysicalPlanBuilder phyPlanBuilder) {
		phyPlanBuilder.visit(this);
	}
}
