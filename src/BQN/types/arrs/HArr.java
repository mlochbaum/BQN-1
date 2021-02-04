package BQN.types.arrs;

import BQN.errors.DomainError;
import BQN.types.*;

import java.util.ArrayList;

public class HArr extends Arr {
  private final Value[] arr;
  public HArr(Value[] v, int[] sh) {
    super(sh, v.length);
    arr = v;
  }
  public HArr(ArrayList<Value> v) { // 1D
    super(new int[]{v.size()});
    arr = v.toArray(new Value[0]);
  }
  public HArr(Value[] v) { // 1D
    super(new int[]{v.length}, v.length);
    arr = v;
  }
  
  public HArr(ArrayList<Value> v, int[] sh) {
    super(sh);
    arr = v.toArray(new Value[0]);
  }
  
  
  
  
  public Value get(int i) { return arr[i]; }
  
  
  
  public Value[] values     () { return arr        ; }
  public Value[] valuesClone() { return arr.clone(); }
  
  
  public Value fItem() {
    if (ia == 0) throw new DomainError("failed to get prototype");
    return arr[0].fMineS();
  }
  public Value fItemS() {
    if (ia == 0) return null;
    return arr[0].fMineS();
  }
  public Value ofShape(int[] sh) {
    assert ia == Arr.prod(sh);
    return new HArr(arr, sh);
  }
  
  
  
  public String asString() {
    StringBuilder r = new StringBuilder(ia);
    for (Value v : arr) {
      if (!(v instanceof Char)) throw new DomainError("Using array containing "+v.humanType(true)+" as string");
      r.append(((Char) v).chr);
    }
    return r.toString();
  }
}