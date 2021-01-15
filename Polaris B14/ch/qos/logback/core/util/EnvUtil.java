/*    */ package ch.qos.logback.core.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */   private static boolean isJDK_N_OrHigher(int n)
/*    */   {
/* 26 */     List<String> versionList = new ArrayList();
/*    */     
/*    */ 
/* 29 */     for (int i = 0; i < 5; i++) {
/* 30 */       versionList.add("1." + (n + i));
/*    */     }
/*    */     
/* 33 */     String javaVersion = System.getProperty("java.version");
/* 34 */     if (javaVersion == null) {
/* 35 */       return false;
/*    */     }
/* 37 */     for (String v : versionList) {
/* 38 */       if (javaVersion.startsWith(v))
/* 39 */         return true;
/*    */     }
/* 41 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean isJDK5() {
/* 45 */     return isJDK_N_OrHigher(5);
/*    */   }
/*    */   
/*    */   public static boolean isJDK6OrHigher() {
/* 49 */     return isJDK_N_OrHigher(6);
/*    */   }
/*    */   
/*    */   public static boolean isJDK7OrHigher() {
/* 53 */     return isJDK_N_OrHigher(7);
/*    */   }
/*    */   
/*    */   public static boolean isJaninoAvailable() {
/* 57 */     ClassLoader classLoader = EnvUtil.class.getClassLoader();
/*    */     try {
/* 59 */       Class<?> bindingClass = classLoader.loadClass("org.codehaus.janino.ScriptEvaluator");
/* 60 */       return bindingClass != null;
/*    */     } catch (ClassNotFoundException e) {}
/* 62 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean isWindows()
/*    */   {
/* 67 */     String os = System.getProperty("os.name");
/* 68 */     return os.startsWith("Windows");
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\util\EnvUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */