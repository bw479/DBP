package utils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import nio.TupleReader;
import nio.TupleWriter;
/**
 * The class for the tuple.
 * @author bingyuwang
 *
 */
public class Tuple {
	private List<Integer> column; 
	
	private TupleReader tr;
	
	/**
	 * Constructor that will create a new tuple instnace with the given column list
	 * @param column the column list
	 */
	public Tuple(List<Integer> column) {
		this.column = new ArrayList<Integer>(column);
	}
	
	/**
	 * Get the column of the current tuple
	 * @return the column of the current tuple
	 */
	public List<Integer> getColumn() {
		return this.column;
	}
	
	/**
	 * Construct a string which represents the current tuple
	 */
	@Override
	public String toString() {
		if (column == null  ||  column.size() == 0) return null;
		StringBuilder sb = new StringBuilder();
		sb.append(column.get(0));
		for (int i = 1; i < column.size(); i++) {
			sb.append(",");
			sb.append(column.get(i));
		}
		return sb.toString();
	}
	
	/**
	 * Write the current tuple to the print stream
	 * @param ps the print stream
	 */
	public void dump(TupleWriter tw) {
		try {
			tw.write(this);
		} catch (IOException e) {
			System.out.println("Exception when dump the tuple");
			e.printStackTrace();
		}
		
	}
	
	public void setTupleReader(TupleReader tr) {
		this.tr = tr;
	}
	
	public TupleReader getTr() {
		return tr;
	}
}
