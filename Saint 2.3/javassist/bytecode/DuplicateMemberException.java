package javassist.bytecode;

import javassist.CannotCompileException;

public class DuplicateMemberException extends CannotCompileException {
   public DuplicateMemberException(String msg) {
      super(msg);
   }
}
