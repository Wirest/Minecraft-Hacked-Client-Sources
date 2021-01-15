package javassist;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Hashtable;
import java.util.Vector;

public class Loader extends ClassLoader {
   private Hashtable notDefinedHere;
   private Vector notDefinedPackages;
   private ClassPool source;
   private Translator translator;
   private ProtectionDomain domain;
   public boolean doDelegation;

   public Loader() {
      this((ClassPool)null);
   }

   public Loader(ClassPool cp) {
      this.doDelegation = true;
      this.init(cp);
   }

   public Loader(ClassLoader parent, ClassPool cp) {
      super(parent);
      this.doDelegation = true;
      this.init(cp);
   }

   private void init(ClassPool cp) {
      this.notDefinedHere = new Hashtable();
      this.notDefinedPackages = new Vector();
      this.source = cp;
      this.translator = null;
      this.domain = null;
      this.delegateLoadingOf("javassist.Loader");
   }

   public void delegateLoadingOf(String classname) {
      if (classname.endsWith(".")) {
         this.notDefinedPackages.addElement(classname);
      } else {
         this.notDefinedHere.put(classname, this);
      }

   }

   public void setDomain(ProtectionDomain d) {
      this.domain = d;
   }

   public void setClassPool(ClassPool cp) {
      this.source = cp;
   }

   public void addTranslator(ClassPool cp, Translator t) throws NotFoundException, CannotCompileException {
      this.source = cp;
      this.translator = t;
      t.start(cp);
   }

   public static void main(String[] args) throws Throwable {
      Loader cl = new Loader();
      cl.run(args);
   }

   public void run(String[] args) throws Throwable {
      int n = args.length - 1;
      if (n >= 0) {
         String[] args2 = new String[n];

         for(int i = 0; i < n; ++i) {
            args2[i] = args[i + 1];
         }

         this.run(args[0], args2);
      }

   }

   public void run(String classname, String[] args) throws Throwable {
      Class c = this.loadClass(classname);

      try {
         c.getDeclaredMethod("main", String[].class).invoke((Object)null, args);
      } catch (InvocationTargetException var5) {
         throw var5.getTargetException();
      }
   }

   protected Class loadClass(String name, boolean resolve) throws ClassFormatError, ClassNotFoundException {
      name = name.intern();
      synchronized(name) {
         Class c = this.findLoadedClass(name);
         if (c == null) {
            c = this.loadClassByDelegation(name);
         }

         if (c == null) {
            c = this.findClass(name);
         }

         if (c == null) {
            c = this.delegateToParent(name);
         }

         if (resolve) {
            this.resolveClass(c);
         }

         return c;
      }
   }

   protected Class findClass(String name) throws ClassNotFoundException {
      byte[] classfile;
      try {
         if (this.source != null) {
            if (this.translator != null) {
               this.translator.onLoad(this.source, name);
            }

            try {
               classfile = this.source.get(name).toBytecode();
            } catch (NotFoundException var7) {
               return null;
            }
         } else {
            String jarname = "/" + name.replace('.', '/') + ".class";
            InputStream in = this.getClass().getResourceAsStream(jarname);
            if (in == null) {
               return null;
            }

            classfile = ClassPoolTail.readStream(in);
         }
      } catch (Exception var8) {
         throw new ClassNotFoundException("caught an exception while obtaining a class file for " + name, var8);
      }

      int i = name.lastIndexOf(46);
      if (i != -1) {
         String pname = name.substring(0, i);
         if (this.getPackage(pname) == null) {
            try {
               this.definePackage(pname, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (URL)null);
            } catch (IllegalArgumentException var6) {
            }
         }
      }

      return this.domain == null ? this.defineClass(name, classfile, 0, classfile.length) : this.defineClass(name, classfile, 0, classfile.length, this.domain);
   }

   protected Class loadClassByDelegation(String name) throws ClassNotFoundException {
      Class c = null;
      if (this.doDelegation && (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("sun.") || name.startsWith("com.sun.") || name.startsWith("org.w3c.") || name.startsWith("org.xml.") || this.notDelegated(name))) {
         c = this.delegateToParent(name);
      }

      return c;
   }

   private boolean notDelegated(String name) {
      if (this.notDefinedHere.get(name) != null) {
         return true;
      } else {
         int n = this.notDefinedPackages.size();

         for(int i = 0; i < n; ++i) {
            if (name.startsWith((String)this.notDefinedPackages.elementAt(i))) {
               return true;
            }
         }

         return false;
      }
   }

   protected Class delegateToParent(String classname) throws ClassNotFoundException {
      ClassLoader cl = this.getParent();
      return cl != null ? cl.loadClass(classname) : this.findSystemClass(classname);
   }
}
