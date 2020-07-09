package APL.types.functions.builtins.fns2;

import APL.types.*;
import APL.types.arrs.BitArr;
import APL.types.functions.Builtin;


public class EQBuiltin extends Builtin {
  @Override public String repr() {
    return "=";
  }
  
  
  
  private static final D_NNeB DNF = new D_NNeB() {
    public boolean on(double w, double x) {
      return w == x;
    }
    public void on(BitArr.BA res, double w, double[] x) {
      for (double cw : x) res.add(w == cw);
    }
    public void on(BitArr.BA res, double[] w, double x) {
      for (double ca : w) res.add(ca == x);
    }
    public void on(BitArr.BA res, double[] w, double[] x) {
      for (int i = 0; i < w.length; i++) res.add(w[i] == x[i]);
    }
    public Value call(BigValue w, BigValue x) {
      return w.equals(x)? Num.ONE : Num.ZERO;
    }
  };
  private static final D_BB DBF = new D_BB() {
    @Override public Value call(boolean w, BitArr x) {
      if (w) return x;
      return NotBuiltin.call(x);
    }
    @Override public Value call(BitArr w, boolean x) {
      if (x) return w;
      return NotBuiltin.call(w);
    }
    @Override public Value call(BitArr w, BitArr x) {
      BitArr.BC bc = BitArr.create(x.shape);
      for (int i = 0; i < bc.arr.length; i++) bc.arr[i] = ~(w.arr[i] ^ x.arr[i]);
      return bc.finish();
    }
  };
  
  public Value call(Value w0, Value x0) {
    return ncbaD(DNF, DBF, (w, x) -> w==x? Num.ONE : Num.ZERO, (w, x) -> w.equals(x)? Num.ONE : Num.ZERO, w0, x0);
  }
}