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
/*     */ public class ElementPath
/*     */ {
/*  27 */   ArrayList<String> partList = new ArrayList();
/*     */   
/*     */   public ElementPath() {}
/*     */   
/*     */   public ElementPath(List<String> list)
/*     */   {
/*  33 */     this.partList.addAll(list);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ElementPath(String pathStr)
/*     */   {
/*  42 */     if (pathStr == null) {
/*  43 */       return;
/*     */     }
/*     */     
/*  46 */     String[] partArray = pathStr.split("/");
/*  47 */     if (partArray == null) { return;
/*     */     }
/*  49 */     for (String part : partArray) {
/*  50 */       if (part.length() > 0) {
/*  51 */         this.partList.add(part);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ElementPath duplicate() {
/*  57 */     ElementPath p = new ElementPath();
/*  58 */     p.partList.addAll(this.partList);
/*  59 */     return p;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  65 */     if ((o == null) || (!(o instanceof ElementPath))) {
/*  66 */       return false;
/*     */     }
/*     */     
/*  69 */     ElementPath r = (ElementPath)o;
/*     */     
/*  71 */     if (r.size() != size()) {
/*  72 */       return false;
/*     */     }
/*     */     
/*  75 */     int len = size();
/*     */     
/*  77 */     for (int i = 0; i < len; i++) {
/*  78 */       if (!equalityCheck(get(i), r.get(i))) {
/*  79 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  84 */     return true;
/*     */   }
/*     */   
/*     */   private boolean equalityCheck(String x, String y) {
/*  88 */     return x.equalsIgnoreCase(y);
/*     */   }
/*     */   
/*     */   public List<String> getCopyOfPartList() {
/*  92 */     return new ArrayList(this.partList);
/*     */   }
/*     */   
/*     */   public void push(String s) {
/*  96 */     this.partList.add(s);
/*     */   }
/*     */   
/*     */   public String get(int i) {
/* 100 */     return (String)this.partList.get(i);
/*     */   }
/*     */   
/*     */   public void pop() {
/* 104 */     if (!this.partList.isEmpty()) {
/* 105 */       this.partList.remove(this.partList.size() - 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public String peekLast() {
/* 110 */     if (!this.partList.isEmpty()) {
/* 111 */       int size = this.partList.size();
/* 112 */       return (String)this.partList.get(size - 1);
/*     */     }
/* 114 */     return null;
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 119 */     return this.partList.size();
/*     */   }
/*     */   
/*     */   protected String toStableString()
/*     */   {
/* 124 */     StringBuilder result = new StringBuilder();
/* 125 */     for (String current : this.partList) {
/* 126 */       result.append("[").append(current).append("]");
/*     */     }
/* 128 */     return result.toString();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 133 */     return toStableString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\spi\ElementPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */