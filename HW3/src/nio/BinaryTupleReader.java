package nio;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import utils.Tuple;

/**
 * Reader for binary tuple file
 */
public class BinaryTupleReader implements TupleReader{
	private File file;
	private static final int bufferSize = 4096;
	private int attributeNum;
	private int tupleNum;
	private static final int intSize = 4;
	private boolean pageNeeded;
	private boolean endOfFile;
	private ByteBuffer buffer;
	private FileChannel channel;
	private FileInputStream input;

	/**
	 * creates a binary tuple reader that can extract tuple from binary files
	 * @param tablePath file path for the tuple file
	 * @throws FileNotFoundException
	 */
	public BinaryTupleReader(String tablePath) throws FileNotFoundException{
		this.file = new File(tablePath);
		this.attributeNum = 0;
		this.tupleNum = 0;
		this.pageNeeded = true;
		this.endOfFile = false;
		buffer = ByteBuffer.allocate(bufferSize);
		input = new FileInputStream(file);
		channel = input.getChannel();
	}
	
	/** get the next available tuple 
	 */
	@Override
	public Tuple read() throws IOException{
		while (!endOfFile) {
			if (pageNeeded) {
				getPage();
				if (endOfFile) {
					return null;
				}
			}
			if (buffer.hasRemaining()) {
				List<Integer> l = new ArrayList<Integer>();
				for (int i = 0; i < attributeNum; i++) {
					l.add(buffer.getInt());
				}
				return new Tuple(l);
			}
			//Get the new page after getting the last tuple in the page
			clearBuffer();
			getPage();
		}
		
		return null;
	}
	/**
	 * Get a new page from the file
	 * @throws IOException
	 */
	private void getPage() throws IOException{
		pageNeeded = false;
		int more = channel.read(buffer);
		if (more < 0) {
			endOfFile = true;
			return;
		}
		buffer.flip();
		attributeNum = buffer.getInt();
		tupleNum = buffer.getInt();
		buffer.limit((2 + attributeNum * tupleNum) * intSize);
	}
	/**
	 * clear the buffer
	 */
	private void clearBuffer() {
		buffer.clear();
		buffer.put(new byte[bufferSize]);
		buffer.clear();
	}
	/**
	 * reset the table back to the first tuple
	 */
	@Override
	public void reset() throws IOException {
		channel.close();
		this.attributeNum = 0;
		this.tupleNum = 0;
		this.pageNeeded = true;
		this.endOfFile = false;
		buffer = ByteBuffer.allocate(bufferSize);
		input = new FileInputStream(file);
		channel = input.getChannel();
		
		
	}
	
	public void reset(int index) {
		try {
			int tuplePerPage = (bufferSize-2*intSize)/(attributeNum*intSize);
			int pages = index/tuplePerPage;
			int overflow = index%tuplePerPage;
			//int bytePerPage = (2 + attributeNum * tuplePerPage) * intSize;
			int overflowByte = overflow * attributeNum * intSize;
			long byteIndex = bufferSize * pages;
			channel.close();
			this.pageNeeded = true;
			this.endOfFile = false;
			input = new FileInputStream(file);
			channel = input.getChannel();
			channel.position(byteIndex);
			//reset();
			
			clearBuffer();
			getPage();
			buffer.position(overflowByte+2*intSize);
			
		} catch (IOException e) {
			System.out.println("IO not found");
		}
		
		
		
	}
	/**
	 * close the file
	 */
	@Override
	public void close() throws IOException {
		input.close();
		channel.close();
	}
}
