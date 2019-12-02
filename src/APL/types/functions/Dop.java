package APL.types.functions;

import APL.errors.*;
import APL.types.*;
import APL.*;

@SuppressWarnings("UnusedParameters")
public abstract class Dop extends Scopeable {
  
  protected Dop(Scope sc) {
    super(sc);
  }
  protected Dop() {
    super(null);
  }
  
  @Override
  public Type type() {
    return Type.dop;
  }
  
  public DerivedDop derive(Obj aa, Obj ww) {
    return new DerivedDop(aa, ww, this);
  }
  public Obj call(Obj aa, Obj ww, Value w, DerivedDop derv) {
    throw new IncorrectArgsError(repr()+" can't be called monadically", this, w);
  }
  public Obj call(Obj aa, Obj ww, Value a, Value w, DerivedDop derv) {
    throw new IncorrectArgsError(repr()+" can't be called dyadically", this, a);
  }
  public Obj callInv(Obj aa, Obj ww, Value w) {
    throw new DomainError(this+" doesn't support monadic inverting", this, w);
  }
  public Obj callInvW(Obj aa, Obj ww, Value a, Value w) {
    throw new DomainError(this+" doesn't support dyadic inverting of ⍵", this, w);
  }
  
  public String toString() {
    return repr();
  }
  public abstract String repr();
  
  // functions are equal per-object basis
  @Override public int hashCode() {
    return actualHashCode();
  }
  @Override public boolean equals(Obj o) {
    return this == o;
  }
}