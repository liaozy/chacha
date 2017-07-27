package com.zliao.chacha.service.RuleEngine.transitioncondition;

/**
 * 基本表达式 比如= ！= like 等表达式 也可以是Expression
 * 
 * @author zliao
 *
 */
public interface BaseExpression extends java.io.Serializable {
	public String toString(int tabCount);
}
