package javassist.tools.web;

public class BadHttpRequest extends Exception {
   private Exception e;

   public BadHttpRequest() {
      this.e = null;
   }

   public BadHttpRequest(Exception _e) {
      this.e = _e;
   }

   public String toString() {
      return this.e == null ? super.toString() : this.e.toString();
   }
}
