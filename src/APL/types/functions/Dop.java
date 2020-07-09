package APL.types.functions;

import APL.Type;
import APL.errors.*;
import APL.types.*;

public abstract class Dop extends Callable {
  
  protected Dop() { }
  
  @Override
  public Type type() {
    return Type.dop;
  }
  
  public Fun derive(Value f, Value g) {
    return new DerivedDop(f, g, this);
  }
  public Mop derive(Value g) {
    return new HalfDerivedDop(g, this);
  }
  
  public Value call(Value f, Value g, Value x, DerivedDop derv) {
    throw new IncorrectArgsError(repr()+" can't be called monadically", derv, x);
  }
  public Value call(Value f, Value g, Value w, Value x, DerivedDop derv) {
    throw new IncorrectArgsError(repr()+" can't be called dyadically", derv, w);
  }
  
  public Value callInv(Value f, Value g, Value x) {
    throw new DomainError(this+" doesn't support monadic inverting", this, x);
  }
  public Value callInvW(Value f, Value g, Value w, Value x) {
    throw new DomainError(this+" doesn't support dyadic inverting of 𝕩", this, x);
  }
  public Value callInvA(Value f, Value g, Value w, Value x) {
    throw new DomainError(this+" doesn't support dyadic inverting of 𝕨", this, x);
  }
  public Value under(Value f, Value g, Value o, Value x, DerivedDop derv) {
    Value v = o instanceof Fun? ((Fun) o).call(call(f, g, x, derv)) : o;
    return callInv(f, g, v);
  }
  public Value underW(Value f, Value g, Value o, Value w, Value x, DerivedDop derv) {
    Value v = o instanceof Fun? ((Fun) o).call(call(f, g, w, x, derv)) : o;
    return callInvW(f, g, w, v);
  }
  public Value underA(Value f, Value g, Value o, Value w, Value x, DerivedDop derv) {
    Value v = o instanceof Fun? ((Fun) o).call(call(f, g, w, x, derv)) : o;
    return callInvA(f, g, v, x);
  }
  
  public String toString() {
    return repr();
  }
  public abstract String repr();
  
  
  public Fun asFun() {
    throw new SyntaxError("Cannot interpret a composition as a function");
  }
  public boolean notIdentity() { return true; }
  
  // functions are equal per-object basis
  @Override public int hashCode() {
    return actualHashCode();
  }
  @Override public boolean equals(Obj o) {
    return this == o;
  }
}