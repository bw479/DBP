package nio;

import java.io.IOException;

import utils.Tuple;
/**
 * Interface for reader of tuple files.
 */
public interface TupleReader {
	/**
	 * read the next available tuple.
	 * @return tuple available tuple
	 * @throws IOException
	 */
	public Tuple read() throws IOException;
	
	/**
	 * reset back to the first tuple
	 * @throws IOException
	 */
	public void reset() throws IOException;
	
	/**
	 * close the file
	 * @throws IOException
	 */
	public void close() throws IOException;
}

