/*     */ package ch.qos.logback.core.util;
/*     */ 
/*     */ import java.text.DateFormatSymbols;
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
/*     */ class CharSequenceToRegexMapper
/*     */ {
/*  26 */   DateFormatSymbols symbols = DateFormatSymbols.getInstance();
/*     */   
/*     */   String toRegex(CharSequenceState css) {
/*  29 */     int occurrences = css.occurrences;
/*  30 */     char c = css.c;
/*  31 */     switch (css.c) {
/*     */     case 'G': 
/*     */     case 'z': 
/*  34 */       return ".*";
/*     */     case 'M': 
/*  36 */       if (occurrences <= 2)
/*  37 */         return number(occurrences);
/*  38 */       if (occurrences == 3) {
/*  39 */         return getRegexForShortMonths();
/*     */       }
/*  41 */       return getRegexForLongMonths();
/*     */     case 'D': 
/*     */     case 'F': 
/*     */     case 'H': 
/*     */     case 'K': 
/*     */     case 'S': 
/*     */     case 'W': 
/*     */     case 'd': 
/*     */     case 'h': 
/*     */     case 'k': 
/*     */     case 'm': 
/*     */     case 's': 
/*     */     case 'w': 
/*     */     case 'y': 
/*  55 */       return number(occurrences);
/*     */     case 'E': 
/*  57 */       if (occurrences >= 4) {
/*  58 */         return getRegexForLongDaysOfTheWeek();
/*     */       }
/*  60 */       return getRegexForShortDaysOfTheWeek();
/*     */     
/*     */     case 'a': 
/*  63 */       return getRegexForAmPms();
/*     */     case 'Z': 
/*  65 */       return "(\\+|-)\\d{4}";
/*     */     case '.': 
/*  67 */       return "\\.";
/*     */     case '\\': 
/*  69 */       throw new IllegalStateException("Forward slashes are not allowed");
/*     */     case '\'': 
/*  71 */       if (occurrences == 1) {
/*  72 */         return "";
/*     */       }
/*  74 */       throw new IllegalStateException("Too many single quotes");
/*     */     }
/*  76 */     if (occurrences == 1) {
/*  77 */       return "" + c;
/*     */     }
/*  79 */     return c + "{" + occurrences + "}";
/*     */   }
/*     */   
/*     */ 
/*     */   private String number(int occurrences)
/*     */   {
/*  85 */     return "\\d{" + occurrences + "}";
/*     */   }
/*     */   
/*     */   private String getRegexForAmPms() {
/*  89 */     return symbolArrayToRegex(this.symbols.getAmPmStrings());
/*     */   }
/*     */   
/*     */   private String getRegexForLongDaysOfTheWeek() {
/*  93 */     return symbolArrayToRegex(this.symbols.getWeekdays());
/*     */   }
/*     */   
/*     */   private String getRegexForShortDaysOfTheWeek() {
/*  97 */     return symbolArrayToRegex(this.symbols.getShortWeekdays());
/*     */   }
/*     */   
/*     */   private String getRegexForLongMonths() {
/* 101 */     return symbolArrayToRegex(this.symbols.getMonths());
/*     */   }
/*     */   
/*     */   String getRegexForShortMonths() {
/* 105 */     return symbolArrayToRegex(this.symbols.getShortMonths());
/*     */   }
/*     */   
/*     */   private String symbolArrayToRegex(String[] symbolArray) {
/* 109 */     int[] minMax = findMinMaxLengthsInSymbols(symbolArray);
/* 110 */     return ".{" + minMax[0] + "," + minMax[1] + "}";
/*     */   }
/*     */   
/*     */   static int[] findMinMaxLengthsInSymbols(String[] symbols) {
/* 114 */     int min = Integer.MAX_VALUE;
/* 115 */     int max = 0;
/* 116 */     for (String symbol : symbols) {
/* 117 */       int len = symbol.length();
/*     */       
/* 119 */       if (len != 0)
/*     */       {
/* 121 */         min = Math.min(min, len);
/* 122 */         max = Math.max(max, len);
/*     */       } }
/* 124 */     return new int[] { min, max };
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\util\CharSequenceToRegexMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */