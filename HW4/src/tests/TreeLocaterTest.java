package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import BPlusTree.DataEntry;
import BPlusTree.IndexNode;
import BPlusTree.LeafNode;
import BPlusTree.Rid;
import BPlusTree.TreeLocater;
import BPlusTree.TreeNode;
import BPlusTree.TreeSerializer;

public class TreeLocaterTest {

	@Test
	public void test() {
		File file = new File("LocatorTestResult");
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
		keys.add(3);
		List<TreeNode> children = new ArrayList<TreeNode>();
		children.add(lf);
		List<Integer> address = new ArrayList<Integer>();
		address.add(1);
		
		//IndexNode iif = new IndexNode(keys, address);
		
		ts.serialize(lf);
		//ts.serialize(iif);
		ts.finishSerialize(1);
		
		
		File file1 = new File("Boats.D");
		TreeLocater tl = new TreeLocater(file1, 5, null);
		Rid next;
		while((next = tl.getNextRid()) != null) {
			//System.out.println("" + next.pageId + " " + next.tupleId);
		}
		
		
	}

}
