package logicOperators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sf.jsqlparser.statement.select.SelectItem;
import utils.Catalog;
import utils.Tuple;
import visitors.PhysicalPlanBuilder;

/**
 * This class is used to create a new logic project operator with one child
 * and the selected attributes list, and the accept method. 
 *
 */
public class LogicProjectOperator extends LogicUnaryOperator {
	
	public List<SelectItem> selectList;
	
	/**
	 * create a project operator that can output tuples with desired attributes
	 * @para child the child operator that gives it tuples
	 * @para selectList the desired attributes
	 */
	public LogicProjectOperator (LogicOperator child, List<SelectItem> selectList) {
		super(child);
		this.selectList = selectList;
	}

	/**
	 * An override accept method which will accept phyPlanBuilder as a parameter.
	 */
	@Override
	public void accept(PhysicalPlanBuilder phyPlanBuilder) {
		phyPlanBuilder.visit(this);
	}
}
