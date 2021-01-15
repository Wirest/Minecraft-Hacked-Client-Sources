package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class ShortMemberValue extends MemberValue {
   int valueIndex;

   public ShortMemberValue(int index, ConstPool cp) {
      super('S', cp);
      this.valueIndex = index;
   }

   public ShortMemberValue(short s, ConstPool cp) {
      super('S', cp);
      this.setValue(s);
   }

   public ShortMemberValue(ConstPool cp) {
      super('S', cp);
      this.setValue((short)0);
   }

   Object getValue(ClassLoader cl, ClassPool cp, Method m) {
      return new Short(this.getValue());
   }

   Class getType(ClassLoader cl) {
      return Short.TYPE;
   }

   public short getValue() {
      return (short)this.cp.getIntegerInfo(this.valueIndex);
   }

   public void setValue(short newValue) {
      this.valueIndex = this.cp.addIntegerInfo(newValue);
   }

   public String toString() {
      return Short.toString(this.getValue());
   }

   public void write(AnnotationsWriter writer) throws IOException {
      writer.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor visitor) {
      visitor.visitShortMemberValue(this);
   }
}
