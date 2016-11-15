package visitors;

import java.util.List;

import net.sf.jsqlparser.schema.Column;
import utils.Catalog;
import utils.Tuple;

/**
 * Visitor for join conditions of two tables, which is able to recursively visit
 * all the conditions.
 *
 */

public class SelectVisitor extends TopVisitor{
	private Tuple t1;
	private List<String> schema;
	
	/**
	 * Create a visitor of select conditions for a table
	 * @param t1 tuple from the table
	 * @param schema the schema for the table
	 */
	public SelectVisitor(Tuple t1, List<String> schema) {
		this.t1 = t1;
		this.schema = schema;
	}
	/**
	 * reset the tuple
	 * @param the new tuple
	 */
	public void setTuple(Tuple t1) {
		this.t1 = t1;
	}
	/**
	 * visitor of the column for select conditions
	 * @para arg0 the column name
	 */
	@Override
	public void visit(Column arg0) {
		String col = "";
		if (Catalog.selfJoinMap.containsKey(arg0.getTable().toString())) col = arg0.toString();
		else if (Catalog.alias.containsKey(arg0.getTable().toString()))
			col += Catalog.alias.get(arg0.getTable().toString()) + "." + arg0.getColumnName();
		else col = arg0.toString();
		int index = schema.indexOf(col);
		if (index == -1) 
			System.out.println("here!");
		curValue = t1.getColumn().get(index);
	}
}
