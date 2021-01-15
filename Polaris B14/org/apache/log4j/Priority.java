/*     */ package org.apache.log4j;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Priority
/*     */ {
/*     */   transient int level;
/*     */   
/*     */ 
/*     */ 
/*     */   transient String levelStr;
/*     */   
/*     */ 
/*     */ 
/*     */   transient int syslogEquivalent;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int OFF_INT = Integer.MAX_VALUE;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int FATAL_INT = 50000;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final int ERROR_INT = 40000;
/*     */   
/*     */ 
/*     */   public static final int WARN_INT = 30000;
/*     */   
/*     */ 
/*     */   public static final int INFO_INT = 20000;
/*     */   
/*     */ 
/*     */   public static final int DEBUG_INT = 10000;
/*     */   
/*     */ 
/*     */   public static final int ALL_INT = Integer.MIN_VALUE;
/*     */   
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*  46 */   public static final Priority FATAL = new Level(50000, "FATAL", 0);
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*  51 */   public static final Priority ERROR = new Level(40000, "ERROR", 3);
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*  56 */   public static final Priority WARN = new Level(30000, "WARN", 4);
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*  61 */   public static final Priority INFO = new Level(20000, "INFO", 6);
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*  66 */   public static final Priority DEBUG = new Level(10000, "DEBUG", 7);
/*     */   
/*     */ 
/*     */ 
/*     */   protected Priority()
/*     */   {
/*  72 */     this.level = 10000;
/*  73 */     this.levelStr = "DEBUG";
/*  74 */     this.syslogEquivalent = 7;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Priority(int level, String levelStr, int syslogEquivalent)
/*     */   {
/*  81 */     this.level = level;
/*  82 */     this.levelStr = levelStr;
/*  83 */     this.syslogEquivalent = syslogEquivalent;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  91 */     if ((o instanceof Priority)) {
/*  92 */       Priority r = (Priority)o;
/*  93 */       return this.level == r.level;
/*     */     }
/*  95 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int getSyslogEquivalent()
/*     */   {
/* 103 */     return this.syslogEquivalent;
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
/*     */   public boolean isGreaterOrEqual(Priority r)
/*     */   {
/* 116 */     return this.level >= r.level;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static Priority[] getAllPossiblePriorities()
/*     */   {
/* 126 */     return new Priority[] { FATAL, ERROR, Level.WARN, INFO, DEBUG };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 133 */     return this.levelStr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final int toInt()
/*     */   {
/* 140 */     return this.level;
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static Priority toPriority(String sArg) {
/* 147 */     return Level.toLevel(sArg);
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static Priority toPriority(int val) {
/* 154 */     return toPriority(val, DEBUG);
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static Priority toPriority(int val, Priority defaultPriority) {
/* 161 */     return Level.toLevel(val, (Level)defaultPriority);
/*     */   }
/*     */   
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static Priority toPriority(String sArg, Priority defaultPriority) {
/* 168 */     return Level.toLevel(sArg, (Level)defaultPriority);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\apache\log4j\Priority.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */