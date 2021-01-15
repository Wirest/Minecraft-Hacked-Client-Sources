package javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

abstract class ConstInfo {
   int index;

   public ConstInfo(int i) {
      this.index = i;
   }

   public abstract int getTag();

   public String getClassName(ConstPool cp) {
      return null;
   }

   public void renameClass(ConstPool cp, String oldName, String newName, HashMap cache) {
   }

   public void renameClass(ConstPool cp, Map classnames, HashMap cache) {
   }

   public abstract int copy(ConstPool src, ConstPool dest, Map classnames);

   public abstract void write(DataOutputStream out) throws IOException;

   public abstract void print(PrintWriter out);

   public String toString() {
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      PrintWriter out = new PrintWriter(bout);
      this.print(out);
      return bout.toString();
   }
}
