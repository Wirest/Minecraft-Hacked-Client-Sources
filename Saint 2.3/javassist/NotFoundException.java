package javassist;

public class NotFoundException extends Exception {
   public NotFoundException(String msg) {
      super(msg);
   }

   public NotFoundException(String msg, Exception e) {
      super(msg + " because of " + e.toString());
   }
}
