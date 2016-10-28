package tests;

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

public class BlockJoinDecTest {
	
	Catalog catalog = new Catalog();
	
//	@Test
	public void Test1() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select * from RandomBoats, RandomReserves "
							+ "where RandomBoats.D > 100"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 1 : " + 
					((Select) statement).getSelectBody());
//			Operator root = tree.root;
//			System.out.println(root.getClass().toString());
//			root = ((ProjectOperator)root).child;
//			System.out.println(root.getClass().toString());
//			Operator left = ((BlockJoinOperator)root).left;
//			System.out.println(left.getClass().toString());
//			left = ((SelectOperator)left).child;
//			System.out.println(left.getClass().toString());
//			Operator right = ((JoinOperator)root).right;
//			System.out.println(right.getClass().toString());
			
			DecimalTupleWriter writer = new DecimalTupleWriter(Catalog.outputPath + 
					"BlockJoinTest1");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinTest1");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
//	@Test
	public void Test2() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select * from RandomBoats, RandomReserves "
							+ "where RandomBoats.D > RandomBoats.E"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 2 : " + 
					((Select) statement).getSelectBody());

			DecimalTupleWriter writer = new DecimalTupleWriter(Catalog.outputPath + 
					"BlockJoinTest2");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinTest2");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
//	@Test
	public void Test3() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select * from RandomBoats, RandomReserves where "
					+ "RandomBoats.D > RandomBoats.E and RandomReserves.G > RandomReserves.H"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 3 : " + 
					((Select) statement).getSelectBody());
			
			DecimalTupleWriter writer = new DecimalTupleWriter(Catalog.outputPath + 
					"BlockJoinTest3");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinTest3");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
//	@Test
	public void Test4() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("select * from RandomBoats, RandomReserves where "
					+ "RandomBoats.D > RandomBoats.E and RandomReserves.G > RandomReserves.H "
					+ "and RandomBoats.F = RandomReserves.H"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 4 : " + 
					((Select) statement).getSelectBody());
			
			DecimalTupleWriter writer = new DecimalTupleWriter(Catalog.outputPath + 
					"BlockJoinTest4");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinTest4");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	@Test
	public void Test5() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors, Reserves WHERE Sailors.A = Reserves.G;"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 5 : " + 
					((Select) statement).getSelectBody());
			
			DecimalTupleWriter writer = new DecimalTupleWriter(Catalog.outputPath + 
					"BlockJoinTest5");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinTest5");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	@Test
	public void Test6() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors, Reserves, Boats WHERE "
							+ "Sailors.A = Reserves.G AND Reserves.H = Boats.D"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 6 : " + 
					((Select) statement).getSelectBody());
			
			DecimalTupleWriter writer = new DecimalTupleWriter(Catalog.outputPath + 
					"BlockJoinTest6");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinTest6");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	@Test
	public void Test7() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors, Reserves, Boats WHERE "
							+ "Sailors.A = Reserves.G AND Reserves.H = Boats.D AND Sailors.B < 150"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 7 : " + 
					((Select) statement).getSelectBody());
			
			DecimalTupleWriter writer = new DecimalTupleWriter(Catalog.outputPath + 
					"BlockJoinTest7");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinTest7");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	@Test
	public void Test8() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors S1, Sailors S2 WHERE S1.A < S2.A"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 8 : " + 
					((Select) statement).getSelectBody());
			
			DecimalTupleWriter writer = new DecimalTupleWriter(Catalog.outputPath + 
					"BlockJoinTest8");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinTest8");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	@Test
	public void Test9() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM Sailors S, Reserves R, Boats B WHERE "
							+ "S.A = R.G AND R.H = B.D ORDER BY S.C;"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 9 : " + 
					((Select) statement).getSelectBody());
			
			DecimalTupleWriter writer = new DecimalTupleWriter(Catalog.outputPath + 
					"BlockJoinTest9");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
//			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinTest9");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
	
	@Test
	public void Test10() {
		
		try {
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT DISTINCT * FROM Sailors S, Reserves R, Boats B "
							+ "WHERE S.A = R.G AND R.H = B.D ORDER BY S.C"));
			Statement statement = parser.Statement();
			TreeBuilder tree = new TreeBuilder(statement);
			System.out.println("--------Test 10 : " + 
					((Select) statement).getSelectBody());
			
			DecimalTupleWriter writer = new DecimalTupleWriter(Catalog.outputPath + 
					"BlockJoinTest10");
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				writer.write(cur);
				cur = tree.root.getNextTuple();
			}
			writer.close();
//			SortTuples.sortTuple(Catalog.outputPath + "BlockJoinTest10");
		} catch (Exception e) {
			System.err.println("Exception during parsing");
			e.printStackTrace();
		}
	}
}
