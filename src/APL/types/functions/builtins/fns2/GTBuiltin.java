package APL.types.functions.builtins.fns2;

import APL.errors.DomainError;
import APL.types.*;
import APL.types.arrs.*;
import APL.types.functions.Builtin;
import APL.types.functions.builtins.fns.OldUpArrowBuiltin;


public class GTBuiltin extends Builtin {
  @Override public String repr() {
    return ">";
  }
  
  
  public Value call(Value w) {
    if (w instanceof Arr) {
      if (w instanceof DoubleArr || w instanceof ChrArr || w instanceof BitArr) return w;
      Value[] subs = w.values();
      return OldUpArrowBuiltin.merge(subs, w.shape, this);
    } else return w;
  }
  
  
  private static final D_NNeB DNF = new D_NNeB() {
    public boolean on(double a, double w) {
      return a > w;
    }
    public void on(BitArr.BA res, double a, double[] w) {
      for (double cw : w) res.add(a > cw);
    }
    public void on(BitArr.BA res, double[] a, double w) {
      for (double ca : a) res.add(ca > w);
    }
    public void on(BitArr.BA res, double[] a, double[] w) {
      for (int i = 0; i < a.length; i++) res.add(a[i] > w[i]);
    }
    public Value call(BigValue a, BigValue w) {
      return a.i.compareTo(w.i) > 0? Num.ONE : Num.ZERO;
    }
  };
  
  public Value call(Value a, Value w) {
    return numChrD(DNF, (ca, cw) -> ca>cw? Num.ONE : Num.ZERO,
      (ca, cw) -> { throw new DomainError("comparing "+ ca.humanType(true)+" and "+cw.humanType(true), this); },
      a, w);
  }
  
}