package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class AnnotationMemberValue extends MemberValue {
   Annotation value;

   public AnnotationMemberValue(ConstPool cp) {
      this((Annotation)null, cp);
   }

   public AnnotationMemberValue(Annotation a, ConstPool cp) {
      super('@', cp);
      this.value = a;
   }

   Object getValue(ClassLoader cl, ClassPool cp, Method m) throws ClassNotFoundException {
      return AnnotationImpl.make(cl, this.getType(cl), cp, this.value);
   }

   Class getType(ClassLoader cl) throws ClassNotFoundException {
      if (this.value == null) {
         throw new ClassNotFoundException("no type specified");
      } else {
         return loadClass(cl, this.value.getTypeName());
      }
   }

   public Annotation getValue() {
      return this.value;
   }

   public void setValue(Annotation newValue) {
      this.value = newValue;
   }

   public String toString() {
      return this.value.toString();
   }

   public void write(AnnotationsWriter writer) throws IOException {
      writer.annotationValue();
      this.value.write(writer);
   }

   public void accept(MemberValueVisitor visitor) {
      visitor.visitAnnotationMemberValue(this);
   }
}
