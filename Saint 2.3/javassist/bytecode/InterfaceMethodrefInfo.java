package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;

class InterfaceMethodrefInfo extends MemberrefInfo {
   static final int tag = 11;

   public InterfaceMethodrefInfo(int cindex, int ntindex, int thisIndex) {
      super(cindex, ntindex, thisIndex);
   }

   public InterfaceMethodrefInfo(DataInputStream in, int thisIndex) throws IOException {
      super(in, thisIndex);
   }

   public int getTag() {
      return 11;
   }

   public String getTagName() {
      return "Interface";
   }

   protected int copy2(ConstPool dest, int cindex, int ntindex) {
      return dest.addInterfaceMethodrefInfo(cindex, ntindex);
   }
}
