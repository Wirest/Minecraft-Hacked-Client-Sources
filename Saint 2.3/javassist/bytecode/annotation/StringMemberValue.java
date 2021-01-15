package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class StringMemberValue extends MemberValue {
   int valueIndex;

   public StringMemberValue(int index, ConstPool cp) {
      super('s', cp);
      this.valueIndex = index;
   }

   public StringMemberValue(String str, ConstPool cp) {
      super('s', cp);
      this.setValue(str);
   }

   public StringMemberValue(ConstPool cp) {
      super('s', cp);
      this.setValue("");
   }

   Object getValue(ClassLoader cl, ClassPool cp, Method m) {
      return this.getValue();
   }

   Class getType(ClassLoader cl) {
      return String.class;
   }

   public String getValue() {
      return this.cp.getUtf8Info(this.valueIndex);
   }

   public void setValue(String newValue) {
      this.valueIndex = this.cp.addUtf8Info(newValue);
   }

   public String toString() {
      return "\"" + this.getValue() + "\"";
   }

   public void write(AnnotationsWriter writer) throws IOException {
      writer.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor visitor) {
      visitor.visitStringMemberValue(this);
   }
}
