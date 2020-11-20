package APL.types;

import APL.errors.DomainError;
import APL.tokenizer.Token;

public abstract class Callable extends Primitive implements Tokenable {
  public Token token;
  
  public Value callInv (         Value x) { throw new DomainError("Cannot invert "+humanType(true));}
  public Value callInvX(Value w, Value x) { throw new DomainError("Cannot invert "+humanType(true));}
  public Value callInvW(Value w, Value x) { throw new DomainError("Cannot invert "+humanType(true));}
  
  
  public Token getToken() {
    return token;
  }
}