package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

abstract class MemberrefInfo extends ConstInfo {
   int classIndex;
   int nameAndTypeIndex;

   public MemberrefInfo(int cindex, int ntindex, int thisIndex) {
      super(thisIndex);
      this.classIndex = cindex;
      this.nameAndTypeIndex = ntindex;
   }

   public MemberrefInfo(DataInputStream in, int thisIndex) throws IOException {
      super(thisIndex);
      this.classIndex = in.readUnsignedShort();
      this.nameAndTypeIndex = in.readUnsignedShort();
   }

   public int hashCode() {
      return this.classIndex << 16 ^ this.nameAndTypeIndex;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof MemberrefInfo)) {
         return false;
      } else {
         MemberrefInfo mri = (MemberrefInfo)obj;
         return mri.classIndex == this.classIndex && mri.nameAndTypeIndex == this.nameAndTypeIndex && mri.getClass() == this.getClass();
      }
   }

   public int copy(ConstPool src, ConstPool dest, Map map) {
      int classIndex2 = src.getItem(this.classIndex).copy(src, dest, map);
      int ntIndex2 = src.getItem(this.nameAndTypeIndex).copy(src, dest, map);
      return this.copy2(dest, classIndex2, ntIndex2);
   }

   protected abstract int copy2(ConstPool dest, int cindex, int ntindex);

   public void write(DataOutputStream out) throws IOException {
      out.writeByte(this.getTag());
      out.writeShort(this.classIndex);
      out.writeShort(this.nameAndTypeIndex);
   }

   public void print(PrintWriter out) {
      out.print(this.getTagName() + " #");
      out.print(this.classIndex);
      out.print(", name&type #");
      out.println(this.nameAndTypeIndex);
   }

   public abstract String getTagName();
}
