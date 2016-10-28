package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import nio.BinaryTupleReader;
import nio.DecimalTupleReader;
import nio.TupleReader;

/**
 * This class is a global class which keeps track of all the data, schema of
 * each table, every path of each file and all other information.
 *
 */
public class Catalog {
	public static String inputPath = "samples" + File.separator + "input" + File.separator;
	public static String outputPath = "samples" + File.separator + "output" + File.separator;
	private static String dbPath = "";
	public static String qryPath = "";
	public static String configPath = "";
	public static String dataPath = "";
	private static String schemaPath = "";
	private static boolean isBinary = true;
	
	public static String joinMethod;
	public static int joinBufNum;
	public static String sortMethod;
	public static int sortBufNum;

	private static HashMap<String, ArrayList<String>> schemaMap = 
			new HashMap<String, ArrayList<String>>();
	public static Map<String, String> selfJoinMap = new HashMap<String, String>();
	public static Map<String, String> alias = new HashMap<String, String>();

	/**
	 * Constructor that will create a catalog instance with the default 
	 * input path and output path
	 */
	public Catalog() {
		dbPath = inputPath + "db" + File.separator;
		qryPath = inputPath + "queries.sql";
		configPath = inputPath + "plan_builder_config.txt";
		dataPath = dbPath + "data" + File.separator;
		schemaPath = dbPath + File.separator + "schema.txt";
		initSchema();
		initConfig();
	}

	/**
	 * Constructor that will create a catalog instance with the given 
	 * input path and output path
	 * @param input input path
	 * @param output output path
	 */
	public Catalog(String input, String output) {
		if (input != null) 
			inputPath = input + File.separator;
		if (output != null)
			outputPath = output + File.separator;
		dbPath = inputPath + "db" + File.separator;
		qryPath = inputPath + "queries.sql";
		dataPath = dbPath + "data" + File.separator;
		schemaPath = dbPath + File.separator + "schema.txt";
		initSchema();
		initConfig();
	}

	/**
	 * Initialize the schema Map for all tables
	 */
	public void initSchema() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(schemaPath));
			String curLine;
			while ((curLine = br.readLine()) != null) {
				String[] curSchema = curLine.split(" ");
				String tableName = curSchema[0];
				ArrayList<String> list = new ArrayList<String>();
				for (int i = 1; i < curSchema.length; i++) {
					list.add(tableName + "." + curSchema[i]);
				}
				schemaMap.put(tableName, list);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Schema not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Exception when reading the schema");
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	/**
	 * Get the schema of the given table
	 * @param tableName the table name
	 * @return the schema of the table
	 */
	public static List<String> getSchema(String tableName) {
		return schemaMap.get(tableName);
	}

	/**
	 * Get the binary table reader for the given table
	 * @param tableName the table name
	 * @return the binary table reader of the table
	 */
	public static TupleReader getBinaryTableReader(String tableName) {
		String tablePath = dataPath + tableName;
		TupleReader tr = null;
		try {
			tr = new BinaryTupleReader(tablePath);
		} catch (FileNotFoundException e) {
			System.out.println("Table not found");
			e.printStackTrace();
		}
		return tr;
	}
	
	/**
	 * Get the decimal table reader for the given table
	 * @param tableName the table name
	 * @return the decimal table reader of the table
	 */
	public static TupleReader getDecimalTableReader(String tableName) {
		String tablePath = dataPath + tableName + "_humanreadable";
		TupleReader tr = null;
		try {
			tr = new DecimalTupleReader(tablePath);
		} catch (FileNotFoundException e) {
			System.out.println("Table not found");
			e.printStackTrace();
		}
		return tr;
	}
	

	/**
	 * Generate a new schema with the alias as the table name 
	 * @param aliasName the alias name of a table
	 * @return the new schema
	 */
	private static List<String> getAlaisSchema(String aliasName) {
		String tableName = alias.get(aliasName);
		List<String> tableSchema = schemaMap.get(tableName);
		List<String> schema = new ArrayList<String>();
		for (String s : tableSchema) {
			String colName = s.split("\\.")[1];
			schema.add(aliasName + "." + colName);
		}
		return schema;
	}
	
	/**
	 * Get the table based on the given table name
	 * @param tableName table name
	 * @return the table
	 */
	public static Table getTable(String tableName) {
		String tmp = tableName;
		if (alias.containsKey(tableName))
			tableName = alias.get(tableName);
		TupleReader tr;
		if (isBinary) tr = getBinaryTableReader(tableName);
		else tr = getDecimalTableReader(tableName);
		
		List<String> schema;
		if (selfJoinMap.containsKey(tmp)) {
			schema = getAlaisSchema(tmp);
			tableName = tmp;
		}
		else {
			schema = getSchema(tableName);
		}
		return new Table(tableName, schema, tr);
		
	}

	public static void initConfig() {
		BufferedReader br = null;
		try {
			
			br = new BufferedReader(new FileReader(configPath));
			// Parse the join method
			String[] join = br.readLine().trim().split(" ");
			if (join[0].equals("0")) joinMethod = "TNLJ";
			else if (join[0].equals("1")) {
				joinMethod = "BNLJ";
				joinBufNum = Integer.parseInt(join[1]);
			}
			else {
				joinMethod = "SMJ";
			}
			
			// Parse the sort method
			String[] sort = br.readLine().trim().split(" ");
			if (sort[0].equals("0")) sortMethod = "INMEM";
			else {
				sortMethod = "EX";
				sortBufNum = Integer.parseInt(sort[1]);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Schema not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Exception when reading the schema");
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

}
