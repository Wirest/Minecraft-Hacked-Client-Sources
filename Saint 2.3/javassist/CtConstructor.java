package javassist;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;

public final class CtConstructor extends CtBehavior {
   protected CtConstructor(MethodInfo minfo, CtClass declaring) {
      super(declaring, minfo);
   }

   public CtConstructor(CtClass[] parameters, CtClass declaring) {
      this((MethodInfo)null, declaring);
      ConstPool cp = declaring.getClassFile2().getConstPool();
      String desc = Descriptor.ofConstructor(parameters);
      this.methodInfo = new MethodInfo(cp, "<init>", desc);
      this.setModifiers(1);
   }

   public CtConstructor(CtConstructor src, CtClass declaring, ClassMap map) throws CannotCompileException {
      this((MethodInfo)null, declaring);
      this.copy(src, true, map);
   }

   public boolean isConstructor() {
      return this.methodInfo.isConstructor();
   }

   public boolean isClassInitializer() {
      return this.methodInfo.isStaticInitializer();
   }

   public String getLongName() {
      return this.getDeclaringClass().getName() + (this.isConstructor() ? Descriptor.toString(this.getSignature()) : ".<clinit>()");
   }

   public String getName() {
      return this.methodInfo.isStaticInitializer() ? "<clinit>" : this.declaringClass.getSimpleName();
   }

   public boolean isEmpty() {
      CodeAttribute ca = this.getMethodInfo2().getCodeAttribute();
      if (ca == null) {
         return false;
      } else {
         ConstPool cp = ca.getConstPool();
         CodeIterator it = ca.iterator();

         try {
            int op0 = it.byteAt(it.next());
            int pos;
            int desc;
            return op0 == 177 || op0 == 42 && it.byteAt(pos = it.next()) == 183 && (desc = cp.isConstructor(this.getSuperclassName(), it.u16bitAt(pos + 1))) != 0 && "()V".equals(cp.getUtf8Info(desc)) && it.byteAt(it.next()) == 177 && !it.hasNext();
         } catch (BadBytecode var7) {
            return false;
         }
      }
   }

   private String getSuperclassName() {
      ClassFile cf = this.declaringClass.getClassFile2();
      return cf.getSuperclass();
   }

   public boolean callsSuper() throws CannotCompileException {
      CodeAttribute codeAttr = this.methodInfo.getCodeAttribute();
      if (codeAttr != null) {
         CodeIterator it = codeAttr.iterator();

         try {
            int index = it.skipSuperConstructor();
            return index >= 0;
         } catch (BadBytecode var4) {
            throw new CannotCompileException(var4);
         }
      } else {
         return false;
      }
   }

   public void setBody(String src) throws CannotCompileException {
      if (src == null) {
         if (this.isClassInitializer()) {
            src = ";";
         } else {
            src = "super();";
         }
      }

      super.setBody(src);
   }

   public void setBody(CtConstructor src, ClassMap map) throws CannotCompileException {
      setBody0(src.declaringClass, src.methodInfo, this.declaringClass, this.methodInfo, map);
   }

   public void insertBeforeBody(String src) throws CannotCompileException {
      CtClass cc = this.declaringClass;
      cc.checkModify();
      if (this.isClassInitializer()) {
         throw new CannotCompileException("class initializer");
      } else {
         CodeAttribute ca = this.methodInfo.getCodeAttribute();
         CodeIterator iterator = ca.iterator();
         Bytecode b = new Bytecode(this.methodInfo.getConstPool(), ca.getMaxStack(), ca.getMaxLocals());
         b.setStackDepth(ca.getMaxStack());
         Javac jv = new Javac(b, cc);

         try {
            jv.recordParams(this.getParameterTypes(), false);
            jv.compileStmnt(src);
            ca.setMaxStack(b.getMaxStack());
            ca.setMaxLocals(b.getMaxLocals());
            iterator.skipConstructor();
            int pos = iterator.insertEx(b.get());
            iterator.insert(b.getExceptionTable(), pos);
            this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
         } catch (NotFoundException var8) {
            throw new CannotCompileException(var8);
         } catch (CompileError var9) {
            throw new CannotCompileException(var9);
         } catch (BadBytecode var10) {
            throw new CannotCompileException(var10);
         }
      }
   }

   int getStartPosOfBody(CodeAttribute ca) throws CannotCompileException {
      CodeIterator ci = ca.iterator();

      try {
         ci.skipConstructor();
         return ci.next();
      } catch (BadBytecode var4) {
         throw new CannotCompileException(var4);
      }
   }

   public CtMethod toMethod(String name, CtClass declaring) throws CannotCompileException {
      return this.toMethod(name, declaring, (ClassMap)null);
   }

   public CtMethod toMethod(String name, CtClass declaring, ClassMap map) throws CannotCompileException {
      CtMethod method = new CtMethod((MethodInfo)null, declaring);
      method.copy(this, false, map);
      if (this.isConstructor()) {
         MethodInfo minfo = method.getMethodInfo2();
         CodeAttribute ca = minfo.getCodeAttribute();
         if (ca != null) {
            removeConsCall(ca);

            try {
               this.methodInfo.rebuildStackMapIf6(declaring.getClassPool(), declaring.getClassFile2());
            } catch (BadBytecode var8) {
               throw new CannotCompileException(var8);
            }
         }
      }

      method.setName(name);
      return method;
   }

   private static void removeConsCall(CodeAttribute ca) throws CannotCompileException {
      CodeIterator iterator = ca.iterator();

      try {
         int pos = iterator.skipConstructor();
         if (pos >= 0) {
            int mref = iterator.u16bitAt(pos + 1);
            String desc = ca.getConstPool().getMethodrefType(mref);
            int num = Descriptor.numOfParameters(desc) + 1;
            if (num > 3) {
               pos = iterator.insertGapAt(pos, num - 3, false).position;
            }

            iterator.writeByte(87, pos++);
            iterator.writeByte(0, pos);
            iterator.writeByte(0, pos + 1);
            Descriptor.Iterator it = new Descriptor.Iterator(desc);

            while(true) {
               it.next();
               if (!it.isParameter()) {
                  break;
               }

               iterator.writeByte(it.is2byte() ? 88 : 87, pos++);
            }
         }

      } catch (BadBytecode var7) {
         throw new CannotCompileException(var7);
      }
   }
}
