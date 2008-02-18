package org.gdms.sql.strategies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import org.gdms.data.ExecutionException;
import org.gdms.data.metadata.DefaultMetadata;
import org.gdms.data.metadata.Metadata;
import org.gdms.data.types.InvalidTypeException;
import org.gdms.data.types.Type;
import org.gdms.data.values.Value;
import org.gdms.data.values.ValueCollection;
import org.gdms.data.values.ValueFactory;
import org.gdms.driver.DriverException;
import org.gdms.driver.ObjectDriver;
import org.gdms.sql.customQuery.QueryManager;
import org.gdms.sql.evaluator.Expression;
import org.gdms.sql.evaluator.Field;
import org.gdms.sql.evaluator.FunctionOperator;
import org.gdms.sql.function.Function;
import org.gdms.sql.function.FunctionManager;

public class ProjectionOp extends AbstractExpressionOperator implements
		Operator, ChangesMetadata {

	private ArrayList<SelectElement> selectElements;
	private String[] aliasList;
	private Expression[] expressionList;
	private ArrayList<Field> groupByFieldNames = new ArrayList<Field>();
	private boolean distinct;

	public ProjectionOp() {
		selectElements = new ArrayList<SelectElement>();
		aliasList = null;
		expressionList = null;
	}

	public ObjectDriver getResultContents() throws ExecutionException {
		ObjectDriver ret = null;
		try {
			if (onlyStar()) {
				ret = getOperator(0).getResult();
			} else {
				ret = new ProjectionDriver(getOperator(0).getResult(),
						getExpressions(), getResultMetadata());
			}
		} catch (DriverException e) {
			throw new ExecutionException(
					"Cannot obtain the metadata of the result", e);
		} catch (SemanticException e) {
			throw new RuntimeException("The preprocessor has failed", e);
		}

		if (distinct) {
			ArrayList<Integer> indexes = new ArrayList<Integer>();
			TreeSet<Value> set = new TreeSet<Value>(new Comparator<Value>() {
				public int compare(Value o1, Value o2) {
					Value v1 = (Value) o1;
					Value v2 = (Value) o2;

					if (v1.equals(v2).getAsBoolean()) {
						return 0;
					} else {
						return 1;
					}
				}
			});

			try {
				for (int i = 0; i < ret.getRowCount(); i++) {
					Value[] row = new Value[ret.getMetadata().getFieldCount()];
					for (int j = 0; j < row.length; j++) {
						row[j] = ret.getFieldValue(i, j);
					}
					ValueCollection rowValue = ValueFactory.createValue(row);

					if (!set.contains(rowValue)) {
						indexes.add(i);
						set.add(rowValue);
					}
				}
				ret = new RowMappedDriver(ret, indexes);
			} catch (DriverException e) {
				throw new ExecutionException("Cannot perform"
						+ " distinct operation", e);
			}
		}

		return ret;
	}

	private boolean onlyStar() throws DriverException, SemanticException {
		return ((getExpressions().length == 1) && (getExpressions()[0] instanceof StarElement));
	}

	private String[] getAliases() throws DriverException, SemanticException {
		if (aliasList == null) {
			expandStars();
		}

		return aliasList;
	}

	private void expandStars() throws DriverException, SemanticException {
		// Replace the select elements by Expression instances
		ArrayList<Expression> expressions = new ArrayList<Expression>();
		ArrayList<String> aliases = new ArrayList<String>();
		for (SelectElement element : selectElements) {
			if (element instanceof StarElement) {
				populateWithChildOperatorMetadata(expressions, aliases);
			} else if (element instanceof TableStarElement) {
				String tableName = ((TableStarElement) element).tableName;
				Operator[] products = getOperators(this, new OperatorFilter() {

					public boolean accept(Operator op) {
						return op instanceof ScalarProductOp;
					}

				});
				for (Operator operator : products) {
					ScalarProductOp op = (ScalarProductOp) operator;
					try {
						Metadata m = op.getMetadata(tableName);
						if (m != null) {
							for (int i = 0; i < m.getFieldCount(); i++) {
								expressions.add(new Field(tableName, m
										.getFieldName(i)));
								aliases.add(null);
							}
						}
					} catch (SemanticException e) {
						throw new RuntimeException(
								"The semantics should be validated "
										+ "before this method is called");
					}

				}
			} else if (element instanceof ExpressionElement) {
				ExpressionElement expressionElement = (ExpressionElement) element;
				expressions.add(expressionElement.expr);
				aliases.add(expressionElement.alias);
			} else {
				throw new RuntimeException("bug");
			}
		}

		aliasList = aliases.toArray(new String[0]);
		expressionList = expressions.toArray(new Expression[0]);

	}

	protected Expression[] getExpressions() throws DriverException,
			SemanticException {
		if (expressionList == null) {
			expandStars();
		}

		return expressionList;
	}

	public void addExpr(Expression expression, String alias) {
		selectElements.add(new ExpressionElement(expression, alias));
	}

	public void addStar() {
		selectElements.add(new StarElement());
	}

	public void addStarOf(String tableName) {
		selectElements.add(new TableStarElement(tableName));
	}

	/**
	 * The metadata returned by a projection operator consists of one field for
	 * each expression in the select clause. The only exception to this is when
	 * there is some aggregated functions they are removed and managed by a
	 * groupby operator further down. If the expression contains an alias, the
	 * name of the field is that alias. Otherwise, if the expression is just a
	 * field reference its name is used. Finally, if a complex expression is
	 * found and there is no alias, the string 'unknown' concatenated with the
	 * index of the field in the resulting metadata is used
	 *
	 * @see org.gdms.sql.strategies.Operator#getResultMetadata()
	 */
	public Metadata getResultMetadata() throws DriverException {
		DefaultMetadata ret = new DefaultMetadata();
		Expression[] expressions;
		try {
			expressions = getExpressions();
			for (int i = 0; i < expressions.length; i++) {
				String name = getFieldName(i);
				Type type = getFieldType(expressions[i]);
				try {
					ret.addField(name, type.getTypeCode(), type
							.getConstraints());
				} catch (InvalidTypeException e) {
					throw new RuntimeException("Bug: Invalid type "
							+ "not detected by preprocessor", e);
				}
			}
		} catch (SemanticException e1) {
			throw new RuntimeException("The preprocessor has failed", e1);
		}

		return ret;
	}

	private Type getFieldType(Expression expression) throws DriverException {
		return expression.getType();
	}

	private String getFieldName(int index) throws DriverException,
			SemanticException {
		String alias = getAliases()[index];
		if (alias != null) {
			return getAliases()[index];
		} else {
			Expression expr = getExpressions()[index];
			if (expr instanceof Field) {
				return ((Field) expr).getFieldName();
			} else {
				return "unknown" + index;
			}
		}
	}

	private interface SelectElement {
	}

	private class ExpressionElement implements SelectElement {
		Expression expr;
		String alias;

		public ExpressionElement(Expression expr, String alias) {
			super();
			this.expr = expr;
			this.alias = alias;
		}
	}

	private class TableStarElement implements SelectElement {
		String tableName;

		public TableStarElement(String tableName) {
			super();
			this.tableName = tableName;
		}
	}

	private class StarElement implements SelectElement {
	}

	public boolean isAggregated() {
		try {
			Expression[] exprs = getExpressions();
			for (Expression expression : exprs) {
				FunctionOperator[] functions = expression
						.getFunctionReferences();
				for (FunctionOperator functionOperator : functions) {
					if (isAggregated(functionOperator)) {
						return true;
					}
				}
			}
		} catch (DriverException e) {
		} catch (SemanticException e) {
		}

		return false;
	}

	private boolean isAggregated(FunctionOperator functionOperator) {
		String functionName = functionOperator.getFunctionName();
		Function fcn = FunctionManager.getFunction(functionName);
		return (fcn != null) && fcn.isAggregate();
	}

	public Operator removeOperator(int i) {
		return children.remove(i);
	}

	/**
	 * This method helps in the movement of aggregate functions to a groupby
	 * operator further down. It changes the expressions that consist of a
	 * aggregate function into a reference to a field of the groupby result.
	 * Returns the aggregated expressions to be added in the groupby operator
	 * further down. This method assumes the instruction contains at least an
	 * aggregated function or a group by clause
	 *
	 * @param groupByFieldNames
	 *            The names used in the group by clause
	 * @return
	 * @throws DriverException
	 *             Error accessing data
	 * @throws SemanticException
	 *             Semantic error expanding '*'
	 */
	public Expression[] transformExpressionsInGroupByReferences()
			throws DriverException, SemanticException {
		Expression[] exprs = getExpressions();
		ArrayList<Expression> ret = new ArrayList<Expression>();
		for (Expression expression : exprs) {
			if (expression instanceof FunctionOperator) {
				ret.add(expression);
			}
		}
		int functionIndex = groupByFieldNames.size();
		for (int i = 0; i < exprs.length; i++) {
			Expression expression = exprs[i];
			if (expression instanceof Field) {
				Field field = (Field) expression;
				if (groupByFieldNames.contains(field)) {
					continue;
				} else {
					throw new SemanticException(field + " is not in the "
							+ "group by clause "
							+ "and outside an aggregate function");
				}
			}

			int groupByIndex = functionIndex;
			functionIndex++;
			Field groupByField = new Field(GroupByOperator.FIELD_PREFIX
					+ groupByIndex);
			exprs[i] = groupByField;
			groupByFieldNames.add(groupByField);
		}

		return ret.toArray(new Expression[0]);
	}

	/**
	 * @return true if the instruction is a custom query and false if it is not
	 *         or it cannot be stated yet
	 */
	public boolean isCustomQuery() {
		try {
			Expression[] exprs = getExpressions();
			boolean custom = false;
			for (Expression expression : exprs) {
				FunctionOperator[] functions = expression
						.getFunctionReferences();
				for (FunctionOperator functionOperator : functions) {
					String functionName = functionOperator.getFunctionName();
					if (QueryManager.getQuery(functionName) != null) {
						custom = true;
					}
				}
			}

			if (custom) {
				if (exprs.length > 1) {
					throw new SemanticException("Custom queries "
							+ "cannot have more than one "
							+ "expression in the select clause");
				}
				return true;
			} else {
				return false;
			}
		} catch (DriverException e) {
		} catch (SemanticException e) {
		}

		return false;
	}

	public int getFieldIndex(Field field) throws DriverException,
			SemanticException {
		if (field.getTableName() != null) {
			// The dependency will be satisfied by a scalar product operator
			return -1;
		} else {
			int fieldIndex = -1;

			Expression[] exprs = getExpressions();
			for (int i = 0; i < exprs.length; i++) {
				if ((aliasList[i] != null)
						&& aliasList[i].equals(field.getFieldName())) {
					fieldIndex = setFieldIndex(field, fieldIndex, i);
				} else {
					if (exprs[i] instanceof Field) {
						Field expr = (Field) exprs[i];
						if (field.getFieldName().equals(expr.getFieldName())) {
							fieldIndex = setFieldIndex(field, fieldIndex, i);
						}
					}
				}
			}

			return fieldIndex;
		}
	}

	private int setFieldIndex(Field field, int fieldIndex, int i)
			throws SemanticException {
		if (fieldIndex == -1) {
			fieldIndex = i;
		} else {
			throw new SemanticException("Ambiguous field reference: "
					+ field.toString());
		}
		return fieldIndex;
	}

	@Override
	public void validateFieldReferences() throws SemanticException,
			DriverException {
		if (groupByFieldNames.size() > 0) {
			// If there is group by, validate there is no nongrouping field
			// outside
			// an aggregated function
			Field[] fieldReferences = getFieldReferences();
			ArrayList<Field> nonGroupByReferences = new ArrayList<Field>();
			for (Field field : fieldReferences) {
				if (!groupByFieldNames.contains(field)) {
					nonGroupByReferences.add(field);
				}
			}

			for (Field field : nonGroupByReferences) {
				// get the path in the expression tree
				Expression[] expressions = getExpressions();
				for (Expression expression : expressions) {
					// If there is no aggregated function in the path: crash
					Expression[] path = expression.getPath(field);
					if (path != null) {
						boolean anyAggregate = false;
						for (Expression pathElement : path) {
							if (pathElement instanceof FunctionOperator) {
								FunctionOperator fo = (FunctionOperator) pathElement;
								if (isAggregated(fo)) {
									anyAggregate = true;
								}
							}
						}

						if (!anyAggregate) {
							throw new SemanticException("Field '"
									+ field.toString()
									+ "' must appear in the GROUP BY clause "
									+ "or be used in an aggregate function");
						}
					}
				}
			}
		}

		super.validateFieldReferences();
	}

	public void setGroupByFieldNames(ArrayList<Field> arrayList) {
		this.groupByFieldNames = arrayList;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}
}
