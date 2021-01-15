package javassist.util.proxy;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

class SecurityActions {
   static Method[] getDeclaredMethods(final Class clazz) {
      return System.getSecurityManager() == null ? clazz.getDeclaredMethods() : (Method[])((Method[])AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return clazz.getDeclaredMethods();
         }
      }));
   }

   static Constructor[] getDeclaredConstructors(final Class clazz) {
      return System.getSecurityManager() == null ? clazz.getDeclaredConstructors() : (Constructor[])((Constructor[])AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            return clazz.getDeclaredConstructors();
         }
      }));
   }

   static Method getDeclaredMethod(final Class clazz, final String name, final Class[] types) throws NoSuchMethodException {
      if (System.getSecurityManager() == null) {
         return clazz.getDeclaredMethod(name, types);
      } else {
         try {
            return (Method)AccessController.doPrivileged(new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  return clazz.getDeclaredMethod(name, types);
               }
            });
         } catch (PrivilegedActionException var4) {
            if (var4.getCause() instanceof NoSuchMethodException) {
               throw (NoSuchMethodException)var4.getCause();
            } else {
               throw new RuntimeException(var4.getCause());
            }
         }
      }
   }

   static Constructor getDeclaredConstructor(final Class clazz, final Class[] types) throws NoSuchMethodException {
      if (System.getSecurityManager() == null) {
         return clazz.getDeclaredConstructor(types);
      } else {
         try {
            return (Constructor)AccessController.doPrivileged(new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  return clazz.getDeclaredConstructor(types);
               }
            });
         } catch (PrivilegedActionException var3) {
            if (var3.getCause() instanceof NoSuchMethodException) {
               throw (NoSuchMethodException)var3.getCause();
            } else {
               throw new RuntimeException(var3.getCause());
            }
         }
      }
   }

   static void setAccessible(final AccessibleObject ao, final boolean accessible) {
      if (System.getSecurityManager() == null) {
         ao.setAccessible(accessible);
      } else {
         AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               ao.setAccessible(accessible);
               return null;
            }
         });
      }

   }

   static void set(final Field fld, final Object target, final Object value) throws IllegalAccessException {
      if (System.getSecurityManager() == null) {
         fld.set(target, value);
      } else {
         try {
            AccessController.doPrivileged(new PrivilegedExceptionAction() {
               public Object run() throws Exception {
                  fld.set(target, value);
                  return null;
               }
            });
         } catch (PrivilegedActionException var4) {
            if (var4.getCause() instanceof NoSuchMethodException) {
               throw (IllegalAccessException)var4.getCause();
            }

            throw new RuntimeException(var4.getCause());
         }
      }

   }
}
