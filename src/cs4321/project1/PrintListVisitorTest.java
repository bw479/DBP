package cs4321.project1;

import static org.junit.Assert.*;
import cs4321.project1.list.*;

import org.junit.Test;

public class PrintListVisitorTest {

	@Test
	public void testSingleNumberNode() {
		ListNode n1 = new NumberListNode(1.0);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0", pv1.getResult());
	}
	
	@Test
	public void testAdditionSimplePrefix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		n3.setNext(n2);
		n2.setNext(n1);
		PrintListVisitor pv1 = new PrintListVisitor();
		n3.accept(pv1);
		assertEquals("+ 2.0 1.0", pv1.getResult());
	}
	
	@Test
	public void testAdditionNestedPrefix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new NumberListNode(2.0);
		ListNode n4 = new AdditionListNode();
		ListNode n5 = new AdditionListNode();
		n5.setNext(n4);
		n4.setNext(n1);
		n1.setNext(n2);
		n2.setNext(n3);
		PrintListVisitor pv1 = new PrintListVisitor();
		n5.accept(pv1);
		assertEquals("+ + 1.0 2.0 2.0", pv1.getResult());
	}
	
	@Test
	public void testAdditionSimplePostfix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0 2.0 +", pv1.getResult());
	}
	
	@Test
	public void testAdditionNestedPostfix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new NumberListNode(2.0);
		ListNode n4 = new AdditionListNode();
		ListNode n5 = new AdditionListNode();
		n1.setNext(n2);
		n2.setNext(n4);
		n4.setNext(n3);
		n3.setNext(n5);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0 2.0 + 2.0 +", pv1.getResult());
	}
	
	@Test
	public void testSubtractionSimplePrefix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new SubtractionListNode();
		n3.setNext(n2);
		n2.setNext(n1);
		PrintListVisitor pv1 = new PrintListVisitor();
		n3.accept(pv1);
		assertEquals("- 2.0 1.0", pv1.getResult());
	}
	
	@Test
	public void testSubtractionNestedPrefix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new NumberListNode(2.0);
		ListNode n4 = new SubtractionListNode();
		ListNode n5 = new SubtractionListNode();
		n5.setNext(n4);
		n4.setNext(n1);
		n1.setNext(n2);
		n2.setNext(n3);
		PrintListVisitor pv1 = new PrintListVisitor();
		n5.accept(pv1);
		assertEquals("- - 1.0 2.0 2.0", pv1.getResult());
	}
	
	@Test
	public void testSubtractionSimplePostfix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new SubtractionListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0 2.0 -", pv1.getResult());
	}
	
	@Test
	public void testSubtractionNestedPostfix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new NumberListNode(2.0);
		ListNode n4 = new SubtractionListNode();
		ListNode n5 = new SubtractionListNode();
		n1.setNext(n2);
		n2.setNext(n4);
		n4.setNext(n3);
		n3.setNext(n5);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0 2.0 - 2.0 -", pv1.getResult());
	}
	
	@Test
	public void testMultiplicationSimplePrefix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new MultiplicationListNode();
		n3.setNext(n2);
		n2.setNext(n1);
		PrintListVisitor pv1 = new PrintListVisitor();
		n3.accept(pv1);
		assertEquals("* 2.0 1.0", pv1.getResult());
	}
	
	@Test
	public void testMultiplicationNestedPrefix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new NumberListNode(5.0);
		ListNode n4 = new AdditionListNode();
		ListNode n5 = new MultiplicationListNode();
		n5.setNext(n4);
		n4.setNext(n1);
		n1.setNext(n2);
		n2.setNext(n3);
		PrintListVisitor pv1 = new PrintListVisitor();
		n5.accept(pv1);
		assertEquals("* + 1.0 2.0 5.0", pv1.getResult());
	}
	
	@Test
	public void testMultiplicationSimplePostfix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new MultiplicationListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0 2.0 *", pv1.getResult());
	}
	
	@Test
	public void testMultiplicationNestedPostfix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new NumberListNode(5.0);
		ListNode n4 = new AdditionListNode();
		ListNode n5 = new MultiplicationListNode();
		n1.setNext(n2);
		n2.setNext(n4);
		n4.setNext(n3);
		n3.setNext(n5);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0 2.0 + 5.0 *", pv1.getResult());
	}
	
	@Test
	public void testUnaryMinusSimplePrefix() {
		ListNode n1 = new NumberListNode(2.0);
		ListNode n2 = new UnaryMinusListNode();
		n2.setNext(n1);
		PrintListVisitor pv1 = new PrintListVisitor();
		n2.accept(pv1);
		assertEquals("~ 2.0", pv1.getResult());
	}
	
	@Test
	public void testUnaryMinusSimplePostfix() {
		ListNode n1 = new NumberListNode(2.0);
		ListNode n2 = new UnaryMinusListNode();
		n1.setNext(n2);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("2.0 ~", pv1.getResult());
	}
	
	@Test
	public void testUnaryMinusNestedPrefix() {
		ListNode n0 = new NumberListNode(9.0);
		ListNode n1 = new UnaryMinusListNode();
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new NumberListNode(5.0);
		ListNode n4 = new AdditionListNode();
		ListNode n5 = new AdditionListNode();
		n5.setNext(n4);
		n4.setNext(n1);
		n1.setNext(n0);
		n0.setNext(n2);
		n2.setNext(n3);
		PrintListVisitor pv1 = new PrintListVisitor();
		n5.accept(pv1);
		assertEquals("+ + ~ 9.0 2.0 5.0", pv1.getResult());
	}
	
	@Test
	public void testUnaryMinusNestedPostfix() {
		ListNode n0 = new NumberListNode(9.0);
		ListNode n1 = new UnaryMinusListNode();
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new NumberListNode(5.0);
		ListNode n4 = new AdditionListNode();
		ListNode n5 = new AdditionListNode();
		n0.setNext(n1);
		n1.setNext(n2);
		n2.setNext(n4);
		n4.setNext(n3);
		n3.setNext(n5);
		PrintListVisitor pv1 = new PrintListVisitor();
		n0.accept(pv1);
		assertEquals("9.0 ~ 2.0 + 5.0 +", pv1.getResult());
	}
}
