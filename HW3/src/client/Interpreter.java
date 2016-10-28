package client;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import nio.BinaryTupleWriter;
import nio.DecimalTupleWriter;
import nio.TupleWriter;
import utils.Catalog;
import utils.TreeBuilder;

/**
 * This class is the top-level class which has a main method and will extract
 * all the arguments in order to execute all the queries and save results 
 * to the output files.
 *
 */

public class Interpreter {
	
	private static boolean isBinary = true;
	
	/**
	 * The main method which receives the query statements, evaluates and writes
	 * results to the output file
	 * @param args arguments from the console
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			throw new IllegalArgumentException("Illegal Arguments");
		}

		String input = args[0];
		String output = args[1];

		Catalog catalog = new Catalog(input, output);

		String queriesFile = Catalog.qryPath;
		System.out.println(queriesFile);
		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
			Statement statement;
			int count = 1;
			while ((statement = parser.Statement()) != null) {
				try {
					Select select = (Select) statement;
					System.out.println("Query " + 
							count + " is " + select.getSelectBody());

					String filePath;
					TupleWriter tw;
					if (isBinary) {
						filePath = Catalog.outputPath 
								+ File.separator + "my_query" + count;
						tw = new BinaryTupleWriter(filePath);
					}
					else {
						filePath = Catalog.outputPath + File.separator 
								+ "my_query" + count + "_humanreadable";
						tw = new DecimalTupleWriter(filePath);
					}
					TreeBuilder tree = new TreeBuilder(statement);
					long before = System.currentTimeMillis();
					tree.root.dump(tw);
					long time = System.currentTimeMillis() - before;
					System.out.println(time + "milliseconds has elapsed");
					tw.close();
					count++;
					Catalog.selfJoinMap.clear();
					Catalog.alias.clear();
					
				} catch (Exception e) {
					System.err.println("Exception occurred "
							+ "during parsing query " + count);
					e.printStackTrace();
					continue;
				}
			}
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}
}
