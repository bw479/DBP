package nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import utils.Catalog;
import utils.Tuple;

/**
 * Writer for binary tuple files
 *
 */
public class BinaryTupleWriter implements TupleWriter {
	private int attributeNum;
	private int tupleNum;
	private FileChannel channel;
	private ByteBuffer buffer;
	private static final int bufferSize = 4096;
	private static final int intSize = 4;
	private boolean firstSetUp;
	private int maxTupleNum;
	private FileOutputStream output;
	
	/**
	 * Create a writer for binary tuples
	 * @param filePath save the tuple to this file path
	 * @throws FileNotFoundException
	 */
	public BinaryTupleWriter(String filePath) throws FileNotFoundException{
		buffer = ByteBuffer.allocate(bufferSize);
		File file = new File(filePath);
		output = new FileOutputStream(file);
		channel = output.getChannel();
		firstSetUp = true;
		maxTupleNum = 0;
		attributeNum = 0;
		tupleNum = 0;

	}

	/**
	 * write the tuple to a file
	 * @param tuple the tuple that will be saved
	 */
	@Override
	public void write(Tuple tuple) throws IOException {
		if (firstSetUp) {
			attributeNum = tuple.getColumn().size();
			buffer.putInt(attributeNum);
			buffer.putInt(0);
			maxTupleNum = (bufferSize-2*intSize)/(attributeNum*intSize);
			firstSetUp = false;
		}

		tupleNum++;
		for (Integer i : tuple.getColumn()) {
			int added = i;
			buffer.putInt(added);

		}
		if (tupleNum >= maxTupleNum) {
			buffer.putInt(intSize, tupleNum);
			tupleNum = 0;
			addZero();
			buffer.flip();
			channel.write(buffer);
			buffer.clear();
			buffer.put(new byte[bufferSize]);
			buffer.clear();
			firstSetUp = true;
		}


	}
	/**
	 * add zero the the end of page
	 */
	private void addZero() {
		while (buffer.hasRemaining()) {
			buffer.putInt(0);
		}
	}
	/**
	 * save the last page and close the file
	 */
	@Override
	public void close() throws IOException {
		if (tupleNum > 0) {
			addZero();
			buffer.putInt(intSize, tupleNum);
			buffer.flip();
			channel.write(buffer);
		}
		buffer.clear();
		channel.close();
		output.close();

	}

}
