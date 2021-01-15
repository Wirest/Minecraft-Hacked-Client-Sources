package javassist;

import java.io.InputStream;
import java.net.URL;

public class ClassClassPath implements ClassPath {
   private Class thisClass;

   public ClassClassPath(Class c) {
      this.thisClass = c;
   }

   ClassClassPath() {
      this(Object.class);
   }

   public InputStream openClassfile(String classname) {
      String jarname = "/" + classname.replace('.', '/') + ".class";
      return this.thisClass.getResourceAsStream(jarname);
   }

   public URL find(String classname) {
      String jarname = "/" + classname.replace('.', '/') + ".class";
      return this.thisClass.getResource(jarname);
   }

   public void close() {
   }

   public String toString() {
      return this.thisClass.getName() + ".class";
   }
}
