package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.Tuple;

/**
 * The RandomDataGenerator class is used to generate a random table by the given
 * tuple number, column number, range, and table path.
 *
 */

public class RandomDataGenerator {
	/**
	 * To generate a random table
	 * @param tpIndex the number of tuples in total
	 * @param colIndex the number of columns in total
	 * @param range the range for all the attributes
	 * @param tablePath the path the table will be saved
	 */
	public static void tableGen(int tpIndex, int colIndex, int range,
			String tablePath) {
		try {
			FileWriter file = new FileWriter(tablePath);
			BufferedWriter bw = new BufferedWriter(file);
			Random random = new Random();
			random.nextInt(range);
			int tpCnt = 0;
			List<Integer> column;
			while (tpCnt < tpIndex) {
				column = new ArrayList<>();
				for (int i = 0; i < colIndex; i++) {
					int randInt = random.nextInt(range);
					column.add(randInt);
				}
				Tuple tuple = new Tuple(column);
				bw.write(tuple.toString());
				bw.newLine();
				tpCnt++;
			}
			bw.close();
		} catch (IOException e) {
			System.out.println("Exception when creating this table");
			e.printStackTrace();
		}
	}
}
