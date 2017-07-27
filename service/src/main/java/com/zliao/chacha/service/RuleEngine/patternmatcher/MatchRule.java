package com.zliao.chacha.service.RuleEngine.patternmatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchRule {
	private ArrayList<MatchCondition> conditions;

	public ArrayList<MatchCondition> getConditions() {
		return conditions;
	}

	private String originalRule;
	private String actionClass;
	private ArrayList<String> params;
	private MatchAction matchAction;

	public ArrayList<String> getParams() {
		return params;
	}

	public String getActionClass() {
		return actionClass;
	}

	private double score;

	public double getScore() {
		return score;
	}

	public String getOriginalRule() {
		return originalRule;
	}

	public MatchRule(String originalRule, ArrayList<MatchCondition> conditions, double score, String actionClass,
			ArrayList<String> params) throws Exception {
		this.conditions = conditions;
		this.originalRule = originalRule;
		this.score = score;
		this.actionClass = actionClass;
		this.params = params;
		matchAction = (MatchAction) Class.forName(actionClass).newInstance();
		for (String param : this.params) {
			if (param.startsWith("$")) {
				int idx = Integer.valueOf(param.substring(1));
				if (idx < 0 || idx >= conditions.size()) {
					throw new RuntimeException("indx out of range: " + (idx));
				}
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append("conditions: \n");
		if (conditions != null) {
			for (MatchCondition condition : conditions) {
				sb.append("\t").append(condition).append("\n");
			}
		}

		return sb.toString();
	}

	/**
	 * 如果没有任何match 返回null
	 * 
	 * @param tokens
	 * @return
	 */
	public Object patternMatch(ArrayList<MatchToken> tokens) {
		int[] matchIdx = new int[this.conditions.size()];
		boolean match = recurMatch(tokens, conditions, 0, 0, matchIdx);
		if (match) {
			Object[] paramArray = new Object[params.size()];

			for (int i = 0; i < params.size(); i++) {
				String param = params.get(i);
				if (param.startsWith("$")) {
					int idx = Integer.valueOf(param.substring(1));
					int matchStart = (idx == 0 ? 0 : matchIdx[idx - 1]);
					int matchEnd = matchIdx[idx];
					int count = matchEnd - matchStart;
					if (count == 0) {
						paramArray[i] = null;
					} else if (count > 1) {
						ArrayList<MatchToken> tks = new ArrayList<MatchToken>(count);
						for (int j = matchStart; j < matchEnd; j++) {
							tks.add(tokens.get(j));
						}
						paramArray[i] = tks;
					} else {// ==1
						paramArray[i] = tokens.get(matchStart);
					}
				} else {
					paramArray[i] = param;
				}
			}

			return this.matchAction.doAction(paramArray, this.score);
		}
		return null;
	}

	public Object patternMatch2(ArrayList<MatchToken> tokens) {
		int[] matchIdx = new int[this.conditions.size()];
		boolean match = recurMatch2(tokens, conditions, 0, 0, matchIdx);
		if (match) {
			Object[] paramArray = new Object[params.size()];

			for (int i = 0; i < params.size(); i++) {
				String param = params.get(i);
				if (param.startsWith("$")) {
					int idx = Integer.valueOf(param.substring(1));
					int matchStart = (idx == 0 ? 0 : matchIdx[idx - 1]);
					int matchEnd = matchIdx[idx];
					int count = matchEnd - matchStart;
					if (count == 0) {
						paramArray[i] = null;
					} else if (count > 1) {
						ArrayList<MatchToken> tks = new ArrayList<MatchToken>(count);
						for (int j = matchStart; j < matchEnd; j++) {
							tks.add(tokens.get(j));
						}
						paramArray[i] = tks;
					} else {// ==1
						paramArray[i] = tokens.get(matchStart);
					}
				} else {
					paramArray[i] = param;
				}
			}

			return this.matchAction.doAction(paramArray, this.score);
		}
		return null;
	}

	private static boolean matchToken(MatchToken token, MatchCondition cond) {
		return cond.matches(token);
	}

	private static int matchToken(ArrayList<MatchToken> tokens, int start, MatchCondition cond) {
		String text = textCondition(cond);
		if (text == null) {
			if (cond.matches(tokens.get(start))) {
				return 1;
			} else {
				return -1;
			}
		} else {
			int i = start;
			int j = 0;
			for (; i < tokens.size() && j < text.length();) {
				MatchToken token = tokens.get(i);
				String word = (String) token.getAttr(MatchToken.KEY_TEXT);
				if (text.startsWith(word, j)) {
					j += word.length();
					i++;
				} else {
					return -1;
				}
			}
			if (j == text.length()) {
				return i - start;
			} else {
				return -1;
			}
		}
	}

	private static String textCondition(MatchCondition cond) {
		BaseExpression be = cond.getExp();
		if(be==null) return null;
		if (!(be instanceof EqualExpression))
			return null;
		EqualExpression ee = (EqualExpression) be;
		if (!ee.getLeft().equals(MatchToken.KEY_TEXT))
			return null;
		return (String) ee.getRight();
	}

	public static boolean recurMatch(ArrayList<MatchToken> tokens, ArrayList<MatchCondition> conditions,
			int curCondIndex, int curTokenIndex, int[] curMatches) {
		boolean noCondition = (curCondIndex == conditions.size());
		boolean noToken = (curTokenIndex == tokens.size());
		if (noCondition && noToken)
			return true; // 递归出口
		if (noCondition && !noToken)
			return false; // 还有token没有匹配完成，失败
		if (!noCondition && noToken) {
			// 判断剩下的条件能否匹配 空，也就是全部是star
			for (int i = curCondIndex; i < conditions.size(); i++) {
				MatchCondition cond = conditions.get(i);
				if (!cond.isStared()) {
					return false;
				} else {
					curMatches[i] = tokens.size();
				}
			}
			return true; // 剩下的都是star，那么返回true
		}
		MatchCondition cond = conditions.get(curCondIndex);

		if (cond.isStared()) {// *
			if (cond.isGreedy()) {// 贪婪的匹配，没有？
				int i = curTokenIndex;
				for (; i < tokens.size(); i++) {
					if (!matchToken(tokens.get(i), cond)) {
						break;
					}
				}
				// curTokenIndex ... i-1 为匹配的项，如果一个都不匹配，那么i = curTokenIndex
				for (int j = i - 1; j >= curTokenIndex - 1; j--) {
					curMatches[curCondIndex] = j + 1;
					if (recurMatch(tokens, conditions, curCondIndex + 1, j + 1, curMatches)) {
						return true;
					}
				}
				return false;
			} else {// 非贪婪的匹配
				// 先不匹配任何东西
				curMatches[curCondIndex] = curTokenIndex;
				if (recurMatch(tokens, conditions, curCondIndex + 1, curTokenIndex, curMatches)) {
					return true;
				}
				// 如果不行，再尝试别的
				int i = curTokenIndex;
				for (; i < tokens.size(); i++) {
					if (!matchToken(tokens.get(i), cond)) {
						return false;
					}
					curMatches[curCondIndex] = i + 1;
					if (recurMatch(tokens, conditions, curCondIndex + 1, i + 1, curMatches)) {
						return true;
					}
				}
				return false;
			}
		} else {// 普通匹配
			// 如果当前下标超过了，那么返回false
			MatchToken token = tokens.get(curTokenIndex);
			if (!matchToken(token, cond))
				return false;
			curMatches[curCondIndex] = curTokenIndex + 1;
			return recurMatch(tokens, conditions, curCondIndex + 1, curTokenIndex + 1, curMatches);
		}

	}

	/**
	 * text的分词问题
	 * 
	 * @param tokens
	 * @param conditions
	 * @param curCondIndex
	 * @param curTokenIndex
	 * @param curMatches
	 * @return
	 */
	public static boolean recurMatch2(ArrayList<MatchToken> tokens, ArrayList<MatchCondition> conditions,
			int curCondIndex, int curTokenIndex, int[] curMatches) {
		boolean noCondition = (curCondIndex == conditions.size());
		boolean noToken = (curTokenIndex == tokens.size());
		if (noCondition && noToken)
			return true; // 递归出口
		if (noCondition && !noToken)
			return false; // 还有token没有匹配完成，失败
		if (!noCondition && noToken) {
			// 判断剩下的条件能否匹配 空，也就是全部是star
			for (int i = curCondIndex; i < conditions.size(); i++) {
				MatchCondition cond = conditions.get(i);
				if (!cond.isStared()) {
					return false;
				} else {
					curMatches[i] = tokens.size();
				}
			}
			return true; // 剩下的都是star，那么返回true
		}
		MatchCondition cond = conditions.get(curCondIndex);

		if (cond.isStared()) {// *
			if (cond.isGreedy()) {// 贪婪的匹配，没有？
				int i = curTokenIndex;
				for (; i < tokens.size(); i++) {
					if (!matchToken(tokens.get(i), cond)) {
						break;
					}
				}
				// curTokenIndex ... i-1 为匹配的项，如果一个都不匹配，那么i = curTokenIndex
				for (int j = i - 1; j >= curTokenIndex - 1; j--) {
					curMatches[curCondIndex] = j + 1;
					if (recurMatch2(tokens, conditions, curCondIndex + 1, j + 1, curMatches)) {
						return true;
					}
				}
				return false;
			} else {// 非贪婪的匹配
				// 先不匹配任何东西
				curMatches[curCondIndex] = curTokenIndex;
				if (recurMatch2(tokens, conditions, curCondIndex + 1, curTokenIndex, curMatches)) {
					return true;
				}
				// 如果不行，再尝试别的
				int i = curTokenIndex;
				for (; i < tokens.size(); i++) {
					if (!matchToken(tokens.get(i), cond)) {
						return false;
					}
					curMatches[curCondIndex] = i + 1;
					if (recurMatch2(tokens, conditions, curCondIndex + 1, i + 1, curMatches)) {
						return true;
					}
				}
				return false;
			}
		} else {// 普通匹配
			// 如果当前下标超过了，那么返回false
			int matchCount = matchToken(tokens, curTokenIndex, cond);
			if (matchCount == -1)
				return false;
			curMatches[curCondIndex] = curTokenIndex + matchCount;
			return recurMatch2(tokens, conditions, curCondIndex + 1, curTokenIndex + matchCount, curMatches);
		}

	}
	
	/**
	 * 支持?，支持范围
	 * @param tokens
	 * @param conditions
	 * @param curCondIndex
	 * @param curTokenIndex
	 * @param curMatches
	 * @return
	 */
	public static boolean recurMatch3(ArrayList<MatchToken> tokens, ArrayList<MatchCondition> conditions,
			int curCondIndex, int curTokenIndex, int[] curMatches) {
		boolean noCondition = (curCondIndex == conditions.size());
		boolean noToken = (curTokenIndex == tokens.size());
		if (noCondition && noToken)
			return true; // 递归出口
		if (noCondition && !noToken)
			return false; // 还有token没有匹配完成，失败
		if (!noCondition && noToken) {
			// 判断剩下的条件能否匹配 空，也就是全部是star
			for (int i = curCondIndex; i < conditions.size(); i++) {
				MatchCondition cond = conditions.get(i);
				boolean canMatch=cond.isFuzzyMatch() && cond.matchMin()==0;
				if (!canMatch) {
					return false;
				} else {
					curMatches[i] = tokens.size();
				}
			}
			return true; // 剩下的都是star，那么返回true
		}
		MatchCondition cond = conditions.get(curCondIndex);
		
		boolean isFuzzyCondition=cond.isFuzzyMatch();
		
		if (isFuzzyCondition) {// *
			boolean isGreedy=cond.isGreedyMatch();
			int minMatch=cond.matchMin();
			int maxMatch=cond.matchMax();
			if (isGreedy) {// 贪婪的匹配
				int i = curTokenIndex;
				for (; i < Math.min(tokens.size(),curTokenIndex+maxMatch); i++) {
					if (!matchToken(tokens.get(i), cond)) {
						break;
					}
				}
				// curTokenIndex ... i-1 为匹配的项，如果一个都不匹配，那么i = curTokenIndex
				for (int j = i - 1; j >= curTokenIndex - 1 + minMatch; j--) {
					curMatches[curCondIndex] = j + 1;
					if (recurMatch3(tokens, conditions, curCondIndex + 1, j + 1, curMatches)) {
						return true;
					}
				}
				return false;
			} else {// 非贪婪的匹配 
				if(curTokenIndex+minMatch>tokens.size()) return false;
				for(int i=curTokenIndex;i<curTokenIndex+minMatch;i++){
					if (!matchToken(tokens.get(i), cond)) {
						return false;
					}
				}
				int maxIdx=curTokenIndex+maxMatch;
				curTokenIndex+=minMatch;
				curMatches[curCondIndex] = curTokenIndex;
				
				if (recurMatch3(tokens, conditions, curCondIndex + 1, curTokenIndex, curMatches)) {
					return true;
				}
				// 如果不行，再尝试别的
				int i = curTokenIndex;
				for (; i < Math.min(tokens.size(), maxIdx); i++) {
					if (!matchToken(tokens.get(i), cond)) {
						return false;
					}
					curMatches[curCondIndex] = i + 1;
					if (recurMatch3(tokens, conditions, curCondIndex + 1, i + 1, curMatches)) {
						return true;
					}
				}
				return false;
			}
		} else {// 普通匹配
			// 如果当前下标超过了，那么返回false
			int matchCount = matchToken(tokens, curTokenIndex, cond);
			if (matchCount == -1)
				return false;
			curMatches[curCondIndex] = curTokenIndex + matchCount;
			return recurMatch3(tokens, conditions, curCondIndex + 1, curTokenIndex + matchCount, curMatches);
		}

	}
	
	public static String printMatch(ArrayList<MatchCondition> conditions, ArrayList<MatchToken> tokens, int[] idx){
		int[] startIdxs=new int[idx.length]; 
		for(int i=0;i<idx.length;i++){
			if(i==0){
				startIdxs[0]=0;
			}else{
				startIdxs[i]=idx[i-1];
			}
		}
		
		return printMatch(conditions, tokens, startIdxs, idx);
	}
	
	public static String printMatch(ArrayList<MatchCondition> conditions, ArrayList<MatchToken> tokens, int[] startIdxs, int[] endIdxs){
		StringBuilder sb=new StringBuilder("");
		for(int i=0;i<startIdxs.length;i++){
			int startIdx=startIdxs[i];
			int endIdx=endIdxs[i];
			MatchCondition cond=conditions.get(i);
			sb.append(cond.toString()+"\n");
			if(startIdx==-1){
				sb.append("\tMatchNothing\n");
			}else{
				for(int j=startIdx;j<endIdx;j++){
					MatchToken tk=tokens.get(j);
					sb.append("\t=match=> "+tk.toString()+"\n");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 增加对%的支持，贪心的算法
	 * @param tokens
	 * @param conditions
	 * @param curMatches
	 * @return
	 */
	public static boolean recurMatch4(ArrayList<MatchToken> tokens, ArrayList<MatchCondition> conditions, int[] curMatches1, int[] curMatches2) {
		ArrayList<Integer> ooConditionIdxs=findOutOfOrderConditions(conditions);
		int[] curMatches=new int[conditions.size()	];
		if(ooConditionIdxs.size()==0){
			boolean match = recurMatch3(tokens, conditions, 0, 0, curMatches);
			if(match){
				for(int i=0;i<conditions.size();i++){
					curMatches2[i]=curMatches[i];
					if(i==0){
						curMatches1[i]=0;
					}else{
						curMatches1[i]=curMatches[i-1];
					}
				}
			}
			return match;
		}
		
		
		int[] ooMatchIdxs=new int[ooConditionIdxs.size()];
		Arrays.fill(ooMatchIdxs, -1);
		//因为可以加？，所以可能匹配，也可能不匹配，不匹配就是-1
		ArrayList<Integer> tkMatched=new ArrayList<>();
		int p=0;
		for(int ooIdx:ooConditionIdxs){
			MatchCondition cond=conditions.get(ooIdx);
			for(int i=0;i<tokens.size();i++){
				if(tkMatched.contains(i)) continue;
				if(matchToken(tokens.get(i), cond)){
					ooMatchIdxs[p]=i;
					tkMatched.add(i);
					break;
				}
			}
			
			if(ooMatchIdxs[p]==-1 && cond.isGreedy()){
				return false;
			}
			p++;
		}
		
		ArrayList<MatchToken> tokensLeft=new ArrayList<>(tokens.size()-tkMatched.size());
		for(int i=0;i<tokens.size();i++){
			if(!tkMatched.contains(i)){
				tokensLeft.add(tokens.get(i));
			}
		}
		ArrayList<MatchCondition> conditionsLeft=new ArrayList<>(conditions.size()-ooConditionIdxs.size());
		for(int i=0;i<conditions.size();i++){
			if(!ooConditionIdxs.contains(i)){
				conditionsLeft.add(conditions.get(i));
			}
		}
		
		int[] matchIdxLeft=new int[conditionsLeft.size()];
		boolean match=recurMatch3(tokensLeft, conditionsLeft, 0, 0, matchIdxLeft);
		if(!match) return false;
		
		int[] tokenIdxMapping=fixIndex(tokensLeft.size(), tkMatched);
		int[] condIdxMapping=fixIndex(conditionsLeft.size(), ooConditionIdxs);
 
		//首先把oo的填上
		for(int i=0;i<ooConditionIdxs.size();i++){
			int ooIdx=ooConditionIdxs.get(i);
			int tokenIdx=ooMatchIdxs[i];
			if(tokenIdx==-1){
				curMatches1[ooIdx]=curMatches2[ooIdx]=-1;
			}else{
				curMatches1[ooIdx]=tokenIdx;
				curMatches2[ooIdx]=tokenIdx+1;
			}
		}
		
		for(int i=0;i<matchIdxLeft.length;i++){
			int fixedCondIdx=condIdxMapping[i];
			int matchEnd=matchIdxLeft[i];
			int matchStart=(i==0?0:matchIdxLeft[i-1]);
			if(matchStart<matchEnd){
				int fixedTkStart=tokenIdxMapping[matchStart];
				int fixedTkEnd=tokenIdxMapping[matchEnd-1]+1;
				curMatches1[fixedCondIdx]=fixedTkStart;
				curMatches2[fixedCondIdx]=fixedTkEnd;
			}else{
				curMatches1[fixedCondIdx]=-1;
				curMatches2[fixedCondIdx]=-1;
			}
		}
		
		return true;
	}
	
	static int[] fixIndex(int remainingCount, ArrayList<Integer> removedIdxs){
		if(remainingCount==0) return new int[0];
		int[] result=new int[remainingCount];
		int lastIdx=0;
		int i=0;
		int recovered=0;
		Collections.sort(removedIdxs);
		for(int idx:removedIdxs){
			//[lastIdx, idx)
			for(int j=lastIdx;j<idx;j++){
				result[i]=i+recovered;
				i++;
			}
			recovered++;
			lastIdx=idx+1;
		}
		//[lastIdx, remainingCount+removedIdxs.size())
		for(int j=lastIdx;j<remainingCount+removedIdxs.size();j++){
			result[i]=i+recovered;
			i++;
		}
		//remove after algorithm stable
		if(i!=remainingCount) {
			StringBuilder sb=new StringBuilder("remainingCount: "+remainingCount+"i: "+i);
			for(int idx:removedIdxs){
				sb.append(" "+idx);
			}
			throw new RuntimeException("algobug "+sb.toString());
		}
		return result;
	}
	
	private static ArrayList<Integer> findOutOfOrderConditions(ArrayList<MatchCondition> conditions){
		ArrayList<Integer> result=new ArrayList<>(3);
		for(int i=0;i<conditions.size();i++){
			MatchCondition cond=conditions.get(i);
			if(cond.isOutOfOrder()){
				result.add(i);
			}
		}
		return result;
	}
}
