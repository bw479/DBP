package tests;

import org.junit.Test;

import utils.Catalog;
import utils.FormatConverter;

public class FormatConvertTest {
	Catalog c = new Catalog();
	
	@Test
	public void bin2DecTest() {
		String input = c.dataPath + "Boats";
		String output = c.dataPath + "Boats_bin2Dec";
		FormatConverter.bin2Dec(input, output);
	}
	
	@Test
	public void dec2BinTest() {
		String input = c.dataPath + "Boats_humanreadable";
		String output = c.dataPath + "Boats_dec2Bin";
		FormatConverter.dec2Bin(input, output);
	}
}
