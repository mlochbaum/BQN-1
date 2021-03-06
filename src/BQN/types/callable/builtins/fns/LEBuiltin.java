package BQN.types.callable.builtins.fns;

import BQN.tools.*;
import BQN.types.*;
import BQN.types.arrs.BitArr;
import BQN.types.callable.builtins.FnBuiltin;


public class LEBuiltin extends FnBuiltin {
  public String ln(FmtInfo f) { return "≤"; }
  
  public static final Pervasion.VV2B DF = new Pervasion.VV2B() {
    public Value on(Primitive w, Primitive x) { return w.compareTo(x) <= 0? Num.ONE : Num.ZERO; }
    public void on(double   w, double[] x, BitArr.BA res) { for (double cx : x) { res.add( w <= cx); } }
    public void on(double[] w, double   x, BitArr.BA res) { for (double cw : w) { res.add(cw <=  x); } }
    public void on(double[] w, double[] x, BitArr.BA res) { for (int i = 0; i < w.length; i++) { res.add(w[i] <= x[i]); } }
    
    public void on(int   w, int[] x, BitArr.BA res) { for (int cx : x) { res.add( w <= cx); } }
    public void on(int[] w, int   x, BitArr.BA res) { for (int cw : w) { res.add(cw <=  x); } }
    public void on(int[] w, int[] x, BitArr.BA res) { for (int i = 0; i < w.length; i++) { res.add(w[i] <= x[i]); } }
    
    public void on(char   w, char[] x, BitArr.BA res) { for (char cx : x) { res.add( w <= cx); } }
    public void on(char[] w, char   x, BitArr.BA res) { for (char cw : w) { res.add(cw <=  x); } }
    public void on(char[] w, char[] x, BitArr.BA res) { for (int i = 0; i < w.length; i++) { res.add(w[i] <= x[i]); } }
    
    public Value on(boolean w, BitArr  x) { if(w) return x; return BitArr.s1(x); }
    public Value on(BitArr  w, boolean x) { if(x) return BitArr.s1(w); return BitArr.not(w); }
    public void  on(long[]  w, long[]  x, long[] res) { for (int i = 0; i < res.length; i++) res[i] = (~w[i]) | x[i]; }
  };
  
  public Value call(Value w, Value x) {
    return DF.call(w, x);
  }
}