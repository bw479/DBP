package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;

import BPlusTree.BPlusTree;
import utils.Catalog;
import utils.IndexBuilder;

public class BPlusTreeTest {

	Catalog catalog = new Catalog();

	@Test
	public void test() {

		try {
			
			ByteBuffer bf = ByteBuffer.allocate(4096);
			String indexFolderPath = Catalog.dbPath + "indexes" + File.separator;
			String sailorsIndexPath = indexFolderPath + "Sailors.A";
			BPlusTree bPlusTree = new BPlusTree("Sailors", sailorsIndexPath, 1, 15);
			
			File file = new File(sailorsIndexPath);
			FileInputStream input = new FileInputStream(file);
			FileChannel channel = input.getChannel();
			int more;
//			File outputFile = new File(boatsIndexPath + "_humanReadable");
//			PrintStream ps = new PrintStream(outputFile);
			
			while ((more = channel.read(bf)) >= 0) {
				bf.flip();
				while(bf.hasRemaining()) {
					String tmp = bf.getInt() + "\n";
//					ps.write(tmp.getBytes());
				}
				clearBuffer(bf);
			}
		} catch (IOException e) {
			System.out.println("Exception");
			e.printStackTrace();
		}
		
		

	}


	private void clearBuffer(ByteBuffer buffer) {
		buffer.clear();
		buffer.put(new byte[4096]);
		buffer.clear();
	}

}
