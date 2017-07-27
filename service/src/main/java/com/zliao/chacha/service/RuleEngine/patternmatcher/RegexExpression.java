package com.zliao.chacha.service.RuleEngine.patternmatcher;

import com.zliao.chacha.service.RuleEngine.tools.Util;

public class RegexExpression implements BaseExpression {
	private String left;
	private String right;

	@Override
	public boolean booleanValue(MatchToken token) {
		// 优化点：预先编译正则表达式(right)，
		// 然后cache住
		String text = (String) token.getAttr(left);
		if (text == null)
			return false;
		return text.matches(right);
	}

	public RegexExpression(String left, String right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString(int tabCount) {
		StringBuilder sb = new StringBuilder(Util.printTabs(tabCount));
		sb.append("'").append(left);
		sb.append("' REGEX '").append(right).append("'\n");
		return sb.toString();
	}
}
