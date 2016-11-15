package logicOperators;

import java.util.List;

import utils.Table;
import utils.Tuple;
import visitors.PhysicalPlanBuilder;

/**
 * This class is used to create a new logic scan operator with the related table, 
 * and the accept method. 
 *
 */

public class LogicScanOperator extends LogicOperator{
	
	public Table table;

	
	/**
	 * Constructor to initialize the operator
	 * @param table
	 */
	public LogicScanOperator(Table table) {
		this.table = table;
	}
	
	/**
	 * An override accept method which will accept phyPlanBuilder as a parameter.
	 */
	@Override
	public void accept(PhysicalPlanBuilder phyPlanBuilder) {
		phyPlanBuilder.visit(this);
	}
	
}
