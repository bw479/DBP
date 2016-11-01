package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nio.DecimalTupleWriter;


public class SortTuples {
	private static class CompareTuple1 implements Comparator<Tuple> {
		public int compare(Tuple t1, Tuple t2) {
			int l = t1.getColumn().size();
			for (int i = 0; i < l; i++) {
				if (t1.getColumn().get(i) > t2.getColumn().get(i)) {
					return 1;
				} else if (t1.getColumn().get(i) < t2.getColumn().get(i)) {
					return -1;
				}
			}
			return 0;
		}
	}

	public static void sortTuple(String input) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(input));
			String line = br.readLine();
			List<Tuple> list = new ArrayList<Tuple>();
			while (line != null) {
				String[] elems = line.split(",");
				List<Integer> tuple = new ArrayList<Integer>();
				for (int i = 0; i < elems.length; i++) {
					tuple.add( Integer.valueOf(elems[i]) );
				}
				list.add(new Tuple(tuple));
				line = br.readLine();
			}
			Collections.sort(list, new CompareTuple1());
			DecimalTupleWriter writer = new DecimalTupleWriter(input + "_sorted");
			for (Tuple t : list) {
				writer.write(t);
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
