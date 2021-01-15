/*    */ package ch.qos.logback.core.joran.action;
/*    */ 
/*    */ import ch.qos.logback.core.Appender;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.spi.AppenderAttachable;
/*    */ import ch.qos.logback.core.util.OptionHelper;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public class AppenderRefAction<E>
/*    */   extends Action
/*    */ {
/* 28 */   boolean inError = false;
/*    */   
/*    */ 
/*    */   public void begin(InterpretationContext ec, String tagName, Attributes attributes)
/*    */   {
/* 33 */     this.inError = false;
/*    */     
/*    */ 
/*    */ 
/* 37 */     Object o = ec.peekObject();
/*    */     
/* 39 */     if (!(o instanceof AppenderAttachable)) {
/* 40 */       String errMsg = "Could not find an AppenderAttachable at the top of execution stack. Near [" + tagName + "] line " + getLineNumber(ec);
/*    */       
/* 42 */       this.inError = true;
/* 43 */       addError(errMsg);
/* 44 */       return;
/*    */     }
/*    */     
/* 47 */     AppenderAttachable<E> appenderAttachable = (AppenderAttachable)o;
/*    */     
/* 49 */     String appenderName = ec.subst(attributes.getValue("ref"));
/*    */     
/* 51 */     if (OptionHelper.isEmpty(appenderName))
/*    */     {
/* 53 */       String errMsg = "Missing appender ref attribute in <appender-ref> tag.";
/* 54 */       this.inError = true;
/* 55 */       addError(errMsg);
/*    */       
/* 57 */       return;
/*    */     }
/*    */     
/* 60 */     HashMap<String, Appender<E>> appenderBag = (HashMap)ec.getObjectMap().get("APPENDER_BAG");
/*    */     
/* 62 */     Appender<E> appender = (Appender)appenderBag.get(appenderName);
/*    */     
/* 64 */     if (appender == null) {
/* 65 */       String msg = "Could not find an appender named [" + appenderName + "]. Did you define it below instead of above in the configuration file?";
/*    */       
/* 67 */       this.inError = true;
/* 68 */       addError(msg);
/* 69 */       addError("See http://logback.qos.ch/codes.html#appender_order for more details.");
/*    */       
/* 71 */       return;
/*    */     }
/*    */     
/* 74 */     addInfo("Attaching appender named [" + appenderName + "] to " + appenderAttachable);
/*    */     
/* 76 */     appenderAttachable.addAppender(appender);
/*    */   }
/*    */   
/*    */   public void end(InterpretationContext ec, String n) {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\AppenderRefAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */