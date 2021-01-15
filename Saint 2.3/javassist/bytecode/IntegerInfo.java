package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

class IntegerInfo extends ConstInfo {
   static final int tag = 3;
   int value;

   public IntegerInfo(int v, int index) {
      super(index);
      this.value = v;
   }

   public IntegerInfo(DataInputStream in, int index) throws IOException {
      super(index);
      this.value = in.readInt();
   }

   public int hashCode() {
      return this.value;
   }

   public boolean equals(Object obj) {
      return obj instanceof IntegerInfo && ((IntegerInfo)obj).value == this.value;
   }

   public int getTag() {
      return 3;
   }

   public int copy(ConstPool src, ConstPool dest, Map map) {
      return dest.addIntegerInfo(this.value);
   }

   public void write(DataOutputStream out) throws IOException {
      out.writeByte(3);
      out.writeInt(this.value);
   }

   public void print(PrintWriter out) {
      out.print("Integer ");
      out.println(this.value);
   }
}
