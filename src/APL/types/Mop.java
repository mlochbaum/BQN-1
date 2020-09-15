package APL.types;

import APL.errors.*;
import APL.types.callable.DerivedMop;

public abstract class Mop extends Callable {
  
  protected Mop() { }
  
  public Value call(         Value x) { throw new SyntaxError("cannot interpret a 1-modifier as a function", this, x); }
  public Value call(Value w, Value x) { throw new SyntaxError("cannot interpret a 1-modifier as a function", this, x); }
  
  
  public Value derive(Value f) {
    return new DerivedMop(f, this);
  }
  public Value call(Value f, Value x, DerivedMop derv) {
    throw new IncorrectArgsError(repr()+" can't be called monadically", derv, x);
  }
  public Value call(Value f, Value w, Value x, DerivedMop derv) {
    throw new IncorrectArgsError(repr()+" can't be called dyadically", derv, w);
  }
  
  public Value callInv(Value f, Value x) {
    throw new DomainError(this+" doesn't support monadic inverting", this, x);
  }
  public Value callInvX(Value f, Value w, Value x) {
    throw new DomainError(this+" doesn't support dyadic inverting of 𝕩", this, x);
  }
  public Value callInvW(Value f, Value w, Value x) {
    throw new DomainError(this+" doesn't support dyadic inverting of 𝕨", this, x);
  }
  public Value under(Value f, Value o, Value x, DerivedMop derv) {
    Value v = o instanceof Fun? o.call(call(f, x, derv)) : o;
    return callInv(f, v);
  }
  public Value underW(Value f, Value o, Value w, Value x, DerivedMop derv) {
    Value v = o instanceof Fun? o.call(call(f, w, x, derv)) : o;
    return callInvX(f, w, v);
  }
  public Value underA(Value f, Value o, Value w, Value x, DerivedMop derv) {
    Value v = o instanceof Fun? o.call(call(f, w, x, derv)) : o;
    return callInvW(f, v, x);
  }
  
  public String toString() {
    return repr();
  }
  public abstract String repr();
  
  
  
  // functions are equal per-object basis
  public int hashCode() {
    return actualHashCode();
  }
  public boolean eq(Value o) {
    return this == o;
  }
}