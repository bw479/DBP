package cs4321.project1;

import cs4321.project1.list.*;
import cs4321.project1.tree.*;

/**
 * Provide a comment about what your class does and the overall logic
 * 
 * @author Your names and netids go here
 */
public class BuildPrefixExpressionTreeVisitor implements TreeVisitor {

	private ListNode treeList;
	private ListNode tmp;
	public BuildPrefixExpressionTreeVisitor() {
		// TODO fill me in
		// Used as a dummy head node
		treeList = new AdditionListNode();
		tmp = treeList;
	}

	public ListNode getResult() {
		// TODO fill me in
		return tmp.getNext();
	}

	@Override
	public void visit(LeafTreeNode node) {
		// TODO fill me in
		NumberListNode numListNode = new NumberListNode(node.getData());
		treeList.setNext(numListNode);
		treeList = treeList.getNext();
	}

	@Override
	public void visit(UnaryMinusTreeNode node) {
		// TODO fill me in
		treeList.setNext(new UnaryMinusListNode());
		treeList = treeList.getNext();
		node.getChild().accept(this);
		
	}

	@Override
	public void visit(AdditionTreeNode node) {
		// TODO fill me in
		treeList.setNext(new AdditionListNode());
		treeList = treeList.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		
	}

	@Override
	public void visit(MultiplicationTreeNode node) {
		// TODO fill me in
		treeList.setNext(new MultiplicationListNode());
		treeList = treeList.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
	}

	@Override
	public void visit(SubtractionTreeNode node) {
		// TODO fill me in
		treeList.setNext(new SubtractionListNode());
		treeList = treeList.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
	}

	@Override
	public void visit(DivisionTreeNode node) {
		// TODO fill me in
		treeList.setNext(new DivisionListNode());
		treeList = treeList.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
	}

}