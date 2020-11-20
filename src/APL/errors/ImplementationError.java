package APL.errors;

import APL.types.Tokenable;

public class ImplementationError extends APLError {
  public ImplementationError(String s) {
    super(s);
  }
  
  public ImplementationError(String s, Tokenable blame) {
    super(s, blame);
  }
  
  public ImplementationError(Throwable orig) {
    super(orig.toString());
    initCause(orig);
  }
}