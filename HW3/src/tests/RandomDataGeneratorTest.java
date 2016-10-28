package tests;

import org.junit.Test;

import utils.Catalog;
import utils.RandomDataGenerator;

public class RandomDataGeneratorTest {
	
	Catalog c = new Catalog();
	
	@Test
	public void genBoatsTest() {
		String tablePath = Catalog.dataPath + "RandomBoats_humanreadable";
		RandomDataGenerator.tableGen(10, 3, 200, tablePath);
	}
	
	@Test
	public void genReservesTest() {
		String tablePath = Catalog.dataPath + "RandomReserves_humanreadable";
		RandomDataGenerator.tableGen(10, 2, 500, tablePath);
	}
	
	@Test
	public void genSailorsTest() {
		String tablePath = Catalog.dataPath + "RandomSailors_humanreadable";
		RandomDataGenerator.tableGen(10, 2, 100, tablePath);
	}
}
