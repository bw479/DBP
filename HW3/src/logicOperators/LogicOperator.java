package logicOperators;

import java.io.PrintStream;
import java.util.List;

import utils.Tuple;
import visitors.PhysicalPlanBuilder;

/**
 * This class is an abstract logic operator class which contains the accept method.
 *
 */
public abstract class LogicOperator {
	
	/**
	 * The accept method for the logic operator
	 * @param phyPlanBuilder the physical plan builder visitor
	 */
	public abstract void accept(PhysicalPlanBuilder phyPlanBuilder);
	
	
}
