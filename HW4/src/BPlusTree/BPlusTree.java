package BPlusTree;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nio.*;
import utils.Catalog;
import utils.Tuple;

/**
 * This class is used for building an index tree for the given relation file.
 */
public class BPlusTree {

	private static final int bufferSize = 4096;
	private static final int intSize = 4;

	private String tableName;
	private int attrIndex;
	private int tuplePerPage;
	private int order;
	private TupleReader tr;
	private TreeSerializer ts;
	private SortedMap<Integer, List<Rid>> entryMap;

	private List<TreeNode> leafNodeLayer;
	private TreeNode root;

	public BPlusTree(String tableName, String indexPath, 
			int attrIndex, int order) {
		this.tableName = tableName;
		this.attrIndex = attrIndex;
		this.order = order;
		int attributeNum = Catalog.getSchema(tableName).size();
		this.tuplePerPage = (bufferSize-2*intSize)/(attributeNum*intSize);
		//		this.tr = Catalog.getBinaryTableReader(tableName);
		this.tr = Catalog.getDecimalTableReader(tableName);
		this.ts = new TreeSerializer(new File(indexPath));
		this.leafNodeLayer = new ArrayList<TreeNode>();

		String indexFolderPath = Catalog.dbPath + "indexes" + File.separator;
		String boatsIndexPath = indexFolderPath + "Sailors.A";
		File outputFile = new File(boatsIndexPath + "_humanReadable");
		try {
			PrintStream ps = new PrintStream(outputFile);

			setUpDataEntries();

			buildLeafNodeLayer();
			ps.write("---------Next layer is leaf nodes---------".getBytes());
			for (int i = 0; i < leafNodeLayer.size(); i++) {
				ps.write((leafNodeLayer.get(i).toString()).getBytes());
			}

			List<TreeNode> layer = buildIndexNodeLayer(leafNodeLayer);
			ps.write("---------Next layer is index nodes---------\n".getBytes());
			for (int i = 0; i < layer.size(); i++) {
				ps.write(layer.get(i).toString().getBytes());
			}
			while (layer.size() != 1) {
				layer = buildIndexNodeLayer(layer);
				ps.write("---------Next layer is index nodes---------\n".getBytes());
				for (int i = 0; i < layer.size(); i++) {
					ps.write(layer.get(i).toString().getBytes());
				}
			}
			ps.close();

			root = layer.get(0);
			ts.serialize(root);
			ts.finishSerialize(order);
			tr.close();
		} catch (IOException e) {
			System.err.println("Exception when closing the table reader during building the BPlusTree");
			e.printStackTrace();
		}
	}

	public void setUpDataEntries() {
		entryMap = new TreeMap<>();
		int cnt = 0;
		Tuple tuple;
		try {
			while ((tuple = tr.read()) != null) {
				int pageId = cnt / tuplePerPage;
				int tupleId = cnt % tuplePerPage;
				int key = tuple.getColumn().get(attrIndex);
				Rid rid = new Rid(pageId, tupleId);
				if (entryMap.containsKey(key)) {
					List<Rid> value = entryMap.get(key);
					value.add(rid);
				}
				else {
					List<Rid> ridList = new ArrayList<Rid>();
					ridList.add(rid);
					entryMap.put(key, ridList);
				}
				cnt++;
			}
		} catch (IOException e) {
			System.err.println("Exception when reading tuples "
					+ "during building the B Plus Tree");
			e.printStackTrace();
		}

	}

	public void buildLeafNodeLayer() {
		// generate the leaf layer list such that 
		// every leaf node gets 2d data entries
		int entryIndex = 0;
		List<DataEntry> entries = new ArrayList<DataEntry>();
		LeafNode leafNode = new LeafNode(entries);
		for (Integer key : entryMap.keySet()) {
			DataEntry dataEntry = new DataEntry(key, entryMap.get(key));
			if (entryIndex == 2 * order) {
				leafNode = new LeafNode(entries);
				leafNodeLayer.add(leafNode);
				entries = new ArrayList<DataEntry>();
				entryIndex = 0;
			}
			entries.add(dataEntry);
			entryIndex++;
		}

		// For small relation that has less than 2 * order entries
		if (leafNodeLayer.size() == 0) {
			leafNode = new LeafNode(entries);
			leafNodeLayer.add(leafNode);
			return;
		}

		// The last node is not underflow, add it to the leafNodeLayer
		if (entries.size() >= order) {
			leafNode = new LeafNode(entries);
			leafNodeLayer.add(leafNode);
		}

		// The last node is underflow, handle the last two nodes
		else if (entries.size() < order) {
			TreeNode secondToLastNode = 
					leafNodeLayer.remove(leafNodeLayer.size() - 1);
			List<DataEntry> secondToLastNodeEntry = secondToLastNode.getEntry();
			
			int half = (2 * order + entries.size()) / 2;

			// Construct the second to last leaf node
			List<DataEntry> secondEntry = new ArrayList<DataEntry>();
			for (int i = 0; i < half; i++) {
				secondEntry.add(secondToLastNodeEntry.get(i));
			}
			LeafNode secondNode = new LeafNode(secondEntry);
			leafNodeLayer.add(secondNode);

			// Construct the last leaf node
			List<DataEntry> lastEntry = new ArrayList<DataEntry>();
			for (int i = half; i < 2 * order; i++) {
				lastEntry.add(secondToLastNodeEntry.get(i));
			}
			lastEntry.addAll(entries);
			LeafNode lastNode = new LeafNode(lastEntry);
			leafNodeLayer.add(lastNode);
		}

	}

	public List<TreeNode> buildIndexNodeLayer(List<TreeNode> childLayer) {
		
		List<Integer> keys = new ArrayList<Integer>();
		List<Integer> address = new ArrayList<Integer>();
		List<TreeNode> children = new ArrayList<TreeNode>();
		List<TreeNode> curLayer = new ArrayList<TreeNode>();
		int nodeIndex = 0;
		// For small relation that has only one leaf node
		if (childLayer.size() == 1) {
			int addr = ts.serialize(childLayer.get(0));
			address.add(addr);
			children.add(childLayer.get(0));
			IndexNode node = new IndexNode(new ArrayList<Integer>(keys), 
					new ArrayList<Integer>(address), new ArrayList<TreeNode>(children));
			curLayer.add(node);
			return curLayer;
		}

		for (int i = 0; i < childLayer.size(); i++) {
			// Add the first child, no key
			if (nodeIndex == 0) {
				int addr = ts.serialize(childLayer.get(i));
				address.add(addr);
				children.add(childLayer.get(i));
				nodeIndex++;
			}
			// Add the middle children with keys
			else if (nodeIndex < 2 * order) {
				int addr = ts.serialize(childLayer.get(i));
				address.add(addr);
				int key = childLayer.get(i).getMinKey();
				keys.add(key);
				children.add(childLayer.get(i));
				nodeIndex++;
			}
			// Add the last child with the key, 
			// and handle underflow cases if necessary
			else if (nodeIndex == 2 * order) {
				int addr = ts.serialize(childLayer.get(i));
				address.add(addr);
				int key = childLayer.get(i).getMinKey();
				keys.add(key);
				children.add(childLayer.get(i));
				// Construct the current layer
				IndexNode node = new IndexNode(new ArrayList<Integer>(keys), 
						new ArrayList<Integer>(address), new ArrayList<TreeNode>(children));
				curLayer.add(node);
				// Reset all the components
				address.clear();
				keys.clear();
				children.clear();
				nodeIndex = 0;

				// Handle the underflow case 
				int childNum = childLayer.size() - i - 1;
				if (childNum > 2 * order + 1  &&  childNum < 3 * order + 2) {
					// Construct the second to last node
					int half = childNum / 2;
					for (int j = i + 1; j < i + 1 + half; j++) {
						if (j == i + 1) {
							addr = ts.serialize(childLayer.get(j));
							address.add(addr);
							children.add(childLayer.get(j));
							continue;
						}
						addr = ts.serialize(childLayer.get(j));
						address.add(addr);
						key = childLayer.get(j).getMinKey();
						keys.add(key);
						children.add(childLayer.get(j));
					}
					node = new IndexNode(new ArrayList<Integer>(keys), 
							new ArrayList<Integer>(address), new ArrayList<TreeNode>(children));
					curLayer.add(node);
					address.clear();
					keys.clear();
					children.clear();

					// Construct the last node
					for (int j = i + 1 + half; j < childLayer.size(); j++) {
						if (j == i + 1 + half) {
							addr = ts.serialize(childLayer.get(j));
							address.add(addr);
							children.add(childLayer.get(j));
							continue;
						}
						addr = ts.serialize(childLayer.get(j));
						address.add(addr);
						key = childLayer.get(j).getMinKey();
						keys.add(key);
						children.add(childLayer.get(j));
					}
					node = new IndexNode(new ArrayList<Integer>(keys), 
							new ArrayList<Integer>(address), new ArrayList<TreeNode>(children));
					curLayer.add(node);
					address.clear();
					keys.clear();
					children.clear();
					break;
				}
			}

		}
		if (keys.size() != 0) {
			IndexNode node = new IndexNode(new ArrayList<Integer>(keys), 
					new ArrayList<Integer>(address), new ArrayList<TreeNode>(children));
			curLayer.add(node);
		}
		return curLayer;

	}

}
