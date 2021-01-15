/*    */ package ch.qos.logback.classic.joran.action;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
/*    */ import ch.qos.logback.classic.Logger;
/*    */ import ch.qos.logback.core.joran.action.Action;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LevelAction
/*    */   extends Action
/*    */ {
/* 34 */   boolean inError = false;
/*    */   
/*    */   public void begin(InterpretationContext ec, String name, Attributes attributes) {
/* 37 */     Object o = ec.peekObject();
/*    */     
/* 39 */     if (!(o instanceof Logger)) {
/* 40 */       this.inError = true;
/* 41 */       addError("For element <level>, could not find a logger at the top of execution stack.");
/* 42 */       return;
/*    */     }
/*    */     
/* 45 */     Logger l = (Logger)o;
/*    */     
/* 47 */     String loggerName = l.getName();
/*    */     
/* 49 */     String levelStr = ec.subst(attributes.getValue("value"));
/*    */     
/*    */ 
/*    */ 
/* 53 */     if (("INHERITED".equalsIgnoreCase(levelStr)) || ("NULL".equalsIgnoreCase(levelStr))) {
/* 54 */       l.setLevel(null);
/*    */     } else {
/* 56 */       l.setLevel(Level.toLevel(levelStr, Level.DEBUG));
/*    */     }
/*    */     
/* 59 */     addInfo(loggerName + " level set to " + l.getLevel());
/*    */   }
/*    */   
/*    */   public void finish(InterpretationContext ec) {}
/*    */   
/*    */   public void end(InterpretationContext ec, String e) {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\joran\action\LevelAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */