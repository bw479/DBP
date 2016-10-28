package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import utils.Tuple;
import utils.TreeBuilder;

public class EndToEndTest {

	public static final PrintStream sysOut = System.out;
	Catalog catalog = new Catalog();

	/**
	 * Select multiple columns under multiple select conditions
	 */
	@Test
	public void Test1() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select Boats.F from Boats where Boats.D = 101 and Boats.E = 2"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			sysOut.println("--------Test 1 : " + 
					((Select) statement).getSelectBody());

			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				sysOut.println(cur.toString());
				cur = tree.root.getNextTuple();
			}

		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

	/**
	 * Basic join test
	 */
	@Test
	public void Test2() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select * from Boats, Sailors where Boats.F = Sailors.A"));
			Statement statement = parser.Statement();
			sysOut.println("--------Test 2 : " + 
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

	/**
	 * Basic join and alias
	 */
	@Test
	public void Test3() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select B.F from Boats B, Sailors where B.F = Sailors.A"));
			Statement statement = parser.Statement();
			sysOut.println("--------Test 3 : " + 
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

	/**
	 * Basic join and alias and distinct
	 */
	@Test
	public void Test4() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select B.F from Boats B, Sailors S where B.F = Sailors.A "));
			Statement statement = parser.Statement();
			sysOut.println("--------Test 4 : " + 
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

	/**
	 * Distinguish join and select condition
	 */
	@Test
	public void Test5() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select Distinct B.E from Boats B, Sailors s where Boats.D = 101 order by Boats.E"));
			Statement statement = parser.Statement();
			sysOut.println("--------Test 5 : " + 
					((Select) statement).getSelectBody());
			TreeBuilder tree = new TreeBuilder(statement);
			// Check tree structure
			Operator root = tree.root;
			assertTrue(root instanceof DuplicateEliminationOperator);
			sysOut.println("Duplicate " + root.getSchema());
			root = ((DuplicateEliminationOperator)root).child;
			assertTrue(root instanceof SortOperator);
			root = ((SortOperator)root).child;
			assertTrue(root instanceof ProjectOperator);
			sysOut.println("project " + root.getSchema());
			root = ((ProjectOperator)root).child;
			assertTrue(root instanceof JoinOperator);
			sysOut.println("join " + root.getSchema());
			Operator left = ((JoinOperator)root).left;
			Operator right = ((JoinOperator)root).right;
			assertTrue(left instanceof SelectOperator);
			left = ((SelectOperator)left).child;
			assertTrue(left instanceof ScanOperator);
			assertTrue(right instanceof ScanOperator);

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
	
	@Test
	public void Test6() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors WHERE Sailors.B >= Sailors.C"));
			Statement statement = parser.Statement();
			sysOut.println("--------Test 6 : " + 
					((Select) statement).getSelectBody());
			TreeBuilder tree = new TreeBuilder(statement);

			Operator root = tree.root;
			assertTrue(root instanceof ProjectOperator);
			sysOut.println("project " + root.getSchema());
			root = ((ProjectOperator)root).child;
			System.out.println(root.getClass());
			assertTrue(root instanceof SelectOperator);
			root = ((SelectOperator)root).child;
			assertTrue(root instanceof ScanOperator);
			List<String> schema = ((ScanOperator)root).getSchema();
			
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
