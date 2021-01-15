/*    */ package ch.qos.logback.classic.spi;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StackTraceElementProxy
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -2374374378980555982L;
/*    */   final StackTraceElement ste;
/*    */   private transient String steAsString;
/*    */   private ClassPackagingData cpd;
/*    */   
/*    */   public StackTraceElementProxy(StackTraceElement ste)
/*    */   {
/* 29 */     if (ste == null) {
/* 30 */       throw new IllegalArgumentException("ste cannot be null");
/*    */     }
/* 32 */     this.ste = ste;
/*    */   }
/*    */   
/*    */   public String getSTEAsString()
/*    */   {
/* 37 */     if (this.steAsString == null) {
/* 38 */       this.steAsString = ("at " + this.ste.toString());
/*    */     }
/* 40 */     return this.steAsString;
/*    */   }
/*    */   
/*    */   public StackTraceElement getStackTraceElement() {
/* 44 */     return this.ste;
/*    */   }
/*    */   
/*    */   public void setClassPackagingData(ClassPackagingData cpd) {
/* 48 */     if (this.cpd != null) {
/* 49 */       throw new IllegalStateException("Packaging data has been already set");
/*    */     }
/* 51 */     this.cpd = cpd;
/*    */   }
/*    */   
/*    */   public ClassPackagingData getClassPackagingData() {
/* 55 */     return this.cpd;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 60 */     return this.ste.hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 65 */     if (this == obj)
/* 66 */       return true;
/* 67 */     if (obj == null)
/* 68 */       return false;
/* 69 */     if (getClass() != obj.getClass())
/* 70 */       return false;
/* 71 */     StackTraceElementProxy other = (StackTraceElementProxy)obj;
/*    */     
/* 73 */     if (!this.ste.equals(other.ste)) {
/* 74 */       return false;
/*    */     }
/* 76 */     if (this.cpd == null) {
/* 77 */       if (other.cpd != null) {
/* 78 */         return false;
/*    */       }
/* 80 */     } else if (!this.cpd.equals(other.cpd)) {
/* 81 */       return false;
/*    */     }
/* 83 */     return true;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 88 */     return getSTEAsString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\StackTraceElementProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */