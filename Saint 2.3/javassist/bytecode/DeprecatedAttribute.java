package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class DeprecatedAttribute extends AttributeInfo {
   public static final String tag = "Deprecated";

   DeprecatedAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
      super(cp, n, in);
   }

   public DeprecatedAttribute(ConstPool cp) {
      super(cp, "Deprecated", new byte[0]);
   }

   public AttributeInfo copy(ConstPool newCp, Map classnames) {
      return new DeprecatedAttribute(newCp);
   }
}
