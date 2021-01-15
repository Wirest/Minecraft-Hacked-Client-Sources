/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import io.netty.util.internal.logging.InternalLogger;
/*    */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*    */ import java.lang.reflect.Method;
/*    */ import javassist.ClassClassPath;
/*    */ import javassist.ClassPath;
/*    */ import javassist.ClassPool;
/*    */ import javassist.CtClass;
/*    */ import javassist.CtMethod;
/*    */ import javassist.NotFoundException;
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
/*    */ public final class JavassistTypeParameterMatcherGenerator
/*    */ {
/* 32 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(JavassistTypeParameterMatcherGenerator.class);
/*    */   
/*    */ 
/* 35 */   private static final ClassPool classPool = new ClassPool(true);
/*    */   
/*    */   static {
/* 38 */     classPool.appendClassPath(new ClassClassPath(NoOpTypeParameterMatcher.class));
/*    */   }
/*    */   
/*    */   public static void appendClassPath(ClassPath classpath) {
/* 42 */     classPool.appendClassPath(classpath);
/*    */   }
/*    */   
/*    */   public static void appendClassPath(String pathname) throws NotFoundException {
/* 46 */     classPool.appendClassPath(pathname);
/*    */   }
/*    */   
/*    */   public static ClassPool classPool() {
/* 50 */     return classPool;
/*    */   }
/*    */   
/*    */   public static TypeParameterMatcher generate(Class<?> type) {
/* 54 */     ClassLoader classLoader = PlatformDependent.getContextClassLoader();
/* 55 */     if (classLoader == null) {
/* 56 */       classLoader = PlatformDependent.getSystemClassLoader();
/*    */     }
/* 58 */     return generate(type, classLoader);
/*    */   }
/*    */   
/*    */   public static TypeParameterMatcher generate(Class<?> type, ClassLoader classLoader) {
/* 62 */     String typeName = typeName(type);
/* 63 */     String className = "io.netty.util.internal.__matchers__." + typeName + "Matcher";
/*    */     try
/*    */     {
/* 66 */       return (TypeParameterMatcher)Class.forName(className, true, classLoader).newInstance();
/*    */ 
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 71 */       CtClass c = classPool.getAndRename(NoOpTypeParameterMatcher.class.getName(), className);
/* 72 */       c.setModifiers(c.getModifiers() | 0x10);
/* 73 */       c.getDeclaredMethod("match").setBody("{ return $1 instanceof " + typeName + "; }");
/* 74 */       byte[] byteCode = c.toBytecode();
/* 75 */       c.detach();
/* 76 */       Method method = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, Integer.TYPE, Integer.TYPE });
/*    */       
/* 78 */       method.setAccessible(true);
/*    */       
/* 80 */       Class<?> generated = (Class)method.invoke(classLoader, new Object[] { className, byteCode, Integer.valueOf(0), Integer.valueOf(byteCode.length) });
/* 81 */       if (type != Object.class) {
/* 82 */         logger.debug("Generated: {}", generated.getName());
/*    */       }
/*    */       
/*    */ 
/* 86 */       return (TypeParameterMatcher)generated.newInstance();
/*    */     } catch (RuntimeException e) {
/* 88 */       throw e;
/*    */     } catch (Exception e) {
/* 90 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */   
/*    */   private static String typeName(Class<?> type) {
/* 95 */     if (type.isArray()) {
/* 96 */       return typeName(type.getComponentType()) + "[]";
/*    */     }
/*    */     
/* 99 */     return type.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\JavassistTypeParameterMatcherGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */