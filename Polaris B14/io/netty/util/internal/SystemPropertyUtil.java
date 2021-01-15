/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SystemPropertyUtil
/*     */ {
/*  38 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SystemPropertyUtil.class);
/*  39 */   private static boolean initializedLogger = true;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean contains(String key)
/*     */   {
/*  47 */     return get(key) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String get(String key)
/*     */   {
/*  57 */     return get(key, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String get(String key, String def)
/*     */   {
/*  70 */     if (key == null) {
/*  71 */       throw new NullPointerException("key");
/*     */     }
/*  73 */     if (key.isEmpty()) {
/*  74 */       throw new IllegalArgumentException("key must not be empty.");
/*     */     }
/*     */     
/*  77 */     String value = null;
/*     */     try {
/*  79 */       if (System.getSecurityManager() == null) {
/*  80 */         value = System.getProperty(key);
/*     */       } else {
/*  82 */         value = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public String run() {
/*  85 */             return System.getProperty(this.val$key);
/*     */           }
/*     */         });
/*     */       }
/*     */     } catch (Exception e) {
/*  90 */       if (!loggedException) {
/*  91 */         log("Unable to retrieve a system property '" + key + "'; default values will be used.", e);
/*  92 */         loggedException = true;
/*     */       }
/*     */     }
/*     */     
/*  96 */     if (value == null) {
/*  97 */       return def;
/*     */     }
/*     */     
/* 100 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean getBoolean(String key, boolean def)
/*     */   {
/* 113 */     String value = get(key);
/* 114 */     if (value == null) {
/* 115 */       return def;
/*     */     }
/*     */     
/* 118 */     value = value.trim().toLowerCase();
/* 119 */     if (value.isEmpty()) {
/* 120 */       return true;
/*     */     }
/*     */     
/* 123 */     if (("true".equals(value)) || ("yes".equals(value)) || ("1".equals(value))) {
/* 124 */       return true;
/*     */     }
/*     */     
/* 127 */     if (("false".equals(value)) || ("no".equals(value)) || ("0".equals(value))) {
/* 128 */       return false;
/*     */     }
/*     */     
/* 131 */     log("Unable to parse the boolean system property '" + key + "':" + value + " - " + "using the default value: " + def);
/*     */     
/*     */ 
/*     */ 
/* 135 */     return def;
/*     */   }
/*     */   
/* 138 */   private static final Pattern INTEGER_PATTERN = Pattern.compile("-?[0-9]+");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean loggedException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getInt(String key, int def)
/*     */   {
/* 150 */     String value = get(key);
/* 151 */     if (value == null) {
/* 152 */       return def;
/*     */     }
/*     */     
/* 155 */     value = value.trim().toLowerCase();
/* 156 */     if (INTEGER_PATTERN.matcher(value).matches()) {
/*     */       try {
/* 158 */         return Integer.parseInt(value);
/*     */       }
/*     */       catch (Exception e) {}
/*     */     }
/*     */     
/*     */ 
/* 164 */     log("Unable to parse the integer system property '" + key + "':" + value + " - " + "using the default value: " + def);
/*     */     
/*     */ 
/*     */ 
/* 168 */     return def;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long getLong(String key, long def)
/*     */   {
/* 181 */     String value = get(key);
/* 182 */     if (value == null) {
/* 183 */       return def;
/*     */     }
/*     */     
/* 186 */     value = value.trim().toLowerCase();
/* 187 */     if (INTEGER_PATTERN.matcher(value).matches()) {
/*     */       try {
/* 189 */         return Long.parseLong(value);
/*     */       }
/*     */       catch (Exception e) {}
/*     */     }
/*     */     
/*     */ 
/* 195 */     log("Unable to parse the long integer system property '" + key + "':" + value + " - " + "using the default value: " + def);
/*     */     
/*     */ 
/*     */ 
/* 199 */     return def;
/*     */   }
/*     */   
/*     */   private static void log(String msg) {
/* 203 */     if (initializedLogger) {
/* 204 */       logger.warn(msg);
/*     */     }
/*     */     else {
/* 207 */       Logger.getLogger(SystemPropertyUtil.class.getName()).log(Level.WARNING, msg);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void log(String msg, Exception e) {
/* 212 */     if (initializedLogger) {
/* 213 */       logger.warn(msg, e);
/*     */     }
/*     */     else {
/* 216 */       Logger.getLogger(SystemPropertyUtil.class.getName()).log(Level.WARNING, msg, e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\SystemPropertyUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */