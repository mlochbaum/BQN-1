package APL.types.functions.builtins.dops;

import APL.types.*;
import APL.types.functions.Dop;
import APL.types.functions.builtins.mops.ReduceBuiltin;

import static APL.Main.up;

public class DotBuiltin extends Dop {
  public DotBuiltin() {
    super(".");
    valid = 0x011;
  }
  public Obj call(Obj aa, Obj ww, Value w) {
    throw up;
  }
  public Obj call(Obj aa, Obj ww, Value a, Value w) {
    return new ReduceBuiltin().derive(aa).call((Value) ((Fun) ww).call(a, w)); // TODO not lazy
  }
}