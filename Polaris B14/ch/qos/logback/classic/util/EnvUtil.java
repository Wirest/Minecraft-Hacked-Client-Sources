/*    */ package ch.qos.logback.classic.util;
/*    */ 
/*    */ import ch.qos.logback.core.util.Loader;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EnvUtil
/*    */ {
/* 33 */   static ClassLoader testServiceLoaderClassLoader = null;
/*    */   private static final Method serviceLoaderLoadMethod;
/*    */   
/* 36 */   public static boolean isGroovyAvailable() { ClassLoader classLoader = Loader.getClassLoaderOfClass(EnvUtil.class);
/*    */     try {
/* 38 */       Class<?> bindingClass = classLoader.loadClass("groovy.lang.Binding");
/* 39 */       return bindingClass != null;
/*    */     } catch (ClassNotFoundException e) {}
/* 41 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static final Method serviceLoaderIteratorMethod;
/*    */   
/*    */ 
/*    */ 
/*    */   static
/*    */   {
/* 53 */     Method tLoadMethod = null;
/* 54 */     Method tIteratorMethod = null;
/*    */     try {
/* 56 */       Class<?> clazz = Class.forName("java.util.ServiceLoader");
/* 57 */       tLoadMethod = clazz.getMethod("load", new Class[] { Class.class, ClassLoader.class });
/* 58 */       tIteratorMethod = clazz.getMethod("iterator", new Class[0]);
/*    */     }
/*    */     catch (ClassNotFoundException ce) {}catch (NoSuchMethodException ne) {}
/*    */     
/*    */ 
/*    */ 
/* 64 */     serviceLoaderLoadMethod = tLoadMethod;
/* 65 */     serviceLoaderIteratorMethod = tIteratorMethod;
/*    */   }
/*    */   
/*    */   public static boolean isServiceLoaderAvailable() {
/* 69 */     return (serviceLoaderLoadMethod != null) && (serviceLoaderIteratorMethod != null);
/*    */   }
/*    */   
/*    */   private static ClassLoader getServiceLoaderClassLoader() {
/* 73 */     return testServiceLoaderClassLoader == null ? Loader.getClassLoaderOfClass(EnvUtil.class) : testServiceLoaderClassLoader;
/*    */   }
/*    */   
/*    */   public static <T> T loadFromServiceLoader(Class<T> c)
/*    */   {
/* 78 */     if (isServiceLoaderAvailable()) {
/*    */       Object loader;
/*    */       try {
/* 81 */         loader = serviceLoaderLoadMethod.invoke(null, new Object[] { c, getServiceLoaderClassLoader() });
/*    */       } catch (Exception e) {
/* 83 */         throw new IllegalStateException("Cannot invoke java.util.ServiceLoader#load()", e);
/*    */       }
/*    */       Iterator<T> it;
/*    */       try
/*    */       {
/* 88 */         it = (Iterator)serviceLoaderIteratorMethod.invoke(loader, new Object[0]);
/*    */       } catch (Exception e) {
/* 90 */         throw new IllegalStateException("Cannot invoke java.util.ServiceLoader#iterator()", e);
/*    */       }
/* 92 */       if (it.hasNext())
/* 93 */         return (T)it.next();
/*    */     }
/* 95 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\util\EnvUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */