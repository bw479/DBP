package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nio.DecimalTupleWriter;

public class SortTuples {

	public static void sortTuple(String input) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(input));
			String line = br.readLine();
			List<String> list = new ArrayList<String>();
			while (line != null) {
				list.add(line);
				line = br.readLine();
			}
			Collections.sort(list);
			DecimalTupleWriter writer = new DecimalTupleWriter(input + "_sorted");
			for (String s : list) {
				String[] elems = s.split(",");
				List<Integer> tuple = new ArrayList<Integer>();
				for (int i = 0; i < elems.length; i++) {
					tuple.add( Integer.valueOf(elems[i]) );
				}
				writer.write(new Tuple(tuple));
			}
			writer.close();

		} catch (FileNotFoundException e) {
			System.out.println("Schema not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Exception when reading the schema");
			e.printStackTrace();
		}
	}
}
