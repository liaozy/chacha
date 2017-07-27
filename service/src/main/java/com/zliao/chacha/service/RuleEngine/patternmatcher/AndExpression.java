package com.zliao.chacha.service.RuleEngine.patternmatcher;

import com.zliao.chacha.service.RuleEngine.tools.Util;

import java.util.ArrayList;
import java.util.List;

public class AndExpression implements BaseExpression {
	// and表达式是基本表达式的与
	// andexp= baseexp1 && baseexp2 && ...
	private List<BaseExpression> baseExps = new ArrayList<BaseExpression>();

	public List<BaseExpression> getBaseExps() {
		return baseExps;
	}

	public void setBaseExps(List<BaseExpression> baseExps) {
		this.baseExps = baseExps;
	}

	@Override
	public boolean booleanValue(MatchToken token) {
		if (baseExps == null || baseExps.size() == 0)
			throw new RuntimeException("Not a valid Expression! " + this);

		// 只要其中一个为false，那么整个表达式都为false
		for (BaseExpression baseExp : baseExps) {
			if (!baseExp.booleanValue(token))
				return false;
		}
		// 所有的都是true，那么就是true
		return true;
	}

	public AndExpression(List<BaseExpression> baseExps) {
		this.baseExps = baseExps;
	}

	public String toString(int tabCount) {
		StringBuilder sb = new StringBuilder(Util.printTabs(tabCount) + "AndExp\n");
		if (baseExps != null) {
			int i=0;
			for (BaseExpression baseExp : baseExps) {
				i++;
				if(i>1){
					sb.append("\n");
				}
				sb.append(baseExp.toString(tabCount + 1));
			}
		}
		return sb.toString();
	}
}
