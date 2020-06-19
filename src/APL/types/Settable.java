package APL.types;

import APL.Type;
import APL.errors.ValueError;

public abstract class Settable extends Obj {
  final Value v;
  protected Settable(Value v) {
    this.v = v;
  }
  @Override
  public Type type() {
    return v == null? Type.var : v.type();
  }
  public abstract void set(Value v, boolean update, Callable blame);
  public Value get() {
    if (v == null) throw new ValueError("trying to get non-existing value", this);
    return v;
  }
  public Obj getOrThis() {
    if (v == null) return this;
    return v;
  }
  
  public Fun asFun() {
    return get().asFun();
  }
}