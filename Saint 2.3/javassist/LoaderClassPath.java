package javassist;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class LoaderClassPath implements ClassPath {
   private WeakReference clref;

   public LoaderClassPath(ClassLoader cl) {
      this.clref = new WeakReference(cl);
   }

   public String toString() {
      Object cl = null;
      if (this.clref != null) {
         cl = this.clref.get();
      }

      return cl == null ? "<null>" : cl.toString();
   }

   public InputStream openClassfile(String classname) {
      String cname = classname.replace('.', '/') + ".class";
      ClassLoader cl = (ClassLoader)this.clref.get();
      return cl == null ? null : cl.getResourceAsStream(cname);
   }

   public URL find(String classname) {
      String cname = classname.replace('.', '/') + ".class";
      ClassLoader cl = (ClassLoader)this.clref.get();
      return cl == null ? null : cl.getResource(cname);
   }

   public void close() {
      this.clref = null;
   }
}
