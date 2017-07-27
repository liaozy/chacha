package com.zliao.chacha.service.RuleEngine.transitioncondition;

import com.zliao.chacha.service.RuleEngine.tools.Util;

public class EndsWithExpression implements BaseExpression {
	private String left;
	private String right;
 
	private EndsWithExpression(){
		
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
