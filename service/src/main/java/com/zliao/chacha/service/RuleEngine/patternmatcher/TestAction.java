package com.zliao.chacha.service.RuleEngine.patternmatcher;

import java.util.List;

public class TestAction implements MatchAction {

	@Override
	public Object doAction(Object[] params, double score) {
		System.out.println("score: " + score);
		for (Object param : params) {
			if (param instanceof String) {
				System.out.println(param);
			} else if (param instanceof MatchToken) {
				MatchToken mt = (MatchToken) param;
				System.out.println(mt);
			} else if (param instanceof List) {
				List list = (List) param;
				for (Object p : list) {
					MatchToken tk = (MatchToken) p;
					System.out.print(tk + "\t");
				}
				System.out.println();
			}
		}
		return score;
	}

}
