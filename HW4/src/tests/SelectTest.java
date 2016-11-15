package tests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import operators.DuplicateEliminationOperator;
import operators.Operator;
import operators.ProjectOperator;
import operators.ScanOperator;
import operators.SelectOperator;
import operators.SortOperator;
import utils.Catalog;
import utils.Table;
import utils.TreeBuilder;
import utils.Tuple;

public class SelectTest {
	
	public static final PrintStream sysOut = System.out;
	Catalog catalog = new Catalog();
	List<String> sailorsSchema;
	List<String> boatsSchema;
	List<String> reservesSchema;

	@Before
	public void setUp() {
		sailorsSchema = new ArrayList<String>();
		sailorsSchema.add("Sailors.A");
		sailorsSchema.add("Sailors.B");
		sailorsSchema.add("Sailors.C");
		boatsSchema = new ArrayList<String>();
		boatsSchema.add("Boats.D");
		boatsSchema.add("Boats.E");
		boatsSchema.add("Boats.F");
		reservesSchema = new ArrayList<String>();
		reservesSchema.add("Reserves.G");
		reservesSchema.add("Reserves.H");
	}
	
	@Test
	public void basicSelect() {
		try {
			// Check decomposition
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors S WHERE S.A < 3;"));
			Statement statement = parser.Statement();
			sysOut.println("--------Decompose Test 1 : " + 
					((Select) statement).getSelectBody());
			TreeBuilder tree = new TreeBuilder(statement);
			sysOut.println("project is: " + tree.project.toString());
			if (tree.distinct == null) tree.distinct = new Distinct();
			sysOut.println("distinct is: " + tree.distinct.toString());
			sysOut.println("orderBy is: " + tree.orderBy.toString());
			sysOut.println("fromItems is: " + tree.fromItems.toString());
			sysOut.println("alias is: " + tree.alias.toString());
			sysOut.println("selectConds is: " + tree.selectConds.toString());
			sysOut.println("joinConds is: " + tree.joinConds.toString());
			sysOut.println("joinExpMap is: " + tree.joinExpMap.toString());
			sysOut.println("selectExpMap is: " + tree.selectExpMap.toString());

			// Check tree structure
			Operator root = tree.root;
			assertTrue(root instanceof ProjectOperator);
			sysOut.println("project " + root.getSchema());
			root = ((ProjectOperator)root).child;
			assertTrue(root instanceof SelectOperator);
			sysOut.println("select " + root.getSchema());
			root = ((SelectOperator)root).child;
			assertTrue(root instanceof ScanOperator);
			List<String> sailorsScma = ((ScanOperator)root).getSchema();
			assertEquals(sailorsSchema, sailorsScma);
			
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				System.out.println(cur.toString());
				cur = tree.root.getNextTuple();
			}
			Catalog.selfJoinMap.clear();
			Catalog.alias.clear();
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	@Test
	public void selfJoinSelect() {
		try {
			// Check decomposition
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors S1, Sailors S2 WHERE S1.A < 3;"));
			Statement statement = parser.Statement();
			sysOut.println("--------Decompose Test 1 : " + 
					((Select) statement).getSelectBody());
			TreeBuilder tree = new TreeBuilder(statement);
			sysOut.println("project is: " + tree.project.toString());
			if (tree.distinct == null) tree.distinct = new Distinct();
			sysOut.println("distinct is: " + tree.distinct.toString());
			sysOut.println("orderBy is: " + tree.orderBy.toString());
			sysOut.println("fromItems is: " + tree.fromItems.toString());
			sysOut.println("alias is: " + tree.alias.toString());
			sysOut.println("selectConds is: " + tree.selectConds.toString());
			sysOut.println("joinConds is: " + tree.joinConds.toString());
			sysOut.println("joinExpMap is: " + tree.joinExpMap.toString());
			sysOut.println("selectExpMap is: " + tree.selectExpMap.toString());
			
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				System.out.println(cur.toString());
				cur = tree.root.getNextTuple();
			}
			Catalog.selfJoinMap.clear();
			Catalog.alias.clear();
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
}

