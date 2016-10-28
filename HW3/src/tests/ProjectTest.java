package tests;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.Test;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import utils.Catalog;
import utils.TreeBuilder;
import utils.Tuple;

public class ProjectTest {

	Catalog catalog = new Catalog();

	/**
	 * Sort alias test
	 */
	@Test
	public void test() {
		try {
			// Check decomposition
			CCJSqlParser parser = new CCJSqlParser(
					new StringReader("SELECT distinct S1.A, S2.A FROM Sailors S1, Sailors S2 "
									+ "WHERE S1.A < 3 order by S2.A"));
			Statement statement = parser.Statement();
			System.out.println("--------Decompose Test 1 : " + 
					((Select) statement).getSelectBody());
			TreeBuilder tree = new TreeBuilder(statement);
			
			Tuple cur = tree.root.getNextTuple();
			while (cur != null) {
				System.out.println(cur.toString());
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
