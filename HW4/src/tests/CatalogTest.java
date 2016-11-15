package tests;

import static org.junit.Assert.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import utils.Catalog;
import utils.Table;

public class CatalogTest {
	
	public static final PrintStream sysOut = System.out;
	Catalog catalog = new Catalog();
	List<String> sailorsSchema;
	List<String> boatsSchema;
	List<String> reservesSchema;
	
	@Before
	public void setUp() {
		sailorsSchema = new ArrayList<String>();
		sailorsSchema.add("Sailors.A");
		sailorsSchema.add("Sailors.B");
		sailorsSchema.add("Sailors.C");
		boatsSchema = new ArrayList<String>();
		boatsSchema.add("Boats.D");
		boatsSchema.add("Boats.E");
		boatsSchema.add("Boats.F");
		reservesSchema = new ArrayList<String>();
		reservesSchema.add("Reserves.G");
		reservesSchema.add("Reserves.H");
	}
	

	/**
	 * Schema setup tests with the default input and output directory
	 */
	@Test
	public void schemaTest() {
		
		List<String> sailors = Catalog.getSchema("Sailors");
		assertEquals(sailorsSchema, sailors);
		
		List<String> boats = Catalog.getSchema("Boats");
		assertEquals(boatsSchema, boats);
		
		List<String> reserves = Catalog.getSchema("Reserves");
		assertEquals(reservesSchema, reserves);
	}
	
	/**
	 * Test the getTable() method
	 */
	@Test
	public void getTableTest() {
		Table sailors = Catalog.getTable("Sailors");
		assertEquals(sailorsSchema, sailors.getSchema());
		
		Table boats = Catalog.getTable("Boats");
		assertEquals(boatsSchema, boats.getSchema());
		
		Table reserves = Catalog.getTable("Reserves");
		assertEquals(reservesSchema, reserves.getSchema());
	}

}
