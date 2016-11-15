package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import BPlusTree.BPlusTree;
import logicOperators.LogicOperator;
import logicOperators.LogicScanOperator;
import logicOperators.LogicSortOperator;
import nio.BinaryTupleWriter;
import nio.DecimalTupleWriter;
import operators.Operator;
import visitors.PhysicalPlanBuilder;

public class IndexBuilder {

	public static void indexBuild(boolean humanreable) {

		String indexFolderPath = Catalog.dbPath + "indexes" + File.separator;
		
		File file = new File(indexFolderPath);

		if (file.exists()) file.delete();
		file.mkdir();

		for (String tableName : Catalog.indexInfo.keySet()) {
			String attr = Catalog.indexInfo.get(tableName).attr;
			int attrIndex = Catalog.getTable(tableName).getSchema().indexOf(tableName + "." + attr);
			int order = Catalog.indexInfo.get(tableName).order;
			boolean isClustered = Catalog.indexInfo.get(tableName).isClustered;
			String tablePath = Catalog.dataPath + tableName;
			String indexPath = indexFolderPath + tableName + "." + attr;

			if (isClustered) {
				LogicOperator op = new LogicScanOperator(Catalog.getTable(tableName));

				String colName = tableName + "." + attr;
				List<String> orderBy = new ArrayList<String>();
				orderBy.add(colName);
				op = new LogicSortOperator(op, orderBy);

				PhysicalPlanBuilder phyPlanBuilder = new PhysicalPlanBuilder();
				op.accept(phyPlanBuilder);
				Operator root = phyPlanBuilder.getPhyPlanOpt();
				try {
					BinaryTupleWriter tw = new BinaryTupleWriter(tablePath);
					root.dump(tw);
					tw.close();
				} catch (IOException e) {
					System.err.println("Exception occurred during building index");
					e.printStackTrace();
				}

			}
			BPlusTree bPlusTree = new BPlusTree(tableName, indexPath, attrIndex, order);
			
		}
	}
}
