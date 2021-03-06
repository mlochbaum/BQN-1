package BQN.types.callable.builtins.fns;

import BQN.Main;
import BQN.errors.*;
import BQN.tools.*;
import BQN.types.*;
import BQN.types.callable.builtins.FnBuiltin;

import java.util.Arrays;


public class GTBuiltin extends FnBuiltin {
  public String ln(FmtInfo f) { return ">"; }
  public Value identity() { return Num.ZERO; }
  
  public Value call(Value x) {
    if (x instanceof Arr && x.ia>0 && !x.quickDepth1()) {
      return merge(x.values(), x.shape, this);
    }
    return x;
  }
  
  public static Value merge(Value[] x, int[] sh, Callable blame) {
    assert x.length != 0;
    // if (x.length == 0) return new EmptyArr(sh, null);
    
    Value x0 = x[0];
    int[] sh0 = x0.shape;
    int[] resShape = new int[sh0.length + sh.length];
    System.arraycopy(sh, 0, resShape, 0, sh.length);
    System.arraycopy(sh0, 0, resShape, sh.length, sh0.length);
    
    MutVal res = new MutVal(resShape);
    
    int i = 0;
    for (Value c : x) {
      if (!Arrays.equals(c.shape, sh0)) {
        if (c.r() != sh0.length) throw new RankError(blame+": expected equal ranks of items (shapes "+Main.fArr(x0.shape)+" vs "+Main.fArr(c.shape)+")", blame);
        throw new DomainError(blame+": mismatched shapes ("+Main.fArr(sh0)+" vs "+Main.fArr(c.shape)+")", blame); // cannot be more specific due to the wide array of uses for merging
      }
      res.copy(c, 0, i, c.ia);
      i+= x0.ia;
    }
    return res.get();
  }
  
  
  public Value call(Value w, Value x) {
    return LTBuiltin.DF.call(x, w);
  }
  
}