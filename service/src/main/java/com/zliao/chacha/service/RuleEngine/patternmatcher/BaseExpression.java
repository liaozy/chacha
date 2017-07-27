package com.zliao.chacha.service.RuleEngine.patternmatcher;

/**
 * 基本表达式 比如= ！= like 等表达式 也可以是Expression
 * 
 * @author zliao
 *
 */
public interface BaseExpression {
	public boolean booleanValue(MatchToken token);

	public String toString(int tabCount);
}
