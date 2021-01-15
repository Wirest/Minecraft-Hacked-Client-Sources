/*     */ package ch.qos.logback.core.joran.spi;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class ElementSelector
/*     */   extends ElementPath
/*     */ {
/*     */   public ElementSelector() {}
/*     */   
/*     */   public ElementSelector(List<String> list)
/*     */   {
/*  35 */     super(list);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ElementSelector(String p)
/*     */   {
/*  45 */     super(p);
/*     */   }
/*     */   
/*     */   public boolean fullPathMatch(ElementPath path) {
/*  49 */     if (path.size() != size()) {
/*  50 */       return false;
/*     */     }
/*     */     
/*  53 */     int len = size();
/*  54 */     for (int i = 0; i < len; i++) {
/*  55 */       if (!equalityCheck(get(i), path.get(i))) {
/*  56 */         return false;
/*     */       }
/*     */     }
/*     */     
/*  60 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTailMatchLength(ElementPath p)
/*     */   {
/*  69 */     if (p == null) {
/*  70 */       return 0;
/*     */     }
/*     */     
/*  73 */     int lSize = this.partList.size();
/*  74 */     int rSize = p.partList.size();
/*     */     
/*     */ 
/*  77 */     if ((lSize == 0) || (rSize == 0)) {
/*  78 */       return 0;
/*     */     }
/*     */     
/*  81 */     int minLen = lSize <= rSize ? lSize : rSize;
/*  82 */     int match = 0;
/*     */     
/*     */ 
/*  85 */     for (int i = 1; i <= minLen; i++) {
/*  86 */       String l = (String)this.partList.get(lSize - i);
/*  87 */       String r = (String)p.partList.get(rSize - i);
/*     */       
/*  89 */       if (!equalityCheck(l, r)) break;
/*  90 */       match++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  95 */     return match;
/*     */   }
/*     */   
/*     */   public boolean isContainedIn(ElementPath p) {
/*  99 */     if (p == null) {
/* 100 */       return false;
/*     */     }
/* 102 */     return p.toStableString().contains(toStableString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPrefixMatchLength(ElementPath p)
/*     */   {
/* 112 */     if (p == null) {
/* 113 */       return 0;
/*     */     }
/*     */     
/* 116 */     int lSize = this.partList.size();
/* 117 */     int rSize = p.partList.size();
/*     */     
/*     */ 
/* 120 */     if ((lSize == 0) || (rSize == 0)) {
/* 121 */       return 0;
/*     */     }
/*     */     
/* 124 */     int minLen = lSize <= rSize ? lSize : rSize;
/* 125 */     int match = 0;
/*     */     
/* 127 */     for (int i = 0; i < minLen; i++) {
/* 128 */       String l = (String)this.partList.get(i);
/* 129 */       String r = (String)p.partList.get(i);
/*     */       
/* 131 */       if (!equalityCheck(l, r)) break;
/* 132 */       match++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 138 */     return match;
/*     */   }
/*     */   
/*     */   private boolean equalityCheck(String x, String y) {
/* 142 */     return x.equalsIgnoreCase(y);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 147 */     if ((o == null) || (!(o instanceof ElementSelector))) {
/* 148 */       return false;
/*     */     }
/*     */     
/* 151 */     ElementSelector r = (ElementSelector)o;
/*     */     
/* 153 */     if (r.size() != size()) {
/* 154 */       return false;
/*     */     }
/*     */     
/* 157 */     int len = size();
/*     */     
/* 159 */     for (int i = 0; i < len; i++) {
/* 160 */       if (!equalityCheck(get(i), r.get(i))) {
/* 161 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 166 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 171 */     int hc = 0;
/* 172 */     int len = size();
/*     */     
/* 174 */     for (int i = 0; i < len; i++)
/*     */     {
/*     */ 
/* 177 */       hc ^= get(i).toLowerCase().hashCode();
/*     */     }
/* 179 */     return hc;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\spi\ElementSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */