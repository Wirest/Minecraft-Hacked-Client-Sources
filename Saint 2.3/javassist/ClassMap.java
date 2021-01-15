package javassist;

import java.util.HashMap;
import javassist.bytecode.Descriptor;

public class ClassMap extends HashMap {
   private ClassMap parent;

   public ClassMap() {
      this.parent = null;
   }

   ClassMap(ClassMap map) {
      this.parent = map;
   }

   public void put(CtClass oldname, CtClass newname) {
      this.put(oldname.getName(), newname.getName());
   }

   public void put(String oldname, String newname) {
      if (oldname != newname) {
         String oldname2 = toJvmName(oldname);
         String s = (String)this.get(oldname2);
         if (s == null || !s.equals(oldname2)) {
            super.put(oldname2, toJvmName(newname));
         }

      }
   }

   public void putIfNone(String oldname, String newname) {
      if (oldname != newname) {
         String oldname2 = toJvmName(oldname);
         String s = (String)this.get(oldname2);
         if (s == null) {
            super.put(oldname2, toJvmName(newname));
         }

      }
   }

   protected final void put0(Object oldname, Object newname) {
      super.put(oldname, newname);
   }

   public Object get(Object jvmClassName) {
      Object found = super.get(jvmClassName);
      return found == null && this.parent != null ? this.parent.get(jvmClassName) : found;
   }

   public void fix(CtClass clazz) {
      this.fix(clazz.getName());
   }

   public void fix(String name) {
      String name2 = toJvmName(name);
      super.put(name2, name2);
   }

   public static String toJvmName(String classname) {
      return Descriptor.toJvmName(classname);
   }

   public static String toJavaName(String classname) {
      return Descriptor.toJavaName(classname);
   }
}
