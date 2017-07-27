package com.zliao.chacha.service.RuleEngine.transitioncondition;

import com.zliao.chacha.service.RuleEngine.tools.Util;

public class StartsWithExpression implements BaseExpression {
	private String left;
	private String right;
 
	@SuppressWarnings("unused")
	private StartsWithExpression(){
		
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
