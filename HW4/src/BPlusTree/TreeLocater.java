package BPlusTree;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.nio.*;

public class TreeLocater {
	private final static int bufferSize = 4096;
	private final static int INT_SIZE = 4;
	private File file;
	private FileChannel fc;
	private FileInputStream output;
	private ByteBuffer buffer;
	private Integer lowKey;
	private Integer highKey;
	private int remainingRid; //the number of remaining rid in current dataentry
	private int remainingEntry;//the number of remaining dataentry in current leaf node
	private int currentPageNum;
	private boolean hasEnd;
	//private int availableLeaf;
	public TreeLocater(File file, Integer lowKey, Integer highKey) {
		try {
			this.file = file;
			buffer = ByteBuffer.allocate(bufferSize);
			output = new FileInputStream(file);
			fc = output.getChannel();
			hasEnd = false;
			this.lowKey = lowKey;
			if (lowKey == null) {
				fetchPage(1);
				buffer.getInt();
				buffer.getInt();
				this.lowKey = buffer.getInt();
				System.out.println(lowKey);
			}
			this.highKey = highKey;
			locateTheFirstEntry();
		} catch (FileNotFoundException e) {
			System.out.println("TreeLocator constructor 1 : File Not Found");
		} catch (Exception e) {
			System.out.println("TreeLocator constructor 2 : out range");
		}
		
	}
	
	private void locateTheFirstEntry() throws Exception{
		fetchPage(0);
		int rootLocation = buffer.getInt();
		currentPageNum = rootLocation;
		//availableLeaf = buffer.getInt();
		fetchPage(rootLocation);
		int flag = buffer.getInt();
		while (flag != 0) {
			int keyNum = buffer.getInt();
			List<Integer> l = new ArrayList<Integer>();
			for (int i = 0; i < keyNum; i++) {
				l.add(buffer.getInt());
			}
			currentPageNum = buffer.getInt();
			if (lowKey >= l.get(0)) {
				 for (int i = 0; i < l.size(); i++) {
					 currentPageNum = buffer.getInt();
					 if ((i+1 == l.size()) || (lowKey >= l.get(i) && lowKey < l.get(i+1))) {
						 break;
					 }
				 }
			}
			fetchPage(currentPageNum);
			flag = buffer.getInt();
		}
		remainingEntry = buffer.getInt();
		int keyValue = buffer.getInt();
		System.out.println(keyValue);
		remainingEntry--;
		while (keyValue != lowKey) {
			System.out.println(lowKey);
			if (remainingEntry <= 0) {
				throw new Exception("no more available data entry!!");
			}
			int ridNum = buffer.getInt();
			for (int i = 0; i < ridNum; i++) {
				buffer.getInt();
				buffer.getInt();
			}
			keyValue = buffer.getInt();
			remainingEntry--;
		}
		remainingRid = buffer.getInt();
		
		
	}
	
	public Rid getNextRid() {
		if (hasEnd) {
			return null;
		}
		if (remainingRid <= 0) {
			if (remainingEntry <= 0) {
				currentPageNum++;
				fetchPage(currentPageNum);
				int flag = buffer.getInt();
				if (flag == 1) {
					hasEnd = true;
					return null;
				}
				remainingEntry = buffer.getInt();
			}
			int curKey = buffer.getInt();
			remainingEntry--;
			if (highKey != null && curKey > highKey) {
				hasEnd = true;
				return null;
			}
			remainingRid = buffer.getInt();
			return getNextRid();
		}
		Rid nextRid = new Rid(buffer.getInt(), buffer.getInt());
		remainingRid--;
		return nextRid;
	}
	
	
	private void fetchPage(int pageNum) {
		try {
			clearBuffer();
			long location = (long)pageNum*bufferSize;
			fc.position(location);
			fc.read(buffer);
			buffer.flip();
		} catch (IOException e) {
			System.out.println("TreeLocator fetchPage 1 : IO Not Found");
		}
		
	}
	
	private void clearBuffer() {
		buffer.clear();
		buffer.put(new byte[bufferSize]);
		buffer.clear();
	}
	
}
