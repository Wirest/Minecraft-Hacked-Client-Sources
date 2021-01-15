package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class IntegerMemberValue extends MemberValue {
   int valueIndex;

   public IntegerMemberValue(int index, ConstPool cp) {
      super('I', cp);
      this.valueIndex = index;
   }

   public IntegerMemberValue(ConstPool cp, int value) {
      super('I', cp);
      this.setValue(value);
   }

   public IntegerMemberValue(ConstPool cp) {
      super('I', cp);
      this.setValue(0);
   }

   Object getValue(ClassLoader cl, ClassPool cp, Method m) {
      return new Integer(this.getValue());
   }

   Class getType(ClassLoader cl) {
      return Integer.TYPE;
   }

   public int getValue() {
      return this.cp.getIntegerInfo(this.valueIndex);
   }

   public void setValue(int newValue) {
      this.valueIndex = this.cp.addIntegerInfo(newValue);
   }

   public String toString() {
      return Integer.toString(this.getValue());
   }

   public void write(AnnotationsWriter writer) throws IOException {
      writer.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor visitor) {
      visitor.visitIntegerMemberValue(this);
   }
}
