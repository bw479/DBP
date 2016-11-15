package operators;

import java.io.PrintStream;
import java.util.List;

import nio.TupleWriter;
import utils.Tuple;

/**
 * This class is an abstract physical operator class which contains the necessary 
 * method for each operator.
 *
 */

public abstract class Operator {
	
	protected List<String> schema;
	
	/**
	 * Return the next valid tuple.
	 * @return the next tuple
	 */
	public abstract Tuple getNextTuple();
	
	/**
	 * Reset the operator to the start.
	 */
	public abstract void reset();
	
	/**
	 * get the current schema of this operator
	 * @return the schema
	 */
	public abstract List<String> getSchema();
	
	/**
	 * A method to writes each tuple to a suitable PrintStream
	 * @param ps the print stream
	 */
	public void dump(TupleWriter tw) {
		Tuple tuple = null;
		while ((tuple = getNextTuple()) != null) {
			tuple.dump(tw);
		}
	}
}
