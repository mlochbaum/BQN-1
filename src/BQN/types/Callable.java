package BQN.types;

import BQN.errors.DomainError;
import BQN.tokenizer.Token;

public abstract class Callable extends Primitive implements Tokenable {
  public Token token;
  
  public Value callInv (         Value x) { throw new DomainError("Cannot invert "+humanType(true), this);}
  public Value callInvX(Value w, Value x) { throw new DomainError("Cannot invert "+humanType(true), this);}
  public Value callInvW(Value w, Value x) { throw new DomainError("Cannot invert "+humanType(true), this);}
  
  
  public Token getToken() {
    return token;
  }
}