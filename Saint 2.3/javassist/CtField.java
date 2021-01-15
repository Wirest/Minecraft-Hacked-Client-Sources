package javassist;

import java.util.ListIterator;
import java.util.Map;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.SignatureAttribute;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.compiler.SymbolTable;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.DoubleConst;
import javassist.compiler.ast.IntConst;
import javassist.compiler.ast.StringL;

public class CtField extends CtMember {
   static final String javaLangString = "java.lang.String";
   protected FieldInfo fieldInfo;

   public CtField(CtClass type, String name, CtClass declaring) throws CannotCompileException {
      this(Descriptor.of(type), name, declaring);
   }

   public CtField(CtField src, CtClass declaring) throws CannotCompileException {
      this(src.fieldInfo.getDescriptor(), src.fieldInfo.getName(), declaring);
      ListIterator iterator = src.fieldInfo.getAttributes().listIterator();
      FieldInfo fi = this.fieldInfo;
      fi.setAccessFlags(src.fieldInfo.getAccessFlags());
      ConstPool cp = fi.getConstPool();

      while(iterator.hasNext()) {
         AttributeInfo ainfo = (AttributeInfo)iterator.next();
         fi.addAttribute(ainfo.copy(cp, (Map)null));
      }

   }

   private CtField(String typeDesc, String name, CtClass clazz) throws CannotCompileException {
      super(clazz);
      ClassFile cf = clazz.getClassFile2();
      if (cf == null) {
         throw new CannotCompileException("bad declaring class: " + clazz.getName());
      } else {
         this.fieldInfo = new FieldInfo(cf.getConstPool(), name, typeDesc);
      }
   }

   CtField(FieldInfo fi, CtClass clazz) {
      super(clazz);
      this.fieldInfo = fi;
   }

   public String toString() {
      return this.getDeclaringClass().getName() + "." + this.getName() + ":" + this.fieldInfo.getDescriptor();
   }

   protected void extendToString(StringBuffer buffer) {
      buffer.append(' ');
      buffer.append(this.getName());
      buffer.append(' ');
      buffer.append(this.fieldInfo.getDescriptor());
   }

   protected ASTree getInitAST() {
      return null;
   }

   CtField.Initializer getInit() {
      ASTree tree = this.getInitAST();
      return tree == null ? null : CtField.Initializer.byExpr(tree);
   }

   public static CtField make(String src, CtClass declaring) throws CannotCompileException {
      Javac compiler = new Javac(declaring);

      try {
         CtMember obj = compiler.compile(src);
         if (obj instanceof CtField) {
            return (CtField)obj;
         }
      } catch (CompileError var4) {
         throw new CannotCompileException(var4);
      }

      throw new CannotCompileException("not a field");
   }

   public FieldInfo getFieldInfo() {
      this.declaringClass.checkModify();
      return this.fieldInfo;
   }

   public FieldInfo getFieldInfo2() {
      return this.fieldInfo;
   }

   public CtClass getDeclaringClass() {
      return super.getDeclaringClass();
   }

   public String getName() {
      return this.fieldInfo.getName();
   }

   public void setName(String newName) {
      this.declaringClass.checkModify();
      this.fieldInfo.setName(newName);
   }

   public int getModifiers() {
      return AccessFlag.toModifier(this.fieldInfo.getAccessFlags());
   }

   public void setModifiers(int mod) {
      this.declaringClass.checkModify();
      this.fieldInfo.setAccessFlags(AccessFlag.of(mod));
   }

   public boolean hasAnnotation(Class clz) {
      FieldInfo fi = this.getFieldInfo2();
      AnnotationsAttribute ainfo = (AnnotationsAttribute)fi.getAttribute("RuntimeInvisibleAnnotations");
      AnnotationsAttribute ainfo2 = (AnnotationsAttribute)fi.getAttribute("RuntimeVisibleAnnotations");
      return CtClassType.hasAnnotationType(clz, this.getDeclaringClass().getClassPool(), ainfo, ainfo2);
   }

   public Object getAnnotation(Class clz) throws ClassNotFoundException {
      FieldInfo fi = this.getFieldInfo2();
      AnnotationsAttribute ainfo = (AnnotationsAttribute)fi.getAttribute("RuntimeInvisibleAnnotations");
      AnnotationsAttribute ainfo2 = (AnnotationsAttribute)fi.getAttribute("RuntimeVisibleAnnotations");
      return CtClassType.getAnnotationType(clz, this.getDeclaringClass().getClassPool(), ainfo, ainfo2);
   }

   public Object[] getAnnotations() throws ClassNotFoundException {
      return this.getAnnotations(false);
   }

   public Object[] getAvailableAnnotations() {
      try {
         return this.getAnnotations(true);
      } catch (ClassNotFoundException var2) {
         throw new RuntimeException("Unexpected exception", var2);
      }
   }

   private Object[] getAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
      FieldInfo fi = this.getFieldInfo2();
      AnnotationsAttribute ainfo = (AnnotationsAttribute)fi.getAttribute("RuntimeInvisibleAnnotations");
      AnnotationsAttribute ainfo2 = (AnnotationsAttribute)fi.getAttribute("RuntimeVisibleAnnotations");
      return CtClassType.toAnnotationType(ignoreNotFound, this.getDeclaringClass().getClassPool(), ainfo, ainfo2);
   }

   public String getSignature() {
      return this.fieldInfo.getDescriptor();
   }

   public String getGenericSignature() {
      SignatureAttribute sa = (SignatureAttribute)this.fieldInfo.getAttribute("Signature");
      return sa == null ? null : sa.getSignature();
   }

   public void setGenericSignature(String sig) {
      this.declaringClass.checkModify();
      this.fieldInfo.addAttribute(new SignatureAttribute(this.fieldInfo.getConstPool(), sig));
   }

   public CtClass getType() throws NotFoundException {
      return Descriptor.toCtClass(this.fieldInfo.getDescriptor(), this.declaringClass.getClassPool());
   }

   public void setType(CtClass clazz) {
      this.declaringClass.checkModify();
      this.fieldInfo.setDescriptor(Descriptor.of(clazz));
   }

   public Object getConstantValue() {
      int index = this.fieldInfo.getConstantValue();
      if (index == 0) {
         return null;
      } else {
         ConstPool cp = this.fieldInfo.getConstPool();
         switch(cp.getTag(index)) {
         case 3:
            int value = cp.getIntegerInfo(index);
            if ("Z".equals(this.fieldInfo.getDescriptor())) {
               return new Boolean(value != 0);
            }

            return new Integer(value);
         case 4:
            return new Float(cp.getFloatInfo(index));
         case 5:
            return new Long(cp.getLongInfo(index));
         case 6:
            return new Double(cp.getDoubleInfo(index));
         case 7:
         default:
            throw new RuntimeException("bad tag: " + cp.getTag(index) + " at " + index);
         case 8:
            return cp.getStringInfo(index);
         }
      }
   }

   public byte[] getAttribute(String name) {
      AttributeInfo ai = this.fieldInfo.getAttribute(name);
      return ai == null ? null : ai.get();
   }

   public void setAttribute(String name, byte[] data) {
      this.declaringClass.checkModify();
      this.fieldInfo.addAttribute(new AttributeInfo(this.fieldInfo.getConstPool(), name, data));
   }

   static class MultiArrayInitializer extends CtField.Initializer {
      CtClass type;
      int[] dim;

      MultiArrayInitializer(CtClass t, int[] d) {
         this.type = t;
         this.dim = d;
      }

      void check(String desc) throws CannotCompileException {
         if (desc.charAt(0) != '[') {
            throw new CannotCompileException("type mismatch");
         }
      }

      int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
         code.addAload(0);
         int s = code.addMultiNewarray(type, this.dim);
         code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
         return s + 1;
      }

      int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
         int s = code.addMultiNewarray(type, this.dim);
         code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
         return s;
      }
   }

   static class ArrayInitializer extends CtField.Initializer {
      CtClass type;
      int size;

      ArrayInitializer(CtClass t, int s) {
         this.type = t;
         this.size = s;
      }

      private void addNewarray(Bytecode code) {
         if (this.type.isPrimitive()) {
            code.addNewarray(((CtPrimitiveType)this.type).getArrayType(), this.size);
         } else {
            code.addAnewarray(this.type, this.size);
         }

      }

      int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
         code.addAload(0);
         this.addNewarray(code);
         code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
         return 2;
      }

      int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
         this.addNewarray(code);
         code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
         return 1;
      }
   }

   static class StringInitializer extends CtField.Initializer {
      String value;

      StringInitializer(String v) {
         this.value = v;
      }

      int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
         code.addAload(0);
         code.addLdc(this.value);
         code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
         return 2;
      }

      int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
         code.addLdc(this.value);
         code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
         return 1;
      }

      int getConstantValue(ConstPool cp, CtClass type) {
         return type.getName().equals("java.lang.String") ? cp.addStringInfo(this.value) : 0;
      }
   }

   static class DoubleInitializer extends CtField.Initializer {
      double value;

      DoubleInitializer(double v) {
         this.value = v;
      }

      void check(String desc) throws CannotCompileException {
         if (!desc.equals("D")) {
            throw new CannotCompileException("type mismatch");
         }
      }

      int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
         code.addAload(0);
         code.addLdc2w(this.value);
         code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
         return 3;
      }

      int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
         code.addLdc2w(this.value);
         code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
         return 2;
      }

      int getConstantValue(ConstPool cp, CtClass type) {
         return type == CtClass.doubleType ? cp.addDoubleInfo(this.value) : 0;
      }
   }

   static class FloatInitializer extends CtField.Initializer {
      float value;

      FloatInitializer(float v) {
         this.value = v;
      }

      void check(String desc) throws CannotCompileException {
         if (!desc.equals("F")) {
            throw new CannotCompileException("type mismatch");
         }
      }

      int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
         code.addAload(0);
         code.addFconst(this.value);
         code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
         return 3;
      }

      int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
         code.addFconst(this.value);
         code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
         return 2;
      }

      int getConstantValue(ConstPool cp, CtClass type) {
         return type == CtClass.floatType ? cp.addFloatInfo(this.value) : 0;
      }
   }

   static class LongInitializer extends CtField.Initializer {
      long value;

      LongInitializer(long v) {
         this.value = v;
      }

      void check(String desc) throws CannotCompileException {
         if (!desc.equals("J")) {
            throw new CannotCompileException("type mismatch");
         }
      }

      int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
         code.addAload(0);
         code.addLdc2w(this.value);
         code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
         return 3;
      }

      int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
         code.addLdc2w(this.value);
         code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
         return 2;
      }

      int getConstantValue(ConstPool cp, CtClass type) {
         return type == CtClass.longType ? cp.addLongInfo(this.value) : 0;
      }
   }

   static class IntInitializer extends CtField.Initializer {
      int value;

      IntInitializer(int v) {
         this.value = v;
      }

      void check(String desc) throws CannotCompileException {
         char c = desc.charAt(0);
         if (c != 'I' && c != 'S' && c != 'B' && c != 'C' && c != 'Z') {
            throw new CannotCompileException("type mismatch");
         }
      }

      int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
         code.addAload(0);
         code.addIconst(this.value);
         code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
         return 2;
      }

      int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
         code.addIconst(this.value);
         code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
         return 1;
      }

      int getConstantValue(ConstPool cp, CtClass type) {
         return cp.addIntegerInfo(this.value);
      }
   }

   static class MethodInitializer extends CtField.NewInitializer {
      String methodName;

      int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
         code.addAload(0);
         code.addAload(0);
         int stacksize;
         if (this.stringParams == null) {
            stacksize = 2;
         } else {
            stacksize = this.compileStringParameter(code) + 2;
         }

         if (this.withConstructorParams) {
            stacksize += CtNewWrappedMethod.compileParameterList(code, parameters, 1);
         }

         String typeDesc = Descriptor.of(type);
         String mDesc = this.getDescriptor() + typeDesc;
         code.addInvokestatic(this.objectType, this.methodName, mDesc);
         code.addPutfield(Bytecode.THIS, name, typeDesc);
         return stacksize;
      }

      private String getDescriptor() {
         String desc3 = "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)";
         if (this.stringParams == null) {
            return this.withConstructorParams ? "(Ljava/lang/Object;[Ljava/lang/Object;)" : "(Ljava/lang/Object;)";
         } else {
            return this.withConstructorParams ? "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)" : "(Ljava/lang/Object;[Ljava/lang/String;)";
         }
      }

      int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
         int stacksize = 1;
         String desc;
         if (this.stringParams == null) {
            desc = "()";
         } else {
            desc = "([Ljava/lang/String;)";
            stacksize += this.compileStringParameter(code);
         }

         String typeDesc = Descriptor.of(type);
         code.addInvokestatic(this.objectType, this.methodName, desc + typeDesc);
         code.addPutstatic(Bytecode.THIS, name, typeDesc);
         return stacksize;
      }
   }

   static class NewInitializer extends CtField.Initializer {
      CtClass objectType;
      String[] stringParams;
      boolean withConstructorParams;

      int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
         code.addAload(0);
         code.addNew(this.objectType);
         code.add(89);
         code.addAload(0);
         int stacksize;
         if (this.stringParams == null) {
            stacksize = 4;
         } else {
            stacksize = this.compileStringParameter(code) + 4;
         }

         if (this.withConstructorParams) {
            stacksize += CtNewWrappedMethod.compileParameterList(code, parameters, 1);
         }

         code.addInvokespecial(this.objectType, "<init>", this.getDescriptor());
         code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
         return stacksize;
      }

      private String getDescriptor() {
         String desc3 = "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)V";
         if (this.stringParams == null) {
            return this.withConstructorParams ? "(Ljava/lang/Object;[Ljava/lang/Object;)V" : "(Ljava/lang/Object;)V";
         } else {
            return this.withConstructorParams ? "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)V" : "(Ljava/lang/Object;[Ljava/lang/String;)V";
         }
      }

      int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
         code.addNew(this.objectType);
         code.add(89);
         int stacksize = 2;
         String desc;
         if (this.stringParams == null) {
            desc = "()V";
         } else {
            desc = "([Ljava/lang/String;)V";
            stacksize += this.compileStringParameter(code);
         }

         code.addInvokespecial(this.objectType, "<init>", desc);
         code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
         return stacksize;
      }

      protected final int compileStringParameter(Bytecode code) throws CannotCompileException {
         int nparam = this.stringParams.length;
         code.addIconst(nparam);
         code.addAnewarray("java.lang.String");

         for(int j = 0; j < nparam; ++j) {
            code.add(89);
            code.addIconst(j);
            code.addLdc(this.stringParams[j]);
            code.add(83);
         }

         return 4;
      }
   }

   static class ParamInitializer extends CtField.Initializer {
      int nthParam;

      int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
         if (parameters != null && this.nthParam < parameters.length) {
            code.addAload(0);
            int nth = nthParamToLocal(this.nthParam, parameters, false);
            int s = code.addLoad(nth, type) + 1;
            code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
            return s;
         } else {
            return 0;
         }
      }

      static int nthParamToLocal(int nth, CtClass[] params, boolean isStatic) {
         CtClass longType = CtClass.longType;
         CtClass doubleType = CtClass.doubleType;
         int k;
         if (isStatic) {
            k = 0;
         } else {
            k = 1;
         }

         for(int i = 0; i < nth; ++i) {
            CtClass type = params[i];
            if (type != longType && type != doubleType) {
               ++k;
            } else {
               k += 2;
            }
         }

         return k;
      }

      int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
         return 0;
      }
   }

   static class PtreeInitializer extends CtField.CodeInitializer0 {
      private ASTree expression;

      PtreeInitializer(ASTree expr) {
         this.expression = expr;
      }

      void compileExpr(Javac drv) throws CompileError {
         drv.compileExpr(this.expression);
      }

      int getConstantValue(ConstPool cp, CtClass type) {
         return this.getConstantValue2(cp, type, this.expression);
      }
   }

   static class CodeInitializer extends CtField.CodeInitializer0 {
      private String expression;

      CodeInitializer(String expr) {
         this.expression = expr;
      }

      void compileExpr(Javac drv) throws CompileError {
         drv.compileExpr(this.expression);
      }

      int getConstantValue(ConstPool cp, CtClass type) {
         try {
            ASTree t = Javac.parseExpr(this.expression, new SymbolTable());
            return this.getConstantValue2(cp, type, t);
         } catch (CompileError var4) {
            return 0;
         }
      }
   }

   abstract static class CodeInitializer0 extends CtField.Initializer {
      abstract void compileExpr(Javac drv) throws CompileError;

      int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
         try {
            code.addAload(0);
            this.compileExpr(drv);
            code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
            return code.getMaxStack();
         } catch (CompileError var7) {
            throw new CannotCompileException(var7);
         }
      }

      int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
         try {
            this.compileExpr(drv);
            code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
            return code.getMaxStack();
         } catch (CompileError var6) {
            throw new CannotCompileException(var6);
         }
      }

      int getConstantValue2(ConstPool cp, CtClass type, ASTree tree) {
         if (type.isPrimitive()) {
            if (tree instanceof IntConst) {
               long value = ((IntConst)tree).get();
               if (type == CtClass.doubleType) {
                  return cp.addDoubleInfo((double)value);
               }

               if (type == CtClass.floatType) {
                  return cp.addFloatInfo((float)value);
               }

               if (type == CtClass.longType) {
                  return cp.addLongInfo(value);
               }

               if (type != CtClass.voidType) {
                  return cp.addIntegerInfo((int)value);
               }
            } else if (tree instanceof DoubleConst) {
               double value = ((DoubleConst)tree).get();
               if (type == CtClass.floatType) {
                  return cp.addFloatInfo((float)value);
               }

               if (type == CtClass.doubleType) {
                  return cp.addDoubleInfo(value);
               }
            }
         } else if (tree instanceof StringL && type.getName().equals("java.lang.String")) {
            return cp.addStringInfo(((StringL)tree).get());
         }

         return 0;
      }
   }

   public abstract static class Initializer {
      public static CtField.Initializer constant(int i) {
         return new CtField.IntInitializer(i);
      }

      public static CtField.Initializer constant(boolean b) {
         return new CtField.IntInitializer(b ? 1 : 0);
      }

      public static CtField.Initializer constant(long l) {
         return new CtField.LongInitializer(l);
      }

      public static CtField.Initializer constant(float l) {
         return new CtField.FloatInitializer(l);
      }

      public static CtField.Initializer constant(double d) {
         return new CtField.DoubleInitializer(d);
      }

      public static CtField.Initializer constant(String s) {
         return new CtField.StringInitializer(s);
      }

      public static CtField.Initializer byParameter(int nth) {
         CtField.ParamInitializer i = new CtField.ParamInitializer();
         i.nthParam = nth;
         return i;
      }

      public static CtField.Initializer byNew(CtClass objectType) {
         CtField.NewInitializer i = new CtField.NewInitializer();
         i.objectType = objectType;
         i.stringParams = null;
         i.withConstructorParams = false;
         return i;
      }

      public static CtField.Initializer byNew(CtClass objectType, String[] stringParams) {
         CtField.NewInitializer i = new CtField.NewInitializer();
         i.objectType = objectType;
         i.stringParams = stringParams;
         i.withConstructorParams = false;
         return i;
      }

      public static CtField.Initializer byNewWithParams(CtClass objectType) {
         CtField.NewInitializer i = new CtField.NewInitializer();
         i.objectType = objectType;
         i.stringParams = null;
         i.withConstructorParams = true;
         return i;
      }

      public static CtField.Initializer byNewWithParams(CtClass objectType, String[] stringParams) {
         CtField.NewInitializer i = new CtField.NewInitializer();
         i.objectType = objectType;
         i.stringParams = stringParams;
         i.withConstructorParams = true;
         return i;
      }

      public static CtField.Initializer byCall(CtClass methodClass, String methodName) {
         CtField.MethodInitializer i = new CtField.MethodInitializer();
         i.objectType = methodClass;
         i.methodName = methodName;
         i.stringParams = null;
         i.withConstructorParams = false;
         return i;
      }

      public static CtField.Initializer byCall(CtClass methodClass, String methodName, String[] stringParams) {
         CtField.MethodInitializer i = new CtField.MethodInitializer();
         i.objectType = methodClass;
         i.methodName = methodName;
         i.stringParams = stringParams;
         i.withConstructorParams = false;
         return i;
      }

      public static CtField.Initializer byCallWithParams(CtClass methodClass, String methodName) {
         CtField.MethodInitializer i = new CtField.MethodInitializer();
         i.objectType = methodClass;
         i.methodName = methodName;
         i.stringParams = null;
         i.withConstructorParams = true;
         return i;
      }

      public static CtField.Initializer byCallWithParams(CtClass methodClass, String methodName, String[] stringParams) {
         CtField.MethodInitializer i = new CtField.MethodInitializer();
         i.objectType = methodClass;
         i.methodName = methodName;
         i.stringParams = stringParams;
         i.withConstructorParams = true;
         return i;
      }

      public static CtField.Initializer byNewArray(CtClass type, int size) throws NotFoundException {
         return new CtField.ArrayInitializer(type.getComponentType(), size);
      }

      public static CtField.Initializer byNewArray(CtClass type, int[] sizes) {
         return new CtField.MultiArrayInitializer(type, sizes);
      }

      public static CtField.Initializer byExpr(String source) {
         return new CtField.CodeInitializer(source);
      }

      static CtField.Initializer byExpr(ASTree source) {
         return new CtField.PtreeInitializer(source);
      }

      void check(String desc) throws CannotCompileException {
      }

      abstract int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException;

      abstract int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException;

      int getConstantValue(ConstPool cp, CtClass type) {
         return 0;
      }
   }
}
