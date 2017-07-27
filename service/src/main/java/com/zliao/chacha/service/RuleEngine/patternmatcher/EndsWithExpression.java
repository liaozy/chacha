package com.zliao.chacha.service.RuleEngine.patternmatcher;

import com.zliao.chacha.service.RuleEngine.tools.Util;

public class EndsWithExpression implements BaseExpression {
	private String left;
	private String right;

	@Override
	public boolean booleanValue(MatchToken token) {
		String text = (String) token.getAttr(left);
		return text.endsWith(right);
	}

	public EndsWithExpression(String left, String right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString(int tabCount) {
		StringBuilder sb = new StringBuilder(Util.printTabs(tabCount));
		sb.append("'" + left + "' endsWith '" + right + "'\n");
		return sb.toString();
	}
}
