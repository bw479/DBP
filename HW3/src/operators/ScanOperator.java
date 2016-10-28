package operators;

import java.util.List;

import utils.Table;
import utils.Tuple;

/**
 * This class is used to create a new physical scan operator with the related table, 
 * and also contains some related methods. 
 *
 */

public class ScanOperator extends Operator{
	
	private Table table;

	
	/**
	 * Constructor to initialize the operator
	 * @param table
	 */
	public ScanOperator(Table table) {
		this.table = table;
		this.schema = table.getSchema();
	}
	
	/**
	 * Return the next tuple.
	 * @return the next tuple
	 */
	@Override
	public Tuple getNextTuple(){
		return table.nextTuple();
	}
	
	
	/**
	 * Reset the operator to start from the beginning.
	 */
	@Override
	public void reset(){
		table.reset();
	}
	
	/**
	 * get the current schema of this scan operator
	 * @return the schema
	 */
	@Override
	public List<String> getSchema() {
		return schema;
	}
	
}
