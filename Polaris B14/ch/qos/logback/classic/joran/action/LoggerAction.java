/*    */ package ch.qos.logback.classic.joran.action;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
/*    */ import ch.qos.logback.classic.Logger;
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import ch.qos.logback.core.joran.action.Action;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LoggerAction
/*    */   extends Action
/*    */ {
/*    */   public static final String LEVEL_ATTRIBUTE = "level";
/* 34 */   boolean inError = false;
/*    */   Logger logger;
/*    */   
/*    */   public void begin(InterpretationContext ec, String name, Attributes attributes) {
/* 38 */     this.inError = false;
/* 39 */     this.logger = null;
/*    */     
/* 41 */     LoggerContext loggerContext = (LoggerContext)this.context;
/*    */     
/* 43 */     String loggerName = ec.subst(attributes.getValue("name"));
/*    */     
/* 45 */     if (OptionHelper.isEmpty(loggerName)) {
/* 46 */       this.inError = true;
/* 47 */       String aroundLine = getLineColStr(ec);
/* 48 */       String errorMsg = "No 'name' attribute in element " + name + ", around " + aroundLine;
/* 49 */       addError(errorMsg);
/* 50 */       return;
/*    */     }
/*    */     
/* 53 */     this.logger = loggerContext.getLogger(loggerName);
/*    */     
/* 55 */     String levelStr = ec.subst(attributes.getValue("level"));
/*    */     
/* 57 */     if (!OptionHelper.isEmpty(levelStr)) {
/* 58 */       if (("INHERITED".equalsIgnoreCase(levelStr)) || ("NULL".equalsIgnoreCase(levelStr)))
/*    */       {
/* 60 */         addInfo("Setting level of logger [" + loggerName + "] to null, i.e. INHERITED");
/*    */         
/* 62 */         this.logger.setLevel(null);
/*    */       } else {
/* 64 */         Level level = Level.toLevel(levelStr);
/* 65 */         addInfo("Setting level of logger [" + loggerName + "] to " + level);
/* 66 */         this.logger.setLevel(level);
/*    */       }
/*    */     }
/*    */     
/* 70 */     String additivityStr = ec.subst(attributes.getValue("additivity"));
/* 71 */     if (!OptionHelper.isEmpty(additivityStr)) {
/* 72 */       boolean additive = OptionHelper.toBoolean(additivityStr, true);
/* 73 */       addInfo("Setting additivity of logger [" + loggerName + "] to " + additive);
/*    */       
/* 75 */       this.logger.setAdditive(additive);
/*    */     }
/* 77 */     ec.pushObject(this.logger);
/*    */   }
/*    */   
/*    */   public void end(InterpretationContext ec, String e) {
/* 81 */     if (this.inError) {
/* 82 */       return;
/*    */     }
/* 84 */     Object o = ec.peekObject();
/* 85 */     if (o != this.logger) {
/* 86 */       addWarn("The object on the top the of the stack is not " + this.logger + " pushed earlier");
/* 87 */       addWarn("It is: " + o);
/*    */     } else {
/* 89 */       ec.popObject();
/*    */     }
/*    */   }
/*    */   
/*    */   public void finish(InterpretationContext ec) {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\joran\action\LoggerAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */