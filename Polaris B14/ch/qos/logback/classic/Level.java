/*     */ package ch.qos.logback.classic;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public final class Level
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -814092767334282137L;
/*     */   public static final int OFF_INT = Integer.MAX_VALUE;
/*     */   public static final int ERROR_INT = 40000;
/*     */   public static final int WARN_INT = 30000;
/*     */   public static final int INFO_INT = 20000;
/*     */   public static final int DEBUG_INT = 10000;
/*     */   public static final int TRACE_INT = 5000;
/*     */   public static final int ALL_INT = Integer.MIN_VALUE;
/*  37 */   public static final Integer OFF_INTEGER = Integer.valueOf(Integer.MAX_VALUE);
/*  38 */   public static final Integer ERROR_INTEGER = Integer.valueOf(40000);
/*  39 */   public static final Integer WARN_INTEGER = Integer.valueOf(30000);
/*  40 */   public static final Integer INFO_INTEGER = Integer.valueOf(20000);
/*  41 */   public static final Integer DEBUG_INTEGER = Integer.valueOf(10000);
/*  42 */   public static final Integer TRACE_INTEGER = Integer.valueOf(5000);
/*  43 */   public static final Integer ALL_INTEGER = Integer.valueOf(Integer.MIN_VALUE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  48 */   public static final Level OFF = new Level(Integer.MAX_VALUE, "OFF");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */   public static final Level ERROR = new Level(40000, "ERROR");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  59 */   public static final Level WARN = new Level(30000, "WARN");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */   public static final Level INFO = new Level(20000, "INFO");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   public static final Level DEBUG = new Level(10000, "DEBUG");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */   public static final Level TRACE = new Level(5000, "TRACE");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  82 */   public static final Level ALL = new Level(Integer.MIN_VALUE, "ALL");
/*     */   
/*     */   public final int levelInt;
/*     */   
/*     */   public final String levelStr;
/*     */   
/*     */ 
/*     */   private Level(int levelInt, String levelStr)
/*     */   {
/*  91 */     this.levelInt = levelInt;
/*  92 */     this.levelStr = levelStr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  99 */     return this.levelStr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int toInt()
/*     */   {
/* 106 */     return this.levelInt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer toInteger()
/*     */   {
/* 115 */     switch (this.levelInt) {
/*     */     case -2147483648: 
/* 117 */       return ALL_INTEGER;
/*     */     case 5000: 
/* 119 */       return TRACE_INTEGER;
/*     */     case 10000: 
/* 121 */       return DEBUG_INTEGER;
/*     */     case 20000: 
/* 123 */       return INFO_INTEGER;
/*     */     case 30000: 
/* 125 */       return WARN_INTEGER;
/*     */     case 40000: 
/* 127 */       return ERROR_INTEGER;
/*     */     case 2147483647: 
/* 129 */       return OFF_INTEGER;
/*     */     }
/* 131 */     throw new IllegalStateException("Level " + this.levelStr + ", " + this.levelInt + " is unknown.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isGreaterOrEqual(Level r)
/*     */   {
/* 141 */     return this.levelInt >= r.levelInt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Level toLevel(String sArg)
/*     */   {
/* 149 */     return toLevel(sArg, DEBUG);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Level valueOf(String sArg)
/*     */   {
/* 160 */     return toLevel(sArg, DEBUG);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Level toLevel(int val)
/*     */   {
/* 169 */     return toLevel(val, DEBUG);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Level toLevel(int val, Level defaultLevel)
/*     */   {
/* 177 */     switch (val) {
/*     */     case -2147483648: 
/* 179 */       return ALL;
/*     */     case 5000: 
/* 181 */       return TRACE;
/*     */     case 10000: 
/* 183 */       return DEBUG;
/*     */     case 20000: 
/* 185 */       return INFO;
/*     */     case 30000: 
/* 187 */       return WARN;
/*     */     case 40000: 
/* 189 */       return ERROR;
/*     */     case 2147483647: 
/* 191 */       return OFF;
/*     */     }
/* 193 */     return defaultLevel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Level toLevel(String sArg, Level defaultLevel)
/*     */   {
/* 202 */     if (sArg == null) {
/* 203 */       return defaultLevel;
/*     */     }
/*     */     
/* 206 */     if (sArg.equalsIgnoreCase("ALL")) {
/* 207 */       return ALL;
/*     */     }
/* 209 */     if (sArg.equalsIgnoreCase("TRACE")) {
/* 210 */       return TRACE;
/*     */     }
/* 212 */     if (sArg.equalsIgnoreCase("DEBUG")) {
/* 213 */       return DEBUG;
/*     */     }
/* 215 */     if (sArg.equalsIgnoreCase("INFO")) {
/* 216 */       return INFO;
/*     */     }
/* 218 */     if (sArg.equalsIgnoreCase("WARN")) {
/* 219 */       return WARN;
/*     */     }
/* 221 */     if (sArg.equalsIgnoreCase("ERROR")) {
/* 222 */       return ERROR;
/*     */     }
/* 224 */     if (sArg.equalsIgnoreCase("OFF")) {
/* 225 */       return OFF;
/*     */     }
/* 227 */     return defaultLevel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object readResolve()
/*     */   {
/* 237 */     return toLevel(this.levelInt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Level fromLocationAwareLoggerInteger(int levelInt)
/*     */   {
/*     */     Level level;
/*     */     
/*     */ 
/*     */ 
/* 250 */     switch (levelInt) {
/*     */     case 0: 
/* 252 */       level = TRACE;
/* 253 */       break;
/*     */     case 10: 
/* 255 */       level = DEBUG;
/* 256 */       break;
/*     */     case 20: 
/* 258 */       level = INFO;
/* 259 */       break;
/*     */     case 30: 
/* 261 */       level = WARN;
/* 262 */       break;
/*     */     case 40: 
/* 264 */       level = ERROR;
/* 265 */       break;
/*     */     default: 
/* 267 */       throw new IllegalArgumentException(levelInt + " not a valid level value");
/*     */     }
/* 269 */     return level;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int toLocationAwareLoggerInteger(Level level)
/*     */   {
/* 281 */     if (level == null)
/* 282 */       throw new IllegalArgumentException("null level parameter is not admitted");
/* 283 */     switch (level.toInt()) {
/*     */     case 5000: 
/* 285 */       return 0;
/*     */     case 10000: 
/* 287 */       return 10;
/*     */     case 20000: 
/* 289 */       return 20;
/*     */     case 30000: 
/* 291 */       return 30;
/*     */     case 40000: 
/* 293 */       return 40;
/*     */     }
/* 295 */     throw new IllegalArgumentException(level + " not a valid level value");
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\Level.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */