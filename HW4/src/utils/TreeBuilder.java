package utils;

import java.util.*;
import java.util.Map.Entry;

import logicOperators.LogicDuplicateEliminationOperator;
import logicOperators.LogicJoinOperator;
import logicOperators.LogicOperator;
import logicOperators.LogicProjectOperator;
import logicOperators.LogicScanOperator;
import logicOperators.LogicSelectOperator;
import logicOperators.LogicSortOperator;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import operators.Operator;
import visitors.PhysicalPlanBuilder;

/**
 * This class will build a query tree from the bottom to the top. 
 * For each selectOperator and JoinOperator, the related conditions 
 * will be added.
 *
 */

public class TreeBuilder {
	public Operator root;
	
	public List<SelectItem> project;
	public List<String> orderBy = new ArrayList<>();
	public List<OrderByElement> order;
	public Distinct distinct;

	public List<String> fromItems = new ArrayList<String>();
	public List<Join> joinItems;
	
	public Map<String, String> alias = Catalog.alias;
	public Map<String, String> selfJoinMap = Catalog.selfJoinMap;
	public Map<String, List<Expression>> selectConds = new HashMap<>();
	public Map<String, List<Expression>> joinConds = new HashMap<>();;
	public Map<String, Expression> joinExpMap;
	public Map<String, Expression> selectExpMap;
	
	/**
	 * Extract the original table name from the given FromItem object.
	 * @param item a FromItem object which contains both table name and its alias
	 * @return the original table name
	 */
	public String getTableName(FromItem item) {
		return item.toString().split(" ")[0];
	}
	
	/**
	 * Add table names to the fromItems list. If the table has an alias,
	 * add the alias to the list, else add the table name. Also, generate
	 * the alias map.
	 * @param item
	 */
	public void fromAdd(FromItem item) {
		String tbl = getTableName(item);
		String als = item.getAlias();
		if (als != null) {
			for (Entry<String, String> entry : alias.entrySet()) {
				String a = entry.getKey();
		        String b = entry.getValue();
		        if (b.equals(tbl)) {
		        	selfJoinMap.put(als, tbl);
		        	selfJoinMap.put(a, b);
		        }
			}
			fromItems.add(als);
			alias.put(als, tbl);
		}
		else {
			fromItems.add(tbl);
		}
	}
	
	/**
	 * Construct the orderBy list in the string fromat from the list of orderby elements.
	 * @param order list of orderby elements
	 * @return list of orderBy elements, each object is in the String format
	 */
	public List<String> genOrderBy(List<OrderByElement> order) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < order.size(); i++) {
			list.add(order.get(i).toString());
		}
		return list;
	}
	
	/**
	 * For a given expression, extract all the relevant tables to a hashset
	 * @param exp an expression
	 * @return a set of relevant tables
	 */
	public List<String> getTableList(Expression exp) {
		List<String> list = new ArrayList<String>();
		if (!(exp instanceof BinaryExpression)) return list;
		BinaryExpression biExp = (BinaryExpression)exp;
		Expression left = biExp.getLeftExpression();
		Expression right = biExp.getRightExpression();
		if (left instanceof Column) {
			Column l = (Column) left;
			if (l.getTable() != null)
				list.add(l.getTable().toString());
		}
		if (right instanceof Column) {
			Column l = (Column) right;
			if (!list.contains(l.getTable().getName()) && l.getTable() != null)
				list.add(l.getTable().toString());
		}
		return list;
	}
	
	/**
	 * For all the tables in tableList, get the last table name in the 
	 * FROM clause
	 * @param tableList a list of tables
	 * @return the name of the last table
	 */
	public String getLastTable(List<String> tableList) {
		for (int i = fromItems.size() - 1; i >= 0; i--) {
			String table = fromItems.get(i);
			if (tableList.contains(table) || tableList.contains(Catalog.alias.get(table))) return table;
		}
		return null;
	}
	/**
	 * Add expression exp to a certain condition list of the relevant table
	 * @param table the table name that should receive the expression
	 * @param conds a certain condition list, could be selectConds or joinConds
	 */
	public void updateCondList(String table, Expression exp, 
									Map<String, List<Expression>> conds) {
		// if the table name is the actual table name, and not included in
		// the fromItems list, need to find its alias from the alias map
		if (!fromItems.contains(table)) {
			for(String a: alias.keySet()) {
			    if(alias.get(a).equals(table)) {
			        table = a;
			        break;
			    }
			}
		}
		List<Expression> tableCondList = conds.get(table);
		tableCondList.add(exp);
	}
	
	/**
	 * Compose a new expression for each table from the given condition list,
	 * then generate a map such that key is the table, value is the new expression
	 * @param condList 
	 * @param expMap
	 */
	public Map<String, Expression> reGenExpMap(Map<String, List<Expression>> condMap) {
		Map<String, Expression> expMap = new HashMap<String, Expression>();
		for (Entry<String, List<Expression>> entry : condMap.entrySet()) {
			String table = entry.getKey();
	        List<Expression> conds = entry.getValue();
	        Expression newExp;
			if (conds.size() == 0) newExp = null;
			else if (conds.size() == 1) newExp = conds.get(0);
			else {
				newExp = conds.get(0);
				for (int i = 1; i < conds.size(); i++) {
					newExp = new AndExpression(newExp, conds.get(i));
				}
			}
			expMap.put(table, newExp);
	    }
		return expMap;
	}
	
	/**
	 * Constructor that decomposes the SQL statement and 
	 * set up the select condition, join condition, and the alias map.
	 * @param statement the SQL statement from jsqlparser
	 */
	public TreeBuilder(Statement statement) {
		SelectBody select = ((Select) statement).getSelectBody();
		PlainSelect ps = (PlainSelect) select;
		
		order = ps.getOrderByElements();
		
		distinct = ps.getDistinct();
		
		project = ps.getSelectItems();
		
		FromItem firstFrom = ps.getFromItem();
		fromAdd(firstFrom);
		joinItems = ps.getJoins();
		if (joinItems != null) {
			for (Join j : joinItems) {
				fromAdd(j.getRightItem());
			}
		}
		
		for (String tableName : fromItems) {
			selectConds.put(tableName, new ArrayList<Expression>());
			joinConds.put(tableName, new ArrayList<Expression>());
		}
		
		Expression ands = ps.getWhere();
		Expression rightExp = null;
		
		// Extract expressions from WHERE clause and assign each expression
		// to an appropriate table
		while (ands instanceof AndExpression) {
			AndExpression and = (AndExpression) ands;
			rightExp = and.getRightExpression();
			List<String> tableList = getTableList(rightExp);
			switch (tableList.size()) {
				case 0: String lastTable = fromItems.get(fromItems.size() - 1);
						updateCondList(lastTable, rightExp, selectConds);
						break;
				case 1: String tableName = tableList.get(0);
						updateCondList(tableName, rightExp, selectConds);
						break;
				case 2: tableName = getLastTable(tableList);
						updateCondList(tableName, rightExp, joinConds);
						break;
			}
			
			ands = and.getLeftExpression();
			
		}
		
		List<String> tableList = getTableList(ands);
		switch (tableList.size()) {
			case 0: String lastTable = fromItems.get(fromItems.size() - 1);
					updateCondList(lastTable, ands, selectConds);
					break;
			case 1: String tableName = tableList.get(0);
					updateCondList(tableName, ands, selectConds);
					break;
			case 2: tableName = getLastTable(tableList);
					updateCondList(tableName, ands, joinConds);
					break;
		}
		
		selectExpMap = reGenExpMap(selectConds);
		joinExpMap = reGenExpMap(joinConds);

		
		
		root = builder();
		
	}
	
	/**
	 * Build a tree for the whole query plan according to 
	 * relevant conditions and data.
	 */
	public Operator builder() {
		String table = fromItems.get(0);
		LogicOperator current = new LogicScanOperator(Catalog.getTable(table));
		if (selectExpMap.containsKey(table) && selectExpMap.get(table) != null) {
			current = new LogicSelectOperator(current, selectExpMap.get(table));
		}
		for (int i = 1; i < fromItems.size(); i++) {
			
			table = fromItems.get(i);
			LogicOperator opt = new LogicScanOperator(Catalog.getTable(table));
			if (selectExpMap.containsKey(table) && selectExpMap.get(table) != null) {
				opt = new LogicSelectOperator(opt, selectExpMap.get(table));
			}
			current = new LogicJoinOperator(current, opt, joinExpMap.get(table));
		}
		current = new LogicProjectOperator(current, project);
		
		
		if (distinct != null) {
			if (order == null) {
				orderBy = new ArrayList<String>();
			}
			else {
				orderBy = genOrderBy(order);
			}
			current = new LogicSortOperator(current, orderBy);
			current = new LogicDuplicateEliminationOperator(current);
		}
		else {
			if (order != null) {
				orderBy = genOrderBy(order);
				current = new LogicSortOperator(current, orderBy);
			}
		}
		
		PhysicalPlanBuilder phyPlanBuilder = new PhysicalPlanBuilder();
		current.accept(phyPlanBuilder);
		return phyPlanBuilder.getPhyPlanOpt();
		
	}
	
}
