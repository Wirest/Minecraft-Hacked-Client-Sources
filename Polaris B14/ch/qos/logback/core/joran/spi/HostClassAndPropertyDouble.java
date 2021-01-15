/*    */ package ch.qos.logback.core.joran.spi;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HostClassAndPropertyDouble
/*    */ {
/*    */   final Class<?> hostClass;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   final String propertyName;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public HostClassAndPropertyDouble(Class<?> hostClass, String propertyName)
/*    */   {
/* 32 */     this.hostClass = hostClass;
/* 33 */     this.propertyName = propertyName;
/*    */   }
/*    */   
/*    */   public Class<?> getHostClass() {
/* 37 */     return this.hostClass;
/*    */   }
/*    */   
/*    */   public String getPropertyName() {
/* 41 */     return this.propertyName;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 46 */     int prime = 31;
/* 47 */     int result = 1;
/* 48 */     result = 31 * result + (this.hostClass == null ? 0 : this.hostClass.hashCode());
/* 49 */     result = 31 * result + (this.propertyName == null ? 0 : this.propertyName.hashCode());
/*    */     
/* 51 */     return result;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 56 */     if (this == obj)
/* 57 */       return true;
/* 58 */     if (obj == null)
/* 59 */       return false;
/* 60 */     if (getClass() != obj.getClass())
/* 61 */       return false;
/* 62 */     HostClassAndPropertyDouble other = (HostClassAndPropertyDouble)obj;
/* 63 */     if (this.hostClass == null) {
/* 64 */       if (other.hostClass != null)
/* 65 */         return false;
/* 66 */     } else if (!this.hostClass.equals(other.hostClass))
/* 67 */       return false;
/* 68 */     if (this.propertyName == null) {
/* 69 */       if (other.propertyName != null)
/* 70 */         return false;
/* 71 */     } else if (!this.propertyName.equals(other.propertyName))
/* 72 */       return false;
/* 73 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\spi\HostClassAndPropertyDouble.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */