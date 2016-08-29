package cs4321.project1;

import cs4321.project1.tree.DivisionTreeNode;
import cs4321.project1.tree.LeafTreeNode;
import cs4321.project1.tree.SubtractionTreeNode;

import java.util.Stack;

import cs4321.project1.tree.AdditionTreeNode;
import cs4321.project1.tree.MultiplicationTreeNode;
import cs4321.project1.tree.UnaryMinusTreeNode;

/**
 * Provide a comment about what your class does and the overall logic
 * 
 * @author Your names and netids go here
 */

public class EvaluateTreeVisitor implements TreeVisitor {
	
	private Stack<Double> treeStack;
	private double result;

	public EvaluateTreeVisitor() {
		// TODO fill me in
		treeStack = new Stack<Double>();
		result = 0;
	}

	public double getResult() {
		// TODO fill me in
		return treeStack.pop(); // so that skeleton code compiles
	}

	@Override
	public void visit(LeafTreeNode node) {
		// TODO fill me in
		treeStack.push(node.getData());
	}

	@Override
	public void visit(UnaryMinusTreeNode node) {
		// TODO fill me in
		node.getChild().accept(this);
		double value = treeStack.pop();
		treeStack.push(-value);
	}

	@Override
	public void visit(AdditionTreeNode node) {
		// TODO fill me in
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		double rightValue = treeStack.pop();
		double leftValue = treeStack.pop();
		treeStack.push(rightValue + leftValue);
	}

	@Override
	public void visit(MultiplicationTreeNode node) {
		// TODO fill me in
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		double rightValue = treeStack.pop();
		double leftValue = treeStack.pop();
		treeStack.push(rightValue * leftValue);
	}

	@Override
	public void visit(SubtractionTreeNode node) {
		// TODO fill me in
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		double rightValue = treeStack.pop();
		double leftValue = treeStack.pop();
		treeStack.push(leftValue - rightValue);
	}

	@Override
	public void visit(DivisionTreeNode node) {
		// TODO fill me in
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		double rightValue = treeStack.pop();
		double leftValue = treeStack.pop();
		treeStack.push(leftValue / rightValue);
	}
}
