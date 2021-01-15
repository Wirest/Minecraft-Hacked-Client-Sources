package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class DoubleMemberValue extends MemberValue {
   int valueIndex;

   public DoubleMemberValue(int index, ConstPool cp) {
      super('D', cp);
      this.valueIndex = index;
   }

   public DoubleMemberValue(double d, ConstPool cp) {
      super('D', cp);
      this.setValue(d);
   }

   public DoubleMemberValue(ConstPool cp) {
      super('D', cp);
      this.setValue(0.0D);
   }

   Object getValue(ClassLoader cl, ClassPool cp, Method m) {
      return new Double(this.getValue());
   }

   Class getType(ClassLoader cl) {
      return Double.TYPE;
   }

   public double getValue() {
      return this.cp.getDoubleInfo(this.valueIndex);
   }

   public void setValue(double newValue) {
      this.valueIndex = this.cp.addDoubleInfo(newValue);
   }

   public String toString() {
      return Double.toString(this.getValue());
   }

   public void write(AnnotationsWriter writer) throws IOException {
      writer.constValueIndex(this.getValue());
   }

   public void accept(MemberValueVisitor visitor) {
      visitor.visitDoubleMemberValue(this);
   }
}
