package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

class ClassInfo extends ConstInfo {
   static final int tag = 7;
   int name;

   public ClassInfo(int className, int index) {
      super(index);
      this.name = className;
   }

   public ClassInfo(DataInputStream in, int index) throws IOException {
      super(index);
      this.name = in.readUnsignedShort();
   }

   public int hashCode() {
      return this.name;
   }

   public boolean equals(Object obj) {
      return obj instanceof ClassInfo && ((ClassInfo)obj).name == this.name;
   }

   public int getTag() {
      return 7;
   }

   public String getClassName(ConstPool cp) {
      return cp.getUtf8Info(this.name);
   }

   public void renameClass(ConstPool cp, String oldName, String newName, HashMap cache) {
      String nameStr = cp.getUtf8Info(this.name);
      String newNameStr = null;
      if (nameStr.equals(oldName)) {
         newNameStr = newName;
      } else if (nameStr.charAt(0) == '[') {
         String s = Descriptor.rename(nameStr, oldName, newName);
         if (nameStr != s) {
            newNameStr = s;
         }
      }

      if (newNameStr != null) {
         if (cache == null) {
            this.name = cp.addUtf8Info(newNameStr);
         } else {
            cache.remove(this);
            this.name = cp.addUtf8Info(newNameStr);
            cache.put(this, this);
         }
      }

   }

   public void renameClass(ConstPool cp, Map map, HashMap cache) {
      String oldName = cp.getUtf8Info(this.name);
      String newName = null;
      String s;
      if (oldName.charAt(0) == '[') {
         s = Descriptor.rename(oldName, map);
         if (oldName != s) {
            newName = s;
         }
      } else {
         s = (String)map.get(oldName);
         if (s != null && !s.equals(oldName)) {
            newName = s;
         }
      }

      if (newName != null) {
         if (cache == null) {
            this.name = cp.addUtf8Info(newName);
         } else {
            cache.remove(this);
            this.name = cp.addUtf8Info(newName);
            cache.put(this, this);
         }
      }

   }

   public int copy(ConstPool src, ConstPool dest, Map map) {
      String classname = src.getUtf8Info(this.name);
      if (map != null) {
         String newname = (String)map.get(classname);
         if (newname != null) {
            classname = newname;
         }
      }

      return dest.addClassInfo(classname);
   }

   public void write(DataOutputStream out) throws IOException {
      out.writeByte(7);
      out.writeShort(this.name);
   }

   public void print(PrintWriter out) {
      out.print("Class #");
      out.println(this.name);
   }
}
