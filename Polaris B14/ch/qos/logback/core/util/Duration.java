/*     */ package ch.qos.logback.core.util;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Duration
/*     */ {
/*     */   private static final String DOUBLE_PART = "([0-9]*(.[0-9]+)?)";
/*     */   private static final int DOUBLE_GROUP = 1;
/*     */   private static final String UNIT_PART = "(|milli(second)?|second(e)?|minute|hour|day)s?";
/*     */   private static final int UNIT_GROUP = 3;
/*  42 */   private static final Pattern DURATION_PATTERN = Pattern.compile("([0-9]*(.[0-9]+)?)\\s*(|milli(second)?|second(e)?|minute|hour|day)s?", 2);
/*     */   
/*     */   static final long SECONDS_COEFFICIENT = 1000L;
/*     */   
/*     */   static final long MINUTES_COEFFICIENT = 60000L;
/*     */   static final long HOURS_COEFFICIENT = 3600000L;
/*     */   static final long DAYS_COEFFICIENT = 86400000L;
/*     */   final long millis;
/*     */   
/*     */   public Duration(long millis)
/*     */   {
/*  53 */     this.millis = millis;
/*     */   }
/*     */   
/*     */   public static Duration buildByMilliseconds(double value) {
/*  57 */     return new Duration(value);
/*     */   }
/*     */   
/*     */   public static Duration buildBySeconds(double value) {
/*  61 */     return new Duration((1000.0D * value));
/*     */   }
/*     */   
/*     */   public static Duration buildByMinutes(double value) {
/*  65 */     return new Duration((60000.0D * value));
/*     */   }
/*     */   
/*     */   public static Duration buildByHours(double value) {
/*  69 */     return new Duration((3600000.0D * value));
/*     */   }
/*     */   
/*     */   public static Duration buildByDays(double value) {
/*  73 */     return new Duration((8.64E7D * value));
/*     */   }
/*     */   
/*     */   public static Duration buildUnbounded() {
/*  77 */     return new Duration(Long.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public long getMilliseconds() {
/*  81 */     return this.millis;
/*     */   }
/*     */   
/*     */   public static Duration valueOf(String durationStr) {
/*  85 */     Matcher matcher = DURATION_PATTERN.matcher(durationStr);
/*     */     
/*  87 */     if (matcher.matches()) {
/*  88 */       String doubleStr = matcher.group(1);
/*  89 */       String unitStr = matcher.group(3);
/*     */       
/*  91 */       double doubleValue = Double.valueOf(doubleStr).doubleValue();
/*  92 */       if ((unitStr.equalsIgnoreCase("milli")) || (unitStr.equalsIgnoreCase("millisecond")) || (unitStr.length() == 0))
/*     */       {
/*  94 */         return buildByMilliseconds(doubleValue); }
/*  95 */       if ((unitStr.equalsIgnoreCase("second")) || (unitStr.equalsIgnoreCase("seconde")))
/*     */       {
/*  97 */         return buildBySeconds(doubleValue); }
/*  98 */       if (unitStr.equalsIgnoreCase("minute"))
/*  99 */         return buildByMinutes(doubleValue);
/* 100 */       if (unitStr.equalsIgnoreCase("hour"))
/* 101 */         return buildByHours(doubleValue);
/* 102 */       if (unitStr.equalsIgnoreCase("day")) {
/* 103 */         return buildByDays(doubleValue);
/*     */       }
/* 105 */       throw new IllegalStateException("Unexpected " + unitStr);
/*     */     }
/*     */     
/* 108 */     throw new IllegalArgumentException("String value [" + durationStr + "] is not in the expected format.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 115 */     if (this.millis < 1000L)
/* 116 */       return this.millis + " milliseconds";
/* 117 */     if (this.millis < 60000L)
/* 118 */       return this.millis / 1000L + " seconds";
/* 119 */     if (this.millis < 3600000L) {
/* 120 */       return this.millis / 60000L + " minutes";
/*     */     }
/* 122 */     return this.millis / 3600000L + " hours";
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\util\Duration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */