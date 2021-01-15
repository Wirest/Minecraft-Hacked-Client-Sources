/*    */ package ch.qos.logback.core.joran.action;
/*    */ 
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.joran.util.PropertySetter;
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
/*    */ public class ParamAction
/*    */   extends Action
/*    */ {
/* 25 */   static String NO_NAME = "No name attribute in <param> element";
/* 26 */   static String NO_VALUE = "No name attribute in <param> element";
/* 27 */   boolean inError = false;
/*    */   
/*    */   public void begin(InterpretationContext ec, String localName, Attributes attributes)
/*    */   {
/* 31 */     String name = attributes.getValue("name");
/* 32 */     String value = attributes.getValue("value");
/*    */     
/* 34 */     if (name == null) {
/* 35 */       this.inError = true;
/* 36 */       addError(NO_NAME);
/* 37 */       return;
/*    */     }
/*    */     
/* 40 */     if (value == null) {
/* 41 */       this.inError = true;
/* 42 */       addError(NO_VALUE);
/* 43 */       return;
/*    */     }
/*    */     
/*    */ 
/* 47 */     value = value.trim();
/*    */     
/* 49 */     Object o = ec.peekObject();
/* 50 */     PropertySetter propSetter = new PropertySetter(o);
/* 51 */     propSetter.setContext(this.context);
/* 52 */     value = ec.subst(value);
/*    */     
/*    */ 
/* 55 */     name = ec.subst(name);
/*    */     
/*    */ 
/*    */ 
/* 59 */     propSetter.setProperty(name, value);
/*    */   }
/*    */   
/*    */   public void end(InterpretationContext ec, String localName) {}
/*    */   
/*    */   public void finish(InterpretationContext ec) {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\ParamAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */