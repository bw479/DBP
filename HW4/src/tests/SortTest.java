package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.io.StringReader;


import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.Select;
import nio.DecimalTupleWriter;
import utils.Catalog;
import utils.SortTuples;
import utils.TreeBuilder;
import utils.Tuple;

public class SortTest {

	Catalog catalog = new Catalog();

	/**
	 * Sort alias test
	 */
	@Test
	public void test9() {
		try {
			// Check decomposition
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM RandomReserves R order by R.G, R.H"));
			Statement statement = parser.Statement();
			System.out.println("--------Decompose Test 9 : " + 
					((Select) statement).getSelectBody());
			TreeBuilder tree = new TreeBuilder(statement);
			
			Tuple cur = tree.root.getNextTuple();
			DecimalTupleWriter tw = new DecimalTupleWriter(Catalog.outputPath + 
					"Dec" + File.separator + "inMemeSortTest");
			
			while (cur != null) {
				tw.write(cur);
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
