package com.zliao.chacha.service.RuleEngine.patternmatcher;

import java.util.ArrayList;
 

public class MatchCondition {
	public boolean isGreedy() {
		return isGreedy;
	}

	public void setGreedy(boolean isGreedy) {
		this.isGreedy = isGreedy;
	}

	// public boolean isCaptured() {
	// return isCaptured;
	// }
	// public void setCaptured(boolean isCaptured) {
	// this.isCaptured = isCaptured;
	// }
	public BaseExpression getExp() {
		return exp;
	}

	public void setExp(BaseExpression exp) {
		this.exp = exp;
	}

	private boolean isGreedy = true;
	// private boolean isCaptured=false;
	private BaseExpression exp;
	private boolean isStared = false;
	private boolean isOutOfOrder=false;
	public int getStarMin() {
		return starMin;
	}

	public void setStarMin(int starMin) {
		this.starMin = starMin;
	}

	public int getStarMax() {
		return starMax;
	}

	public void setStarMax(int starMax) {
		if(starMax>100){
			throw new RuntimeException("starMax>100: "+starMax);
		}
		this.starMax = starMax;
	}

	private int starMin=0;
	private int starMax=100;
	
	public boolean isOutOfOrder() {
		return isOutOfOrder;
	}

	public void setOutOfOrder(boolean isOutOfOrder) {
		this.isOutOfOrder = isOutOfOrder;
	}

	public boolean isStared() {
		return isStared;
	}

	public void setStared(boolean isStared) {
		this.isStared = isStared;
	}

	public MatchCondition(BaseExpression exp, boolean isGreedy, boolean isStared, boolean isOutOfOrder) {
		this.exp = exp;
		this.isGreedy = isGreedy;
		// this.isCaptured=isCaptured;
		this.isStared = isStared;
		this.isOutOfOrder=isOutOfOrder;
		if(isStared && isOutOfOrder){
			throw new RuntimeException("*不支持乱序: "+(exp!=null?exp.toString(1):""));
		}
		if(exp!=null){
			this.exp=this.optimizeExp(exp);
		}
	}
	
	public BaseExpression optimizeAndOrExp(BaseExpression exp){
		if (exp instanceof AndExpression) {
			AndExpression andExp = (AndExpression) exp;
			ArrayList<BaseExpression> newExps=new ArrayList<>(andExp.getBaseExps().size());
			for(BaseExpression be:andExp.getBaseExps()){
				if(be instanceof AndExpression){
					AndExpression childExp=(AndExpression) be;
					for(BaseExpression childChild:childExp.getBaseExps()){
						BaseExpression optimized=this.optimizeAndOrExp(childChild);
						if(!newExps.contains(optimized)){
							newExps.add(optimized);
						} 
					}
				}else{
					newExps.add(this.optimizeAndOrExp(be));
				}
			}
			andExp.setBaseExps(newExps);
			if (andExp.getBaseExps().size() == 1) {
				return andExp.getBaseExps().get(0);
			}
		} else if (exp instanceof OrExpression) {
			OrExpression orExp = (OrExpression) exp;
			ArrayList<BaseExpression> newExps=new ArrayList<>(orExp.getAndExps().size());
			for(BaseExpression be:orExp.getAndExps()){
				if(be instanceof OrExpression){
					OrExpression childExp=(OrExpression) be;
					for(BaseExpression childChild:childExp.getAndExps()){
						BaseExpression optimized=this.optimizeAndOrExp(childChild);
						if(!newExps.contains(optimized)){
							newExps.add(optimized);
						}
					}
				}else{
					newExps.add(this.optimizeAndOrExp(be));
				}
			}
			orExp.setAndExps(newExps);
			if (orExp.getAndExps().size() == 1) {
				return orExp.getAndExps().get(0);
			}
		}
		return exp;
		
	}
	
	private BaseExpression optimizeExp(BaseExpression exp){
		if (exp instanceof AndExpression) {
			AndExpression andExp = (AndExpression) exp;
			ArrayList<BaseExpression> newExps=new ArrayList<>(andExp.getBaseExps().size());
			for(BaseExpression be:andExp.getBaseExps()){
				newExps.add(this.optimizeExp(be));
			}
			andExp.setBaseExps(newExps);
			if (andExp.getBaseExps().size() == 1) {
				return andExp.getBaseExps().get(0);
			}
		} else if (exp instanceof OrExpression) {
			OrExpression orExp = (OrExpression) exp;
			ArrayList<BaseExpression> newExps=new ArrayList<>(orExp.getAndExps().size());
			for(BaseExpression be:orExp.getAndExps()){
				newExps.add(this.optimizeExp(be));
			}
			orExp.setAndExps(newExps);			
			if (orExp.getAndExps().size() == 1) {
				return orExp.getAndExps().get(0);
			}
		}
		return exp;
	}
	
	public boolean isFuzzyMatch(){
		return isStared || !isGreedy;
	}
	
	/**
	 * 只有isFuzzyMatch==true才有意义
	 * @return
	 */
	public int matchMin(){
		if(isStared){
			return this.starMin;
		}else{
			return 0;
		}
	}
	/**
	 * 只有isFuzzyMatch==true才有意义
	 * @return
	 */
	public int matchMax(){
		if(isStared){
			return this.starMax;
		}else{
			return 1;
		}
	}
	/**
	 * 只有isFuzzyMatch==true才有意义
	 * @return
	 */
	public boolean isGreedyMatch(){
		if(isStared){
			return this.isGreedy;
		}else{
			return true;
		}
	}

	public boolean matches(MatchToken token) { 
		if (exp == null)
			return true;
		return exp.booleanValue(token);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append("isGreedy: " + isGreedy /* +", isCaptured: "+isCaptured */ + ", isStared: " + isStared
				+", isOutOfOrder: "+this.isOutOfOrder+", min="+this.starMin+", max="+this.starMax).append("\n");
		if (exp == null) {
			sb.append("TRUE\n");
		} else {
			sb.append(exp.toString(0)).append("\n");
		}
		return sb.toString();
	}
}
