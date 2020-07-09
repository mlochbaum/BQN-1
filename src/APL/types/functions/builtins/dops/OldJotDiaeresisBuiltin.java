package APL.types.functions.builtins.dops;

import APL.types.*;
import APL.types.functions.*;

public class OldJotDiaeresisBuiltin extends Dop {
  @Override public String repr() {
    return "⍤";
  }
  
  
  
  @Override
  public Value call(Value aa, Value ww, Value a, Value w, DerivedDop derv) {
    Fun aaf = aa.asFun(); Fun wwf = ww.asFun();
    return aaf.call(wwf.call(a, w));
  }
  
  public Value call(Value aa, Value ww, Value w, DerivedDop derv) {
    Fun aaf = aa.asFun(); Fun wwf = ww.asFun();
    return aaf.call(wwf.call(w));
  }
  
  public Value callInv(Value aa, Value ww, Value w) {
    Fun aaf = aa.asFun(); Fun wwf = ww.asFun();
    return aaf.call(wwf.call(w));
  }
  
  public Value callInvW(Value aa, Value ww, Value a, Value w) {
    Fun aaf = aa.asFun(); Fun wwf = ww.asFun();
    return wwf.callInvW(a, aaf.callInv(w));
  }
  
  public Value callInvA(Value aa, Value ww, Value a, Value w) {
    Fun aaf = aa.asFun(); Fun wwf = ww.asFun();
    return wwf.callInvA(aaf.callInv(a), w);
  }
}