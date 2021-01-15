package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;

class MethodrefInfo extends MemberrefInfo {
   static final int tag = 10;

   public MethodrefInfo(int cindex, int ntindex, int thisIndex) {
      super(cindex, ntindex, thisIndex);
   }

   public MethodrefInfo(DataInputStream in, int thisIndex) throws IOException {
      super(in, thisIndex);
   }

   public int getTag() {
      return 10;
   }

   public String getTagName() {
      return "Method";
   }

   protected int copy2(ConstPool dest, int cindex, int ntindex) {
      return dest.addMethodrefInfo(cindex, ntindex);
   }
}
