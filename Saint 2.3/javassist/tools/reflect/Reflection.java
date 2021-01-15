package javassist.tools.reflect;

import java.util.Iterator;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;

public class Reflection implements Translator {
   static final String classobjectField = "_classobject";
   static final String classobjectAccessor = "_getClass";
   static final String metaobjectField = "_metaobject";
   static final String metaobjectGetter = "_getMetaobject";
   static final String metaobjectSetter = "_setMetaobject";
   static final String readPrefix = "_r_";
   static final String writePrefix = "_w_";
   static final String metaobjectClassName = "javassist.tools.reflect.Metaobject";
   static final String classMetaobjectClassName = "javassist.tools.reflect.ClassMetaobject";
   protected CtMethod trapMethod;
   protected CtMethod trapStaticMethod;
   protected CtMethod trapRead;
   protected CtMethod trapWrite;
   protected CtClass[] readParam;
   protected ClassPool classPool = null;
   protected CodeConverter converter = new CodeConverter();

   private boolean isExcluded(String name) {
      return name.startsWith("_m_") || name.equals("_getClass") || name.equals("_setMetaobject") || name.equals("_getMetaobject") || name.startsWith("_r_") || name.startsWith("_w_");
   }

   public void start(ClassPool pool) throws NotFoundException {
      this.classPool = pool;
      String var2 = "javassist.tools.reflect.Sample is not found or broken.";

      try {
         CtClass c = this.classPool.get("javassist.tools.reflect.Sample");
         this.rebuildClassFile(c.getClassFile());
         this.trapMethod = c.getDeclaredMethod("trap");
         this.trapStaticMethod = c.getDeclaredMethod("trapStatic");
         this.trapRead = c.getDeclaredMethod("trapRead");
         this.trapWrite = c.getDeclaredMethod("trapWrite");
         this.readParam = new CtClass[]{this.classPool.get("java.lang.Object")};
      } catch (NotFoundException var4) {
         throw new RuntimeException("javassist.tools.reflect.Sample is not found or broken.");
      } catch (BadBytecode var5) {
         throw new RuntimeException("javassist.tools.reflect.Sample is not found or broken.");
      }
   }

   public void onLoad(ClassPool pool, String classname) throws CannotCompileException, NotFoundException {
      CtClass clazz = pool.get(classname);
      clazz.instrument(this.converter);
   }

   public boolean makeReflective(String classname, String metaobject, String metaclass) throws CannotCompileException, NotFoundException {
      return this.makeReflective(this.classPool.get(classname), this.classPool.get(metaobject), this.classPool.get(metaclass));
   }

   public boolean makeReflective(Class clazz, Class metaobject, Class metaclass) throws CannotCompileException, NotFoundException {
      return this.makeReflective(clazz.getName(), metaobject.getName(), metaclass.getName());
   }

   public boolean makeReflective(CtClass clazz, CtClass metaobject, CtClass metaclass) throws CannotCompileException, CannotReflectException, NotFoundException {
      if (clazz.isInterface()) {
         throw new CannotReflectException("Cannot reflect an interface: " + clazz.getName());
      } else if (clazz.subclassOf(this.classPool.get("javassist.tools.reflect.ClassMetaobject"))) {
         throw new CannotReflectException("Cannot reflect a subclass of ClassMetaobject: " + clazz.getName());
      } else if (clazz.subclassOf(this.classPool.get("javassist.tools.reflect.Metaobject"))) {
         throw new CannotReflectException("Cannot reflect a subclass of Metaobject: " + clazz.getName());
      } else {
         this.registerReflectiveClass(clazz);
         return this.modifyClassfile(clazz, metaobject, metaclass);
      }
   }

   private void registerReflectiveClass(CtClass clazz) {
      CtField[] fs = clazz.getDeclaredFields();

      for(int i = 0; i < fs.length; ++i) {
         CtField f = fs[i];
         int mod = f.getModifiers();
         if ((mod & 1) != 0 && (mod & 16) == 0) {
            String name = f.getName();
            this.converter.replaceFieldRead(f, clazz, "_r_" + name);
            this.converter.replaceFieldWrite(f, clazz, "_w_" + name);
         }
      }

   }

   private boolean modifyClassfile(CtClass clazz, CtClass metaobject, CtClass metaclass) throws CannotCompileException, NotFoundException {
      if (clazz.getAttribute("Reflective") != null) {
         return false;
      } else {
         clazz.setAttribute("Reflective", new byte[0]);
         CtClass mlevel = this.classPool.get("javassist.tools.reflect.Metalevel");
         boolean addMeta = !clazz.subtypeOf(mlevel);
         if (addMeta) {
            clazz.addInterface(mlevel);
         }

         this.processMethods(clazz, addMeta);
         this.processFields(clazz);
         CtField f;
         if (addMeta) {
            f = new CtField(this.classPool.get("javassist.tools.reflect.Metaobject"), "_metaobject", clazz);
            f.setModifiers(4);
            clazz.addField(f, CtField.Initializer.byNewWithParams(metaobject));
            clazz.addMethod(CtNewMethod.getter("_getMetaobject", f));
            clazz.addMethod(CtNewMethod.setter("_setMetaobject", f));
         }

         f = new CtField(this.classPool.get("javassist.tools.reflect.ClassMetaobject"), "_classobject", clazz);
         f.setModifiers(10);
         clazz.addField(f, CtField.Initializer.byNew(metaclass, new String[]{clazz.getName()}));
         clazz.addMethod(CtNewMethod.getter("_getClass", f));
         return true;
      }
   }

   private void processMethods(CtClass clazz, boolean dontSearch) throws CannotCompileException, NotFoundException {
      CtMethod[] ms = clazz.getMethods();

      for(int i = 0; i < ms.length; ++i) {
         CtMethod m = ms[i];
         int mod = m.getModifiers();
         if (Modifier.isPublic(mod) && !Modifier.isAbstract(mod)) {
            this.processMethods0(mod, clazz, m, i, dontSearch);
         }
      }

   }

   private void processMethods0(int mod, CtClass clazz, CtMethod m, int identifier, boolean dontSearch) throws CannotCompileException, NotFoundException {
      String name = m.getName();
      if (!this.isExcluded(name)) {
         CtMethod m2;
         if (m.getDeclaringClass() == clazz) {
            if (Modifier.isNative(mod)) {
               return;
            }

            m2 = m;
            if (Modifier.isFinal(mod)) {
               mod &= -17;
               m.setModifiers(mod);
            }
         } else {
            if (Modifier.isFinal(mod)) {
               return;
            }

            mod &= -257;
            m2 = CtNewMethod.delegator(this.findOriginal(m, dontSearch), clazz);
            m2.setModifiers(mod);
            clazz.addMethod(m2);
         }

         m2.setName("_m_" + identifier + "_" + name);
         CtMethod body;
         if (Modifier.isStatic(mod)) {
            body = this.trapStaticMethod;
         } else {
            body = this.trapMethod;
         }

         CtMethod wmethod = CtNewMethod.wrapped(m.getReturnType(), name, m.getParameterTypes(), m.getExceptionTypes(), body, CtMethod.ConstParameter.integer(identifier), clazz);
         wmethod.setModifiers(mod);
         clazz.addMethod(wmethod);
      }
   }

   private CtMethod findOriginal(CtMethod m, boolean dontSearch) throws NotFoundException {
      if (dontSearch) {
         return m;
      } else {
         String name = m.getName();
         CtMethod[] ms = m.getDeclaringClass().getDeclaredMethods();

         for(int i = 0; i < ms.length; ++i) {
            String orgName = ms[i].getName();
            if (orgName.endsWith(name) && orgName.startsWith("_m_") && ms[i].getSignature().equals(m.getSignature())) {
               return ms[i];
            }
         }

         return m;
      }
   }

   private void processFields(CtClass clazz) throws CannotCompileException, NotFoundException {
      CtField[] fs = clazz.getDeclaredFields();

      for(int i = 0; i < fs.length; ++i) {
         CtField f = fs[i];
         int mod = f.getModifiers();
         if ((mod & 1) != 0 && (mod & 16) == 0) {
            mod |= 8;
            String name = f.getName();
            CtClass ftype = f.getType();
            CtMethod wmethod = CtNewMethod.wrapped(ftype, "_r_" + name, this.readParam, (CtClass[])null, this.trapRead, CtMethod.ConstParameter.string(name), clazz);
            wmethod.setModifiers(mod);
            clazz.addMethod(wmethod);
            CtClass[] writeParam = new CtClass[]{this.classPool.get("java.lang.Object"), ftype};
            wmethod = CtNewMethod.wrapped(CtClass.voidType, "_w_" + name, writeParam, (CtClass[])null, this.trapWrite, CtMethod.ConstParameter.string(name), clazz);
            wmethod.setModifiers(mod);
            clazz.addMethod(wmethod);
         }
      }

   }

   public void rebuildClassFile(ClassFile cf) throws BadBytecode {
      if (ClassFile.MAJOR_VERSION >= 50) {
         Iterator methods = cf.getMethods().iterator();

         while(methods.hasNext()) {
            MethodInfo mi = (MethodInfo)methods.next();
            mi.rebuildStackMap(this.classPool);
         }

      }
   }
}
