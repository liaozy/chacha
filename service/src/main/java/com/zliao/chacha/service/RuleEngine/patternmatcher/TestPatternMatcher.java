package com.zliao.chacha.service.RuleEngine.patternmatcher;

public class TestPatternMatcher {

	public static void main(String[] args) throws Exception {
		String rule = "[text='明天']% []*? [text='从'] []{0,2} [type='city'] []? [text='到'] []{0,3} [type='city'] []*  [text='机票'] []* "
				+ " ->  5 com.easemob.ai.robotapi.nlulib.ruleengine.patternmatcher.TestAction('$1' '$2'  '$3' 'aaa')";
		PatternMatcher pm = new PatternMatcher(rule);
		MatchRule mr = pm.matchrule(rule);
		for (MatchCondition cond : mr.getConditions()) {
			System.out.println(cond.toString());
		}
	}

}
