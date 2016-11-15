package tests;

import org.junit.Test;

import utils.Catalog;
import utils.FormatConverter;
import utils.SortTuples;

public class FormatConvertTest {
	Catalog c = new Catalog();
	
	@Test
	public void bin2DecTest() {
		String input = "samples/expected/query9";
		String output = input + "bin2Dec";
		FormatConverter.bin2Dec(input, output);
		SortTuples.sortTuple(output);
	}
	
	@Test
	public void dec2BinTest() {
		String input = c.dataPath + "Boats_humanreadable";
		String output = c.dataPath + "Boats_dec2Bin";
		FormatConverter.dec2Bin(input, output);
	}
}
