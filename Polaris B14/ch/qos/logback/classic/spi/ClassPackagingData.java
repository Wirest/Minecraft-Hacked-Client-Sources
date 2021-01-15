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
/*    */ public class ClassPackagingData
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -804643281218337001L;
/*    */   final String codeLocation;
/*    */   final String version;
/*    */   private final boolean exact;
/*    */   
/*    */   public ClassPackagingData(String codeLocation, String version)
/*    */   {
/* 26 */     this.codeLocation = codeLocation;
/* 27 */     this.version = version;
/* 28 */     this.exact = true;
/*    */   }
/*    */   
/*    */   public ClassPackagingData(String classLocation, String version, boolean exact) {
/* 32 */     this.codeLocation = classLocation;
/* 33 */     this.version = version;
/* 34 */     this.exact = exact;
/*    */   }
/*    */   
/*    */   public String getCodeLocation() {
/* 38 */     return this.codeLocation;
/*    */   }
/*    */   
/*    */   public String getVersion() {
/* 42 */     return this.version;
/*    */   }
/*    */   
/*    */   public boolean isExact() {
/* 46 */     return this.exact;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 51 */     int PRIME = 31;
/* 52 */     int result = 1;
/* 53 */     result = 31 * result + (this.codeLocation == null ? 0 : this.codeLocation.hashCode());
/* 54 */     return result;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 59 */     if (this == obj)
/* 60 */       return true;
/* 61 */     if (obj == null)
/* 62 */       return false;
/* 63 */     if (getClass() != obj.getClass())
/* 64 */       return false;
/* 65 */     ClassPackagingData other = (ClassPackagingData)obj;
/* 66 */     if (this.codeLocation == null) {
/* 67 */       if (other.codeLocation != null)
/* 68 */         return false;
/* 69 */     } else if (!this.codeLocation.equals(other.codeLocation))
/* 70 */       return false;
/* 71 */     if (this.exact != other.exact)
/* 72 */       return false;
/* 73 */     if (this.version == null) {
/* 74 */       if (other.version != null)
/* 75 */         return false;
/* 76 */     } else if (!this.version.equals(other.version))
/* 77 */       return false;
/* 78 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\ClassPackagingData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */