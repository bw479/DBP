package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.StringReader;

import org.junit.Test;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import nio.BinaryTupleWriter;
import operators.*;
import utils.Catalog;
import utils.FormatConverter;
import utils.SortTuples;
import utils.Table;
import utils.TreeBuilder;
import utils.Tuple;

public class TupleJoinTest {

	Catalog catalog = new Catalog();

	@Test
	public void equalTest() {
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors, Reserves"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);

			Operator root = tree.root;
			Tuple cur = root.getNextTuple();
			while (cur != null) {
				System.out.println(cur.toString());
				cur = tree.root.getNextTuple();
			}
			
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

	@Test
	public void compareTest() {
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors S1, Sailors S2 WHERE S1.A < S2.A"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Decompose compareTest 1 : " + 
					((Select) statement).getSelectBody());
			System.out.println("project is: " + tree.project.toString());
			if (tree.distinct == null) tree.distinct = new Distinct();
			System.out.println("distinct is: " + tree.distinct.toString());
			System.out.println("orderBy is: " + tree.orderBy.toString());
			System.out.println("fromItems is: " + tree.fromItems.toString());
			System.out.println("alias is: " + tree.alias.toString());
			System.out.println("selectConds is: " + tree.selectConds.toString());
			System.out.println("joinConds is: " + tree.joinConds.toString());
			System.out.println("joinExpMap is: " + tree.joinExpMap.toString());
			System.out.println("selectExpMap is: " + tree.selectExpMap.toString());

			System.out.println(tree.root.getSchema());

			Tuple nextTuple = tree.root.getNextTuple();

			while (nextTuple != null) {
				System.out.println(nextTuple.toString());
				nextTuple = tree.root.getNextTuple();
			}
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

	@Test
	public void Test1() {
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors S1, Sailors S2, Sailors S3 "
							+ "WHERE S1.A < S2.A and S2.B < S3.C"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Decompose Test1 : " + 
					((Select) statement).getSelectBody());
			System.out.println("project is: " + tree.project.toString());
			if (tree.distinct == null) tree.distinct = new Distinct();
			System.out.println("distinct is: " + tree.distinct.toString());
			System.out.println("orderBy is: " + tree.orderBy.toString());
			System.out.println("fromItems is: " + tree.fromItems.toString());
			System.out.println("alias is: " + tree.alias.toString());
			System.out.println("selectConds is: " + tree.selectConds.toString());
			System.out.println("joinConds is: " + tree.joinConds.toString());
			System.out.println("joinExpMap is: " + tree.joinExpMap.toString());
			System.out.println("selectExpMap is: " + tree.selectExpMap.toString());

			System.out.println(tree.root.getSchema());

			Tuple nextTuple = tree.root.getNextTuple();

			while (nextTuple != null) {
				System.out.println(nextTuple.toString());
				nextTuple = tree.root.getNextTuple();
			}
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
}
