package com.zliao.chacha.service.RuleEngine.patternmatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MatchToken {
	public void addIntValue(String key, int inc) {
		Integer c = (Integer) attrs.get(key);
		if (c == null) {
			attrs.put(key, inc);
		} else {
			attrs.put(key, inc + c);
		}
	}

	private Map<String, Object> attrs;
	public static final String KEY_TEXT = "text";
	public static final String KEY_TYPE="type";
	public static final String KEY_SYNONYM="syn";
	public static final String KEY_SYNONYM_EXPAND="synExpand";
	public static final String START_INDEX = "startIndex";
	public static final String END_INDEX = "endIndex";

	public MatchToken(Map<String, Object> attrs) {
		this.attrs = attrs;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("");
		for (Entry<String, Object> entry : attrs.entrySet()) {
			sb.append(entry.getKey() + "->" + entry.getValue() + ", ");
		}
		return sb.toString();
	}

	public MatchToken(String s) {
		attrs = new HashMap<>(1);
		attrs.put(KEY_TEXT, s);
	}

	public Object getAttr(String attrName) {
		return attrs.get(attrName);
	}

	public void setAttr(String attrName, Object attrValue) {
		this.attrs.put(attrName, attrValue);
	}

	private static MatchToken emptyToken = new MatchToken(new HashMap<String, Object>(0));

	/**
	 * 没有任何属性的Token， 一般表示普通的词语
	 * 
	 * @return
	 */
	public static MatchToken getEmptyAttrToken() {
		return emptyToken;
	}
}
