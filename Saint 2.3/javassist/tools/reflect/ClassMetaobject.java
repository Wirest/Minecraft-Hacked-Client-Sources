package javassist.tools.reflect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassMetaobject implements Serializable {
   static final String methodPrefix = "_m_";
   static final int methodPrefixLen = 3;
   private Class javaClass;
   private Constructor[] constructors;
   private Method[] methods;
   public static boolean useContextClassLoader = false;

   public ClassMetaobject(String[] params) {
      try {
         this.javaClass = this.getClassObject(params[0]);
      } catch (ClassNotFoundException var3) {
         throw new RuntimeException("not found: " + params[0] + ", useContextClassLoader: " + Boolean.toString(useContextClassLoader), var3);
      }

      this.constructors = this.javaClass.getConstructors();
      this.methods = null;
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.writeUTF(this.javaClass.getName());
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      this.javaClass = this.getClassObject(in.readUTF());
      this.constructors = this.javaClass.getConstructors();
      this.methods = null;
   }

   private Class getClassObject(String name) throws ClassNotFoundException {
      return useContextClassLoader ? Thread.currentThread().getContextClassLoader().loadClass(name) : Class.forName(name);
   }

   public final Class getJavaClass() {
      return this.javaClass;
   }

   public final String getName() {
      return this.javaClass.getName();
   }

   public final boolean isInstance(Object obj) {
      return this.javaClass.isInstance(obj);
   }

   public final Object newInstance(Object[] args) throws CannotCreateException {
      int n = this.constructors.length;
      int i = 0;

      while(i < n) {
         try {
            return this.constructors[i].newInstance(args);
         } catch (IllegalArgumentException var5) {
            ++i;
         } catch (InstantiationException var6) {
            throw new CannotCreateException(var6);
         } catch (IllegalAccessException var7) {
            throw new CannotCreateException(var7);
         } catch (InvocationTargetException var8) {
            throw new CannotCreateException(var8);
         }
      }

      throw new CannotCreateException("no constructor matches");
   }

   public Object trapFieldRead(String name) {
      Class jc = this.getJavaClass();

      try {
         return jc.getField(name).get((Object)null);
      } catch (NoSuchFieldException var4) {
         throw new RuntimeException(var4.toString());
      } catch (IllegalAccessException var5) {
         throw new RuntimeException(var5.toString());
      }
   }

   public void trapFieldWrite(String name, Object value) {
      Class jc = this.getJavaClass();

      try {
         jc.getField(name).set((Object)null, value);
      } catch (NoSuchFieldException var5) {
         throw new RuntimeException(var5.toString());
      } catch (IllegalAccessException var6) {
         throw new RuntimeException(var6.toString());
      }
   }

   public static Object invoke(Object target, int identifier, Object[] args) throws Throwable {
      Method[] allmethods = target.getClass().getMethods();
      int n = allmethods.length;
      String head = "_m_" + identifier;

      for(int i = 0; i < n; ++i) {
         if (allmethods[i].getName().startsWith(head)) {
            try {
               return allmethods[i].invoke(target, args);
            } catch (InvocationTargetException var8) {
               throw var8.getTargetException();
            } catch (IllegalAccessException var9) {
               throw new CannotInvokeException(var9);
            }
         }
      }

      throw new CannotInvokeException("cannot find a method");
   }

   public Object trapMethodcall(int identifier, Object[] args) throws Throwable {
      try {
         Method[] m = this.getReflectiveMethods();
         return m[identifier].invoke((Object)null, args);
      } catch (InvocationTargetException var4) {
         throw var4.getTargetException();
      } catch (IllegalAccessException var5) {
         throw new CannotInvokeException(var5);
      }
   }

   public final Method[] getReflectiveMethods() {
      if (this.methods != null) {
         return this.methods;
      } else {
         Class baseclass = this.getJavaClass();
         Method[] allmethods = baseclass.getDeclaredMethods();
         int n = allmethods.length;
         int[] index = new int[n];
         int max = 0;

         int i;
         for(i = 0; i < n; ++i) {
            Method m = allmethods[i];
            String mname = m.getName();
            if (mname.startsWith("_m_")) {
               int k = 0;
               int j = 3;

               while(true) {
                  char c = mname.charAt(j);
                  if ('0' > c || c > '9') {
                     ++k;
                     index[i] = k;
                     if (k > max) {
                        max = k;
                     }
                     break;
                  }

                  k = k * 10 + c - 48;
                  ++j;
               }
            }
         }

         this.methods = new Method[max];

         for(i = 0; i < n; ++i) {
            if (index[i] > 0) {
               this.methods[index[i] - 1] = allmethods[i];
            }
         }

         return this.methods;
      }
   }

   public final Method getMethod(int identifier) {
      return this.getReflectiveMethods()[identifier];
   }

   public final String getMethodName(int identifier) {
      String mname = this.getReflectiveMethods()[identifier].getName();
      int j = 3;

      char c;
      do {
         c = mname.charAt(j++);
      } while(c >= '0' && '9' >= c);

      return mname.substring(j);
   }

   public final Class[] getParameterTypes(int identifier) {
      return this.getReflectiveMethods()[identifier].getParameterTypes();
   }

   public final Class getReturnType(int identifier) {
      return this.getReflectiveMethods()[identifier].getReturnType();
   }

   public final int getMethodIndex(String originalName, Class[] argTypes) throws NoSuchMethodException {
      Method[] mthds = this.getReflectiveMethods();

      for(int i = 0; i < mthds.length; ++i) {
         if (mthds[i] != null && this.getMethodName(i).equals(originalName) && Arrays.equals(argTypes, mthds[i].getParameterTypes())) {
            return i;
         }
      }

      throw new NoSuchMethodException("Method " + originalName + " not found");
   }
}
