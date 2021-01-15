package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class SyntheticAttribute extends AttributeInfo {
   public static final String tag = "Synthetic";

   SyntheticAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
      super(cp, n, in);
   }

   public SyntheticAttribute(ConstPool cp) {
      super(cp, "Synthetic", new byte[0]);
   }

   public AttributeInfo copy(ConstPool newCp, Map classnames) {
      return new SyntheticAttribute(newCp);
   }
}
