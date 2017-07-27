package com.zliao.chacha.service.RuleEngine.patternmatcher;

import com.zliao.chacha.service.RuleEngine.tools.Util;

public class ContainsExpression implements BaseExpression {
	public String getLeft() {
		return left;
	}

	public String getRight() {
		return right;
	}

	private String left;
	private String right;

	@Override
	public boolean booleanValue(MatchToken token) {
		String text = (String) token.getAttr(left);
		if (text == null)
			return false;
		return text.contains(right);
	}

	public ContainsExpression(String left, String right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString(int tabCount) {
		StringBuilder sb = new StringBuilder(Util.printTabs(tabCount));
		sb.append("'").append(left);
		sb.append("' LIKE '").append(right).append("'\n");
		return sb.toString();
	}
}
