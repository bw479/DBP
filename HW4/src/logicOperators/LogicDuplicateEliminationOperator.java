package logicOperators;

import java.util.List;

import utils.Tuple;
import visitors.PhysicalPlanBuilder;

/**
 * This class constructs a logic duplicate elimination operator which 
 * will have a child and the accept method.
 *
 */

public class LogicDuplicateEliminationOperator extends LogicUnaryOperator{
	
	/**
	 * create a duplicate operator that can remove duplicate tuples
	 * @para child the child operator that gives it tuples
	 */
	public LogicDuplicateEliminationOperator(LogicOperator child) {
		super(child);
	}
	
	/**
	 * An override accept method which will accept phyPlanBuilder as a parameter.
	 */
	@Override
	public void accept(PhysicalPlanBuilder phyPlanBuilder) {
		phyPlanBuilder.visit(this);
	}
}
