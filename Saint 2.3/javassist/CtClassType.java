package javassist;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ConstantAttribute;
import javassist.bytecode.Descriptor;
import javassist.bytecode.EnclosingMethodAttribute;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.InnerClassesAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.compiler.AccessorMaker;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.expr.ExprEditor;

class CtClassType extends CtClass {
   ClassPool classPool;
   boolean wasChanged;
   private boolean wasFrozen;
   boolean wasPruned;
   boolean gcConstPool;
   ClassFile classfile;
   byte[] rawClassfile;
   private WeakReference memberCache;
   private AccessorMaker accessors;
   private FieldInitLink fieldInitializers;
   private Hashtable hiddenMethods;
   private int uniqueNumberSeed;
   private boolean doPruning;
   private int getCount;
   private static final int GET_THRESHOLD = 2;

   CtClassType(String name, ClassPool cp) {
      super(name);
      this.doPruning = ClassPool.doPruning;
      this.classPool = cp;
      this.wasChanged = this.wasFrozen = this.wasPruned = this.gcConstPool = false;
      this.classfile = null;
      this.rawClassfile = null;
      this.memberCache = null;
      this.accessors = null;
      this.fieldInitializers = null;
      this.hiddenMethods = null;
      this.uniqueNumberSeed = 0;
      this.getCount = 0;
   }

   CtClassType(InputStream ins, ClassPool cp) throws IOException {
      this((String)null, cp);
      this.classfile = new ClassFile(new DataInputStream(ins));
      this.qualifiedName = this.classfile.getName();
   }

   protected void extendToString(StringBuffer buffer) {
      if (this.wasChanged) {
         buffer.append("changed ");
      }

      if (this.wasFrozen) {
         buffer.append("frozen ");
      }

      if (this.wasPruned) {
         buffer.append("pruned ");
      }

      buffer.append(Modifier.toString(this.getModifiers()));
      buffer.append(" class ");
      buffer.append(this.getName());

      try {
         CtClass ext = this.getSuperclass();
         if (ext != null) {
            String name = ext.getName();
            if (!name.equals("java.lang.Object")) {
               buffer.append(" extends " + ext.getName());
            }
         }
      } catch (NotFoundException var4) {
         buffer.append(" extends ??");
      }

      try {
         CtClass[] intf = this.getInterfaces();
         if (intf.length > 0) {
            buffer.append(" implements ");
         }

         for(int i = 0; i < intf.length; ++i) {
            buffer.append(intf[i].getName());
            buffer.append(", ");
         }
      } catch (NotFoundException var5) {
         buffer.append(" extends ??");
      }

      CtMember.Cache memCache = this.getMembers();
      this.exToString(buffer, " fields=", memCache.fieldHead(), memCache.lastField());
      this.exToString(buffer, " constructors=", memCache.consHead(), memCache.lastCons());
      this.exToString(buffer, " methods=", memCache.methodHead(), memCache.lastMethod());
   }

   private void exToString(StringBuffer buffer, String msg, CtMember head, CtMember tail) {
      buffer.append(msg);

      while(head != tail) {
         head = head.next();
         buffer.append(head);
         buffer.append(", ");
      }

   }

   public AccessorMaker getAccessorMaker() {
      if (this.accessors == null) {
         this.accessors = new AccessorMaker(this);
      }

      return this.accessors;
   }

   public ClassFile getClassFile2() {
      ClassFile cfile = this.classfile;
      if (cfile != null) {
         return cfile;
      } else {
         this.classPool.compress();
         if (this.rawClassfile != null) {
            try {
               this.classfile = new ClassFile(new DataInputStream(new ByteArrayInputStream(this.rawClassfile)));
               this.rawClassfile = null;
               this.getCount = 2;
               return this.classfile;
            } catch (IOException var15) {
               throw new RuntimeException(var15.toString(), var15);
            }
         } else {
            Object fin = null;

            ClassFile var4;
            try {
               fin = this.classPool.openClassfile(this.getName());
               if (fin == null) {
                  throw new NotFoundException(this.getName());
               }

               fin = new BufferedInputStream((InputStream)fin);
               ClassFile cf = new ClassFile(new DataInputStream((InputStream)fin));
               if (!cf.getName().equals(this.qualifiedName)) {
                  throw new RuntimeException("cannot find " + this.qualifiedName + ": " + cf.getName() + " found in " + this.qualifiedName.replace('.', '/') + ".class");
               }

               this.classfile = cf;
               var4 = cf;
            } catch (NotFoundException var16) {
               throw new RuntimeException(var16.toString(), var16);
            } catch (IOException var17) {
               throw new RuntimeException(var17.toString(), var17);
            } finally {
               if (fin != null) {
                  try {
                     ((InputStream)fin).close();
                  } catch (IOException var14) {
                  }
               }

            }

            return var4;
         }
      }
   }

   final void incGetCounter() {
      ++this.getCount;
   }

   void compress() {
      if (this.getCount < 2) {
         if (!this.isModified() && ClassPool.releaseUnmodifiedClassFile) {
            this.removeClassFile();
         } else if (this.isFrozen() && !this.wasPruned) {
            this.saveClassFile();
         }
      }

      this.getCount = 0;
   }

   private synchronized void saveClassFile() {
      if (this.classfile != null && this.hasMemberCache() == null) {
         ByteArrayOutputStream barray = new ByteArrayOutputStream();
         DataOutputStream out = new DataOutputStream(barray);

         try {
            this.classfile.write(out);
            barray.close();
            this.rawClassfile = barray.toByteArray();
            this.classfile = null;
         } catch (IOException var4) {
         }

      }
   }

   private synchronized void removeClassFile() {
      if (this.classfile != null && !this.isModified() && this.hasMemberCache() == null) {
         this.classfile = null;
      }

   }

   public ClassPool getClassPool() {
      return this.classPool;
   }

   void setClassPool(ClassPool cp) {
      this.classPool = cp;
   }

   public URL getURL() throws NotFoundException {
      URL url = this.classPool.find(this.getName());
      if (url == null) {
         throw new NotFoundException(this.getName());
      } else {
         return url;
      }
   }

   public boolean isModified() {
      return this.wasChanged;
   }

   public boolean isFrozen() {
      return this.wasFrozen;
   }

   public void freeze() {
      this.wasFrozen = true;
   }

   void checkModify() throws RuntimeException {
      if (this.isFrozen()) {
         String msg = this.getName() + " class is frozen";
         if (this.wasPruned) {
            msg = msg + " and pruned";
         }

         throw new RuntimeException(msg);
      } else {
         this.wasChanged = true;
      }
   }

   public void defrost() {
      this.checkPruned("defrost");
      this.wasFrozen = false;
   }

   public boolean subtypeOf(CtClass clazz) throws NotFoundException {
      String cname = clazz.getName();
      if (this != clazz && !this.getName().equals(cname)) {
         ClassFile file = this.getClassFile2();
         String supername = file.getSuperclass();
         if (supername != null && supername.equals(cname)) {
            return true;
         } else {
            String[] ifs = file.getInterfaces();
            int num = ifs.length;

            int i;
            for(i = 0; i < num; ++i) {
               if (ifs[i].equals(cname)) {
                  return true;
               }
            }

            if (supername != null && this.classPool.get(supername).subtypeOf(clazz)) {
               return true;
            } else {
               for(i = 0; i < num; ++i) {
                  if (this.classPool.get(ifs[i]).subtypeOf(clazz)) {
                     return true;
                  }
               }

               return false;
            }
         }
      } else {
         return true;
      }
   }

   public void setName(String name) throws RuntimeException {
      String oldname = this.getName();
      if (!name.equals(oldname)) {
         this.classPool.checkNotFrozen(name);
         ClassFile cf = this.getClassFile2();
         super.setName(name);
         cf.setName(name);
         this.nameReplaced();
         this.classPool.classNameChanged(oldname, this);
      }
   }

   public String getGenericSignature() {
      SignatureAttribute sa = (SignatureAttribute)this.getClassFile2().getAttribute("Signature");
      return sa == null ? null : sa.getSignature();
   }

   public void setGenericSignature(String sig) {
      ClassFile cf = this.getClassFile();
      SignatureAttribute sa = new SignatureAttribute(cf.getConstPool(), sig);
      cf.addAttribute(sa);
   }

   public void replaceClassName(ClassMap classnames) throws RuntimeException {
      String oldClassName = this.getName();
      String newClassName = (String)classnames.get(Descriptor.toJvmName(oldClassName));
      if (newClassName != null) {
         newClassName = Descriptor.toJavaName(newClassName);
         this.classPool.checkNotFrozen(newClassName);
      }

      super.replaceClassName(classnames);
      ClassFile cf = this.getClassFile2();
      cf.renameClass(classnames);
      this.nameReplaced();
      if (newClassName != null) {
         super.setName(newClassName);
         this.classPool.classNameChanged(oldClassName, this);
      }

   }

   public void replaceClassName(String oldname, String newname) throws RuntimeException {
      String thisname = this.getName();
      if (thisname.equals(oldname)) {
         this.setName(newname);
      } else {
         super.replaceClassName(oldname, newname);
         this.getClassFile2().renameClass(oldname, newname);
         this.nameReplaced();
      }

   }

   public boolean isInterface() {
      return Modifier.isInterface(this.getModifiers());
   }

   public boolean isAnnotation() {
      return Modifier.isAnnotation(this.getModifiers());
   }

   public boolean isEnum() {
      return Modifier.isEnum(this.getModifiers());
   }

   public int getModifiers() {
      ClassFile cf = this.getClassFile2();
      int acc = cf.getAccessFlags();
      acc = AccessFlag.clear(acc, 32);
      int inner = cf.getInnerAccessFlags();
      if (inner != -1 && (inner & 8) != 0) {
         acc |= 8;
      }

      return AccessFlag.toModifier(acc);
   }

   public CtClass[] getNestedClasses() throws NotFoundException {
      ClassFile cf = this.getClassFile2();
      InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
      if (ica == null) {
         return new CtClass[0];
      } else {
         String thisName = cf.getName() + "$";
         int n = ica.tableLength();
         ArrayList list = new ArrayList(n);

         for(int i = 0; i < n; ++i) {
            String name = ica.innerClass(i);
            if (name != null && name.startsWith(thisName) && name.lastIndexOf(36) < thisName.length()) {
               list.add(this.classPool.get(name));
            }
         }

         return (CtClass[])((CtClass[])list.toArray(new CtClass[list.size()]));
      }
   }

   public void setModifiers(int mod) {
      ClassFile cf = this.getClassFile2();
      if (Modifier.isStatic(mod)) {
         int flags = cf.getInnerAccessFlags();
         if (flags == -1 || (flags & 8) == 0) {
            throw new RuntimeException("cannot change " + this.getName() + " into a static class");
         }

         mod &= -9;
      }

      this.checkModify();
      cf.setAccessFlags(AccessFlag.of(mod));
   }

   public boolean hasAnnotation(Class clz) {
      ClassFile cf = this.getClassFile2();
      AnnotationsAttribute ainfo = (AnnotationsAttribute)cf.getAttribute("RuntimeInvisibleAnnotations");
      AnnotationsAttribute ainfo2 = (AnnotationsAttribute)cf.getAttribute("RuntimeVisibleAnnotations");
      return hasAnnotationType(clz, this.getClassPool(), ainfo, ainfo2);
   }

   static boolean hasAnnotationType(Class clz, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) {
      Annotation[] anno1;
      if (a1 == null) {
         anno1 = null;
      } else {
         anno1 = a1.getAnnotations();
      }

      Annotation[] anno2;
      if (a2 == null) {
         anno2 = null;
      } else {
         anno2 = a2.getAnnotations();
      }

      String typeName = clz.getName();
      int i;
      if (anno1 != null) {
         for(i = 0; i < anno1.length; ++i) {
            if (anno1[i].getTypeName().equals(typeName)) {
               return true;
            }
         }
      }

      if (anno2 != null) {
         for(i = 0; i < anno2.length; ++i) {
            if (anno2[i].getTypeName().equals(typeName)) {
               return true;
            }
         }
      }

      return false;
   }

   public Object getAnnotation(Class clz) throws ClassNotFoundException {
      ClassFile cf = this.getClassFile2();
      AnnotationsAttribute ainfo = (AnnotationsAttribute)cf.getAttribute("RuntimeInvisibleAnnotations");
      AnnotationsAttribute ainfo2 = (AnnotationsAttribute)cf.getAttribute("RuntimeVisibleAnnotations");
      return getAnnotationType(clz, this.getClassPool(), ainfo, ainfo2);
   }

   static Object getAnnotationType(Class clz, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) throws ClassNotFoundException {
      Annotation[] anno1;
      if (a1 == null) {
         anno1 = null;
      } else {
         anno1 = a1.getAnnotations();
      }

      Annotation[] anno2;
      if (a2 == null) {
         anno2 = null;
      } else {
         anno2 = a2.getAnnotations();
      }

      String typeName = clz.getName();
      int i;
      if (anno1 != null) {
         for(i = 0; i < anno1.length; ++i) {
            if (anno1[i].getTypeName().equals(typeName)) {
               return toAnnoType(anno1[i], cp);
            }
         }
      }

      if (anno2 != null) {
         for(i = 0; i < anno2.length; ++i) {
            if (anno2[i].getTypeName().equals(typeName)) {
               return toAnnoType(anno2[i], cp);
            }
         }
      }

      return null;
   }

   public Object[] getAnnotations() throws ClassNotFoundException {
      return this.getAnnotations(false);
   }

   public Object[] getAvailableAnnotations() {
      try {
         return this.getAnnotations(true);
      } catch (ClassNotFoundException var2) {
         throw new RuntimeException("Unexpected exception ", var2);
      }
   }

   private Object[] getAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
      ClassFile cf = this.getClassFile2();
      AnnotationsAttribute ainfo = (AnnotationsAttribute)cf.getAttribute("RuntimeInvisibleAnnotations");
      AnnotationsAttribute ainfo2 = (AnnotationsAttribute)cf.getAttribute("RuntimeVisibleAnnotations");
      return toAnnotationType(ignoreNotFound, this.getClassPool(), ainfo, ainfo2);
   }

   static Object[] toAnnotationType(boolean ignoreNotFound, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) throws ClassNotFoundException {
      Annotation[] anno1;
      int size1;
      if (a1 == null) {
         anno1 = null;
         size1 = 0;
      } else {
         anno1 = a1.getAnnotations();
         size1 = anno1.length;
      }

      Annotation[] anno2;
      int size2;
      if (a2 == null) {
         anno2 = null;
         size2 = 0;
      } else {
         anno2 = a2.getAnnotations();
         size2 = anno2.length;
      }

      int j;
      if (!ignoreNotFound) {
         Object[] result = new Object[size1 + size2];

         for(j = 0; j < size1; ++j) {
            result[j] = toAnnoType(anno1[j], cp);
         }

         for(j = 0; j < size2; ++j) {
            result[j + size1] = toAnnoType(anno2[j], cp);
         }

         return result;
      } else {
         ArrayList annotations = new ArrayList();

         for(j = 0; j < size1; ++j) {
            try {
               annotations.add(toAnnoType(anno1[j], cp));
            } catch (ClassNotFoundException var12) {
            }
         }

         for(j = 0; j < size2; ++j) {
            try {
               annotations.add(toAnnoType(anno2[j], cp));
            } catch (ClassNotFoundException var11) {
            }
         }

         return annotations.toArray();
      }
   }

   static Object[][] toAnnotationType(boolean ignoreNotFound, ClassPool cp, ParameterAnnotationsAttribute a1, ParameterAnnotationsAttribute a2, MethodInfo minfo) throws ClassNotFoundException {
      int numParameters = false;
      int numParameters;
      if (a1 != null) {
         numParameters = a1.numParameters();
      } else if (a2 != null) {
         numParameters = a2.numParameters();
      } else {
         numParameters = Descriptor.numOfParameters(minfo.getDescriptor());
      }

      Object[][] result = new Object[numParameters][];

      for(int i = 0; i < numParameters; ++i) {
         Annotation[] anno1;
         int size1;
         if (a1 == null) {
            anno1 = null;
            size1 = 0;
         } else {
            anno1 = a1.getAnnotations()[i];
            size1 = anno1.length;
         }

         Annotation[] anno2;
         int size2;
         if (a2 == null) {
            anno2 = null;
            size2 = 0;
         } else {
            anno2 = a2.getAnnotations()[i];
            size2 = anno2.length;
         }

         if (!ignoreNotFound) {
            result[i] = new Object[size1 + size2];

            int j;
            for(j = 0; j < size1; ++j) {
               result[i][j] = toAnnoType(anno1[j], cp);
            }

            for(j = 0; j < size2; ++j) {
               result[i][j + size1] = toAnnoType(anno2[j], cp);
            }
         } else {
            ArrayList annotations = new ArrayList();

            int j;
            for(j = 0; j < size1; ++j) {
               try {
                  annotations.add(toAnnoType(anno1[j], cp));
               } catch (ClassNotFoundException var16) {
               }
            }

            for(j = 0; j < size2; ++j) {
               try {
                  annotations.add(toAnnoType(anno2[j], cp));
               } catch (ClassNotFoundException var15) {
               }
            }

            result[i] = annotations.toArray();
         }
      }

      return result;
   }

   private static Object toAnnoType(Annotation anno, ClassPool cp) throws ClassNotFoundException {
      try {
         ClassLoader cl = cp.getClassLoader();
         return anno.toAnnotationType(cl, cp);
      } catch (ClassNotFoundException var8) {
         ClassLoader cl2 = cp.getClass().getClassLoader();

         try {
            return anno.toAnnotationType(cl2, cp);
         } catch (ClassNotFoundException var7) {
            try {
               Class clazz = cp.get(anno.getTypeName()).toClass();
               return AnnotationImpl.make(clazz.getClassLoader(), clazz, cp, anno);
            } catch (Throwable var6) {
               throw new ClassNotFoundException(anno.getTypeName());
            }
         }
      }
   }

   public boolean subclassOf(CtClass superclass) {
      if (superclass == null) {
         return false;
      } else {
         String superName = superclass.getName();
         Object curr = this;

         try {
            while(curr != null) {
               if (((CtClass)curr).getName().equals(superName)) {
                  return true;
               }

               curr = ((CtClass)curr).getSuperclass();
            }
         } catch (Exception var5) {
         }

         return false;
      }
   }

   public CtClass getSuperclass() throws NotFoundException {
      String supername = this.getClassFile2().getSuperclass();
      return supername == null ? null : this.classPool.get(supername);
   }

   public void setSuperclass(CtClass clazz) throws CannotCompileException {
      this.checkModify();
      if (this.isInterface()) {
         this.addInterface(clazz);
      } else {
         this.getClassFile2().setSuperclass(clazz.getName());
      }

   }

   public CtClass[] getInterfaces() throws NotFoundException {
      String[] ifs = this.getClassFile2().getInterfaces();
      int num = ifs.length;
      CtClass[] ifc = new CtClass[num];

      for(int i = 0; i < num; ++i) {
         ifc[i] = this.classPool.get(ifs[i]);
      }

      return ifc;
   }

   public void setInterfaces(CtClass[] list) {
      this.checkModify();
      String[] ifs;
      if (list == null) {
         ifs = new String[0];
      } else {
         int num = list.length;
         ifs = new String[num];

         for(int i = 0; i < num; ++i) {
            ifs[i] = list[i].getName();
         }
      }

      this.getClassFile2().setInterfaces(ifs);
   }

   public void addInterface(CtClass anInterface) {
      this.checkModify();
      if (anInterface != null) {
         this.getClassFile2().addInterface(anInterface.getName());
      }

   }

   public CtClass getDeclaringClass() throws NotFoundException {
      ClassFile cf = this.getClassFile2();
      InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
      if (ica == null) {
         return null;
      } else {
         String name = this.getName();
         int n = ica.tableLength();

         for(int i = 0; i < n; ++i) {
            if (name.equals(ica.innerClass(i))) {
               String outName = ica.outerClass(i);
               if (outName != null) {
                  return this.classPool.get(outName);
               }

               EnclosingMethodAttribute ema = (EnclosingMethodAttribute)cf.getAttribute("EnclosingMethod");
               if (ema != null) {
                  return this.classPool.get(ema.className());
               }
            }
         }

         return null;
      }
   }

   public CtBehavior getEnclosingBehavior() throws NotFoundException {
      ClassFile cf = this.getClassFile2();
      EnclosingMethodAttribute ema = (EnclosingMethodAttribute)cf.getAttribute("EnclosingMethod");
      if (ema == null) {
         return null;
      } else {
         CtClass enc = this.classPool.get(ema.className());
         String name = ema.methodName();
         if ("<init>".equals(name)) {
            return enc.getConstructor(ema.methodDescriptor());
         } else {
            return (CtBehavior)("<clinit>".equals(name) ? enc.getClassInitializer() : enc.getMethod(name, ema.methodDescriptor()));
         }
      }
   }

   public CtClass makeNestedClass(String name, boolean isStatic) {
      if (!isStatic) {
         throw new RuntimeException("sorry, only nested static class is supported");
      } else {
         this.checkModify();
         CtClass c = this.classPool.makeNestedClass(this.getName() + "$" + name);
         ClassFile cf = this.getClassFile2();
         ClassFile cf2 = c.getClassFile2();
         InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
         if (ica == null) {
            ica = new InnerClassesAttribute(cf.getConstPool());
            cf.addAttribute(ica);
         }

         ica.append(c.getName(), this.getName(), name, cf2.getAccessFlags() & -33 | 8);
         cf2.addAttribute(ica.copy(cf2.getConstPool(), (Map)null));
         return c;
      }
   }

   private void nameReplaced() {
      CtMember.Cache cache = this.hasMemberCache();
      if (cache != null) {
         CtMember mth = cache.methodHead();
         CtMember tail = cache.lastMethod();

         while(mth != tail) {
            mth = mth.next();
            mth.nameReplaced();
         }
      }

   }

   protected CtMember.Cache hasMemberCache() {
      return this.memberCache != null ? (CtMember.Cache)this.memberCache.get() : null;
   }

   protected synchronized CtMember.Cache getMembers() {
      CtMember.Cache cache = null;
      if (this.memberCache == null || (cache = (CtMember.Cache)this.memberCache.get()) == null) {
         cache = new CtMember.Cache(this);
         this.makeFieldCache(cache);
         this.makeBehaviorCache(cache);
         this.memberCache = new WeakReference(cache);
      }

      return cache;
   }

   private void makeFieldCache(CtMember.Cache cache) {
      List list = this.getClassFile2().getFields();
      int n = list.size();

      for(int i = 0; i < n; ++i) {
         FieldInfo finfo = (FieldInfo)list.get(i);
         CtField newField = new CtField(finfo, this);
         cache.addField(newField);
      }

   }

   private void makeBehaviorCache(CtMember.Cache cache) {
      List list = this.getClassFile2().getMethods();
      int n = list.size();

      for(int i = 0; i < n; ++i) {
         MethodInfo minfo = (MethodInfo)list.get(i);
         if (minfo.isMethod()) {
            CtMethod newMethod = new CtMethod(minfo, this);
            cache.addMethod(newMethod);
         } else {
            CtConstructor newCons = new CtConstructor(minfo, this);
            cache.addConstructor(newCons);
         }
      }

   }

   public CtField[] getFields() {
      ArrayList alist = new ArrayList();
      getFields(alist, this);
      return (CtField[])((CtField[])alist.toArray(new CtField[alist.size()]));
   }

   private static void getFields(ArrayList alist, CtClass cc) {
      if (cc != null) {
         try {
            getFields(alist, cc.getSuperclass());
         } catch (NotFoundException var7) {
         }

         try {
            CtClass[] ifs = cc.getInterfaces();
            int num = ifs.length;

            for(int i = 0; i < num; ++i) {
               getFields(alist, ifs[i]);
            }
         } catch (NotFoundException var8) {
         }

         CtMember.Cache memCache = ((CtClassType)cc).getMembers();
         CtMember field = memCache.fieldHead();
         CtMember tail = memCache.lastField();

         while(field != tail) {
            field = field.next();
            if (!Modifier.isPrivate(field.getModifiers())) {
               alist.add(field);
            }
         }

      }
   }

   public CtField getField(String name, String desc) throws NotFoundException {
      CtField f = this.getField2(name, desc);
      return this.checkGetField(f, name, desc);
   }

   private CtField checkGetField(CtField f, String name, String desc) throws NotFoundException {
      if (f == null) {
         String msg = "field: " + name;
         if (desc != null) {
            msg = msg + " type " + desc;
         }

         throw new NotFoundException(msg + " in " + this.getName());
      } else {
         return f;
      }
   }

   CtField getField2(String name, String desc) {
      CtField df = this.getDeclaredField2(name, desc);
      if (df != null) {
         return df;
      } else {
         try {
            CtClass[] ifs = this.getInterfaces();
            int num = ifs.length;

            for(int i = 0; i < num; ++i) {
               CtField f = ifs[i].getField2(name, desc);
               if (f != null) {
                  return f;
               }
            }

            CtClass s = this.getSuperclass();
            if (s != null) {
               return s.getField2(name, desc);
            }
         } catch (NotFoundException var8) {
         }

         return null;
      }
   }

   public CtField[] getDeclaredFields() {
      CtMember.Cache memCache = this.getMembers();
      CtMember field = memCache.fieldHead();
      CtMember tail = memCache.lastField();
      int num = CtMember.Cache.count(field, tail);
      CtField[] cfs = new CtField[num];

      for(int var6 = 0; field != tail; cfs[var6++] = (CtField)field) {
         field = field.next();
      }

      return cfs;
   }

   public CtField getDeclaredField(String name) throws NotFoundException {
      return this.getDeclaredField(name, (String)null);
   }

   public CtField getDeclaredField(String name, String desc) throws NotFoundException {
      CtField f = this.getDeclaredField2(name, desc);
      return this.checkGetField(f, name, desc);
   }

   private CtField getDeclaredField2(String name, String desc) {
      CtMember.Cache memCache = this.getMembers();
      CtMember field = memCache.fieldHead();
      CtMember tail = memCache.lastField();

      do {
         do {
            if (field == tail) {
               return null;
            }

            field = field.next();
         } while(!field.getName().equals(name));
      } while(desc != null && !desc.equals(field.getSignature()));

      return (CtField)field;
   }

   public CtBehavior[] getDeclaredBehaviors() {
      CtMember.Cache memCache = this.getMembers();
      CtMember cons = memCache.consHead();
      CtMember consTail = memCache.lastCons();
      int cnum = CtMember.Cache.count(cons, consTail);
      CtMember mth = memCache.methodHead();
      CtMember mthTail = memCache.lastMethod();
      int mnum = CtMember.Cache.count(mth, mthTail);
      CtBehavior[] cb = new CtBehavior[cnum + mnum];

      int var9;
      for(var9 = 0; cons != consTail; cb[var9++] = (CtBehavior)cons) {
         cons = cons.next();
      }

      while(mth != mthTail) {
         mth = mth.next();
         cb[var9++] = (CtBehavior)mth;
      }

      return cb;
   }

   public CtConstructor[] getConstructors() {
      CtMember.Cache memCache = this.getMembers();
      CtMember cons = memCache.consHead();
      CtMember consTail = memCache.lastCons();
      int n = 0;
      CtMember mem = cons;

      while(mem != consTail) {
         mem = mem.next();
         if (isPubCons((CtConstructor)mem)) {
            ++n;
         }
      }

      CtConstructor[] result = new CtConstructor[n];
      int i = 0;
      mem = cons;

      while(mem != consTail) {
         mem = mem.next();
         CtConstructor cc = (CtConstructor)mem;
         if (isPubCons(cc)) {
            result[i++] = cc;
         }
      }

      return result;
   }

   private static boolean isPubCons(CtConstructor cons) {
      return !Modifier.isPrivate(cons.getModifiers()) && cons.isConstructor();
   }

   public CtConstructor getConstructor(String desc) throws NotFoundException {
      CtMember.Cache memCache = this.getMembers();
      CtMember cons = memCache.consHead();
      CtMember consTail = memCache.lastCons();

      CtConstructor cc;
      do {
         if (cons == consTail) {
            return super.getConstructor(desc);
         }

         cons = cons.next();
         cc = (CtConstructor)cons;
      } while(!cc.getMethodInfo2().getDescriptor().equals(desc) || !cc.isConstructor());

      return cc;
   }

   public CtConstructor[] getDeclaredConstructors() {
      CtMember.Cache memCache = this.getMembers();
      CtMember cons = memCache.consHead();
      CtMember consTail = memCache.lastCons();
      int n = 0;
      CtMember mem = cons;

      while(mem != consTail) {
         mem = mem.next();
         CtConstructor cc = (CtConstructor)mem;
         if (cc.isConstructor()) {
            ++n;
         }
      }

      CtConstructor[] result = new CtConstructor[n];
      int i = 0;
      mem = cons;

      while(mem != consTail) {
         mem = mem.next();
         CtConstructor cc = (CtConstructor)mem;
         if (cc.isConstructor()) {
            result[i++] = cc;
         }
      }

      return result;
   }

   public CtConstructor getClassInitializer() {
      CtMember.Cache memCache = this.getMembers();
      CtMember cons = memCache.consHead();
      CtMember consTail = memCache.lastCons();

      CtConstructor cc;
      do {
         if (cons == consTail) {
            return null;
         }

         cons = cons.next();
         cc = (CtConstructor)cons;
      } while(!cc.isClassInitializer());

      return cc;
   }

   public CtMethod[] getMethods() {
      HashMap h = new HashMap();
      getMethods0(h, this);
      return (CtMethod[])((CtMethod[])h.values().toArray(new CtMethod[h.size()]));
   }

   private static void getMethods0(HashMap h, CtClass cc) {
      try {
         CtClass[] ifs = cc.getInterfaces();
         int size = ifs.length;

         for(int i = 0; i < size; ++i) {
            getMethods0(h, ifs[i]);
         }
      } catch (NotFoundException var6) {
      }

      try {
         CtClass s = cc.getSuperclass();
         if (s != null) {
            getMethods0(h, s);
         }
      } catch (NotFoundException var5) {
      }

      if (cc instanceof CtClassType) {
         CtMember.Cache memCache = ((CtClassType)cc).getMembers();
         CtMember mth = memCache.methodHead();
         CtMember mthTail = memCache.lastMethod();

         while(mth != mthTail) {
            mth = mth.next();
            if (!Modifier.isPrivate(mth.getModifiers())) {
               h.put(((CtMethod)mth).getStringRep(), mth);
            }
         }
      }

   }

   public CtMethod getMethod(String name, String desc) throws NotFoundException {
      CtMethod m = getMethod0(this, name, desc);
      if (m != null) {
         return m;
      } else {
         throw new NotFoundException(name + "(..) is not found in " + this.getName());
      }
   }

   private static CtMethod getMethod0(CtClass cc, String name, String desc) {
      if (cc instanceof CtClassType) {
         CtMember.Cache memCache = ((CtClassType)cc).getMembers();
         CtMember mth = memCache.methodHead();
         CtMember mthTail = memCache.lastMethod();

         while(mth != mthTail) {
            mth = mth.next();
            if (mth.getName().equals(name) && ((CtMethod)mth).getMethodInfo2().getDescriptor().equals(desc)) {
               return (CtMethod)mth;
            }
         }
      }

      try {
         CtClass s = cc.getSuperclass();
         if (s != null) {
            CtMethod m = getMethod0(s, name, desc);
            if (m != null) {
               return m;
            }
         }
      } catch (NotFoundException var7) {
      }

      try {
         CtClass[] ifs = cc.getInterfaces();
         int size = ifs.length;

         for(int i = 0; i < size; ++i) {
            CtMethod m = getMethod0(ifs[i], name, desc);
            if (m != null) {
               return m;
            }
         }
      } catch (NotFoundException var8) {
      }

      return null;
   }

   public CtMethod[] getDeclaredMethods() {
      CtMember.Cache memCache = this.getMembers();
      CtMember mth = memCache.methodHead();
      CtMember mthTail = memCache.lastMethod();
      int num = CtMember.Cache.count(mth, mthTail);
      CtMethod[] cms = new CtMethod[num];

      for(int var6 = 0; mth != mthTail; cms[var6++] = (CtMethod)mth) {
         mth = mth.next();
      }

      return cms;
   }

   public CtMethod[] getDeclaredMethods(String name) throws NotFoundException {
      CtMember.Cache memCache = this.getMembers();
      CtMember mth = memCache.methodHead();
      CtMember mthTail = memCache.lastMethod();
      ArrayList methods = new ArrayList();

      while(mth != mthTail) {
         mth = mth.next();
         if (mth.getName().equals(name)) {
            methods.add((CtMethod)mth);
         }
      }

      return (CtMethod[])methods.toArray(new CtMethod[methods.size()]);
   }

   public CtMethod getDeclaredMethod(String name) throws NotFoundException {
      CtMember.Cache memCache = this.getMembers();
      CtMember mth = memCache.methodHead();
      CtMember mthTail = memCache.lastMethod();

      do {
         if (mth == mthTail) {
            throw new NotFoundException(name + "(..) is not found in " + this.getName());
         }

         mth = mth.next();
      } while(!mth.getName().equals(name));

      return (CtMethod)mth;
   }

   public CtMethod getDeclaredMethod(String name, CtClass[] params) throws NotFoundException {
      String desc = Descriptor.ofParameters(params);
      CtMember.Cache memCache = this.getMembers();
      CtMember mth = memCache.methodHead();
      CtMember mthTail = memCache.lastMethod();

      do {
         if (mth == mthTail) {
            throw new NotFoundException(name + "(..) is not found in " + this.getName());
         }

         mth = mth.next();
      } while(!mth.getName().equals(name) || !((CtMethod)mth).getMethodInfo2().getDescriptor().startsWith(desc));

      return (CtMethod)mth;
   }

   public void addField(CtField f, String init) throws CannotCompileException {
      this.addField(f, CtField.Initializer.byExpr(init));
   }

   public void addField(CtField f, CtField.Initializer init) throws CannotCompileException {
      this.checkModify();
      if (f.getDeclaringClass() != this) {
         throw new CannotCompileException("cannot add");
      } else {
         if (init == null) {
            init = f.getInit();
         }

         if (init != null) {
            init.check(f.getSignature());
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
               try {
                  ConstPool cp = this.getClassFile2().getConstPool();
                  int index = init.getConstantValue(cp, f.getType());
                  if (index != 0) {
                     f.getFieldInfo2().addAttribute(new ConstantAttribute(cp, index));
                     init = null;
                  }
               } catch (NotFoundException var6) {
               }
            }
         }

         this.getMembers().addField(f);
         this.getClassFile2().addField(f.getFieldInfo2());
         if (init != null) {
            FieldInitLink fil = new FieldInitLink(f, init);
            FieldInitLink link = this.fieldInitializers;
            if (link == null) {
               this.fieldInitializers = fil;
            } else {
               while(link.next != null) {
                  link = link.next;
               }

               link.next = fil;
            }
         }

      }
   }

   public void removeField(CtField f) throws NotFoundException {
      this.checkModify();
      FieldInfo fi = f.getFieldInfo2();
      ClassFile cf = this.getClassFile2();
      if (cf.getFields().remove(fi)) {
         this.getMembers().remove(f);
         this.gcConstPool = true;
      } else {
         throw new NotFoundException(f.toString());
      }
   }

   public CtConstructor makeClassInitializer() throws CannotCompileException {
      CtConstructor clinit = this.getClassInitializer();
      if (clinit != null) {
         return clinit;
      } else {
         this.checkModify();
         ClassFile cf = this.getClassFile2();
         Bytecode code = new Bytecode(cf.getConstPool(), 0, 0);
         this.modifyClassConstructor(cf, code, 0, 0);
         return this.getClassInitializer();
      }
   }

   public void addConstructor(CtConstructor c) throws CannotCompileException {
      this.checkModify();
      if (c.getDeclaringClass() != this) {
         throw new CannotCompileException("cannot add");
      } else {
         this.getMembers().addConstructor(c);
         this.getClassFile2().addMethod(c.getMethodInfo2());
      }
   }

   public void removeConstructor(CtConstructor m) throws NotFoundException {
      this.checkModify();
      MethodInfo mi = m.getMethodInfo2();
      ClassFile cf = this.getClassFile2();
      if (cf.getMethods().remove(mi)) {
         this.getMembers().remove(m);
         this.gcConstPool = true;
      } else {
         throw new NotFoundException(m.toString());
      }
   }

   public void addMethod(CtMethod m) throws CannotCompileException {
      this.checkModify();
      if (m.getDeclaringClass() != this) {
         throw new CannotCompileException("bad declaring class");
      } else {
         int mod = m.getModifiers();
         if ((this.getModifiers() & 512) != 0) {
            m.setModifiers(mod | 1);
            if ((mod & 1024) == 0) {
               throw new CannotCompileException("an interface method must be abstract: " + m.toString());
            }
         }

         this.getMembers().addMethod(m);
         this.getClassFile2().addMethod(m.getMethodInfo2());
         if ((mod & 1024) != 0) {
            this.setModifiers(this.getModifiers() | 1024);
         }

      }
   }

   public void removeMethod(CtMethod m) throws NotFoundException {
      this.checkModify();
      MethodInfo mi = m.getMethodInfo2();
      ClassFile cf = this.getClassFile2();
      if (cf.getMethods().remove(mi)) {
         this.getMembers().remove(m);
         this.gcConstPool = true;
      } else {
         throw new NotFoundException(m.toString());
      }
   }

   public byte[] getAttribute(String name) {
      AttributeInfo ai = this.getClassFile2().getAttribute(name);
      return ai == null ? null : ai.get();
   }

   public void setAttribute(String name, byte[] data) {
      this.checkModify();
      ClassFile cf = this.getClassFile2();
      cf.addAttribute(new AttributeInfo(cf.getConstPool(), name, data));
   }

   public void instrument(CodeConverter converter) throws CannotCompileException {
      this.checkModify();
      ClassFile cf = this.getClassFile2();
      ConstPool cp = cf.getConstPool();
      List list = cf.getMethods();
      int n = list.size();

      for(int i = 0; i < n; ++i) {
         MethodInfo minfo = (MethodInfo)list.get(i);
         converter.doit(this, minfo, cp);
      }

   }

   public void instrument(ExprEditor editor) throws CannotCompileException {
      this.checkModify();
      ClassFile cf = this.getClassFile2();
      List list = cf.getMethods();
      int n = list.size();

      for(int i = 0; i < n; ++i) {
         MethodInfo minfo = (MethodInfo)list.get(i);
         editor.doit(this, minfo);
      }

   }

   public void prune() {
      if (!this.wasPruned) {
         this.wasPruned = this.wasFrozen = true;
         this.getClassFile2().prune();
      }
   }

   public void rebuildClassFile() {
      this.gcConstPool = true;
   }

   public void toBytecode(DataOutputStream out) throws CannotCompileException, IOException {
      try {
         if (this.isModified()) {
            this.checkPruned("toBytecode");
            ClassFile cf = this.getClassFile2();
            if (this.gcConstPool) {
               cf.compact();
               this.gcConstPool = false;
            }

            this.modifyClassConstructor(cf);
            this.modifyConstructors(cf);
            if (debugDump != null) {
               this.dumpClassFile(cf);
            }

            cf.write(out);
            out.flush();
            this.fieldInitializers = null;
            if (this.doPruning) {
               cf.prune();
               this.wasPruned = true;
            }
         } else {
            this.classPool.writeClassfile(this.getName(), out);
         }

         this.getCount = 0;
         this.wasFrozen = true;
      } catch (NotFoundException var3) {
         throw new CannotCompileException(var3);
      } catch (IOException var4) {
         throw new CannotCompileException(var4);
      }
   }

   private void dumpClassFile(ClassFile cf) throws IOException {
      DataOutputStream dump = this.makeFileOutput(debugDump);

      try {
         cf.write(dump);
      } finally {
         dump.close();
      }

   }

   private void checkPruned(String method) {
      if (this.wasPruned) {
         throw new RuntimeException(method + "(): " + this.getName() + " was pruned.");
      }
   }

   public boolean stopPruning(boolean stop) {
      boolean prev = !this.doPruning;
      this.doPruning = !stop;
      return prev;
   }

   private void modifyClassConstructor(ClassFile cf) throws CannotCompileException, NotFoundException {
      if (this.fieldInitializers != null) {
         Bytecode code = new Bytecode(cf.getConstPool(), 0, 0);
         Javac jv = new Javac(code, this);
         int stacksize = 0;
         boolean doInit = false;

         for(FieldInitLink fi = this.fieldInitializers; fi != null; fi = fi.next) {
            CtField f = fi.field;
            if (Modifier.isStatic(f.getModifiers())) {
               doInit = true;
               int s = fi.init.compileIfStatic(f.getType(), f.getName(), code, jv);
               if (stacksize < s) {
                  stacksize = s;
               }
            }
         }

         if (doInit) {
            this.modifyClassConstructor(cf, code, stacksize, 0);
         }

      }
   }

   private void modifyClassConstructor(ClassFile cf, Bytecode code, int stacksize, int localsize) throws CannotCompileException {
      MethodInfo m = cf.getStaticInitializer();
      if (m == null) {
         code.add(177);
         code.setMaxStack(stacksize);
         code.setMaxLocals(localsize);
         m = new MethodInfo(cf.getConstPool(), "<clinit>", "()V");
         m.setAccessFlags(8);
         m.setCodeAttribute(code.toCodeAttribute());
         cf.addMethod(m);
         CtMember.Cache cache = this.hasMemberCache();
         if (cache != null) {
            cache.addConstructor(new CtConstructor(m, this));
         }
      } else {
         CodeAttribute codeAttr = m.getCodeAttribute();
         if (codeAttr == null) {
            throw new CannotCompileException("empty <clinit>");
         }

         try {
            CodeIterator it = codeAttr.iterator();
            int pos = it.insertEx(code.get());
            it.insert(code.getExceptionTable(), pos);
            int maxstack = codeAttr.getMaxStack();
            if (maxstack < stacksize) {
               codeAttr.setMaxStack(stacksize);
            }

            int maxlocals = codeAttr.getMaxLocals();
            if (maxlocals < localsize) {
               codeAttr.setMaxLocals(localsize);
            }
         } catch (BadBytecode var12) {
            throw new CannotCompileException(var12);
         }
      }

      try {
         m.rebuildStackMapIf6(this.classPool, cf);
      } catch (BadBytecode var11) {
         throw new CannotCompileException(var11);
      }
   }

   private void modifyConstructors(ClassFile cf) throws CannotCompileException, NotFoundException {
      if (this.fieldInitializers != null) {
         ConstPool cp = cf.getConstPool();
         List list = cf.getMethods();
         int n = list.size();

         for(int i = 0; i < n; ++i) {
            MethodInfo minfo = (MethodInfo)list.get(i);
            if (minfo.isConstructor()) {
               CodeAttribute codeAttr = minfo.getCodeAttribute();
               if (codeAttr != null) {
                  try {
                     Bytecode init = new Bytecode(cp, 0, codeAttr.getMaxLocals());
                     CtClass[] params = Descriptor.getParameterTypes(minfo.getDescriptor(), this.classPool);
                     int stacksize = this.makeFieldInitializer(init, params);
                     insertAuxInitializer(codeAttr, init, stacksize);
                     minfo.rebuildStackMapIf6(this.classPool, cf);
                  } catch (BadBytecode var11) {
                     throw new CannotCompileException(var11);
                  }
               }
            }
         }

      }
   }

   private static void insertAuxInitializer(CodeAttribute codeAttr, Bytecode initializer, int stacksize) throws BadBytecode {
      CodeIterator it = codeAttr.iterator();
      int index = it.skipSuperConstructor();
      if (index < 0) {
         index = it.skipThisConstructor();
         if (index >= 0) {
            return;
         }
      }

      int pos = it.insertEx(initializer.get());
      it.insert(initializer.getExceptionTable(), pos);
      int maxstack = codeAttr.getMaxStack();
      if (maxstack < stacksize) {
         codeAttr.setMaxStack(stacksize);
      }

   }

   private int makeFieldInitializer(Bytecode code, CtClass[] parameters) throws CannotCompileException, NotFoundException {
      int stacksize = 0;
      Javac jv = new Javac(code, this);

      try {
         jv.recordParams(parameters, false);
      } catch (CompileError var8) {
         throw new CannotCompileException(var8);
      }

      for(FieldInitLink fi = this.fieldInitializers; fi != null; fi = fi.next) {
         CtField f = fi.field;
         if (!Modifier.isStatic(f.getModifiers())) {
            int s = fi.init.compile(f.getType(), f.getName(), code, parameters, jv);
            if (stacksize < s) {
               stacksize = s;
            }
         }
      }

      return stacksize;
   }

   Hashtable getHiddenMethods() {
      if (this.hiddenMethods == null) {
         this.hiddenMethods = new Hashtable();
      }

      return this.hiddenMethods;
   }

   int getUniqueNumber() {
      return this.uniqueNumberSeed++;
   }

   public String makeUniqueName(String prefix) {
      HashMap table = new HashMap();
      this.makeMemberList(table);
      Set keys = table.keySet();
      String[] methods = new String[keys.size()];
      keys.toArray(methods);
      if (notFindInArray(prefix, methods)) {
         return prefix;
      } else {
         int i = 100;

         while(i <= 999) {
            String name = prefix + i++;
            if (notFindInArray(name, methods)) {
               return name;
            }
         }

         throw new RuntimeException("too many unique name");
      }
   }

   private static boolean notFindInArray(String prefix, String[] values) {
      int len = values.length;

      for(int i = 0; i < len; ++i) {
         if (values[i].startsWith(prefix)) {
            return false;
         }
      }

      return true;
   }

   private void makeMemberList(HashMap table) {
      int mod = this.getModifiers();
      int n;
      int i;
      if (Modifier.isAbstract(mod) || Modifier.isInterface(mod)) {
         try {
            CtClass[] ifs = this.getInterfaces();
            n = ifs.length;

            for(i = 0; i < n; ++i) {
               CtClass ic = ifs[i];
               if (ic != null && ic instanceof CtClassType) {
                  ((CtClassType)ic).makeMemberList(table);
               }
            }
         } catch (NotFoundException var8) {
         }
      }

      try {
         CtClass s = this.getSuperclass();
         if (s != null && s instanceof CtClassType) {
            ((CtClassType)s).makeMemberList(table);
         }
      } catch (NotFoundException var7) {
      }

      List list = this.getClassFile2().getMethods();
      n = list.size();

      for(i = 0; i < n; ++i) {
         MethodInfo minfo = (MethodInfo)list.get(i);
         table.put(minfo.getName(), this);
      }

      list = this.getClassFile2().getFields();
      n = list.size();

      for(i = 0; i < n; ++i) {
         FieldInfo finfo = (FieldInfo)list.get(i);
         table.put(finfo.getName(), this);
      }

   }
}
