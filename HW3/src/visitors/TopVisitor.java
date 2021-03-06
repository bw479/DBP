package visitors;

import java.util.function.Function;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.InverseExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * Visitors for all the conditions, which is able to recursively visit
 * all the conditions.
 */
public abstract class TopVisitor implements ExpressionVisitor {
	protected long curValue;
	protected boolean curResult;
	/**
	 * create a visitor for conditions
	 */
	public TopVisitor() {
		curValue = 0;
		curResult = false;
	}
	/**
	 * get the result for the expression
	 * @return true or false for the expression
	 */
	public boolean getResult() {
		return curResult;
	}
	/**
	 * get the result for null value.
	 */
	public void visit(NullValue n) {
		
	}
		
		
	@Override
	public void visit(DateValue arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(TimeValue arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(Subtraction arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(OrExpression arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(Between arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(InExpression arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(TimestampValue arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(Parenthesis arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(StringValue arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(Addition arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(Division arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(Multiplication arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	
	@Override
	public void visit(IsNullExpression arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(LikeExpression arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(SubSelect arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(CaseExpression arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(WhenClause arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(ExistsExpression arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(AllComparisonExpression arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(AnyComparisonExpression arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(Concat arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(Matches arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(BitwiseAnd arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(BitwiseOr arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(BitwiseXor arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(InverseExpression arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(JdbcParameter arg0) {
		throw new UnsupportedOperationException("not supported");
	}
	@Override
	public void visit(DoubleValue arg0) {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	public void visit(net.sf.jsqlparser.expression.Function arg0) {
		throw new UnsupportedOperationException("not supported");
		
	}
	/**
	 * visitor for the long number
	 */
	@Override
	public void visit(LongValue arg0) {
		curValue = arg0.getValue();
		
	}
	/**
	 * visitor for the and expression
	 */
	@Override
	public void visit(AndExpression arg0) {
		arg0.getLeftExpression().accept(this);
		boolean leftResult = curResult;
		arg0.getRightExpression().accept(this);
		curResult = leftResult && curResult;
		
	}
	
	/**
	 * visitor for the equal to expression
	 */
	@Override
	public void visit(EqualsTo arg0) {
		arg0.getLeftExpression().accept(this);
		long leftValue = curValue;
		arg0.getRightExpression().accept(this);
		curResult = leftValue == curValue;
		
	}

	/**
	 * visitor for the greater than expression
	 */
	@Override
	public void visit(GreaterThan arg0) {
		arg0.getLeftExpression().accept(this);
		long leftValue = curValue;
		arg0.getRightExpression().accept(this);
		curResult = leftValue > curValue;
		
	}

	/**
	 * visitor for the greater than and equal to expression
	 */
	@Override
	public void visit(GreaterThanEquals arg0) {
		arg0.getLeftExpression().accept(this);
		long leftValue = curValue;
		arg0.getRightExpression().accept(this);
		curResult = (leftValue == curValue) || (leftValue > curValue);
		
	}

	/**
	 * visitor for the less than expression
	 */
	@Override
	public void visit(MinorThan arg0) {
		arg0.getLeftExpression().accept(this);
		long leftValue = curValue;
		arg0.getRightExpression().accept(this);
		curResult = leftValue < curValue;
		
	}

	/**
	 * visitor for the less than and equal to expression
	 */
	@Override
	public void visit(MinorThanEquals arg0) {
		arg0.getLeftExpression().accept(this);
		long leftValue = curValue;
		arg0.getRightExpression().accept(this);
		curResult = (leftValue == curValue) || (leftValue < curValue);
		
	}

	/**
	 * visitor for the not equal to expression
	 */
	@Override
	public void visit(NotEqualsTo arg0) {
		arg0.getLeftExpression().accept(this);
		long leftValue = curValue;
		arg0.getRightExpression().accept(this);
		curResult = leftValue != curValue;
		
	}

	/**
	 * visitor for the column name
	 */
	@Override
	public abstract void visit(Column arg0);

		
}
