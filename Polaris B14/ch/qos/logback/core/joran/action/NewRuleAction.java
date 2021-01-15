/*    */ package ch.qos.logback.core.joran.action;
/*    */ 
/*    */ import ch.qos.logback.core.joran.spi.ElementSelector;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.joran.spi.Interpreter;
/*    */ import ch.qos.logback.core.joran.spi.RuleStore;
/*    */ import ch.qos.logback.core.util.OptionHelper;
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
/*    */ public class NewRuleAction
/*    */   extends Action
/*    */ {
/* 24 */   boolean inError = false;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void begin(InterpretationContext ec, String localName, Attributes attributes)
/*    */   {
/* 31 */     this.inError = false;
/*    */     
/* 33 */     String pattern = attributes.getValue("pattern");
/* 34 */     String actionClass = attributes.getValue("actionClass");
/*    */     
/* 36 */     if (OptionHelper.isEmpty(pattern)) {
/* 37 */       this.inError = true;
/* 38 */       String errorMsg = "No 'pattern' attribute in <newRule>";
/* 39 */       addError(errorMsg);
/* 40 */       return;
/*    */     }
/*    */     
/* 43 */     if (OptionHelper.isEmpty(actionClass)) {
/* 44 */       this.inError = true;
/* 45 */       String errorMsg = "No 'actionClass' attribute in <newRule>";
/* 46 */       addError(errorMsg);
/* 47 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 51 */       addInfo("About to add new Joran parsing rule [" + pattern + "," + actionClass + "].");
/*    */       
/* 53 */       ec.getJoranInterpreter().getRuleStore().addRule(new ElementSelector(pattern), actionClass);
/*    */     }
/*    */     catch (Exception oops) {
/* 56 */       this.inError = true;
/* 57 */       String errorMsg = "Could not add new Joran parsing rule [" + pattern + "," + actionClass + "]";
/*    */       
/* 59 */       addError(errorMsg);
/*    */     }
/*    */   }
/*    */   
/*    */   public void end(InterpretationContext ec, String n) {}
/*    */   
/*    */   public void finish(InterpretationContext ec) {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\NewRuleAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */