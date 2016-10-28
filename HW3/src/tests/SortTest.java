package tests;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.Select;
import nio.DecimalTupleWriter;
import utils.Catalog;
import utils.TreeBuilder;
import utils.Tuple;

public class SortTest {
	Catalog catalog = new Catalog();

	/**
	 * Sort alias test
	 */
	@Test
	public void test() {
		try {
			// Check decomposition
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT * FROM query12 S1 order by query12.I"));
			Statement statement = parser.Statement();
			System.out.println("--------Decompose Test 1 : " + 
					((Select) statement).getSelectBody());
			TreeBuilder tree = new TreeBuilder(statement);
			
			Tuple cur = tree.root.getNextTuple();
			DecimalTupleWriter tw = new DecimalTupleWriter("testResult");
			
			while (cur != null) {
				//System.out.println(cur.toString());
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
