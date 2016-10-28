package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nio.TupleReader;

/**
 * This class is used to create a table instance and some related methods. 
 *
 */

public class Table {
	public String name = "";
	public List<String> schema = null;
	private TupleReader tr = null;
	
	
	/**
	 * Constructor.
	 * @param name: name of the table
	 * @param schema: schema of the table
	 * @param tr: the file reader
	 * 
	 */
	public Table(String name, List<String> schema, TupleReader tr) {
		this.name = name;
		this.schema = schema;
		this.tr = tr;
	}	
	
	
	/**
	 * Read the next line of the table.
	 * @return the next line converted into a tuple
	 */
	public Tuple nextTuple() {
		try {
			return tr.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Close the current tuple reader and open a new one for current table 
	 */
	public void reset() {
		try {
		tr.reset();
		} catch (IOException e) {
			System.out.println("Exception when reset the table reader");
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the schema of the current table
	 * @return the schema of the table
	 */
	public List<String> getSchema() {
		return schema;
	}
	
}
