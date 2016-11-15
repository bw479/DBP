package logicOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.jsqlparser.statement.select.OrderByElement;
import utils.Catalog;
import utils.Tuple;
import visitors.PhysicalPlanBuilder;

/**
 * This class is used to create a new logic sort operator with one child
 * and the orderBy attributes list, and the accept method. 
 *
 */

public class LogicSortOperator extends LogicUnaryOperator{
	public List<String> orderBy;
	/**
	 * create a duplicate operator that can sort tuples with a desired order
	 * @para child the child operator that gives it tuples
	 * @para the attributes that determine the order
	 */
	public LogicSortOperator(LogicOperator child, List<String> orderBy) {
		super(child);
		this.orderBy = orderBy;
	}
	
	/**
	 * An override accept method which will accept phyPlanBuilder as a parameter.
	 */
	@Override
	public void accept(PhysicalPlanBuilder phyPlanBuilder) {
		phyPlanBuilder.visit(this);
	}
	
 }
