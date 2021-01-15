package javassist.util.proxy;

import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

class SerializedProxy implements Serializable {
   private String superClass;
   private String[] interfaces;
   private byte[] filterSignature;
   private MethodHandler handler;

   SerializedProxy(Class proxy, byte[] sig, MethodHandler h) {
      this.filterSignature = sig;
      this.handler = h;
      this.superClass = proxy.getSuperclass().getName();
      Class[] infs = proxy.getInterfaces();
      int n = infs.length;
      this.interfaces = new String[n - 1];
      String setterInf = ProxyObject.class.getName();
      String setterInf2 = Proxy.class.getName();

      for(int i = 0; i < n; ++i) {
         String name = infs[i].getName();
         if (!name.equals(setterInf) && !name.equals(setterInf2)) {
            this.interfaces[i] = name;
         }
      }

   }

   protected Class loadClass(final String className) throws ClassNotFoundException {
      try {
         return (Class)AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               ClassLoader cl = Thread.currentThread().getContextClassLoader();
               return Class.forName(className, true, cl);
            }
         });
      } catch (PrivilegedActionException var3) {
         throw new RuntimeException("cannot load the class: " + className, var3.getException());
      }
   }

   Object readResolve() throws ObjectStreamException {
      try {
         int n = this.interfaces.length;
         Class[] infs = new Class[n];

         for(int i = 0; i < n; ++i) {
            infs[i] = this.loadClass(this.interfaces[i]);
         }

         ProxyFactory f = new ProxyFactory();
         f.setSuperclass(this.loadClass(this.superClass));
         f.setInterfaces(infs);
         Proxy proxy = (Proxy)f.createClass(this.filterSignature).newInstance();
         proxy.setHandler(this.handler);
         return proxy;
      } catch (ClassNotFoundException var5) {
         throw new InvalidClassException(var5.getMessage());
      } catch (InstantiationException var6) {
         throw new InvalidObjectException(var6.getMessage());
      } catch (IllegalAccessException var7) {
         throw new InvalidClassException(var7.getMessage());
      }
   }
}
