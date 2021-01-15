/*    */ package ch.qos.logback.classic.turbo;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
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
/*    */ public class MDCValueLevelPair
/*    */ {
/*    */   private String value;
/*    */   private Level level;
/*    */   
/*    */   public String getValue()
/*    */   {
/* 30 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String name) {
/* 34 */     this.value = name;
/*    */   }
/*    */   
/*    */   public Level getLevel() {
/* 38 */     return this.level;
/*    */   }
/*    */   
/*    */   public void setLevel(Level level) {
/* 42 */     this.level = level;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\turbo\MDCValueLevelPair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */