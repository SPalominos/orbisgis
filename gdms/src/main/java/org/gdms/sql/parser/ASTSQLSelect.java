/* Generated By:JJTree: Do not edit this line. ASTSQLSelect.java */

package org.gdms.sql.parser;

public class ASTSQLSelect extends SimpleNode {
	public ASTSQLSelect(int id) {
		super(id);
	}

	public ASTSQLSelect(SQLEngine p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	public Object jjtAccept(SQLEngineVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
