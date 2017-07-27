package com.zliao.chacha.service.RuleEngine.patternmatcher;

import com.zliao.chacha.service.RuleEngine.tools.Util;

public class StartsWithExpression implements BaseExpression {
	private String left;
	private String right;

	@Override
	public boolean booleanValue(MatchToken token) {
		String text = (String) token.getAttr(left);
		return text.startsWith(right);
	}

	public StartsWithExpression(String left, String right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString(int tabCount) {
		StringBuilder sb = new StringBuilder(Util.printTabs(tabCount));
		sb.append("'").append(left);
		sb.append("' STARTSWITH '").append(right).append("'\n");
		return sb.toString();
	}
}
