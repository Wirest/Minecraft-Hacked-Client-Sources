/*    */ package io.netty.handler.codec.serialization;
/*    */ 
/*    */ import io.netty.util.internal.PlatformDependent;
/*    */ import java.util.HashMap;
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
/*    */ public final class ClassResolvers
/*    */ {
/*    */   public static ClassResolver cacheDisabled(ClassLoader classLoader)
/*    */   {
/* 31 */     return new ClassLoaderClassResolver(defaultClassLoader(classLoader));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ClassResolver weakCachingResolver(ClassLoader classLoader)
/*    */   {
/* 42 */     return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(classLoader)), new WeakReferenceMap(new HashMap()));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ClassResolver softCachingResolver(ClassLoader classLoader)
/*    */   {
/* 55 */     return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(classLoader)), new SoftReferenceMap(new HashMap()));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ClassResolver weakCachingConcurrentResolver(ClassLoader classLoader)
/*    */   {
/* 68 */     return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(classLoader)), new WeakReferenceMap(PlatformDependent.newConcurrentHashMap()));
/*    */   }
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
/*    */   public static ClassResolver softCachingConcurrentResolver(ClassLoader classLoader)
/*    */   {
/* 82 */     return new CachingClassResolver(new ClassLoaderClassResolver(defaultClassLoader(classLoader)), new SoftReferenceMap(PlatformDependent.newConcurrentHashMap()));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   static ClassLoader defaultClassLoader(ClassLoader classLoader)
/*    */   {
/* 89 */     if (classLoader != null) {
/* 90 */       return classLoader;
/*    */     }
/*    */     
/* 93 */     ClassLoader contextClassLoader = PlatformDependent.getContextClassLoader();
/* 94 */     if (contextClassLoader != null) {
/* 95 */       return contextClassLoader;
/*    */     }
/*    */     
/* 98 */     return PlatformDependent.getClassLoader(ClassResolvers.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\serialization\ClassResolvers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */