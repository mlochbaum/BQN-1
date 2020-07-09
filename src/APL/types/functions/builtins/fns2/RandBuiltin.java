package APL.types.functions.builtins.fns2;

import APL.Scope;
import APL.errors.DomainError;
import APL.types.*;
import APL.types.arrs.*;
import APL.types.functions.Builtin;

import java.math.BigInteger;
import java.util.*;

public class RandBuiltin extends Builtin {
  private final Scope sc;
  
  public String repr() {
    return "?";
  }
  
  protected RandBuiltin(Scope sc) {
    this.sc = sc;
  }
  
  private final NumMV nf = new NumMV() {
    @Override public Value call(Num x) {
      if (x.num == 0) return new Num(sc.rand(1d));
      else return Num.of(sc.rand(x.asInt()));
    }
    @Override public void call(double[] res, double[] x) {
      for (int i = 0; i < res.length; i++) {
        res[i] = x[i]==0? sc.rand(1d) : Math.floor(sc.rand(x[i]));
      }
    }
    @Override public Value call(BigValue x) {
      if (x.i.signum()==0) throw new DomainError("?0L", x);
      BigInteger n;
      do {
        n = new BigInteger(x.i.bitLength(), sc.rnd);
      } while (n.compareTo(x.i) >= 0);
      return new BigValue(n);
    }
  };
  
  public Value call(Value x) {
    if (x instanceof SingleItemArr) {
      Value f = x.first();
      if (f instanceof Num && ((Num) f).num==2) {
        long[] ls = new long[BitArr.sizeof(x.ia)];
        for (int i = 0; i < ls.length; i++) {
          ls[i] = sc.randLong();
        }
        return new BitArr(ls, x.shape);
      }
    }
    return numM(nf, x);
  }
  
  public static Value on(Value w, Scope sc) {
    return new RandBuiltin(sc).call(w);
  }
  
  public Value call(Value w, Value x) {
    ArrayList<Integer> vs = new ArrayList<>(x.ia);
    int wi = w.asInt();
    int xi = x.asInt();
    for (int i = 0; i < xi; i++) {
      vs.add(i);
    }
    Collections.shuffle(vs, sc.rnd);
    double[] res = new double[wi];
    for (int i = 0; i < wi; i++) {
      res[i] = vs.get(i);
    }
    return new DoubleArr(res);
  }
}