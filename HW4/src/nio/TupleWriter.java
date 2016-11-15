package nio;

import java.io.IOException;

import utils.Tuple;
/**
 * Interface for the writer of tuple files.
 *
 */
public interface TupleWriter {
	/**
	 * write the tuple to a file
	 * @param tuple the tuple to be written
	 * @throws IOException
	 */
	public void write(Tuple tuple) throws IOException;
	
	/**
	 * close the file.
	 * @throws IOException
	 */
	public void close() throws IOException;
}
