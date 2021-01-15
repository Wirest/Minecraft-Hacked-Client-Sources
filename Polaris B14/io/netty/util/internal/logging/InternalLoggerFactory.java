/*    */ package io.netty.util.internal.logging;
/*    */ 
/*    */ import io.netty.util.internal.ThreadLocalRandom;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class InternalLoggerFactory
/*    */ {
/* 37 */   private static volatile InternalLoggerFactory defaultFactory = newDefaultFactory(InternalLoggerFactory.class.getName());
/*    */   
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 44 */       Class.forName(ThreadLocalRandom.class.getName(), true, InternalLoggerFactory.class.getClassLoader());
/*    */     }
/*    */     catch (Exception ignored) {}
/*    */   }
/*    */   
/*    */   private static InternalLoggerFactory newDefaultFactory(String name)
/*    */   {
/*    */     InternalLoggerFactory f;
/*    */     try
/*    */     {
/* 54 */       f = new Slf4JLoggerFactory(true);
/* 55 */       f.newInstance(name).debug("Using SLF4J as the default logging framework");
/*    */     } catch (Throwable t1) {
/*    */       try {
/* 58 */         f = new Log4JLoggerFactory();
/* 59 */         f.newInstance(name).debug("Using Log4J as the default logging framework");
/*    */       } catch (Throwable t2) {
/* 61 */         f = new JdkLoggerFactory();
/* 62 */         f.newInstance(name).debug("Using java.util.logging as the default logging framework");
/*    */       }
/*    */     }
/* 65 */     return f;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static InternalLoggerFactory getDefaultFactory()
/*    */   {
/* 73 */     return defaultFactory;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static void setDefaultFactory(InternalLoggerFactory defaultFactory)
/*    */   {
/* 80 */     if (defaultFactory == null) {
/* 81 */       throw new NullPointerException("defaultFactory");
/*    */     }
/* 83 */     defaultFactory = defaultFactory;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static InternalLogger getInstance(Class<?> clazz)
/*    */   {
/* 90 */     return getInstance(clazz.getName());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static InternalLogger getInstance(String name)
/*    */   {
/* 97 */     return getDefaultFactory().newInstance(name);
/*    */   }
/*    */   
/*    */   protected abstract InternalLogger newInstance(String paramString);
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\logging\InternalLoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */