package nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import utils.Tuple;
/**
 * writer for humanreadable tuple file
 *
 */
public class DecimalTupleWriter implements TupleWriter{
	private PrintStream ps;
	
	/**
	 * create a writer for humanreadale tuple files
	 * @param filePath
	 * @throws FileNotFoundException
	 */
	public DecimalTupleWriter(String filePath) throws FileNotFoundException{
		File file = new File(filePath);
		ps = new PrintStream(file);
	}
	
	/**
	 * write the tuple to a file
	 * @param tuple the tuple that will be saved
	 */
	@Override
	public void write(Tuple tuple) throws IOException {
		try {
			String str = tuple.toString() + '\n';
			ps.write(str.getBytes());
		} catch (IOException e) {
			System.out.println("Exception when dump the tuple");
			e.printStackTrace();
		}
		
	}
	/**
	 * close the file;
	 */
	@Override
	public void close() throws IOException {
		ps.close();
	}
	
}
