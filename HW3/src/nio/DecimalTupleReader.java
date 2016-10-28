package nio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.Tuple;
/**
 * Reader for the humanreadable tuple files
 *
 */
public class DecimalTupleReader implements TupleReader{
	private File file;
	private FileReader fr;
	private BufferedReader br;
	
	/**
	 * create a reader for tuple files with decimal numbers
	 * @param tablePath the file path
	 * @throws FileNotFoundException
	 */
	public DecimalTupleReader(String tablePath) throws FileNotFoundException{
		file = new File(tablePath);
		this.fr = new FileReader(file);
		br = new BufferedReader(fr);
	}
	
	/** read the next available tuple
	 */
	@Override
	public Tuple read() throws IOException {
		try {
			String line = br.readLine();
			if (line == null) return null;
			String[] elems = line.split(",");
			int len = elems.length;
			List<Integer> tuple = new ArrayList<Integer>();
			for (int i = 0; i < len; i++) {
				tuple.add( Integer.valueOf(elems[i]) );
			}
			return new Tuple(tuple);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * reset the table back to the first tuple
	 */
	@Override
	public void reset() throws IOException {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		br = new BufferedReader(new FileReader(file));
		
	}
	/**
	 * close the file
	 */
	@Override
	public void close() throws IOException {
		fr.close();
		
	}
	
}
