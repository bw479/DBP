package tests;

import static org.junit.Assert.*;

import java.io.PrintStream;
import java.io.StringReader;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import operators.*;
import utils.Catalog;
import utils.TreeBuilder;

public class TreeBuilderTest {

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
	 * Basic test of SELECT-FROM-WHERE clause
	 */
	@Test
	public void statementDecomposeTest1() {
		try {
			// Check decomposition
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select * from Boats where Boats.D = 101"));
			
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
			List<String> boatsScma = ((ScanOperator)root).getSchema();
			assertEquals(boatsScma, boatsSchema);
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

	/**
	 * Select multiple columns
	 */
	@Test
	public void statementDecomposeTest2() {

		try {
			// Check decomposition
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select *, Boats.D from Boats where Boats.D = 101"));
			Statement statement = parser.Statement();
			sysOut.println("--------Decompose Test 2 : " + 
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
			List<String> boatsScma = ((ScanOperator)root).getSchema();
			assertEquals(boatsScma, boatsSchema);
			Catalog.selfJoinMap.clear();
			Catalog.alias.clear();
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

	/**
	 * Select multiple columns under multiple select conditions
	 */
	@Test
	public void statementDecomposeTest3() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select Boats.D from Boats where Boats.D = 101 and Boats.E = 2"));
			Statement statement = parser.Statement();
			sysOut.println("--------Decompose Test 3 : " + 
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
			List<String> boatsScma = ((ScanOperator)root).getSchema();
			assertEquals(boatsScma, boatsSchema);
			Catalog.selfJoinMap.clear();
			Catalog.alias.clear();
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

	/**
	 * Alias test 
	 */
	@Test
	public void statementDecomposeTest4() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select B.D from Boats as B where Boats.D = 101"));
			Statement statement = parser.Statement();
			sysOut.println("--------Decompose Test 4 : " + 
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
			List<String> boatsScma = ((ScanOperator)root).getSchema();
			assertEquals(boatsSchema, boatsScma);
			Catalog.selfJoinMap.clear();
			Catalog.alias.clear();
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	/**
	 * Alias test such that alias exists in WHERE
	 */
	@Test
	public void statementDecomposeTest10() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors S WHERE S.A < 3;"));
			Statement statement = parser.Statement();
			sysOut.println("--------Decompose Test 10 : " + 
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
			Catalog.selfJoinMap.clear();
			Catalog.alias.clear();
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

	/**
	 * Basic join test
	 */
	@Test
	public void statementDecomposeTest5() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select * from Boats, Sailors where Boats.A = Sailors.B"));
			Statement statement = parser.Statement();
			sysOut.println("--------Decompose Test 5 : " + 
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
			assertTrue(root instanceof JoinOperator);
			sysOut.println("join " + root.getSchema());
			Operator left = ((JoinOperator)root).left;
			Operator right = ((JoinOperator)root).right;
			assertTrue(left instanceof ScanOperator);
			assertTrue(right instanceof ScanOperator);
			List<String> boatsScma = ((ScanOperator)left).getSchema();
			assertEquals(boatsScma, boatsSchema);
			List<String> sailorsScma = ((ScanOperator)right).getSchema();
			assertEquals(sailorsSchema, sailorsScma);
			Catalog.selfJoinMap.clear();
			Catalog.alias.clear();
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

	/**
	 * Join and Selection test
	 */
	@Test
	public void statementDecomposeTest6() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select B.D, Sailors.A from Boats B, Sailors where Boats.D = Sailors.B and Boats.D = 101"));
			Statement statement = parser.Statement();
			sysOut.println("--------Decompose Test 6 : " + 
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
			assertTrue(root instanceof JoinOperator);
			sysOut.println("join " + root.getSchema());
			Operator left = ((JoinOperator)root).left;
			Operator right = ((JoinOperator)root).right;
			assertTrue(left instanceof SelectOperator);
			sysOut.println("select " + left.getSchema());
			assertTrue(right instanceof ScanOperator);
			Operator leftScan = ((SelectOperator)left).child;
			assertTrue(leftScan instanceof ScanOperator);
			List<String> boatsScma = ((ScanOperator)leftScan).getSchema();
			assertEquals(boatsScma, boatsSchema);
			List<String> sailorsScma = ((ScanOperator)right).getSchema();
			assertEquals(sailorsScma, sailorsSchema);
			Catalog.selfJoinMap.clear();
			Catalog.alias.clear();
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

	/**
	 * OrderBy test
	 */
	@Test
	public void statementDecomposeTest7() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select * from Boats where Boats.D = 101 order by Boats.E"));
			Statement statement = parser.Statement();
			sysOut.println("--------Decompose Test 7 : " + 
					((Select) statement).getSelectBody());
			TreeBuilder tree = new TreeBuilder(statement);
			sysOut.println("project is: " + tree.project.toString());
			if (tree.distinct == null) {
				tree.distinct = new Distinct();
				sysOut.println("distinct is: " + null);
			}
			else sysOut.println("distinct is: " + tree.distinct.toString());
			sysOut.println("orderBy is: " + tree.orderBy.toString());
			sysOut.println("fromItems is: " + tree.fromItems.toString());
			sysOut.println("alias is: " + tree.alias.toString());
			sysOut.println("selectConds is: " + tree.selectConds.toString());
			sysOut.println("joinConds is: " + tree.joinConds.toString());
			sysOut.println("joinExpMap is: " + tree.joinExpMap.toString());
			sysOut.println("selectExpMap is: " + tree.selectExpMap.toString());

			// Check tree structure
			Operator root = tree.root;
			assertTrue(root instanceof SortOperator);
			sysOut.println("sort " + root.getSchema());
			root = ((SortOperator)root).child;
			assertTrue(root instanceof ProjectOperator);
			sysOut.println("project " + root.getSchema());
			root = ((ProjectOperator)root).child;
			assertTrue(root instanceof SelectOperator);
			sysOut.println("select " + root.getSchema());
			root = ((SelectOperator)root).child;
			assertTrue(root instanceof ScanOperator);
			List<String> boatsScma = ((ScanOperator)root).getSchema();
			assertEquals(boatsScma, boatsSchema);
			Catalog.selfJoinMap.clear();
			Catalog.alias.clear();
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

	/**
	 * Distinct test
	 */
	@Test
	public void statementDecomposeTest8() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select Distinct * from Boats where Boats.D = 101 order by Boats.E"));
			Statement statement = parser.Statement();
			sysOut.println("--------Decompose Test 8 : " + 
					((Select) statement).getSelectBody());
			TreeBuilder tree = new TreeBuilder(statement);
			
			sysOut.println("project is: " + tree.project.toString());
			if (tree.distinct == null) {
				tree.distinct = new Distinct();
				sysOut.println("distinct is: " + null);
			}
			else sysOut.println("distinct is: " + tree.distinct.toString());
			sysOut.println("orderBy is: " + tree.orderBy.toString());
			sysOut.println("fromItems is: " + tree.fromItems.toString());
			sysOut.println("alias is: " + tree.alias.toString());
			sysOut.println("selectConds is: " + tree.selectConds.toString());
			sysOut.println("joinConds is: " + tree.joinConds.toString());
			sysOut.println("joinExpMap is: " + tree.joinExpMap.toString());
			sysOut.println("selectExpMap is: " + tree.selectExpMap.toString());

			// Check tree structure
			Operator root = tree.root;
			assertTrue(root instanceof DuplicateEliminationOperator);
			sysOut.println("Duplicate " + root.getSchema());
			root = ((DuplicateEliminationOperator)root).child;
			assertTrue(root instanceof SortOperator);
			sysOut.println("sort " + root.getSchema());
			root = ((SortOperator)root).child;
			assertTrue(root instanceof ProjectOperator);
			sysOut.println("project " + root.getSchema());
			root = ((ProjectOperator)root).child;
			assertTrue(root instanceof SelectOperator);
			sysOut.println("select " + root.getSchema());
			root = ((SelectOperator)root).child;
			assertTrue(root instanceof ScanOperator);
			List<String> boatsScma = ((ScanOperator)root).getSchema();
			assertEquals(boatsScma, boatsSchema);
			Catalog.selfJoinMap.clear();
			Catalog.alias.clear();
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

	/**
	 * Integration test
	 */
	@Test
	public void statementDecomposeTest9() {

		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select Distinct *, Boats.F, Sailors.A "
							+ "from Boats as B, Sailors as S, Reserves as R "
							+ "where B.D = 101 and Reserves.H = 101 and S.A = R.G and B.E = S.B "
							+ "order by B.E"));
			Statement statement = parser.Statement();
			sysOut.println("--------Decompose Test 9 : " + 
					((Select) statement).getSelectBody());
			TreeBuilder tree = new TreeBuilder(statement);
			
			sysOut.println("project is: " + tree.project.toString());
			if (tree.distinct == null) {
				tree.distinct = new Distinct();
				sysOut.println("distinct is: " + null);
			}
			else sysOut.println("distinct is: " + tree.distinct.toString());
			sysOut.println("orderBy is: " + tree.orderBy.toString());
			sysOut.println("fromItems is: " + tree.fromItems.toString());
			sysOut.println("alias is: " + tree.alias.toString());
			sysOut.println("selectConds is: " + tree.selectConds.toString());
			sysOut.println("joinConds is: " + tree.joinConds.toString());
			sysOut.println("joinExpMap is: " + tree.joinExpMap.toString());
			sysOut.println("selectExpMap is: " + tree.selectExpMap.toString());

			// Check tree structure
			Operator root = tree.root;
			assertTrue(root instanceof DuplicateEliminationOperator);
			sysOut.println("Duplicate " + root.getSchema());
			root = ((DuplicateEliminationOperator)root).child;
			assertTrue(root instanceof SortOperator);
			sysOut.println("sort " + root.getSchema());
			root = ((SortOperator)root).child;
			assertTrue(root instanceof ProjectOperator);
			sysOut.println("project " + root.getSchema());
			root = ((ProjectOperator)root).child;
			assertTrue(root instanceof JoinOperator);
			sysOut.println("join " + root.getSchema());
			
			Operator left = ((JoinOperator)root).left;
			assertTrue(left instanceof JoinOperator);
			sysOut.println("join-left join" + root.getSchema());
			Operator left1 = ((JoinOperator)left).left;
			Operator left2 = ((JoinOperator)left).right;
			assertTrue(left1 instanceof SelectOperator);
			sysOut.println("join-left-left1 select" + root.getSchema());
			assertTrue(left2 instanceof ScanOperator);
			Operator left1Scan = ((SelectOperator)left1).child;
			assertTrue(left1Scan instanceof ScanOperator);
			List<String> boatsScma = ((ScanOperator)left1Scan).getSchema();
			assertEquals(boatsScma, boatsSchema);
			List<String> sailorsScma = ((ScanOperator)left2).getSchema();
			assertEquals(sailorsScma, sailorsSchema);
			
			Operator right = ((JoinOperator)root).right;
			assertTrue(right instanceof SelectOperator);
			sysOut.println("right select" + right.getSchema());
			Operator rightScan = ((SelectOperator)right).child;
			assertTrue(rightScan instanceof ScanOperator);
			List<String> reservesScma = ((ScanOperator)rightScan).getSchema();
			assertEquals(reservesScma, reservesSchema);
			Catalog.selfJoinMap.clear();
			Catalog.alias.clear();
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
}
