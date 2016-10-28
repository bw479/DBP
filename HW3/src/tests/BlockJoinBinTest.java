package tests;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import nio.BinaryTupleWriter;
import nio.BinaryTupleWriter;
import utils.Catalog;
import utils.FormatConverter;
import utils.SortTuples;
import utils.TreeBuilder;
import utils.Tuple;

public class BlockJoinBinTest {

	Catalog catalog = new Catalog();
	
	@Test
	public void Test8() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors, Reserves WHERE Sailors.A = Reserves.G"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 8 : " + 
					((Select) statement).getSelectBody());
			
			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + 
					"BlockJoinBinTest8");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			FormatConverter.bin2Dec(Catalog.outputPath + "BlockJoinBinTest8", 
					Catalog.outputPath + "BlockJoinBinTest8_Dec");
			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinBinTest8_Dec");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	@Test
	public void Test9() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors, Reserves, Boats WHERE "
							+ "Sailors.A = Reserves.G AND Reserves.H = Boats.D"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 9 : " + 
					((Select) statement).getSelectBody());
			
			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + 
					"BlockJoinBinTest9");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			FormatConverter.bin2Dec(Catalog.outputPath + "BlockJoinBinTest9", 
					Catalog.outputPath + "BlockJoinBinTest9_Dec");
			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinBinTest9_Dec");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	@Test
	public void Test10() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors, Reserves, Boats WHERE "
							+ "Sailors.A = Reserves.G AND Reserves.H = Boats.D AND Sailors.B < 150"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 10 : " + 
					((Select) statement).getSelectBody());
			
			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + 
					"BlockJoinBinTest10");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			FormatConverter.bin2Dec(Catalog.outputPath + "BlockJoinBinTest10", 
					Catalog.outputPath + "BlockJoinBinTest10_Dec");
			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinBinTest10_Dec");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	@Test
	public void Test12() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors S1, Sailors S2 WHERE S1.A < S2.A"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 12 : " + 
					((Select) statement).getSelectBody());
			
			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + 
					"BlockJoinBinTest12");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			FormatConverter.bin2Dec(Catalog.outputPath + "BlockJoinBinTest12", 
					Catalog.outputPath + "BlockJoinBinTest12_Dec");
			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinBinTest12_Dec");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	//@Test
	public void Test14() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors S, Reserves R, Boats B WHERE "
							+ "S.A = R.G AND R.H = B.D ORDER BY S.C;"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 14 : " + 
					((Select) statement).getSelectBody());
			
			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + 
					"BlockJoinBinTest14");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			FormatConverter.bin2Dec(Catalog.outputPath + "BlockJoinBinTest9", 
					Catalog.outputPath + "BlockJoinBinTest9_Dec");
			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinBinTest9_Dec");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	//@Test
	public void Test15() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT DISTINCT * FROM Sailors S, Reserves R, Boats B "
							+ "WHERE S.A = R.G AND R.H = B.D ORDER BY S.C"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 15 : " + 
					((Select) statement).getSelectBody());
			
			BinaryTupleWriter writer = new BinaryTupleWriter(Catalog.outputPath + 
					"BlockJoinBinTest15");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();

		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}

}
