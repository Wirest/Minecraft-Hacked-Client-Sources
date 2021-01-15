package javassist;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.convert.TransformAccessArrayField;
import javassist.convert.TransformAfter;
import javassist.convert.TransformBefore;
import javassist.convert.TransformCall;
import javassist.convert.TransformFieldAccess;
import javassist.convert.TransformNew;
import javassist.convert.TransformNewClass;
import javassist.convert.TransformReadField;
import javassist.convert.TransformWriteField;
import javassist.convert.Transformer;

public class CodeConverter {
   protected Transformer transformers = null;

   public void replaceNew(CtClass newClass, CtClass calledClass, String calledMethod) {
      this.transformers = new TransformNew(this.transformers, newClass.getName(), calledClass.getName(), calledMethod);
   }

   public void replaceNew(CtClass oldClass, CtClass newClass) {
      this.transformers = new TransformNewClass(this.transformers, oldClass.getName(), newClass.getName());
   }

   public void redirectFieldAccess(CtField field, CtClass newClass, String newFieldname) {
      this.transformers = new TransformFieldAccess(this.transformers, field, newClass.getName(), newFieldname);
   }

   public void replaceFieldRead(CtField field, CtClass calledClass, String calledMethod) {
      this.transformers = new TransformReadField(this.transformers, field, calledClass.getName(), calledMethod);
   }

   public void replaceFieldWrite(CtField field, CtClass calledClass, String calledMethod) {
      this.transformers = new TransformWriteField(this.transformers, field, calledClass.getName(), calledMethod);
   }

   public void replaceArrayAccess(CtClass calledClass, CodeConverter.ArrayAccessReplacementMethodNames names) throws NotFoundException {
      this.transformers = new TransformAccessArrayField(this.transformers, calledClass.getName(), names);
   }

   public void redirectMethodCall(CtMethod origMethod, CtMethod substMethod) throws CannotCompileException {
      String d1 = origMethod.getMethodInfo2().getDescriptor();
      String d2 = substMethod.getMethodInfo2().getDescriptor();
      if (!d1.equals(d2)) {
         throw new CannotCompileException("signature mismatch: " + substMethod.getLongName());
      } else {
         int mod1 = origMethod.getModifiers();
         int mod2 = substMethod.getModifiers();
         if (Modifier.isStatic(mod1) == Modifier.isStatic(mod2) && (!Modifier.isPrivate(mod1) || Modifier.isPrivate(mod2)) && origMethod.getDeclaringClass().isInterface() == substMethod.getDeclaringClass().isInterface()) {
            this.transformers = new TransformCall(this.transformers, origMethod, substMethod);
         } else {
            throw new CannotCompileException("invoke-type mismatch " + substMethod.getLongName());
         }
      }
   }

   public void redirectMethodCall(String oldMethodName, CtMethod newMethod) throws CannotCompileException {
      this.transformers = new TransformCall(this.transformers, oldMethodName, newMethod);
   }

   public void insertBeforeMethod(CtMethod origMethod, CtMethod beforeMethod) throws CannotCompileException {
      try {
         this.transformers = new TransformBefore(this.transformers, origMethod, beforeMethod);
      } catch (NotFoundException var4) {
         throw new CannotCompileException(var4);
      }
   }

   public void insertAfterMethod(CtMethod origMethod, CtMethod afterMethod) throws CannotCompileException {
      try {
         this.transformers = new TransformAfter(this.transformers, origMethod, afterMethod);
      } catch (NotFoundException var4) {
         throw new CannotCompileException(var4);
      }
   }

   protected void doit(CtClass clazz, MethodInfo minfo, ConstPool cp) throws CannotCompileException {
      CodeAttribute codeAttr = minfo.getCodeAttribute();
      if (codeAttr != null && this.transformers != null) {
         Transformer t;
         for(t = this.transformers; t != null; t = t.getNext()) {
            t.initialize(cp, clazz, minfo);
         }

         CodeIterator iterator = codeAttr.iterator();

         int locals;
         while(iterator.hasNext()) {
            try {
               locals = iterator.next();

               for(t = this.transformers; t != null; t = t.getNext()) {
                  locals = t.transform(clazz, locals, iterator, cp);
               }
            } catch (BadBytecode var11) {
               throw new CannotCompileException(var11);
            }
         }

         locals = 0;
         int stack = 0;

         for(t = this.transformers; t != null; t = t.getNext()) {
            int s = t.extraLocals();
            if (s > locals) {
               locals = s;
            }

            s = t.extraStack();
            if (s > stack) {
               stack = s;
            }
         }

         for(t = this.transformers; t != null; t = t.getNext()) {
            t.clean();
         }

         if (locals > 0) {
            codeAttr.setMaxLocals(codeAttr.getMaxLocals() + locals);
         }

         if (stack > 0) {
            codeAttr.setMaxStack(codeAttr.getMaxStack() + stack);
         }

         try {
            minfo.rebuildStackMapIf6(clazz.getClassPool(), clazz.getClassFile2());
         } catch (BadBytecode var10) {
            throw new CannotCompileException(var10.getMessage(), var10);
         }
      }
   }

   public static class DefaultArrayAccessReplacementMethodNames implements CodeConverter.ArrayAccessReplacementMethodNames {
      public String byteOrBooleanRead() {
         return "arrayReadByteOrBoolean";
      }

      public String byteOrBooleanWrite() {
         return "arrayWriteByteOrBoolean";
      }

      public String charRead() {
         return "arrayReadChar";
      }

      public String charWrite() {
         return "arrayWriteChar";
      }

      public String doubleRead() {
         return "arrayReadDouble";
      }

      public String doubleWrite() {
         return "arrayWriteDouble";
      }

      public String floatRead() {
         return "arrayReadFloat";
      }

      public String floatWrite() {
         return "arrayWriteFloat";
      }

      public String intRead() {
         return "arrayReadInt";
      }

      public String intWrite() {
         return "arrayWriteInt";
      }

      public String longRead() {
         return "arrayReadLong";
      }

      public String longWrite() {
         return "arrayWriteLong";
      }

      public String objectRead() {
         return "arrayReadObject";
      }

      public String objectWrite() {
         return "arrayWriteObject";
      }

      public String shortRead() {
         return "arrayReadShort";
      }

      public String shortWrite() {
         return "arrayWriteShort";
      }
   }

   public interface ArrayAccessReplacementMethodNames {
      String byteOrBooleanRead();

      String byteOrBooleanWrite();

      String charRead();

      String charWrite();

      String doubleRead();

      String doubleWrite();

      String floatRead();

      String floatWrite();

      String intRead();

      String intWrite();

      String longRead();

      String longWrite();

      String objectRead();

      String objectWrite();

      String shortRead();

      String shortWrite();
   }
}
