package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import nio.BinaryTupleReader;
import nio.BinaryTupleWriter;
import utils.Tuple;

public class BinaryTupleTest {
	/**
	 * tests the reader and writer for binary files.
	 */
	@Test
	public void test() {
		try {
			BinaryTupleReader btr = new BinaryTupleReader("query14");
			BinaryTupleWriter btw = new BinaryTupleWriter("testWriter");
			Tuple r;
			while ((r = btr.read()) != null) {
				System.out.println(r.getColumn().toString());
				btw.write(r);
				
			}
			
			btw.close();
			
			
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("io not found");
		}
		
		
	}

}
