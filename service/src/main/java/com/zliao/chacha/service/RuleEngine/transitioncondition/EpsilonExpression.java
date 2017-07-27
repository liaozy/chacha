package com.zliao.chacha.service.RuleEngine.transitioncondition;

import com.zliao.chacha.service.RuleEngine.tools.Util;

public class EpsilonExpression implements BaseExpression{

	@Override
	public String toString(int tabCount) {
		return Util.printTabs(tabCount)+EpsilonExpression.class.getSimpleName();
	}

	
	private EpsilonExpression(){
		
	}
	
	private final static EpsilonExpression instance=new EpsilonExpression();
	public static EpsilonExpression getInstance(){
		return instance;
	}
}
