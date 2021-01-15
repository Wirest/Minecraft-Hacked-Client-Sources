package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

class MethodTypeInfo extends ConstInfo {
   static final int tag = 16;
   int descriptor;

   public MethodTypeInfo(int desc, int index) {
      super(index);
      this.descriptor = desc;
   }

   public MethodTypeInfo(DataInputStream in, int index) throws IOException {
      super(index);
      this.descriptor = in.readUnsignedShort();
   }

   public int hashCode() {
      return this.descriptor;
   }

   public boolean equals(Object obj) {
      if (obj instanceof MethodTypeInfo) {
         return ((MethodTypeInfo)obj).descriptor == this.descriptor;
      } else {
         return false;
      }
   }

   public int getTag() {
      return 16;
   }

   public void renameClass(ConstPool cp, String oldName, String newName, HashMap cache) {
      String desc = cp.getUtf8Info(this.descriptor);
      String desc2 = Descriptor.rename(desc, oldName, newName);
      if (desc != desc2) {
         if (cache == null) {
            this.descriptor = cp.addUtf8Info(desc2);
         } else {
            cache.remove(this);
            this.descriptor = cp.addUtf8Info(desc2);
            cache.put(this, this);
         }
      }

   }

   public void renameClass(ConstPool cp, Map map, HashMap cache) {
      String desc = cp.getUtf8Info(this.descriptor);
      String desc2 = Descriptor.rename(desc, map);
      if (desc != desc2) {
         if (cache == null) {
            this.descriptor = cp.addUtf8Info(desc2);
         } else {
            cache.remove(this);
            this.descriptor = cp.addUtf8Info(desc2);
            cache.put(this, this);
         }
      }

   }

   public int copy(ConstPool src, ConstPool dest, Map map) {
      String desc = src.getUtf8Info(this.descriptor);
      desc = Descriptor.rename(desc, map);
      return dest.addMethodTypeInfo(dest.addUtf8Info(desc));
   }

   public void write(DataOutputStream out) throws IOException {
      out.writeByte(16);
      out.writeShort(this.descriptor);
   }

   public void print(PrintWriter out) {
      out.print("MethodType #");
      out.println(this.descriptor);
   }
}
