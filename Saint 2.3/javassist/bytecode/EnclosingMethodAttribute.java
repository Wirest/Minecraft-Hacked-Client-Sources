package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class EnclosingMethodAttribute extends AttributeInfo {
   public static final String tag = "EnclosingMethod";

   EnclosingMethodAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
      super(cp, n, in);
   }

   public EnclosingMethodAttribute(ConstPool cp, String className, String methodName, String methodDesc) {
      super(cp, "EnclosingMethod");
      int ci = cp.addClassInfo(className);
      int ni = cp.addNameAndTypeInfo(methodName, methodDesc);
      byte[] bvalue = new byte[]{(byte)(ci >>> 8), (byte)ci, (byte)(ni >>> 8), (byte)ni};
      this.set(bvalue);
   }

   public EnclosingMethodAttribute(ConstPool cp, String className) {
      super(cp, "EnclosingMethod");
      int ci = cp.addClassInfo(className);
      int ni = 0;
      byte[] bvalue = new byte[]{(byte)(ci >>> 8), (byte)ci, (byte)(ni >>> 8), (byte)ni};
      this.set(bvalue);
   }

   public int classIndex() {
      return ByteArray.readU16bit(this.get(), 0);
   }

   public int methodIndex() {
      return ByteArray.readU16bit(this.get(), 2);
   }

   public String className() {
      return this.getConstPool().getClassInfo(this.classIndex());
   }

   public String methodName() {
      ConstPool cp = this.getConstPool();
      int mi = this.methodIndex();
      if (mi == 0) {
         return "<clinit>";
      } else {
         int ni = cp.getNameAndTypeName(mi);
         return cp.getUtf8Info(ni);
      }
   }

   public String methodDescriptor() {
      ConstPool cp = this.getConstPool();
      int mi = this.methodIndex();
      int ti = cp.getNameAndTypeDescriptor(mi);
      return cp.getUtf8Info(ti);
   }

   public AttributeInfo copy(ConstPool newCp, Map classnames) {
      return this.methodIndex() == 0 ? new EnclosingMethodAttribute(newCp, this.className()) : new EnclosingMethodAttribute(newCp, this.className(), this.methodName(), this.methodDescriptor());
   }
}
