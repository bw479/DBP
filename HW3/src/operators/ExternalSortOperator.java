package operators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import nio.*;
import utils.Tuple;

public class ExternalSortOperator extends SortOperator{
	private static int fileGlobalCnt = 0;
	private TupleReader tr = null;
	private int tuplePerPage;
	private static final int bufferSize = 4096;
	private List<TupleReader> buffer;
	private int fileNum = 0;
	private int pageNum = 3;
	
	private String fileName (int fileNum, int fileCount) {
		return "temporaryFile" +fileGlobalCnt + "_" + fileNum + "_" + fileCount;
	}
	
	public ExternalSortOperator(Operator child, List<String> orderBy, int pageNum) {
		super(child, orderBy);
		fileGlobalCnt++;
		if (pageNum > 3) this.pageNum = pageNum;
		buffer = new ArrayList<TupleReader>(); 
		int attributeNum = child.getSchema().size();
		tuplePerPage = bufferSize/(attributeNum*4);
		Tuple nextTuple = child.getNextTuple();
		int fileCount = 0;
		while(true) {
			List<Tuple> onePage = new ArrayList<Tuple>();
			int i = 0;
			while (nextTuple != null && i < tuplePerPage) {
				onePage.add(nextTuple);
				i++;
				nextTuple = child.getNextTuple();
			}
			Collections.sort(onePage, new CompareTuple());
			BinaryTupleWriter tw;
			try {
				tw = new BinaryTupleWriter(fileName(fileNum, fileCount));
			} catch (FileNotFoundException e) {
				System.out.println("file NOT FOUND");
				tw = null;
			}
			for (Tuple t : onePage) {
				try {
					tw.write(t);
				} catch (IOException e) {
					System.out.println("IO not found");
				} 
				
			}
			try {
				tw.close();
				BinaryTupleReader page = new BinaryTupleReader(fileName(fileNum, fileCount));
				buffer.add(page);
			} catch (IOException e) {
				System.out.println("IO not found");
			}
			if (onePage.size() < tuplePerPage) {
				break;
			}
			fileCount++;
			
		}
		//fileNum++;	
		try {
			combine();
			tr = new BinaryTupleReader(fileName(fileNum, 0));
		} catch (IOException e) {
			System.out.println("IO not found");
		}
	}
	
	private void combine() throws IOException{
		while (buffer.size() > 1) {
			List<TupleReader> newBuffer = new ArrayList<TupleReader>();
			int fileCount = 0;
			fileNum++;
			for (int i = 0; i < buffer.size(); i = i + (pageNum-1)) {
				PriorityQueue<Tuple> pq = new PriorityQueue<Tuple>(new CompareTuple());
				
				for (int j = i; j < i+pageNum-1; j++) {
					if (j < buffer.size()) {
						TupleReader r = buffer.get(j);
						Tuple next = r.read();
						next.setTupleReader(r);
						pq.add(next);
					} else {
						break;
					}
				}
				
				BinaryTupleWriter tw = new BinaryTupleWriter(fileName(fileNum, fileCount));
				while (!pq.isEmpty()) {
					Tuple added = pq.poll();
					TupleReader r = added.getTr();
					Tuple temp = r.read();
					if (temp != null) {
						temp.setTupleReader(r);
						pq.add(temp);
					}
					tw.write(added);
				}
				tw.close();
				BinaryTupleReader newTr = new BinaryTupleReader(fileName(fileNum, fileCount));
				newBuffer.add(newTr);
				fileCount++;
			}
			for (int i = 0; i < buffer.size(); i++) {
				File f = new File(fileName(fileNum-1, i));
				f.delete();
			}
			buffer = newBuffer;
		}
	}

	public Tuple getNextTuple() {
		if (tr == null) {
			return null;
		}
		try {
			return tr.read();
		} catch (IOException e) {
			System.out.println("Nothing to read");
			return null;
		}
	}
	
	@Override
	public void reset() {
		try {
			tr.reset();
		} catch (IOException e) {
			System.out.println("Nothing to reset");
		}
	}
	
	@Override
	public void reset(int index) {
		try {
			tr.reset(index);
		} catch (IOException e) {
			System.out.println("Nothing to reset");
		}
	}
}
