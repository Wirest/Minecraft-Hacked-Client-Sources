package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;

public class EnumMemberValue extends MemberValue {
   int typeIndex;
   int valueIndex;

   public EnumMemberValue(int type, int value, ConstPool cp) {
      super('e', cp);
      this.typeIndex = type;
      this.valueIndex = value;
   }

   public EnumMemberValue(ConstPool cp) {
      super('e', cp);
      this.typeIndex = this.valueIndex = 0;
   }

   Object getValue(ClassLoader cl, ClassPool cp, Method m) throws ClassNotFoundException {
      try {
         return this.getType(cl).getField(this.getValue()).get((Object)null);
      } catch (NoSuchFieldException var5) {
         throw new ClassNotFoundException(this.getType() + "." + this.getValue());
      } catch (IllegalAccessException var6) {
         throw new ClassNotFoundException(this.getType() + "." + this.getValue());
      }
   }

   Class getType(ClassLoader cl) throws ClassNotFoundException {
      return loadClass(cl, this.getType());
   }

   public String getType() {
      return Descriptor.toClassName(this.cp.getUtf8Info(this.typeIndex));
   }

   public void setType(String typename) {
      this.typeIndex = this.cp.addUtf8Info(Descriptor.of(typename));
   }

   public String getValue() {
      return this.cp.getUtf8Info(this.valueIndex);
   }

   public void setValue(String name) {
      this.valueIndex = this.cp.addUtf8Info(name);
   }

   public String toString() {
      return this.getType() + "." + this.getValue();
   }

   public void write(AnnotationsWriter writer) throws IOException {
      writer.enumConstValue(this.cp.getUtf8Info(this.typeIndex), this.getValue());
   }

   public void accept(MemberValueVisitor visitor) {
      visitor.visitEnumMemberValue(this);
   }
}
