package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import BPlusTree.DataEntry;
import BPlusTree.IndexNode;
import BPlusTree.LeafNode;
import BPlusTree.Rid;
import BPlusTree.TreeNode;
import BPlusTree.TreeSerializer;

public class TreeSerializerTest {

	@Test
	public void test() {
		try {
		File file = new File("serilazerTestResult");
		TreeSerializer ts = new TreeSerializer(file);
		Rid r1 = new Rid(100,1);
		Rid r2 = new Rid(101,200);
		
		Rid r3 = new Rid(200,1);
		Rid r4 = new Rid(201,200);
		Rid r5 = new Rid(300,4);
		
		ArrayList<Rid> l1 = new ArrayList<Rid>();
		ArrayList<Rid> l2 = new ArrayList<Rid>();
		
		l1.add(r1);
		l1.add(r2);
		l2.add(r3);
		l2.add(r4);
		l2.add(r5);
		
		DataEntry d1 = new DataEntry(1, l1);
		DataEntry d2 = new DataEntry(2, l2);
		
		List<DataEntry> de = new ArrayList<DataEntry>();
		de.add(d1);
		de.add(d2);
		
		LeafNode lf = new LeafNode(de);
		
		List<Integer> keys = new ArrayList<Integer>();
		keys.add(1);
		List<TreeNode> children = new ArrayList<TreeNode>();
		children.add(lf);
		List<Integer> address = new ArrayList<Integer>();
		address.add(1);
		
//		IndexNode iif = new IndexNode(keys, address);
		
		ts.serialize(lf);
//		ts.serialize(iif);
		ts.finishSerialize(1);
		
		ByteBuffer bf = ByteBuffer.allocate(4096);
		FileInputStream input = new FileInputStream(file);
		FileChannel channel = input.getChannel();
		int more;
		int count = 0;
		while ((more = channel.read(bf)) >= 0) {
			bf.flip();
			while(bf.hasRemaining()) {
				System.out.println(count + ": " + bf.getInt() + " ");
				count++;
			}
			clearBuffer(bf);
		}
		
		
		
		
		} catch (FileNotFoundException e){
			System.out.println("Test: file not found");
		} catch (IOException e) {
			System.out.println("Test: IO not found");
		}
		
		
		
		
		
	}
	private void clearBuffer(ByteBuffer buffer) {
		buffer.clear();
		buffer.put(new byte[4096]);
		buffer.clear();
	}

}
