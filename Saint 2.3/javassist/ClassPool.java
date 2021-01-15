package javassist;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import javassist.bytecode.Descriptor;

public class ClassPool {
   private static Method defineClass1;
   private static Method defineClass2;
   private static Method definePackage;
   public boolean childFirstLookup;
   public static boolean doPruning;
   private int compressCount;
   private static final int COMPRESS_THRESHOLD = 100;
   public static boolean releaseUnmodifiedClassFile;
   protected ClassPoolTail source;
   protected ClassPool parent;
   protected Hashtable classes;
   private Hashtable cflow;
   private static final int INIT_HASH_SIZE = 191;
   private ArrayList importedPackages;
   private static ClassPool defaultPool;

   public ClassPool() {
      this((ClassPool)null);
   }

   public ClassPool(boolean useDefaultPath) {
      this((ClassPool)null);
      if (useDefaultPath) {
         this.appendSystemPath();
      }

   }

   public ClassPool(ClassPool parent) {
      this.childFirstLookup = false;
      this.cflow = null;
      this.classes = new Hashtable(191);
      this.source = new ClassPoolTail();
      this.parent = parent;
      if (parent == null) {
         CtClass[] pt = CtClass.primitiveTypes;

         for(int i = 0; i < pt.length; ++i) {
            this.classes.put(pt[i].getName(), pt[i]);
         }
      }

      this.cflow = null;
      this.compressCount = 0;
      this.clearImportedPackages();
   }

   public static synchronized ClassPool getDefault() {
      if (defaultPool == null) {
         defaultPool = new ClassPool((ClassPool)null);
         defaultPool.appendSystemPath();
      }

      return defaultPool;
   }

   protected CtClass getCached(String classname) {
      return (CtClass)this.classes.get(classname);
   }

   protected void cacheCtClass(String classname, CtClass c, boolean dynamic) {
      this.classes.put(classname, c);
   }

   protected CtClass removeCached(String classname) {
      return (CtClass)this.classes.remove(classname);
   }

   public String toString() {
      return this.source.toString();
   }

   void compress() {
      if (this.compressCount++ > 100) {
         this.compressCount = 0;
         Enumeration e = this.classes.elements();

         while(e.hasMoreElements()) {
            ((CtClass)e.nextElement()).compress();
         }
      }

   }

   public void importPackage(String packageName) {
      this.importedPackages.add(packageName);
   }

   public void clearImportedPackages() {
      this.importedPackages = new ArrayList();
      this.importedPackages.add("java.lang");
   }

   public Iterator getImportedPackages() {
      return this.importedPackages.iterator();
   }

   /** @deprecated */
   public void recordInvalidClassName(String name) {
   }

   void recordCflow(String name, String cname, String fname) {
      if (this.cflow == null) {
         this.cflow = new Hashtable();
      }

      this.cflow.put(name, new Object[]{cname, fname});
   }

   public Object[] lookupCflow(String name) {
      if (this.cflow == null) {
         this.cflow = new Hashtable();
      }

      return (Object[])((Object[])this.cflow.get(name));
   }

   public CtClass getAndRename(String orgName, String newName) throws NotFoundException {
      CtClass clazz = this.get0(orgName, false);
      if (clazz == null) {
         throw new NotFoundException(orgName);
      } else {
         if (clazz instanceof CtClassType) {
            ((CtClassType)clazz).setClassPool(this);
         }

         clazz.setName(newName);
         return clazz;
      }
   }

   synchronized void classNameChanged(String oldname, CtClass clazz) {
      CtClass c = this.getCached(oldname);
      if (c == clazz) {
         this.removeCached(oldname);
      }

      String newName = clazz.getName();
      this.checkNotFrozen(newName);
      this.cacheCtClass(newName, clazz, false);
   }

   public CtClass get(String classname) throws NotFoundException {
      CtClass clazz;
      if (classname == null) {
         clazz = null;
      } else {
         clazz = this.get0(classname, true);
      }

      if (clazz == null) {
         throw new NotFoundException(classname);
      } else {
         clazz.incGetCounter();
         return clazz;
      }
   }

   public CtClass getOrNull(String classname) {
      CtClass clazz = null;
      if (classname == null) {
         clazz = null;
      } else {
         try {
            clazz = this.get0(classname, true);
         } catch (NotFoundException var4) {
         }
      }

      if (clazz != null) {
         clazz.incGetCounter();
      }

      return clazz;
   }

   public CtClass getCtClass(String classname) throws NotFoundException {
      return classname.charAt(0) == '[' ? Descriptor.toCtClass(classname, this) : this.get(classname);
   }

   protected synchronized CtClass get0(String classname, boolean useCache) throws NotFoundException {
      CtClass clazz = null;
      if (useCache) {
         clazz = this.getCached(classname);
         if (clazz != null) {
            return clazz;
         }
      }

      if (!this.childFirstLookup && this.parent != null) {
         clazz = this.parent.get0(classname, useCache);
         if (clazz != null) {
            return clazz;
         }
      }

      clazz = this.createCtClass(classname, useCache);
      if (clazz != null) {
         if (useCache) {
            this.cacheCtClass(clazz.getName(), clazz, false);
         }

         return clazz;
      } else {
         if (this.childFirstLookup && this.parent != null) {
            clazz = this.parent.get0(classname, useCache);
         }

         return clazz;
      }
   }

   protected CtClass createCtClass(String classname, boolean useCache) {
      if (classname.charAt(0) == '[') {
         classname = Descriptor.toClassName(classname);
      }

      if (!classname.endsWith("[]")) {
         return this.find(classname) == null ? null : new CtClassType(classname, this);
      } else {
         String base = classname.substring(0, classname.indexOf(91));
         return (!useCache || this.getCached(base) == null) && this.find(base) == null ? null : new CtArray(classname, this);
      }
   }

   public URL find(String classname) {
      return this.source.find(classname);
   }

   void checkNotFrozen(String classname) throws RuntimeException {
      CtClass clazz = this.getCached(classname);
      if (clazz == null) {
         if (!this.childFirstLookup && this.parent != null) {
            try {
               clazz = this.parent.get0(classname, true);
            } catch (NotFoundException var4) {
            }

            if (clazz != null) {
               throw new RuntimeException(classname + " is in a parent ClassPool.  Use the parent.");
            }
         }
      } else if (clazz.isFrozen()) {
         throw new RuntimeException(classname + ": frozen class (cannot edit)");
      }

   }

   CtClass checkNotExists(String classname) {
      CtClass clazz = this.getCached(classname);
      if (clazz == null && !this.childFirstLookup && this.parent != null) {
         try {
            clazz = this.parent.get0(classname, true);
         } catch (NotFoundException var4) {
         }
      }

      return clazz;
   }

   InputStream openClassfile(String classname) throws NotFoundException {
      return this.source.openClassfile(classname);
   }

   void writeClassfile(String classname, OutputStream out) throws NotFoundException, IOException, CannotCompileException {
      this.source.writeClassfile(classname, out);
   }

   public CtClass[] get(String[] classnames) throws NotFoundException {
      if (classnames == null) {
         return new CtClass[0];
      } else {
         int num = classnames.length;
         CtClass[] result = new CtClass[num];

         for(int i = 0; i < num; ++i) {
            result[i] = this.get(classnames[i]);
         }

         return result;
      }
   }

   public CtMethod getMethod(String classname, String methodname) throws NotFoundException {
      CtClass c = this.get(classname);
      return c.getDeclaredMethod(methodname);
   }

   public CtClass makeClass(InputStream classfile) throws IOException, RuntimeException {
      return this.makeClass(classfile, true);
   }

   public CtClass makeClass(InputStream classfile, boolean ifNotFrozen) throws IOException, RuntimeException {
      this.compress();
      InputStream classfile = new BufferedInputStream(classfile);
      CtClass clazz = new CtClassType(classfile, this);
      clazz.checkModify();
      String classname = clazz.getName();
      if (ifNotFrozen) {
         this.checkNotFrozen(classname);
      }

      this.cacheCtClass(classname, clazz, true);
      return clazz;
   }

   public CtClass makeClassIfNew(InputStream classfile) throws IOException, RuntimeException {
      this.compress();
      InputStream classfile = new BufferedInputStream(classfile);
      CtClass clazz = new CtClassType(classfile, this);
      clazz.checkModify();
      String classname = clazz.getName();
      CtClass found = this.checkNotExists(classname);
      if (found != null) {
         return found;
      } else {
         this.cacheCtClass(classname, clazz, true);
         return clazz;
      }
   }

   public CtClass makeClass(String classname) throws RuntimeException {
      return this.makeClass(classname, (CtClass)null);
   }

   public synchronized CtClass makeClass(String classname, CtClass superclass) throws RuntimeException {
      this.checkNotFrozen(classname);
      CtClass clazz = new CtNewClass(classname, this, false, superclass);
      this.cacheCtClass(classname, clazz, true);
      return clazz;
   }

   synchronized CtClass makeNestedClass(String classname) {
      this.checkNotFrozen(classname);
      CtClass clazz = new CtNewNestedClass(classname, this, false, (CtClass)null);
      this.cacheCtClass(classname, clazz, true);
      return clazz;
   }

   public CtClass makeInterface(String name) throws RuntimeException {
      return this.makeInterface(name, (CtClass)null);
   }

   public synchronized CtClass makeInterface(String name, CtClass superclass) throws RuntimeException {
      this.checkNotFrozen(name);
      CtClass clazz = new CtNewClass(name, this, true, superclass);
      this.cacheCtClass(name, clazz, true);
      return clazz;
   }

   public CtClass makeAnnotation(String name) throws RuntimeException {
      try {
         CtClass cc = this.makeInterface(name, this.get("java.lang.annotation.Annotation"));
         cc.setModifiers(cc.getModifiers() | 8192);
         return cc;
      } catch (NotFoundException var3) {
         throw new RuntimeException(var3.getMessage(), var3);
      }
   }

   public ClassPath appendSystemPath() {
      return this.source.appendSystemPath();
   }

   public ClassPath insertClassPath(ClassPath cp) {
      return this.source.insertClassPath(cp);
   }

   public ClassPath appendClassPath(ClassPath cp) {
      return this.source.appendClassPath(cp);
   }

   public ClassPath insertClassPath(String pathname) throws NotFoundException {
      return this.source.insertClassPath(pathname);
   }

   public ClassPath appendClassPath(String pathname) throws NotFoundException {
      return this.source.appendClassPath(pathname);
   }

   public void removeClassPath(ClassPath cp) {
      this.source.removeClassPath(cp);
   }

   public void appendPathList(String pathlist) throws NotFoundException {
      char sep = File.pathSeparatorChar;
      int i = 0;

      while(true) {
         int j = pathlist.indexOf(sep, i);
         if (j < 0) {
            this.appendClassPath(pathlist.substring(i));
            return;
         }

         this.appendClassPath(pathlist.substring(i, j));
         i = j + 1;
      }
   }

   public Class toClass(CtClass clazz) throws CannotCompileException {
      return this.toClass(clazz, this.getClassLoader());
   }

   public ClassLoader getClassLoader() {
      return getContextClassLoader();
   }

   static ClassLoader getContextClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }

   /** @deprecated */
   public Class toClass(CtClass ct, ClassLoader loader) throws CannotCompileException {
      return this.toClass(ct, loader, (ProtectionDomain)null);
   }

   public Class toClass(CtClass ct, ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
      try {
         byte[] b = ct.toBytecode();
         Method method;
         Object[] args;
         if (domain == null) {
            method = defineClass1;
            args = new Object[]{ct.getName(), b, new Integer(0), new Integer(b.length)};
         } else {
            method = defineClass2;
            args = new Object[]{ct.getName(), b, new Integer(0), new Integer(b.length), domain};
         }

         return (Class)toClass2(method, loader, args);
      } catch (RuntimeException var7) {
         throw var7;
      } catch (InvocationTargetException var8) {
         throw new CannotCompileException(var8.getTargetException());
      } catch (Exception var9) {
         throw new CannotCompileException(var9);
      }
   }

   private static synchronized Object toClass2(Method method, ClassLoader loader, Object[] args) throws Exception {
      method.setAccessible(true);

      Object var3;
      try {
         var3 = method.invoke(loader, args);
      } finally {
         method.setAccessible(false);
      }

      return var3;
   }

   public void makePackage(ClassLoader loader, String name) throws CannotCompileException {
      Object[] args = new Object[]{name, null, null, null, null, null, null, null};

      Object t;
      try {
         toClass2(definePackage, loader, args);
         return;
      } catch (InvocationTargetException var6) {
         t = var6.getTargetException();
         if (t == null) {
            t = var6;
         } else if (t instanceof IllegalArgumentException) {
            return;
         }
      } catch (Exception var7) {
         t = var7;
      }

      throw new CannotCompileException((Throwable)t);
   }

   static {
      try {
         AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               Class cl = Class.forName("java.lang.ClassLoader");
               ClassPool.defineClass1 = cl.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE);
               ClassPool.defineClass2 = cl.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class);
               ClassPool.definePackage = cl.getDeclaredMethod("definePackage", String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class);
               return null;
            }
         });
      } catch (PrivilegedActionException var1) {
         throw new RuntimeException("cannot initialize ClassPool", var1.getException());
      }

      doPruning = false;
      releaseUnmodifiedClassFile = true;
      defaultPool = null;
   }
}
