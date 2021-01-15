package javassist;

import javassist.bytecode.ClassFile;
import javassist.bytecode.InnerClassesAttribute;

class CtNewNestedClass extends CtNewClass {
   CtNewNestedClass(String realName, ClassPool cp, boolean isInterface, CtClass superclass) {
      super(realName, cp, isInterface, superclass);
   }

   public void setModifiers(int mod) {
      mod &= -9;
      super.setModifiers(mod);
      updateInnerEntry(mod, this.getName(), this, true);
   }

   private static void updateInnerEntry(int mod, String name, CtClass clazz, boolean outer) {
      ClassFile cf = clazz.getClassFile2();
      InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
      if (ica != null) {
         int n = ica.tableLength();

         for(int i = 0; i < n; ++i) {
            if (name.equals(ica.innerClass(i))) {
               int acc = ica.accessFlags(i) & 8;
               ica.setAccessFlags(i, mod | acc);
               String outName = ica.outerClass(i);
               if (outName != null && outer) {
                  try {
                     CtClass parent = clazz.getClassPool().get(outName);
                     updateInnerEntry(mod, name, parent, false);
                  } catch (NotFoundException var11) {
                     throw new RuntimeException("cannot find the declaring class: " + outName);
                  }
               }
               break;
            }
         }

      }
   }
}
