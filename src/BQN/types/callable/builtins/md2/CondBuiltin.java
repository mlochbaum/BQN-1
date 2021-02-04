package BQN.types.callable.builtins.md2;

import BQN.errors.*;
import BQN.tools.FmtInfo;
import BQN.types.*;
import BQN.types.callable.Md2Derv;
import BQN.types.callable.builtins.Md2Builtin;
import BQN.types.callable.builtins.fns.LBoxUBBuiltin;

public class CondBuiltin extends Md2Builtin {
  public String ln(FmtInfo f) { return "◶"; }
  
  public Value call(Value f, Value g, Value x, Md2Derv derv) {
    return get(f.call(x), g).call(x);
  }
  public Value call(Value f, Value g, Value w, Value x, Md2Derv derv) {
    return get(f.call(w, x), g).call(w, x);
  }
  
  private Value get(Value F, Value g) {
    if (F instanceof Num) {
      int f = F.asInt();
      if (g.r() != 1) throw new RankError("◶: Expected 𝕘 to be a vector, had rank "+g.r(), this);
      if (f>=g.ia || f<0) throw new LengthError("◶: 𝔽 out of bounds of 𝕘 (𝔽 = "+f+")", this);
      return g.get(f);
    }
    return LBoxUBBuiltin.on(F, g, this);
  }
}