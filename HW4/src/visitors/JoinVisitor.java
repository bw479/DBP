package visitors;

import java.util.List;

import net.sf.jsqlparser.schema.Column;
import visitors.TopVisitor;
import utils.Catalog;
import utils.Tuple;
/**
 * Visitor for join conditions of two tables, which is able to recursively visit
 * all the conditions.
 *
 */
public class JoinVisitor extends TopVisitor{
	private Tuple t1;
	private Tuple t2;
	private List<String> schema1;
	private List<String> schema2;
	/**
	 * Create a visitor of join conditions for two tables
	 * @param t1 tuple from the first table
	 * @param t2 tuple from the second table
	 * @param schema1 the schema for the first table
	 * @param schema2 the schema for the second table
	 */
	public JoinVisitor(Tuple t1, Tuple t2, List<String> schema1, List<String> schema2) {
		this.t1 = t1;
		this.t2 = t2;
		this.schema1 = schema1;
		this.schema2 = schema2;
	}
	/**
	 * reset the two tuples
	 * @param t1 the first tuple
	 * @param t2 the second tuple
	 */
	public void setTuples(Tuple t1, Tuple t2) {
		this.t1 = t1;
		this.t2 = t2;
	}
	
	/**
	 * visitor for the column name in the condition
	 * @param the column name
	 */
	@Override
	public void visit(Column arg0) {
		String col = "";
		if (Catalog.selfJoinMap.containsKey(arg0.getTable().toString())) col = arg0.toString();
		else if (Catalog.alias.containsKey(arg0.getTable().toString()))
			col += Catalog.alias.get(arg0.getTable().toString()) + "." + arg0.getColumnName();
		else col = arg0.toString();
		int index = schema1.indexOf(col);

		if (index != -1) {
			this.curValue = t1.getColumn().get(index);
		} else {
			index = schema2.indexOf(col);
			this.curValue = t2.getColumn().get(index);
		}
		
	}
}
