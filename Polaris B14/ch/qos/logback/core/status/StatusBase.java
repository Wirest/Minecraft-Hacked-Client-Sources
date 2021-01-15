/*     */ package ch.qos.logback.core.status;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
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
/*     */ public abstract class StatusBase
/*     */   implements Status
/*     */ {
/*  22 */   private static final List<Status> EMPTY_LIST = new ArrayList(0);
/*     */   int level;
/*     */   final String message;
/*     */   final Object origin;
/*     */   List<Status> childrenList;
/*     */   Throwable throwable;
/*     */   long date;
/*     */   
/*     */   StatusBase(int level, String msg, Object origin)
/*     */   {
/*  32 */     this(level, msg, origin, null);
/*     */   }
/*     */   
/*     */   StatusBase(int level, String msg, Object origin, Throwable t) {
/*  36 */     this.level = level;
/*  37 */     this.message = msg;
/*  38 */     this.origin = origin;
/*  39 */     this.throwable = t;
/*  40 */     this.date = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   public synchronized void add(Status child) {
/*  44 */     if (child == null) {
/*  45 */       throw new NullPointerException("Null values are not valid Status.");
/*     */     }
/*  47 */     if (this.childrenList == null) {
/*  48 */       this.childrenList = new ArrayList();
/*     */     }
/*  50 */     this.childrenList.add(child);
/*     */   }
/*     */   
/*     */   public synchronized boolean hasChildren() {
/*  54 */     return (this.childrenList != null) && (this.childrenList.size() > 0);
/*     */   }
/*     */   
/*     */   public synchronized Iterator<Status> iterator() {
/*  58 */     if (this.childrenList != null) {
/*  59 */       return this.childrenList.iterator();
/*     */     }
/*  61 */     return EMPTY_LIST.iterator();
/*     */   }
/*     */   
/*     */   public synchronized boolean remove(Status statusToRemove)
/*     */   {
/*  66 */     if (this.childrenList == null) {
/*  67 */       return false;
/*     */     }
/*     */     
/*  70 */     return this.childrenList.remove(statusToRemove);
/*     */   }
/*     */   
/*     */   public int getLevel() {
/*  74 */     return this.level;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized int getEffectiveLevel()
/*     */   {
/*  82 */     int result = this.level;
/*     */     
/*     */ 
/*  85 */     Iterator it = iterator();
/*     */     
/*  87 */     while (it.hasNext()) {
/*  88 */       Status s = (Status)it.next();
/*  89 */       int effLevel = s.getEffectiveLevel();
/*  90 */       if (effLevel > result) {
/*  91 */         result = effLevel;
/*     */       }
/*     */     }
/*  94 */     return result;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/*  98 */     return this.message;
/*     */   }
/*     */   
/*     */   public Object getOrigin() {
/* 102 */     return this.origin;
/*     */   }
/*     */   
/*     */   public Throwable getThrowable() {
/* 106 */     return this.throwable;
/*     */   }
/*     */   
/*     */   public Long getDate() {
/* 110 */     return Long.valueOf(this.date);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 117 */     StringBuilder buf = new StringBuilder();
/* 118 */     switch (getEffectiveLevel()) {
/*     */     case 0: 
/* 120 */       buf.append("INFO");
/* 121 */       break;
/*     */     case 1: 
/* 123 */       buf.append("WARN");
/* 124 */       break;
/*     */     case 2: 
/* 126 */       buf.append("ERROR");
/*     */     }
/*     */     
/* 129 */     if (this.origin != null) {
/* 130 */       buf.append(" in ");
/* 131 */       buf.append(this.origin);
/* 132 */       buf.append(" -");
/*     */     }
/*     */     
/* 135 */     buf.append(" ");
/* 136 */     buf.append(this.message);
/*     */     
/* 138 */     if (this.throwable != null) {
/* 139 */       buf.append(" ");
/* 140 */       buf.append(this.throwable);
/*     */     }
/*     */     
/* 143 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 148 */     int prime = 31;
/* 149 */     int result = 1;
/* 150 */     result = 31 * result + this.level;
/* 151 */     result = 31 * result + (this.message == null ? 0 : this.message.hashCode());
/* 152 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 157 */     if (this == obj)
/* 158 */       return true;
/* 159 */     if (obj == null)
/* 160 */       return false;
/* 161 */     if (getClass() != obj.getClass())
/* 162 */       return false;
/* 163 */     StatusBase other = (StatusBase)obj;
/* 164 */     if (this.level != other.level)
/* 165 */       return false;
/* 166 */     if (this.message == null) {
/* 167 */       if (other.message != null)
/* 168 */         return false;
/* 169 */     } else if (!this.message.equals(other.message))
/* 170 */       return false;
/* 171 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\status\StatusBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */