/*     */ package ch.qos.logback.core.rolling.helper;
/*     */ 
/*     */ import ch.qos.logback.core.pattern.DynamicConverter;
/*     */ import ch.qos.logback.core.util.CachingDateFormatter;
/*     */ import ch.qos.logback.core.util.DatePatternToRegexUtil;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.TimeZone;
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
/*     */ public class DateTokenConverter<E>
/*     */   extends DynamicConverter<E>
/*     */   implements MonoTypedConverter
/*     */ {
/*     */   public static final String CONVERTER_KEY = "d";
/*     */   public static final String AUXILIARY_TOKEN = "AUX";
/*     */   public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
/*     */   private String datePattern;
/*     */   private TimeZone timeZone;
/*     */   private CachingDateFormatter cdf;
/*  44 */   private boolean primary = true;
/*     */   
/*  46 */   public void start() { this.datePattern = getFirstOption();
/*  47 */     if (this.datePattern == null) {
/*  48 */       this.datePattern = "yyyy-MM-dd";
/*     */     }
/*     */     
/*  51 */     List<String> optionList = getOptionList();
/*  52 */     if (optionList != null) {
/*  53 */       for (int optionIndex = 1; optionIndex < optionList.size(); optionIndex++) {
/*  54 */         String option = (String)optionList.get(optionIndex);
/*  55 */         if ("AUX".equalsIgnoreCase(option)) {
/*  56 */           this.primary = false;
/*     */         } else {
/*  58 */           this.timeZone = TimeZone.getTimeZone(option);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  63 */     this.cdf = new CachingDateFormatter(this.datePattern);
/*  64 */     if (this.timeZone != null) {
/*  65 */       this.cdf.setTimeZone(this.timeZone);
/*     */     }
/*     */   }
/*     */   
/*     */   public String convert(Date date) {
/*  70 */     return this.cdf.format(date.getTime());
/*     */   }
/*     */   
/*     */   public String convert(Object o) {
/*  74 */     if (o == null) {
/*  75 */       throw new IllegalArgumentException("Null argument forbidden");
/*     */     }
/*  77 */     if ((o instanceof Date)) {
/*  78 */       return convert((Date)o);
/*     */     }
/*  80 */     throw new IllegalArgumentException("Cannot convert " + o + " of type" + o.getClass().getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getDatePattern()
/*     */   {
/*  87 */     return this.datePattern;
/*     */   }
/*     */   
/*     */   public TimeZone getTimeZone() {
/*  91 */     return this.timeZone;
/*     */   }
/*     */   
/*     */   public boolean isApplicable(Object o) {
/*  95 */     return o instanceof Date;
/*     */   }
/*     */   
/*     */   public String toRegex() {
/*  99 */     DatePatternToRegexUtil datePatternToRegexUtil = new DatePatternToRegexUtil(this.datePattern);
/* 100 */     return datePatternToRegexUtil.toRegex();
/*     */   }
/*     */   
/*     */   public boolean isPrimary() {
/* 104 */     return this.primary;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\helper\DateTokenConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */