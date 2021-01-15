package javassist.tools.reflect;

import javassist.CannotCompileException;

public class CannotReflectException extends CannotCompileException {
   public CannotReflectException(String msg) {
      super(msg);
   }
}
