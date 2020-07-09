package APL.types;

import APL.Main;
import APL.errors.*;
import APL.types.arrs.*;
import APL.types.functions.builtins.fns2.*;

import java.util.*;

public abstract class Arr extends Value {
  public Arr(int[] shape) {
    super(shape);
  }
  public Arr(int[] shape, int ia) {
    super(shape, ia, shape.length);
  }
  public Arr(int[] shape, int ia, int rank) {
    super(shape, ia, rank);
  }
  
  public String basicFormat(boolean quote) {
    if (ia == 0) {
      String mr = safePrototype() instanceof Char? "\"\"" : "⟨⟩";
      if (rank == 1) return mr;
      else return Main.formatAPL(shape) + "⥊" + mr;
    }
    if (rank == 1) { // strings
      StringBuilder all = new StringBuilder();
      for (Value v : this) {
        if (v instanceof Char) {
          char c = ((Char) v).chr;
          if (quote && c == '\"') all.append("\"\"");
          else all.append(c);
        } else return null;
      }
      if (quote) return "\"" + all + "\"";
      else return all.toString();
    }
    return null;
  }
  public String toString() {
    String f = basicFormat(Main.quotestrings);
    if (f != null) return f;
    
    if (Main.quotestrings) {
      boolean str = true;
      for (Value v : this) {
        if (!(v instanceof Char)) {
          str = false;
          break;
        }
      }
      if (str) return oneliner();
    }
    
    if (rank == 0) return "<"+first().toString().replace("\n", "\n ");
    if (ia == 1) {
      Value c = get(0);
      if (c instanceof Primitive || rank > 2) {
        String enc = c instanceof Primitive? "" : "<";
        if (rank==1) return "⟨"+c+"⟩";
        String pre = Main.formatAPL(shape);
        return pre + "⥊" + enc + c.toString().replace("\n", "\n" + Main.repeat(" ", pre.length()+2));
      }
    }
    if (rank == 1) { // simple vectors
      StringBuilder res = new StringBuilder();
      var simple = true;
      for (Value v : this) {
        if (res.length() > 0) res.append('‿');
        if (v != null) {
          if (!(v instanceof Primitive)) {
            simple = false;
            break;
          }
          res.append(v.oneliner());
        } else res.append("NULLPTR");
      }
      if (simple) return res.toString();
    }
    
    if (rank == 2) {
      boolean charmat = true;
      if (!(this instanceof ChrArr)) {
        for (Value v : this) {
          if (!(v instanceof Char)) {
            charmat = false;
            break;
          }
        }
      }
      
      if (charmat) {
        StringBuilder b = new StringBuilder();
        int i = 0;
        for (Value v : this) {
          if (i++ % shape[1] == 0 && i!=1) b.append('\n');
          b.append(((Char) v).chr);
        }
        return b.toString();
      }
    }
    
    if (rank < 3) { // boxed arrays
      int w = rank==1? shape[0] : shape[1];
      int h = rank==1? 1 : shape[0];
      String[][][] stringified = new String[w][h][];
      int[][] itemWidths = new int[w][h];
      int[] widths = new int[w];
      int[] heights = new int[h];
      var simple = true;
      int x=0, y=0;
      for (Value v : this) {
        if (v == null) v = Main.toAPL("NULLPTR");
        simple&= v instanceof Primitive;
        var c = v.toString().split("\n");
        var cw = 0;
        for (var ln : c) {
          cw = Math.max(ln.length(), cw);
        }
        itemWidths[x][y] = cw;
        widths[x] = Math.max(widths[x], cw);
        heights[y] = Math.max(heights[y], c.length);
        stringified[x][y] = c;
        x++;
        if (x==w) {
          x = 0;
          y++;
        }
      }
      int borderSize = simple? 0 : 1;
      int rw = simple? -1 : 1,
      rh = borderSize ; // result w&h;
      for (x = 0; x < w; x++) rw+= widths[x]+1;
      for (y = 0; y < h; y++) rh+= heights[y]+borderSize;
      char[][] chars = new char[rh][rw];
      int rx = borderSize , ry; // x&y in chars
      for (x = 0; x < w; x++) {
        ry = borderSize;
        for (y = 0; y < h; y++) {
          String[] cobj = stringified[x][y];
          for (int cy = 0; cy < cobj.length; cy++) {
            String s = cobj[cy];
            char[] line = s.toCharArray();
            int sx = get(y*w + x) instanceof Num? rx+widths[x]-itemWidths[x][y] : rx;
            System.arraycopy(line, 0, chars[ry + cy], sx, line.length);
          }
          ry+= heights[y]+borderSize;
        }
        rx+= widths[x]+1;
      }
      if (!simple) { // draw borders
        rx = 0;
        for (x = 0; x < w; x++) {
          ry = 0;
          for (y = 0; y < h; y++) {
            chars[ry][rx] = '┼';
            for (int cx = 1; cx <=  widths[x]; cx++) chars[ry][rx+cx] = '─';
            for (int cy = 1; cy <= heights[y]; cy++) chars[ry+cy][rx] = '│';
            if (x == 0) {
              for (int cy = 1; cy <= heights[y]; cy++) chars[ry+cy][rw-1] = '│';
              chars[ry][rw-1] = y==0? '┐' : '┤';
              chars[ry][0] = '├';
            }
            ry+= heights[y]+borderSize;
          }
          chars[0][rx] = '┬';
          chars[rh-1][rx] = x==0?'└' : '┴';
          for (int cx = 1; cx <=  widths[x]; cx++) chars[rh-1][rx+cx] = '─';
          rx+= widths[x]+1;
        }
        chars[0][0] = '┌';
        chars[rh-1][rw-1] = '┘';
      }
      for (char[] ca : chars) {
        for (int i = 0; i < ca.length; i++) {
          if (ca[i] == 0) ca[i] = ' ';
        }
      }
      StringBuilder res = new StringBuilder();
      boolean next = false;
      for (char[] ln : chars) {
        if (next) res.append('\n');
        res.append(ln);
        next = true;
      }
      return res.toString();
    } else return oneliner();
  }
  public String oneliner() {
    String f = basicFormat(true);
    if (f != null) return f;
    if (rank == 0) return "<" + get(0).oneliner();
    if (rank == 1) {
      if (ia == 1) return "⟨"+get(0).oneliner()+"⟩";
      boolean vec = MatchBuiltin.full(this) != 1;
      StringBuilder b = new StringBuilder();
      if (vec) b.append('⟨');
      boolean first = true;
      for (Value v : this) {
        if (first) first = false;
        else b.append(vec? ',' : '‿');
        b.append(v.oneliner());
      }
      if (vec) b.append('⟩');
      return b.toString();
    }
    
    StringBuilder b = new StringBuilder(">⟨");
    for (int i = 0; i < shape[0]; i++) {
      if (i != 0) b.append(",");
      b.append(LBoxBuiltin.getCell(i, this, null).oneliner());
    }
    b.append("⟩");
    return b.toString();
  }
  public Arr reverseOn(int dim) {
    if (rank == 0) {
      if (dim != 0) throw new DomainError("rotating a scalar with a non-0 axis", this);
      return this;
    }
    if (dim < 0) dim+= rank;
    // 2×3×4:
    // 0 - 3×4s for 2
    // 1 - 4s for 3
    // 2 - 1s for 4
    int chunkS = 1;
    int cPSec = shape[dim]; // chunks per section
    for (int i = rank-1; i > dim; i--) {
      chunkS*= shape[i];
    }
    int sec = chunkS * cPSec; // section length
    Value[] res = new Value[ia];
    int c = 0;
    while (c < ia) {
      for (int i = 0; i < cPSec; i++) {
        for (int j = 0; j < chunkS; j++) {
          res[c + (cPSec-i-1)*chunkS + j] = get(c + i*chunkS + j);
        }
      }
      c+= sec;
    }
    return Arr.create(res, shape);
  }
  
  
  public static Arr create(Value[] v) {
    return create(v, new int[]{v.length});
  }
  public static Arr create(Value[] v, int[] sh) { // note, doesn't attempt individual item squeezing
    assert Arr.prod(sh) == v.length : v.length+" ≢ ×´"+Main.formatAPL(sh);
    if (v.length == 0) return new EmptyArr(sh, null);
    if (v[0] instanceof Num) {
      double[] da = new double[v.length];
      for (int i = 0; i < v.length; i++) {
        if (v[i] instanceof Num) da[i] = ((Num)v[i]).num;
        else {
          da = null;
          break;
        }
      }
      if (da != null) return new DoubleArr(da, sh);
    }
    if (v[0] instanceof Char) {
      StringBuilder s = new StringBuilder();
      for (Value cv : v) {
        if (cv instanceof Char) s.append(((Char) cv).chr);
        else {
          s = null;
          break;
        }
      }
      if (s != null) return new ChrArr(s.toString(), sh);
    }
    return new HArr(v, sh);
  }
  
  public static Arr create(ArrayList<Value> v) {
    return create(v, new int[]{v.size()});
  }
  public static Arr create(ArrayList<Value> v, int[] sh) { // note, doesn't attempt individual item squeezing
    if (v.size() == 0) return new EmptyArr(sh, null);
    Value f = v.get(0);
    if (f instanceof Num) {
      double[] da = new double[v.size()];
      for (int i = 0; i < v.size(); i++) {
        if (v.get(i) instanceof Num) da[i] = ((Num) v.get(i)).num;
        else {
          da = null;
          break;
        }
      }
      if (da != null) return new DoubleArr(da, sh);
    }
    if (f instanceof Char) {
      StringBuilder s = new StringBuilder();
      for (Value cv : v) {
        if (cv instanceof Char) s.append(((Char) cv).chr);
        else {
          s = null;
          break;
        }
      }
      if (s != null) return new ChrArr(s.toString(), sh);
    }
    return new HArr(v, sh);
  }
  
  @Override
  public boolean equals(Obj o) {
    if (!(o instanceof Arr)) return false;
    Arr a = (Arr) o;
    if (!Arrays.equals(shape, a.shape)) return false;
    if (hash!=0 && a.hash!=0 && hash != a.hash) return false;
    Value[] mvs = values();
    Value[] ovs = a.values();
    for (int i = 0; i < mvs.length; i++) {
      if (!mvs[i].equals(ovs[i])) return false;
    }
    return true;
  }
  protected int hash;
  @Override
  public int hashCode() {
    if (hash == 0) {
      for (Value v : this) {
        hash = hash*31 + v.hashCode();
      }
    }
    return shapeHash(hash);
  }
  
  protected int shapeHash(int hash) {
    int h = 0;
    for (int i : shape) {
      h = h*31 + i;
    }
    int res = hash*113 + h;
    if (res == 0) return 100003;
    return res;
  }
  
  public static int prod(int[] ia) {
    int r = 1;
    for (int i : ia) r*= i;
    return r;
  }
  public static int prod(int[] is, int s, int e) {
    int r = 1;
    for (int i = s; i < e; i++) r*= is[i];
    return r;
  }
  
  public static boolean eqPrefix(int[] w, int[] x, int prefix) {
    assert prefix <= w.length && prefix <= x.length;
    for (int i = 0; i < prefix; i++) if (w[i] != x[i]) return false;
    return true;
  }
  
  public static void eqShapes(Value w, Value x) {
    int[] ws = w.shape;
    int[] xs = x.shape;
    if (ws.length != xs.length) throw new RankError("ranks don't equal (shapes: " + Main.formatAPL(ws) + " vs " + Main.formatAPL(xs) + ")", x);
    for (int i = 0; i < ws.length; i++) {
      if (ws[i] != xs[i]) throw new LengthError("shapes don't match (" + Main.formatAPL(ws) + " vs " + Main.formatAPL(xs) + ")", x);
    }
  }
  public static void eqShapes(int[] w, int[] x, Callable blame) {
    if (w.length != x.length) throw new RankError("ranks don't equal (shapes: " + Main.formatAPL(w) + " vs " + Main.formatAPL(x) + ")", blame);
    for (int i = 0; i < w.length; i++) {
      if (w[i] != x[i]) throw new LengthError("shapes don't match (" + Main.formatAPL(w) + " vs " + Main.formatAPL(x) + ")", blame);
    }
  }
}