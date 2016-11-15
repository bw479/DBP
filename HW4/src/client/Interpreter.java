package client;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import nio.BinaryTupleWriter;
import nio.DecimalTupleWriter;
import nio.TupleWriter;
import utils.Catalog;
import utils.IndexBuilder;
import utils.TreeBuilder;

/**
 * This class is the top-level class which has a main method and will extract
 * all the arguments in order to execute all the queries and save results 
 * to the output files.
 *
 */

public class Interpreter {

	private static boolean isBinary = true;
	private static boolean humanreable = true;
	private static String input = "";
	private static String output = "";
	private static String tempdir = "";
	private static boolean shouldBuildIndex = false;
	private static boolean shouldEvaluateQuery = false;

	/**
	 * This method will read the interpreter configuration file and set up all the arguments
	 * @param args arguments from the console
	 */
	public static void interpreterConfig(String[] args) {
		try {
			String configPath = args[0];
			BufferedReader br =  new BufferedReader(new FileReader(configPath));
			input = br.readLine();
			output = br.readLine();
			tempdir = br.readLine();
			shouldBuildIndex = br.readLine().equals("1");
			shouldEvaluateQuery = br.readLine().equals("0");
			br.close();
			
		} catch (FileNotFoundException e) {
			System.err.println("Interpreter Configuration File Not Found");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Exception when reading the interpreter configuration file");
			e.printStackTrace();
		} 
	}
	
	/**
	 * This method will evaluate all the queries and write results to the output file
	 */
	
	public static void evaluateQuery() {
		
		String queriesFile = Catalog.qryPath;
		
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
								+ File.separator + "query" + count;
						tw = new BinaryTupleWriter(filePath);
					}
					else {
						filePath = Catalog.outputPath + File.separator 
								+ "query" + count + "_humanreadable";
						tw = new DecimalTupleWriter(filePath);
					}
					TreeBuilder tree = new TreeBuilder(statement);
					long before = System.currentTimeMillis();
					tree.root.dump(tw);
					long time = System.currentTimeMillis() - before;
					System.out.println(time + " milliseconds has elapsed");
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

	/**
	 * The main method which receives the path of the interpreter configuration file, 
	 * parses all the arguments, and build the index or execute queries.
	 * @param args arguments from the console
	 */
	public static void main(String[] args) {
		if (args == null  ||  args.length != 1) {
			throw new IllegalArgumentException("Illegal Arguments");
		}

		interpreterConfig(args);

		Catalog catalog = new Catalog(input, output, tempdir);
		
		if (shouldBuildIndex) {
			IndexBuilder.indexBuild(humanreable);
		}
		
		if (shouldEvaluateQuery) {
			evaluateQuery();
		}
	}
}
