/*    */ package ch.qos.logback.core.rolling.helper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TokenConverter
/*    */ {
/*    */   static final int IDENTITY = 0;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   static final int INTEGER = 1;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   static final int DATE = 1;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   int type;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   TokenConverter next;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected TokenConverter(int t)
/*    */   {
/* 37 */     this.type = t;
/*    */   }
/*    */   
/*    */   public TokenConverter getNext() {
/* 41 */     return this.next;
/*    */   }
/*    */   
/*    */   public void setNext(TokenConverter next) {
/* 45 */     this.next = next;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 49 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(int i) {
/* 53 */     this.type = i;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\helper\TokenConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */