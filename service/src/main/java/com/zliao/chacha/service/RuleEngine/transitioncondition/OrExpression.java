package com.zliao.chacha.service.RuleEngine.transitioncondition;

import com.zliao.chacha.service.RuleEngine.tools.Util;

import java.util.ArrayList;
import java.util.List;

public class OrExpression implements BaseExpression {
	// or表达式是and表达式的或
	// orexp= andexp1 || andexp2 || ...
	private List<BaseExpression> exps = new ArrayList<BaseExpression>();

	public void setExps(List<BaseExpression> exps) {
		this.exps = exps;
	}

	public List<BaseExpression> getExps() {
		return exps;
	}
 
	@SuppressWarnings("unused")
	private OrExpression(){
		
	}
	public OrExpression(List<BaseExpression> exps) {
		this.exps = exps;
	}

	public String toString(int tabCount) {
		StringBuilder sb = new StringBuilder(Util.printTabs(tabCount) + "OrExp\n");
		if (exps != null && exps.size() > 0) {
			for (BaseExpression andExp : exps) {
				sb.append(andExp.toString(tabCount + 1)).append("\n");
			}
		}
		return sb.toString();

	}
}
