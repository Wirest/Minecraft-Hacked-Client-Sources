/*    */ package ch.qos.logback.core.joran.action;
/*    */ 
/*    */ import ch.qos.logback.core.joran.spi.ActionException;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import org.xml.sax.Attributes;
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
/*    */ public class ContextPropertyAction
/*    */   extends Action
/*    */ {
/*    */   public void begin(InterpretationContext ec, String name, Attributes attributes)
/*    */     throws ActionException
/*    */   {
/* 29 */     addError("The [contextProperty] element has been removed. Please use [substitutionProperty] element instead");
/*    */   }
/*    */   
/*    */   public void end(InterpretationContext ec, String name)
/*    */     throws ActionException
/*    */   {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\ContextPropertyAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */