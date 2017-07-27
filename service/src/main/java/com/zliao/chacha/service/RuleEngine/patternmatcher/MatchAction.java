package com.zliao.chacha.service.RuleEngine.patternmatcher;

public interface MatchAction {
	public Object doAction(Object[] params, double score);
}
