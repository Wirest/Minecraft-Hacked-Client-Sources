/*    */ package ch.qos.logback.classic.spi;
/*    */ 
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import java.io.Serializable;
/*    */ import java.util.Map;
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
/*    */ public class LoggerContextVO
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 5488023392483144387L;
/*    */   final String name;
/*    */   final Map<String, String> propertyMap;
/*    */   final long birthTime;
/*    */   
/*    */   public LoggerContextVO(LoggerContext lc)
/*    */   {
/* 45 */     this.name = lc.getName();
/* 46 */     this.propertyMap = lc.getCopyOfPropertyMap();
/* 47 */     this.birthTime = lc.getBirthTime();
/*    */   }
/*    */   
/*    */   public LoggerContextVO(String name, Map<String, String> propertyMap, long birthTime) {
/* 51 */     this.name = name;
/* 52 */     this.propertyMap = propertyMap;
/* 53 */     this.birthTime = birthTime;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 57 */     return this.name;
/*    */   }
/*    */   
/*    */   public Map<String, String> getPropertyMap() {
/* 61 */     return this.propertyMap;
/*    */   }
/*    */   
/*    */   public long getBirthTime() {
/* 65 */     return this.birthTime;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 71 */     return "LoggerContextVO{name='" + this.name + '\'' + ", propertyMap=" + this.propertyMap + ", birthTime=" + this.birthTime + '}';
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 79 */     if (this == o) return true;
/* 80 */     if (!(o instanceof LoggerContextVO)) { return false;
/*    */     }
/* 82 */     LoggerContextVO that = (LoggerContextVO)o;
/*    */     
/* 84 */     if (this.birthTime != that.birthTime) return false;
/* 85 */     if (this.name != null ? !this.name.equals(that.name) : that.name != null) return false;
/* 86 */     if (this.propertyMap != null ? !this.propertyMap.equals(that.propertyMap) : that.propertyMap != null) { return false;
/*    */     }
/* 88 */     return true;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 93 */     int result = this.name != null ? this.name.hashCode() : 0;
/* 94 */     result = 31 * result + (this.propertyMap != null ? this.propertyMap.hashCode() : 0);
/* 95 */     result = 31 * result + (int)(this.birthTime ^ this.birthTime >>> 32);
/*    */     
/* 97 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\LoggerContextVO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */