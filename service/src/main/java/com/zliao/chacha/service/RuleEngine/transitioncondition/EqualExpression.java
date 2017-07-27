package com.zliao.chacha.service.RuleEngine.transitioncondition;

import com.zliao.chacha.service.RuleEngine.tools.Util;

import java.lang.reflect.Array;
import java.util.Collection;

public class EqualExpression implements BaseExpression {
	public String getLeft() {
		return left;
	}

	public Object getRight() {
		return right;
	}

	private String left;
	private Object right;
 
	public String toString(int tabCount) {
		StringBuilder sb = new StringBuilder(Util.printTabs(tabCount));
		sb.append("'").append(left);
		sb.append("' == '").append(right).append("'\n");
		return sb.toString();
	}

	private EqualExpression(){
		
	}
	public EqualExpression(String left, Object right) {
		this.left = left;
		this.right = right;
	}
}
