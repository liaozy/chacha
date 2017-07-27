package com.zliao.chacha.service.RuleEngine.patternmatcher;

import java.lang.reflect.Array;
import java.util.Collection;

import com.zliao.chacha.service.RuleEngine.tools.Util;

public class NotEqualExpression implements BaseExpression {
	public String getLeft() {
		return left;
	}

	public Object getRight() {
		return right;
	}

	private String left;
	private Object right;

	@Override
	public boolean booleanValue(MatchToken token) {
		Object v = token.getAttr(left);
		if (v == null)
			return true;
		if (v instanceof Collection) {
			Collection c = (Collection) v;
			return !c.contains(right);
		} else if (v.getClass().isArray()) {
			int length = Array.getLength(v);
			for (int i = 0; i < length; i++) {
				Object arrayElement = Array.get(v, i);
				if (arrayElement.equals(right)) {
					return false;
				}
			}
			return true;
		} else {
			return !v.equals(right);
		}
	}

	public String toString(int tabCount) {
		StringBuilder sb = new StringBuilder(Util.printTabs(tabCount));
		sb.append("'").append(left);
		sb.append("' != '").append(right).append("'");
		return sb.toString();
	}

	public NotEqualExpression(String left, Object right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public boolean equals(Object other){
		if(! (other instanceof NotEqualExpression)) return false;
		NotEqualExpression ee=(NotEqualExpression)other;
		return this.left.equals(ee.getLeft()) && this.right.equals(ee.getRight());
	}
}
