package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class BootstrapMethodsAttribute extends AttributeInfo {
   public static final String tag = "BootstrapMethods";

   BootstrapMethodsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
      super(cp, n, in);
   }

   public BootstrapMethodsAttribute(ConstPool cp, BootstrapMethodsAttribute.BootstrapMethod[] methods) {
      super(cp, "BootstrapMethods");
      int size = 2;

      for(int i = 0; i < methods.length; ++i) {
         size += 4 + methods[i].arguments.length * 2;
      }

      byte[] data = new byte[size];
      ByteArray.write16bit(methods.length, data, 0);
      int pos = 2;

      for(int i = 0; i < methods.length; ++i) {
         ByteArray.write16bit(methods[i].methodRef, data, pos);
         ByteArray.write16bit(methods[i].arguments.length, data, pos + 2);
         int[] args = methods[i].arguments;
         pos += 4;

         for(int k = 0; k < args.length; ++k) {
            ByteArray.write16bit(args[k], data, pos);
            pos += 2;
         }
      }

      this.set(data);
   }

   public BootstrapMethodsAttribute.BootstrapMethod[] getMethods() {
      byte[] data = this.get();
      int num = ByteArray.readU16bit(data, 0);
      BootstrapMethodsAttribute.BootstrapMethod[] methods = new BootstrapMethodsAttribute.BootstrapMethod[num];
      int pos = 2;

      for(int i = 0; i < num; ++i) {
         int ref = ByteArray.readU16bit(data, pos);
         int len = ByteArray.readU16bit(data, pos + 2);
         int[] args = new int[len];
         pos += 4;

         for(int k = 0; k < len; ++k) {
            args[k] = ByteArray.readU16bit(data, pos);
            pos += 2;
         }

         methods[i] = new BootstrapMethodsAttribute.BootstrapMethod(ref, args);
      }

      return methods;
   }

   public AttributeInfo copy(ConstPool newCp, Map classnames) {
      BootstrapMethodsAttribute.BootstrapMethod[] methods = this.getMethods();
      ConstPool thisCp = this.getConstPool();

      for(int i = 0; i < methods.length; ++i) {
         BootstrapMethodsAttribute.BootstrapMethod m = methods[i];
         m.methodRef = thisCp.copy(m.methodRef, newCp, classnames);

         for(int k = 0; k < m.arguments.length; ++k) {
            m.arguments[k] = thisCp.copy(m.arguments[k], newCp, classnames);
         }
      }

      return new BootstrapMethodsAttribute(newCp, methods);
   }

   public static class BootstrapMethod {
      public int methodRef;
      public int[] arguments;

      public BootstrapMethod(int method, int[] args) {
         this.methodRef = method;
         this.arguments = args;
      }
   }
}
