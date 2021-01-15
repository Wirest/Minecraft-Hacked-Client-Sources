/*    */ package ch.qos.logback.core.layout;
/*    */ 
/*    */ import ch.qos.logback.core.CoreConstants;
/*    */ import ch.qos.logback.core.LayoutBase;
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
/*    */ public class EchoLayout<E>
/*    */   extends LayoutBase<E>
/*    */ {
/*    */   public String doLayout(E event)
/*    */   {
/* 27 */     return event + CoreConstants.LINE_SEPARATOR;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\layout\EchoLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */