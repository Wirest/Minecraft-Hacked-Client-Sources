package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class SourceFileAttribute extends AttributeInfo {
   public static final String tag = "SourceFile";

   SourceFileAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
      super(cp, n, in);
   }

   public SourceFileAttribute(ConstPool cp, String filename) {
      super(cp, "SourceFile");
      int index = cp.addUtf8Info(filename);
      byte[] bvalue = new byte[]{(byte)(index >>> 8), (byte)index};
      this.set(bvalue);
   }

   public String getFileName() {
      return this.getConstPool().getUtf8Info(ByteArray.readU16bit(this.get(), 0));
   }

   public AttributeInfo copy(ConstPool newCp, Map classnames) {
      return new SourceFileAttribute(newCp, this.getFileName());
   }
}
