package com.zliao.chacha.service.RuleEngine.transitioncondition;

import com.zliao.chacha.service.RuleEngine.tools.Util;

import java.util.ArrayList;
import java.util.List;

public class AndExpression implements BaseExpression {
	// and表达式是基本表达式的与
	// andexp= baseexp1 && baseexp2 && ...
	private List<BaseExpression> exps = new ArrayList<BaseExpression>();

	public void setExps(List<BaseExpression> exps) {
		this.exps = exps;
	}


	public List<BaseExpression> getExps() {
		return exps;
	}
	
	private AndExpression(){
		
	}
	public AndExpression(List<BaseExpression> exps) {
		this.exps = exps;
	}

	public String toString(int tabCount) {
		StringBuilder sb = new StringBuilder(Util.printTabs(tabCount) + "AndExp\n");
		if (exps != null) {
			for (BaseExpression baseExp : exps) {
				sb.append(baseExp.toString(tabCount + 1)).append("\n");
			}
		}
		return sb.toString();
	}
}
