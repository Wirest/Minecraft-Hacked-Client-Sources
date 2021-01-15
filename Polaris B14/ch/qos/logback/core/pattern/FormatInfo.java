/*     */ package ch.qos.logback.core.pattern;
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
/*     */ public class FormatInfo
/*     */ {
/*  23 */   private int min = Integer.MIN_VALUE;
/*  24 */   private int max = Integer.MAX_VALUE;
/*  25 */   private boolean leftPad = true;
/*  26 */   private boolean leftTruncate = true;
/*     */   
/*     */   public FormatInfo() {}
/*     */   
/*     */   public FormatInfo(int min, int max)
/*     */   {
/*  32 */     this.min = min;
/*  33 */     this.max = max;
/*     */   }
/*     */   
/*     */   public FormatInfo(int min, int max, boolean leftPad, boolean leftTruncate) {
/*  37 */     this.min = min;
/*  38 */     this.max = max;
/*  39 */     this.leftPad = leftPad;
/*  40 */     this.leftTruncate = leftTruncate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static FormatInfo valueOf(String str)
/*     */     throws IllegalArgumentException
/*     */   {
/*  52 */     if (str == null) {
/*  53 */       throw new NullPointerException("Argument cannot be null");
/*     */     }
/*     */     
/*  56 */     FormatInfo fi = new FormatInfo();
/*     */     
/*  58 */     int indexOfDot = str.indexOf('.');
/*  59 */     String minPart = null;
/*  60 */     String maxPart = null;
/*  61 */     if (indexOfDot != -1) {
/*  62 */       minPart = str.substring(0, indexOfDot);
/*  63 */       if (indexOfDot + 1 == str.length()) {
/*  64 */         throw new IllegalArgumentException("Formatting string [" + str + "] should not end with '.'");
/*     */       }
/*     */       
/*  67 */       maxPart = str.substring(indexOfDot + 1);
/*     */     }
/*     */     else {
/*  70 */       minPart = str;
/*     */     }
/*     */     
/*  73 */     if ((minPart != null) && (minPart.length() > 0)) {
/*  74 */       int min = Integer.parseInt(minPart);
/*  75 */       if (min >= 0) {
/*  76 */         fi.min = min;
/*     */       } else {
/*  78 */         fi.min = (-min);
/*  79 */         fi.leftPad = false;
/*     */       }
/*     */     }
/*     */     
/*  83 */     if ((maxPart != null) && (maxPart.length() > 0)) {
/*  84 */       int max = Integer.parseInt(maxPart);
/*  85 */       if (max >= 0) {
/*  86 */         fi.max = max;
/*     */       } else {
/*  88 */         fi.max = (-max);
/*  89 */         fi.leftTruncate = false;
/*     */       }
/*     */     }
/*     */     
/*  93 */     return fi;
/*     */   }
/*     */   
/*     */   public boolean isLeftPad()
/*     */   {
/*  98 */     return this.leftPad;
/*     */   }
/*     */   
/*     */   public void setLeftPad(boolean leftAlign) {
/* 102 */     this.leftPad = leftAlign;
/*     */   }
/*     */   
/*     */   public int getMax() {
/* 106 */     return this.max;
/*     */   }
/*     */   
/*     */   public void setMax(int max) {
/* 110 */     this.max = max;
/*     */   }
/*     */   
/*     */   public int getMin() {
/* 114 */     return this.min;
/*     */   }
/*     */   
/*     */   public void setMin(int min) {
/* 118 */     this.min = min;
/*     */   }
/*     */   
/*     */   public boolean isLeftTruncate() {
/* 122 */     return this.leftTruncate;
/*     */   }
/*     */   
/*     */   public void setLeftTruncate(boolean leftTruncate) {
/* 126 */     this.leftTruncate = leftTruncate;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 130 */     if (this == o) {
/* 131 */       return true;
/*     */     }
/* 133 */     if (!(o instanceof FormatInfo)) {
/* 134 */       return false;
/*     */     }
/* 136 */     FormatInfo r = (FormatInfo)o;
/*     */     
/* 138 */     return (this.min == r.min) && (this.max == r.max) && (this.leftPad == r.leftPad) && (this.leftTruncate == r.leftTruncate);
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 144 */     int result = this.min;
/* 145 */     result = 31 * result + this.max;
/* 146 */     result = 31 * result + (this.leftPad ? 1 : 0);
/* 147 */     result = 31 * result + (this.leftTruncate ? 1 : 0);
/* 148 */     return result;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 152 */     return "FormatInfo(" + this.min + ", " + this.max + ", " + this.leftPad + ", " + this.leftTruncate + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\FormatInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */