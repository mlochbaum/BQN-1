package BQN.tokenizer.types;

import BQN.types.Num;

public class NumTok extends ConstTok {
  
  public NumTok(String line, int spos, int epos, double d) {
    super(line, spos, epos, Num.of(d));
  }
  
  public String toRepr() {
    return source();
  }
}