package com.zliao.chacha.service.RuleEngine.transitioncondition;

public class TestConditionParser {

	public static void main(String[] args) throws ParseException {
		String exp="weather.info.city='北\"京' && (!(weather.info.date='2016-12-30') || weather.info.date.op='set\\'s')";
		ConditionParser parser=new ConditionParser(exp);
		Condition cond=parser.condition();
		System.out.println(cond);
	}

}
