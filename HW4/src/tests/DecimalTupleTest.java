package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import nio.DecimalTupleReader;
import nio.DecimalTupleWriter;
import utils.Tuple;

public class DecimalTupleTest {
	
	/**
	 * test the reader and writer for the decimal tuple files.
	 */
	@Test
	public void test() {
		try {
			DecimalTupleReader dtr = new DecimalTupleReader("Boats_humanreadable");
			DecimalTupleWriter dtw = new DecimalTupleWriter("testDecimalwrite");
			Tuple r;
			while ((r = dtr.read()) != null) {
				System.out.println(r.toString());
				dtw.write(r);
			}
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		} catch (IOException e) {
			System.out.println("io not found");
		}
		
	}

}
