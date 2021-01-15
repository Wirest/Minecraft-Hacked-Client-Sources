package javassist.tools.rmi;

public class ObjectNotFoundException extends Exception {
   public ObjectNotFoundException(String name) {
      super(name + " is not exported");
   }

   public ObjectNotFoundException(String name, Exception e) {
      super(name + " because of " + e.toString());
   }
}
