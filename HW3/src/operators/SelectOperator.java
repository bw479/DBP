package operators;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import visitors.SelectVisitor;
import utils.Tuple;

/**
 * This class is used to create a new physical select operator with one child
 * and an expression, and also contains some related methods. 
 *
 */

public class SelectOperator extends UnaryOperator{
	private Expression ex;
	private SelectVisitor sv;

	/**
	 * create a select operator that can output tuples with desired conditions
	 * @para child the child operator that gives it tuples
	 * @para the desired conditions
	 */
	public SelectOperator(Operator child, Expression ex) {
		super(child);
		this.ex = ex;
		this.schema = child.schema; 
		sv = null;
	}
	/**
	 * output the next tuple 
	 * @return return the next available tuple
	 */
	@Override
	public Tuple getNextTuple() {
		Tuple cur = child.getNextTuple();
		if (ex == null) {
			return cur;
		}
		
		while (true) {
			if (cur == null) {
				return null;
			}
			if (sv == null) {
				sv = new SelectVisitor(cur, this.schema);
			} else {
				sv.setTuple(cur);
			}
			
			ex.accept(sv);
			if (sv.getResult()) {
				return cur;
			}
			cur = child.getNextTuple();
			
		}
	}
	/**
	 * return the current schema
	 */
	@Override
	public List<String> getSchema() {
		// TODO Auto-generated method stub
		return this.schema;
	}
	
	/**
	 * Reset the operator
	 */
	@Override
	public void reset() {
		child.reset();
	}

}
