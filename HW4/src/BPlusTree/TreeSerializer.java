package BPlusTree;

import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel;
import java.util.*;

public class TreeSerializer {
	private final static int bufferSize = 4096;
	private final static int INT_SIZE = 4;
	private File file;
	private FileChannel fc;
	private ByteBuffer buffer;
	private int pageNum;
	private FileOutputStream output;
	private int leafNum;
	
	
	public TreeSerializer(File indexFile) {
		try {
			file = indexFile;
			output = new FileOutputStream(file);
			fc = output.getChannel();
			pageNum = 1;
			buffer = ByteBuffer.allocate(bufferSize);
			
		} catch (FileNotFoundException e) {
			System.err.println("TreeSerializer constructor 1 : File Not Found");
		}
		
	}
	
	public int serialize(TreeNode node) {
		if (node instanceof LeafNode) {
			 return serialize((LeafNode)node);
		} else {
			return serialize((IndexNode)node);
		}
	}
	
	public int serialize(LeafNode node) {
		buffer.putInt(0);
		buffer.putInt(node.getEntry().size());
		
		for (DataEntry de : node.getEntry()) {
			List<Rid> ridList = de.getRids(); 
			buffer.putInt(de.getKey());
			buffer.putInt(ridList.size());
			for (Rid r : ridList) {
				buffer.putInt(r.pageId);
				buffer.putInt(r.tupleId);
			}
		}
		writeToFile();
		leafNum++;
		pageNum++;
		return pageNum-1;
	}
	
	public int serialize(IndexNode node) {
		buffer.putInt(1);
		List<Integer> keysList = node.getKeys();
		buffer.putInt(keysList.size());
		for (Integer i : keysList) {
			buffer.putInt(i);
		}
		List<Integer> childrenAddr = node.getAdresses();
		for (Integer i : childrenAddr) {
			buffer.putInt(i);
		}
		writeToFile();
		pageNum++;
		return pageNum-1;
		
	}
	
	
	private void writeToFile() {
		try {
			long location = (long)pageNum*bufferSize;
			fc.position(location);
			while (buffer.hasRemaining()) {
				buffer.putInt(0);
			}
			buffer.flip();
			fc.write(buffer);
			clearBuffer();
		} catch (IOException e) {
			System.err.println("TreeSerializer writeToFile 1 : IO Not Found");
		}
		
		
	}
	
	private void clearBuffer() {
		buffer.clear();
		buffer.put(new byte[bufferSize]);
		buffer.clear();
	}
	
	public void finishSerialize(int order) {
		try {
			buffer.putInt(pageNum-1);
			buffer.putInt(leafNum);
			buffer.putInt(order);
			pageNum = 0;
			writeToFile();
			fc.close();
			output.close();
		} catch (IOException e) {
			System.err.println("TreeSerializer finishSerialize 1 : IO Not Found");
		}
	}
}
