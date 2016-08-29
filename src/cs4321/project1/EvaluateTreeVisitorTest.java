package cs4321.project1;

import static org.junit.Assert.*;

import org.junit.Test;

import cs4321.project1.tree.*;

public class EvaluateTreeVisitorTest {

	private static final double DELTA = 1e-15;

	@Test
	public void testSingleLeafNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
        EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n1.accept(v1);
		assertEquals(1.0, v1.getResult(), DELTA);
	}
	
	@Test
	public void testUnaryMinusNode() {
		TreeNode leafNode = new LeafTreeNode(1.0);
		TreeNode n1 = new UnaryMinusTreeNode(leafNode);
        EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
        n1.accept(v1);
        assertEquals(-1.0, v1.getResult(), DELTA);
        // Test nested UnaryMinusTreeNode.
		TreeNode n2 = new UnaryMinusTreeNode(n1);
		n2.accept(v1);
        assertEquals(1.0, v1.getResult(), DELTA);
	}
	
	@Test
	public void testAdditionNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new LeafTreeNode(2.0);
		TreeNode n3 = new AdditionTreeNode(n1, n2);
		TreeNode n4 = new AdditionTreeNode(n2, n1);
        EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n3.accept(v1);
		assertEquals(3.0, v1.getResult(), DELTA);
        EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		n4.accept(v2);
		assertEquals(3.0, v2.getResult(), DELTA);
	}
	
	@Test
	public void testNestedAdditionNode() {
		TreeNode leafNode1 = new LeafTreeNode(1.0);
		TreeNode leafNode2 = new LeafTreeNode(2.0);
		TreeNode n1 = new AdditionTreeNode(leafNode1, leafNode2);
		TreeNode n2 = new AdditionTreeNode(n1, leafNode2);
		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n2.accept(v1);
		assertEquals(5.0, v1.getResult(), DELTA);
	}
	
	@Test
	public void testMultiplicationNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new LeafTreeNode(2.0);
		TreeNode n3 = new MultiplicationTreeNode(n1, n2);
		TreeNode n4 = new MultiplicationTreeNode(n2, n1);
        EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n3.accept(v1);
		assertEquals(2.0, v1.getResult(), DELTA);
        EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		n4.accept(v2);
		assertEquals(2.0, v2.getResult(), DELTA);
	}
	
	@Test
	public void testNestedMultiplicationNode() {
		TreeNode leafNode1 = new LeafTreeNode(1.0);
		TreeNode leafNode2 = new LeafTreeNode(2.0);
		TreeNode n1 = new MultiplicationTreeNode(leafNode1, leafNode2);
		TreeNode n2 = new MultiplicationTreeNode(n1, leafNode2);
		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n2.accept(v1);
		assertEquals(4.0, v1.getResult(), DELTA);
	}
	
	@Test
	public void testDivisionNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new LeafTreeNode(2.0);
		TreeNode n3 = new DivisionTreeNode(n1, n2);
		TreeNode n4 = new DivisionTreeNode(n2, n1);
        EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n3.accept(v1);
		assertEquals(0.5, v1.getResult(), DELTA);
        EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		n4.accept(v2);
		assertEquals(2.0, v2.getResult(), DELTA);
	}
	
	@Test
	public void testNestedDivisionNode() {
		TreeNode leafNode1 = new LeafTreeNode(1.0);
		TreeNode leafNode2 = new LeafTreeNode(2.0);
		TreeNode n1 = new DivisionTreeNode(leafNode1, leafNode2);
		TreeNode n2 = new DivisionTreeNode(n1, leafNode2);
		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n2.accept(v1);
		assertEquals(0.25, v1.getResult(), DELTA);
	}

	@Test
	public void testSubtractionNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new LeafTreeNode(2.0);
		TreeNode n3 = new SubtractionTreeNode(n1, n2);
		TreeNode n4 = new SubtractionTreeNode(n2, n1);
        EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n3.accept(v1);
		assertEquals(-1.0, v1.getResult(), DELTA);
        EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		n4.accept(v2);
		assertEquals(1.0, v2.getResult(), DELTA);
	}
	
	@Test
	public void testNestedSubtractionNode() {
		TreeNode leafNode1 = new LeafTreeNode(1.0);
		TreeNode leafNode2 = new LeafTreeNode(2.0);
		TreeNode n1 = new SubtractionTreeNode(leafNode1, leafNode2);
		TreeNode n2 = new SubtractionTreeNode(n1, leafNode2);
		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n2.accept(v1);
		assertEquals(-3.0, v1.getResult(), DELTA);
	}
}
