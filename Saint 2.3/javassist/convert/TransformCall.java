package javassist.convert;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;

public class TransformCall extends Transformer {
   protected String classname;
   protected String methodname;
   protected String methodDescriptor;
   protected String newClassname;
   protected String newMethodname;
   protected boolean newMethodIsPrivate;
   protected int newIndex;
   protected ConstPool constPool;

   public TransformCall(Transformer next, CtMethod origMethod, CtMethod substMethod) {
      this(next, origMethod.getName(), substMethod);
      this.classname = origMethod.getDeclaringClass().getName();
   }

   public TransformCall(Transformer next, String oldMethodName, CtMethod substMethod) {
      super(next);
      this.methodname = oldMethodName;
      this.methodDescriptor = substMethod.getMethodInfo2().getDescriptor();
      this.classname = this.newClassname = substMethod.getDeclaringClass().getName();
      this.newMethodname = substMethod.getName();
      this.constPool = null;
      this.newMethodIsPrivate = Modifier.isPrivate(substMethod.getModifiers());
   }

   public void initialize(ConstPool cp, CodeAttribute attr) {
      if (this.constPool != cp) {
         this.newIndex = 0;
      }

   }

   public int transform(CtClass clazz, int pos, CodeIterator iterator, ConstPool cp) throws BadBytecode {
      int c = iterator.byteAt(pos);
      if (c == 185 || c == 183 || c == 184 || c == 182) {
         int index = iterator.u16bitAt(pos + 1);
         String cname = cp.eqMember(this.methodname, this.methodDescriptor, index);
         if (cname != null && this.matchClass(cname, clazz.getClassPool())) {
            int ntinfo = cp.getMemberNameAndType(index);
            pos = this.match(c, pos, iterator, cp.getNameAndTypeDescriptor(ntinfo), cp);
         }
      }

      return pos;
   }

   private boolean matchClass(String name, ClassPool pool) {
      if (this.classname.equals(name)) {
         return true;
      } else {
         try {
            CtClass clazz = pool.get(name);
            CtClass declClazz = pool.get(this.classname);
            if (clazz.subtypeOf(declClazz)) {
               try {
                  CtMethod m = clazz.getMethod(this.methodname, this.methodDescriptor);
                  return m.getDeclaringClass().getName().equals(this.classname);
               } catch (NotFoundException var6) {
                  return true;
               }
            } else {
               return false;
            }
         } catch (NotFoundException var7) {
            return false;
         }
      }
   }

   protected int match(int c, int pos, CodeIterator iterator, int typedesc, ConstPool cp) throws BadBytecode {
      if (this.newIndex == 0) {
         int nt = cp.addNameAndTypeInfo(cp.addUtf8Info(this.newMethodname), typedesc);
         int ci = cp.addClassInfo(this.newClassname);
         if (c == 185) {
            this.newIndex = cp.addInterfaceMethodrefInfo(ci, nt);
         } else {
            if (this.newMethodIsPrivate && c == 182) {
               iterator.writeByte(183, pos);
            }

            this.newIndex = cp.addMethodrefInfo(ci, nt);
         }

         this.constPool = cp;
      }

      iterator.write16bit(this.newIndex, pos + 1);
      return pos;
   }
}
