package com.zliao.chacha.service.RuleEngine.patternmatcher;

import com.zliao.chacha.service.RuleEngine.tools.Util;

public class NotExpression implements BaseExpression{
	private BaseExpression exp;
	public BaseExpression getExp(){
		return exp;
	}
	@Override
	public String toString(int tabCount) {
		StringBuilder sb = new StringBuilder(Util.printTabs(tabCount));
		sb.append("[not]\n");
		sb.append(exp.toString(tabCount+1)+"\n");
		return sb.toString();
	}
	
	public NotExpression(BaseExpression exp){
		this.exp=exp;
	}

	@Override
	public boolean booleanValue(MatchToken token) {
		return !exp.booleanValue(token);
	}
}
