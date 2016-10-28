package visitors;

import java.util.ArrayList;
import java.util.List;

import logicOperators.*;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import operators.*;
import utils.Catalog;

/**
 * The visitor class to build a physical query tree by 
 * the given logic query tree
 *
 */

public class PhysicalPlanBuilder {
	private Operator cur;

	/**
	 * Get the physical query plan tree root
	 * @return
	 */
	public Operator getPhyPlanOpt() {
		return cur;
	}

	/**
	 * Iteratively visit the child of the logic unary operator and 
	 * assign the cur operator.
	 * @param opt the logic unary operator
	 */
	public void descent(LogicUnaryOperator opt) {
		opt.child.accept(this);
	}

	/**
	 * Visit the logic duplicate elimination operator, assign the cur operator
	 * with its child and create a new DuplicateEliminationOperator node.
	 * @param opt the LogicDuplicateEliminationOperator operator
	 */
	public void visit(LogicDuplicateEliminationOperator opt) {
		descent(opt);
		cur = new DuplicateEliminationOperator(cur);
	}

	/**
	 * Visit the logic project operator, assign the cur operator with its child 
	 * and create a new ProjectOperator node.
	 * @param opt the LogicProjectOperator operator
	 */
	public void visit(LogicProjectOperator opt) {
		descent(opt);
		cur = new ProjectOperator(cur, opt.selectList);
	}

	/**
	 * Visit the logic select operator, assign the cur operator with 
	 * its child and create a new SelectOperator node.
	 * @param opt the LogicSelectOperator operator
	 */
	public void visit(LogicSelectOperator opt) {
		descent(opt);
		cur = new SelectOperator(cur, opt.ex);
	}

	/**
	 * Visit the logic scan operator, assign the cur operator and 
	 * create a new ScanOperator node.
	 * @param opt the LogicScanOperator operator
	 */
	public void visit(LogicScanOperator opt) {
		cur = new ScanOperator(opt.table);
	}

	/**
	 * Visit the logic sort operator, assign the cur operator with its child and 
	 * create a new SortOperator node.
	 * @param opt the LogicSortOperator operator
	 */
	public void visit(LogicSortOperator opt) {
		descent(opt);
		if (Catalog.sortMethod.equals("INMEM"))
			cur = new InMemSortOperator(cur, opt.orderBy);
		else if (Catalog.sortMethod.equals("EX"))
			cur = new ExternalSortOperator(cur, opt.orderBy, Catalog.sortBufNum);
	}

	/**
	 * Visit the logic join operator, assign the cur operator with 
	 * two children and create a new SortOperator node.
	 * @param opt the LogicSortOperator operator
	 */
	public void visit(LogicJoinOperator opt) {
		opt.left.accept(this);
		Operator leftChild = cur;

		opt.right.accept(this);
		Operator rightChild = cur;
		if (Catalog.joinMethod.equals("TNLJ"))
			cur = new TupleJoinOperator(leftChild, rightChild, opt.exp);
		else if (Catalog.joinMethod.equals("BNLJ"))
			cur = new BlockJoinOperator(leftChild, rightChild,
					opt.exp, Catalog.joinBufNum);
		else if (Catalog.joinMethod.equals("SMJ")) {
			List<String> leftOrder = new ArrayList<String>();
			List<String> rightOrder = new ArrayList<String>();
			Expression exp = genSortOrder(leftChild, rightChild, opt.exp,
					leftOrder, rightOrder);
			cur = new SortMergeJoinOperator(leftChild, rightChild, 
					exp, leftOrder, rightOrder);
		}
	}

	/**
	 * Extract the sort order of the left child and right child according to 
	 * the join expression.
	 * @param leftChild the left order of the join operator
	 * @param rightChild the left order of the join operator
	 * @param exp the join expression of the join operator
	 * @param leftOrder the new generated order of the left child
	 * @param rightOrder the new generated order of the right child
	 * @return the new generated expression with the expressions related with 
	 * two tables put first
	 */
	public Expression genSortOrder(Operator leftChild, Operator rightChild, 
			Expression exp, List<String> leftOrder, List<String> rightOrder) {
		List<Expression> expList = new ArrayList<Expression>();
		if (!(exp instanceof AndExpression)) expList.add(exp);
		else {
			Expression rightExp = null;
			while (exp instanceof AndExpression) {
				AndExpression and = (AndExpression) exp;
				rightExp = and.getRightExpression();
				expList.add(rightExp);
				exp = and.getLeftExpression();
			}
			expList.add(exp);
		}
		
		List<Expression> doubleCondList = new ArrayList<Expression>();
		List<Expression> singleCondList = new ArrayList<Expression>();
		for (Expression expression : expList) {
			if (!(expression instanceof EqualsTo))
				continue;
			EqualsTo equal = (EqualsTo) expression;
			String leftCol = equal.getLeftExpression().toString();
			String rightCol = equal.getRightExpression().toString();
			
			boolean lContainLeftCol = leftChild.getSchema().contains(leftCol);
			boolean lContainRightCol = leftChild.getSchema().contains(rightCol);
			boolean rContainLeftCol = leftChild.getSchema().contains(leftCol);
			boolean rContainRightCol = rightChild.getSchema().contains(rightCol);
			
			if (!(lContainLeftCol && rContainRightCol  ||  
					lContainRightCol && rContainLeftCol)) {
				singleCondList.add(expression);
				continue;
			}
			
			doubleCondList.add(expression);
			if (lContainLeftCol && rContainRightCol) {
				leftOrder.add(leftCol);
				rightOrder.add(rightCol);
			}
			else if (lContainRightCol && rContainLeftCol) {
				leftOrder.add(rightCol);
				rightOrder.add(leftCol);
			}
		}
		doubleCondList.addAll(singleCondList);
		
		return genExpression(doubleCondList);
	}
	
	/**
	 * Generate a new expression or AndExpression by the given expression list
	 * @param list the expression list
	 * @return the new expression or AndExpression
	 */
	public Expression genExpression(List<Expression> list) {
		Expression exp = list.get(0);
		if (list.size() == 1) return exp;
		for (int i = 1; i < list.size(); i++) {
			exp = new AndExpression(exp, list.get(i));
		}
		return exp;
	}
}
