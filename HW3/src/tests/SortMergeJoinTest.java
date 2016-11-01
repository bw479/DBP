package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.StringReader;

import org.junit.Test;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import nio.DecimalTupleWriter;
import operators.*;
import utils.Catalog;
import utils.SortTuples;
import utils.TreeBuilder;
import utils.Tuple;

public class SortMergeJoinTest {

	Catalog catalog = new Catalog();
	
	@Test
	public void test8() {
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors, Reserves WHERE Sailors.A = Reserves.G"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 2 : " + 
					((Select) statement).getSelectBody());

			Operator root = tree.root;
			
			DecimalTupleWriter writer = new DecimalTupleWriter(Catalog.outputPath + "Dec" + 
						File.separator + "SortMergeJoinDecTest8");
			Tuple cur = root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}

		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	@Test
	public void test9() {
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors, Reserves, Boats "
							+ "WHERE Sailors.A = Reserves.G AND Reserves.H = Boats.D"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 9 : " + 
					((Select) statement).getSelectBody());

			Operator root = tree.root;
			
			DecimalTupleWriter writer = new DecimalTupleWriter(Catalog.outputPath + "Dec" + 
						File.separator + "SortMergeJoinDecTest9");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			SortTuples.sortTuple("samples/output/Dec/SortMergeJoinDecTest9");
			SortTuples.sortTuple("samples/expected/query9_humanreadable");

		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	@Test
	public void test10() {
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors, Reserves, Boats WHERE Sailors.A = Reserves.G "
							+ "AND Reserves.H = Boats.D AND Sailors.B < 150;"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 10 : " + 
					((Select) statement).getSelectBody());

			Operator root = tree.root;
			
			DecimalTupleWriter writer = new DecimalTupleWriter(Catalog.outputPath + "Dec" + 
						File.separator + "SortMergeJoinDecTest12");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			SortTuples.sortTuple("samples/output/Dec/SortMergeJoinDecTest10");
			SortTuples.sortTuple("samples/expected/query10_humanreadable");

		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

	@Test
	public void test12() {
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors S1, Sailors S2 WHERE S1.A < S2.A;"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 12 : " + 
					((Select) statement).getSelectBody());

			Operator root = tree.root;
			
			DecimalTupleWriter writer = new DecimalTupleWriter(Catalog.outputPath + "Dec" + 
						File.separator + "SortMergeJoinDecTest12");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			SortTuples.sortTuple("samples/output/Dec/SortMergeJoinDecTest12");
			SortTuples.sortTuple("samples/expected/query12_humanreadable");

		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
}
