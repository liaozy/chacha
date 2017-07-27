package com.zliao.chacha.service.RuleEngine.patternmatcher;

import java.util.ArrayList;
import java.util.List;

import com.zliao.chacha.service.RuleEngine.tools.Util;

public class OrExpression implements BaseExpression {
	// or表达式是and表达式的或
	// orexp= andexp1 || andexp2 || ...
	private List<BaseExpression> andExps = new ArrayList<BaseExpression>();

	public void setAndExps(List<BaseExpression> andExps) {
		this.andExps = andExps;
	}

	public List<BaseExpression> getAndExps() {
		return andExps;
	}

	@Override
	public boolean booleanValue(MatchToken token) {
		if (andExps == null || andExps.size() == 0)
			throw new RuntimeException("Not a valid Expression! " + this);

		// 只要其中一个为true，那么整个表达式都为true
		for (BaseExpression andExp : andExps) {
			if (andExp.booleanValue(token))
				return true;
		}
		// 所有的都是false，那么就是false
		return false;
	}

	public OrExpression(List<BaseExpression> andExps) {
		this.andExps = andExps;
	}

	public String toString(int tabCount) {
		StringBuilder sb = new StringBuilder(Util.printTabs(tabCount) + "OrExp\n");
		if (andExps != null && andExps.size() > 0) {
			int i=0;
			for (BaseExpression andExp : andExps) {
				i++;
				if(i>1){
					sb.append("\n");
				}
				sb.append(andExp.toString(tabCount + 1));
			}
		}
		return sb.toString();

	}
}
