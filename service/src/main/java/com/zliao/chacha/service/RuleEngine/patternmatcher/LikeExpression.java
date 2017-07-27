package com.zliao.chacha.service.RuleEngine.patternmatcher;


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

	@Override
	public boolean booleanValue(MatchToken token) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented yet.");
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
