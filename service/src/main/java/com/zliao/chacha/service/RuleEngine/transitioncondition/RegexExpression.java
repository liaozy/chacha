package com.zliao.chacha.service.RuleEngine.transitioncondition;

import com.zliao.chacha.service.RuleEngine.tools.Util;

public class RegexExpression implements BaseExpression {
	private String left;
	private String right;

	@SuppressWarnings("unused")
	private RegexExpression(){
		
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
