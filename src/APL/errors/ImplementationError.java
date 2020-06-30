package APL.errors;

import APL.types.*;

public class ImplementationError extends APLError {
  public ImplementationError(String s) {
    super(s);
  }
  public ImplementationError(String s, Tokenable fun) {
    super(s, fun);
  }
  public ImplementationError(Throwable orig) {
    super(orig.getMessage());
    initCause(orig);
  }
  public ImplementationError(String s, Callable fun, Tokenable cause) {
    super(s, fun, cause);
  }
}