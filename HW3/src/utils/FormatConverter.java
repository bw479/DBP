package utils;

import java.io.IOException;

import nio.BinaryTupleReader;
import nio.BinaryTupleWriter;
import nio.DecimalTupleReader;
import nio.DecimalTupleWriter;
import nio.TupleReader;
import nio.TupleWriter;

/**
 * The FormatConverter class is used to converter file formats between binary 
 * format and decimal format.
 *
 */
public class FormatConverter {

	/**
	 * Change the file format from the binary format to decimal format.
	 * @param input the input path of the binary format file
	 * @param output the output path of converted decimal format file
	 */
	public static void bin2Dec(String input, String output) {
		try {
			TupleReader reader = new BinaryTupleReader(input);
			TupleWriter writer = new DecimalTupleWriter(output);
			Tuple tuple;
			while ((tuple = reader.read()) != null) {
				writer.write(tuple);
			}
			reader.close();
			writer.close();
			
		} catch (IOException e) {
			System.err.println("File not found during formatting");
			e.printStackTrace();
		}
	}
	
	/**
	 * Change the file format from the decimal format to binary format.
	 * @param input the input path of the decimal format file
	 * @param output the output path of converted binary format file
	 */
	public static void dec2Bin(String input, String output) {
		try {
			TupleReader reader = new DecimalTupleReader(input);
			TupleWriter writer = new BinaryTupleWriter(output);
			Tuple tuple;
			while ((tuple = reader.read()) != null) {
				writer.write(tuple);
			}
			reader.close();
			writer.close();
			
		} catch (IOException e) {
			System.err.println("File not found during formatting");
			e.printStackTrace();
		}
	}
}
