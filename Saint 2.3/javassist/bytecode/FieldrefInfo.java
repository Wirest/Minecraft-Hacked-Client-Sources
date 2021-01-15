package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;

class FieldrefInfo extends MemberrefInfo {
   static final int tag = 9;

   public FieldrefInfo(int cindex, int ntindex, int thisIndex) {
      super(cindex, ntindex, thisIndex);
   }

   public FieldrefInfo(DataInputStream in, int thisIndex) throws IOException {
      super(in, thisIndex);
   }

   public int getTag() {
      return 9;
   }

   public String getTagName() {
      return "Field";
   }

   protected int copy2(ConstPool dest, int cindex, int ntindex) {
      return dest.addFieldrefInfo(cindex, ntindex);
   }
}
