package com.zliao.chacha.service.RuleEngine.transitioncondition;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ReadContext;

public class BooleanExpressionMatcher {
	private BaseExpression exp; 
	private static Configuration conf =  Configuration.defaultConfiguration().addOptions(Option.ALWAYS_RETURN_LIST);
	
	public BooleanExpressionMatcher(String expStr) throws Exception{
		if(expStr==null){
			return;
		}
		ConditionParser parser=new ConditionParser(expStr);
		exp=parser.condition().getExp();
		exp=this.optimizeExpression(exp);
	}
	
	public boolean match(Object obj){
		if(exp==null) return true;
		return this.match(exp, obj);
	}

	private boolean match(BaseExpression exp, Object obj) {
		if(exp==null) return true;
		if (exp instanceof OrExpression) {
			OrExpression orExp = (OrExpression) exp;
			return this.matchOrExp(orExp, obj);
		}else if(exp instanceof AndExpression){
			AndExpression andExp=(AndExpression) exp;
			return this.matchAndExp(andExp, obj);
		}else if(exp instanceof EqualExpression){
			EqualExpression ee=(EqualExpression) exp;
			return this.matchEqualExp(ee, obj);
		}else if(exp instanceof NotEqualExpression){
			NotEqualExpression ne=(NotEqualExpression) exp;
			return this.matchNotEqualExp(ne, obj);
		}else if(exp instanceof NotExpression){
			NotExpression ne=(NotExpression) exp;
			return !this.match(ne.getExp(), obj);
		}
		else{
			throw new RuntimeException("exp not implemented yet: "+exp.getClass().getCanonicalName());
		}
	}
	
	

	private boolean matchEqualExp(EqualExpression ee, Object obj){
		String left=ee.getLeft();
		String json=JSON.toJSONString(obj);
		ReadContext ctx = JsonPath.using(conf).parse(json);
		try{
			List<Object> list=ctx.read(left);
			if(list.size()!=1) return false;
			return ee.getRight().equals(list.iterator().next());
		}catch(Exception e){
		}
		return false;
	}
	
	private boolean matchNotEqualExp(NotEqualExpression ne, Object obj){
		String left=ne.getLeft();
		String json=JSON.toJSONString(obj);
		ReadContext ctx = JsonPath.using(conf).parse(json);
		try{
			List<Object> list=ctx.read(left);
			if(list.size()!=1) return false;
			return !ne.getRight().equals(list.iterator().next());
		}catch(Exception e){
		}
		return false;
	}

	private boolean matchAndExp(AndExpression andExp, Object obj) {
		List<BaseExpression> baseExps=andExp.getExps();
		if (baseExps == null || baseExps.size() == 0)
			throw new RuntimeException("Not a valid Expression! " + this);

		// 只要其中一个为false，那么整个表达式都为false
		for (BaseExpression baseExp : baseExps) {
			if(!this.match(baseExp, obj)){ 
				return false;
			}
		}
		// 所有的都是true，那么就是true
		return true;
	}

	private boolean matchOrExp(OrExpression orExp, Object obj) {
		if (orExp == null)
			return true;
		List<BaseExpression> andExps = orExp.getExps();
		if (andExps == null || andExps.size() == 0)
			throw new RuntimeException("Not a valid Expression! " + this);

		// 只要其中一个为true，那么整个表达式都为true
		for (BaseExpression andExp : andExps) {
			if (this.match(andExp, obj)) {
				return true;
			}
		}
		// 所有的都是false，那么就是false
		return false;
	}
	
	private BaseExpression optimizeExpression(BaseExpression exp) {
		if (exp instanceof AndExpression) {
			AndExpression andExp = (AndExpression) exp;
			ArrayList<BaseExpression> newExps=new ArrayList<>(andExp.getExps().size());
			for(BaseExpression be:andExp.getExps()){
				newExps.add(this.optimizeExpression(be));
			}
			andExp.setExps(newExps);
			if (andExp.getExps().size() == 1) {
				return andExp.getExps().get(0);
			}
		} else if (exp instanceof OrExpression) {
			OrExpression orExp = (OrExpression) exp;
			ArrayList<BaseExpression> newExps=new ArrayList<>(orExp.getExps().size());
			for(BaseExpression be:orExp.getExps()){
				newExps.add(this.optimizeExpression(be));
			}
			orExp.setExps(newExps);			
			if (orExp.getExps().size() == 1) {
				return orExp.getExps().get(0);
			}
		}
		return exp;
	}
}
