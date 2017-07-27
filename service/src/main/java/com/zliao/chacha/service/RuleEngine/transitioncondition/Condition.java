package com.zliao.chacha.service.RuleEngine.transitioncondition;

public class Condition implements java.io.Serializable{
	//public final static Condition EPSILON_COND=new Condition(new EmptyExpression());
	public boolean isEpsilonCondition(){
		return exp instanceof EpsilonExpression;
	}
	
	public BaseExpression getExp() {
		return exp;
	}

	public void setExp(BaseExpression exp) {
		this.exp = exp;
	}  
	private BaseExpression exp; 
	private Condition(){
		
	}
	public Condition(BaseExpression exp) {
		this.exp = exp;
	}
	

	public String toString() {
		StringBuilder sb = new StringBuilder(""); 
		if (exp == null) {
			sb.append("TRUE\n");
		} else {
			sb.append(exp.toString(0)).append("\n");
		}
		return sb.toString();
	}
}
