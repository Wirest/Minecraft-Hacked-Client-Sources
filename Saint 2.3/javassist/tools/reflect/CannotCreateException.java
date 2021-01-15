package javassist.tools.reflect;

public class CannotCreateException extends Exception {
   public CannotCreateException(String s) {
      super(s);
   }

   public CannotCreateException(Exception e) {
      super("by " + e.toString());
   }
}
