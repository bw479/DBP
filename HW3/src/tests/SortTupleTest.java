package tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import utils.Catalog;
import utils.SortTuples;

public class SortTupleTest {


	@Test
	public void test() throws IOException{
		SortTuples.sortTuple("samples/output/Dec/SortMergeJoinDecTest8");
		SortTuples.sortTuple("samples/expected/query8_humanreadable");
	}

}
