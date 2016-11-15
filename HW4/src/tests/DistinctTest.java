package tests;

import static org.junit.Assert.*;

import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.Select;
import operators.DuplicateEliminationOperator;
import operators.JoinOperator;
import operators.Operator;
import operators.ProjectOperator;
import operators.ScanOperator;
import operators.SelectOperator;
import operators.SortOperator;
import utils.Catalog;
import utils.TreeBuilder;
import utils.Tuple;

public class DistinctTest {

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
	
	/**
	 * Basic test 
	 */
	@Test
	public void Test1() {
		try {
			// Check decomposition
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT DISTINCT R.G FROM Reserves AS R"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			sysOut.println("--------Test 1 : " + 
					((Select) statement).getSelectBody());
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

			Tuple nextTuple = tree.root.getNextTuple();
			System.out.println(tree.root.getSchema());
			while (nextTuple != null) {
				System.out.println(nextTuple.toString());
				nextTuple = tree.root.getNextTuple();
			}
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	/**
	 * Distinguish join and select condition
	 */
	@Test
	public void Test5() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select Distinct * from Boats B, Sailors s where Boats.D = 101 order by Boats.E"));
			Statement statement = parser.Statement();
			sysOut.println("--------Test 5 : " + 
					((Select) statement).getSelectBody());
			TreeBuilder tree = new TreeBuilder(statement);

			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				sysOut.println(cur.toString());
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
