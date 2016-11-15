package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.StringReader;

import org.junit.Test;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import nio.BinaryTupleWriter;
import operators.*;
import utils.Catalog;
import utils.FormatConverter;
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
			System.out.println("--------Test 8 : " + 
					((Select) statement).getSelectBody());

			Operator root = tree.root;

			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest8");
			Tuple cur = root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			FormatConverter.bin2Dec(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest8", Catalog.outputPath + "Dec" + 
							File.separator + "SortMergeJoinDecTest8");
			SortTuples.sortTuple(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinDecTest8");

		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

	@Test
	public void test9() {
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors, Reserves, Boats WHERE Sailors.A = Reserves.G AND Reserves.H = Boats.D;"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);

			Operator root = tree.root;

			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest9");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			FormatConverter.bin2Dec(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest9", Catalog.outputPath + "Dec" + 
							File.separator + "SortMergeJoinDecTest9");
			SortTuples.sortTuple(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinDecTest9");

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

			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest10");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			FormatConverter.bin2Dec(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest10", Catalog.outputPath + "Dec" + 
							File.separator + "SortMergeJoinDecTest10");
			SortTuples.sortTuple(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinDecTest10");

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

			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest12");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			FormatConverter.bin2Dec(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest12", Catalog.outputPath + "Dec" + 
							File.separator + "SortMergeJoinDecTest12");
			SortTuples.sortTuple(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinDecTest12");

		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

	@Test
	public void test15() {
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT DISTINCT * FROM Sailors S, Reserves R, Boats B WHERE S.A = R.G AND R.H = B.D ORDER BY S.C;"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);

			Operator root = tree.root;

			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest15");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			FormatConverter.bin2Dec(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest15", Catalog.outputPath + "Dec" + 
							File.separator + "SortMergeJoinDecTest15");

			SortTuples.sortTuple(Catalog.outputPath + "Dec" + 		
					File.separator + "SortMergeJoinDecTest15");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	@Test
	public void test14() {
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors S, Reserves R, Boats B WHERE S.A = R.G AND R.H = B.D ORDER BY S.C;"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);

			Operator root = tree.root;

			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest14");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			FormatConverter.bin2Dec(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest14", Catalog.outputPath + "Dec" + 
							File.separator + "SortMergeJoinDecTest14");
			
			SortTuples.sortTuple("samples/expected/query14_humanreadable");
			SortTuples.sortTuple(Catalog.outputPath + "Dec" + 		
					File.separator + "SortMergeJoinDecTest14");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	@Test
	public void test13() {
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT B.F, B.D FROM Boats B ORDER BY B.D;"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);

			Operator root = tree.root;

			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest13");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			FormatConverter.bin2Dec(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest13", Catalog.outputPath + "Dec" + 
							File.separator + "SortMergeJoinDecTest13");
			
			SortTuples.sortTuple("samples/expected/query13_humanreadable");
			SortTuples.sortTuple(Catalog.outputPath + "Dec" + 		
					File.separator + "SortMergeJoinDecTest13");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	//@Test
	public void test4() {
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT Reserves.G, Reserves.H FROM Reserves;"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);

			Operator root = tree.root;

			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest4");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			FormatConverter.bin2Dec(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest4", Catalog.outputPath + "Dec" + 
							File.separator + "SortMergeJoinDecTest4");
			
			SortTuples.sortTuple("samples/expected/query4_humanreadable");
			SortTuples.sortTuple(Catalog.outputPath + "Dec" + 		
					File.separator + "SortMergeJoinDecTest4");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	//@Test
	public void test6() {
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT Sailors.A FROM Sailors WHERE Sailors.B >= Sailors.C"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);

			Operator root = tree.root;

			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest6");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			FormatConverter.bin2Dec(Catalog.outputPath + "Dec" + 
					File.separator + "SortMergeJoinBinTest6", Catalog.outputPath + "Dec" + 
							File.separator + "SortMergeJoinDecTest6");
			
			SortTuples.sortTuple("samples/expected/query6_humanreadable");
			SortTuples.sortTuple(Catalog.outputPath + "Dec" + 		
					File.separator + "SortMergeJoinDecTest6");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
}
