package javassist.convert;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;

public class TransformReadField extends Transformer {
   protected String fieldname;
   protected CtClass fieldClass;
   protected boolean isPrivate;
   protected String methodClassname;
   protected String methodName;

   public TransformReadField(Transformer next, CtField field, String methodClassname, String methodName) {
      super(next);
      this.fieldClass = field.getDeclaringClass();
      this.fieldname = field.getName();
      this.methodClassname = methodClassname;
      this.methodName = methodName;
      this.isPrivate = Modifier.isPrivate(field.getModifiers());
   }

   static String isField(ClassPool pool, ConstPool cp, CtClass fclass, String fname, boolean is_private, int index) {
      if (!cp.getFieldrefName(index).equals(fname)) {
         return null;
      } else {
         try {
            CtClass c = pool.get(cp.getFieldrefClassName(index));
            if (c == fclass || !is_private && isFieldInSuper(c, fclass, fname)) {
               return cp.getFieldrefType(index);
            }
         } catch (NotFoundException var7) {
         }

         return null;
      }
   }

   static boolean isFieldInSuper(CtClass clazz, CtClass fclass, String fname) {
      if (!clazz.subclassOf(fclass)) {
         return false;
      } else {
         try {
            CtField f = clazz.getField(fname);
            return f.getDeclaringClass() == fclass;
         } catch (NotFoundException var4) {
            return false;
         }
      }
   }

   public int transform(CtClass tclazz, int pos, CodeIterator iterator, ConstPool cp) throws BadBytecode {
      int c = iterator.byteAt(pos);
      if (c == 180 || c == 178) {
         int index = iterator.u16bitAt(pos + 1);
         String typedesc = isField(tclazz.getClassPool(), cp, this.fieldClass, this.fieldname, this.isPrivate, index);
         if (typedesc != null) {
            if (c == 178) {
               iterator.move(pos);
               pos = iterator.insertGap(1);
               iterator.writeByte(1, pos);
               pos = iterator.next();
            }

            String type = "(Ljava/lang/Object;)" + typedesc;
            int mi = cp.addClassInfo(this.methodClassname);
            int methodref = cp.addMethodrefInfo(mi, this.methodName, type);
            iterator.writeByte(184, pos);
            iterator.write16bit(methodref, pos + 1);
            return pos;
         }
      }

      return pos;
   }
}
