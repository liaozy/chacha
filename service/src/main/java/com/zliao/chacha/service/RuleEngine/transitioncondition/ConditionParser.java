/* Generated By:JavaCC: Do not edit this line. ConditionParser.java */
package com.zliao.chacha.service.RuleEngine.transitioncondition;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import com.zliao.chacha.service.RuleEngine.tools.Util;
public class ConditionParser implements ConditionParserConstants {
    public ConditionParser(String s){
        this((new StringReader(s)));
    }

  final public Condition condition() throws ParseException {
        OrExpression expression=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NOT:
    case LPAREN:
    case QUOTED_STRING:
    case QUOTED_STRING2:
    case CLASSNAME:
      expression = orExp();
      break;
    default:
      jj_la1[0] = jj_gen;
      ;
    }
                {if (true) return new Condition(expression);}
    throw new Error("Missing return statement in function");
  }

  final public OrExpression orExp() throws ParseException {
    List<BaseExpression> andExps=new ArrayList<BaseExpression>();
    AndExpression andexpRtn;
    andexpRtn = andexp();
                andExps.add(andexpRtn);
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OR:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
      jj_consume_token(OR);
      andexpRtn = andexp();
                andExps.add(andexpRtn);
    }
                {if (true) return new OrExpression(andExps);}
    throw new Error("Missing return statement in function");
  }

  final public AndExpression andexp() throws ParseException {
    List<BaseExpression> baseExps=new ArrayList<BaseExpression>();
        BaseExpression expRtn;
    expRtn = baseExp();
                baseExps.add(expRtn);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AND:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
      jj_consume_token(AND);
      expRtn = baseExp();
                baseExps.add(expRtn);
    }
                {if (true) return new AndExpression(baseExps);}
    throw new Error("Missing return statement in function");
  }

  final public BaseExpression baseExp() throws ParseException {
        BaseExpression expsRtn;
        Token id;
        Object attr=null;
        int opType=0;
        String left;
        Object right;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case QUOTED_STRING:
    case QUOTED_STRING2:
    case CLASSNAME:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CLASSNAME:
        id = jj_consume_token(CLASSNAME);
                        //sb.append("attrs.get(\""+id.image+"\")!=null && attrs.get(\""+id.image+"\")");  
                        left=id.image;
        break;
      case QUOTED_STRING:
        id = jj_consume_token(QUOTED_STRING);
                        left=id.image.substring(1,id.image.length()-1);
                        left=Util.unescapeJavaString(left.toString());
        break;
      case QUOTED_STRING2:
        id = jj_consume_token(QUOTED_STRING2);
                        left=id.image.substring(1,id.image.length()-1);
                        left=Util.unescapeJavaString(left.toString());
        break;
      default:
        jj_la1[3] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case EQUALS:
        jj_consume_token(EQUALS);
                        opType=1;
        break;
      case NOTEQUAL:
        jj_consume_token(NOTEQUAL);
                        opType=2;
        break;
      case STARTSWITH:
        jj_consume_token(STARTSWITH);
                        opType=3;
        break;
      case ENDSWITH:
        jj_consume_token(ENDSWITH);
                        opType=4;
        break;
      case CONTAINS:
        jj_consume_token(CONTAINS);
                        opType=5;
        break;
      case LIKE:
        jj_consume_token(LIKE);
                        opType=6;
        break;
      case REGEX:
        jj_consume_token(REGEX);
                        opType=7;
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case QUOTED_STRING:
        id = jj_consume_token(QUOTED_STRING);
                        right=id.image.substring(1,id.image.length()-1);
                        right=Util.unescapeJavaString(right.toString());
        break;
      case QUOTED_STRING2:
        id = jj_consume_token(QUOTED_STRING2);
                        right=id.image.substring(1,id.image.length()-1);
                        right=Util.unescapeJavaString(right.toString());
        break;
      case TRUE:
        jj_consume_token(TRUE);
                        right=true;
        break;
      case FALSE:
        jj_consume_token(FALSE);
                        right=false;
        break;
      case INTEGER:
        jj_consume_token(INTEGER);
                        right=Integer.valueOf(id.image);
        break;
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                        switch(opType)
                        {
                        case 1:
                        expsRtn=new EqualExpression(left, right);
                                break;
                    case 2:
                        expsRtn=new NotEqualExpression(left, right);
                                break;
                        case 3:
                        expsRtn=new StartsWithExpression(left, (String)right);
                                break;
                        case 4:
                        expsRtn=new EndsWithExpression(left, (String)right);
                                break;
                        case 5:
                                expsRtn=new ContainsExpression(left, (String)right);
                                break;
                        case 7:
                                expsRtn=new RegexExpression(left, (String)right);
                                break;
                        default:
                            {if (true) throw new RuntimeException("unknown opType: "+opType);}
                        }
                {if (true) return expsRtn;}
      break;
    case LPAREN:
      jj_consume_token(LPAREN);
      expsRtn = orExp();
      jj_consume_token(RPAREN);
                        {if (true) return expsRtn;}
      break;
    case NOT:
      jj_consume_token(NOT);
      jj_consume_token(LPAREN);
      expsRtn = orExp();
      jj_consume_token(RPAREN);
                {if (true) return new NotExpression(expsRtn);}
      break;
    default:
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public ConditionParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[7];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x4300180,0x40,0x20,0x4300000,0x7f000,0xb00c00,0x4300180,};
   }

  /** Constructor with InputStream. */
  public ConditionParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public ConditionParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ConditionParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public ConditionParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ConditionParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public ConditionParser(ConditionParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(ConditionParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 7; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[27];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 7; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 27; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
