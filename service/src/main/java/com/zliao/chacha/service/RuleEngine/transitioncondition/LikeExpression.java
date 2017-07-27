package com.zliao.chacha.service.RuleEngine.transitioncondition;

import com.zliao.chacha.service.RuleEngine.tools.Util;

public class LikeExpression implements BaseExpression {
	public String getLeft() {
		return left;
	}

	public String getRight() {
		return right;
	}

	private String left;
	private String right;
 
	private LikeExpression(){
		
	}
	public LikeExpression(String left, String right) {
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
