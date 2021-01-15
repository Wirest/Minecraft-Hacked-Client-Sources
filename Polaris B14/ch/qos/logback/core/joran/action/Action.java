/*    */ package ch.qos.logback.core.joran.action;
/*    */ 
/*    */ import ch.qos.logback.core.joran.spi.ActionException;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.joran.spi.Interpreter;
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.Locator;
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
/*    */ public abstract class Action
/*    */   extends ContextAwareBase
/*    */ {
/*    */   public static final String NAME_ATTRIBUTE = "name";
/*    */   public static final String KEY_ATTRIBUTE = "key";
/*    */   public static final String VALUE_ATTRIBUTE = "value";
/*    */   public static final String FILE_ATTRIBUTE = "file";
/*    */   public static final String CLASS_ATTRIBUTE = "class";
/*    */   public static final String PATTERN_ATTRIBUTE = "pattern";
/*    */   public static final String SCOPE_ATTRIBUTE = "scope";
/*    */   public static final String ACTION_CLASS_ATTRIBUTE = "actionClass";
/*    */   
/*    */   public abstract void begin(InterpretationContext paramInterpretationContext, String paramString, Attributes paramAttributes)
/*    */     throws ActionException;
/*    */   
/*    */   public void body(InterpretationContext ic, String body)
/*    */     throws ActionException
/*    */   {}
/*    */   
/*    */   public abstract void end(InterpretationContext paramInterpretationContext, String paramString)
/*    */     throws ActionException;
/*    */   
/*    */   public String toString()
/*    */   {
/* 77 */     return getClass().getName();
/*    */   }
/*    */   
/*    */   protected int getColumnNumber(InterpretationContext ic) {
/* 81 */     Interpreter ji = ic.getJoranInterpreter();
/* 82 */     Locator locator = ji.getLocator();
/* 83 */     if (locator != null) {
/* 84 */       return locator.getColumnNumber();
/*    */     }
/* 86 */     return -1;
/*    */   }
/*    */   
/*    */   protected int getLineNumber(InterpretationContext ic) {
/* 90 */     Interpreter ji = ic.getJoranInterpreter();
/* 91 */     Locator locator = ji.getLocator();
/* 92 */     if (locator != null) {
/* 93 */       return locator.getLineNumber();
/*    */     }
/* 95 */     return -1;
/*    */   }
/*    */   
/*    */   protected String getLineColStr(InterpretationContext ic) {
/* 99 */     return "line: " + getLineNumber(ic) + ", column: " + getColumnNumber(ic);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\Action.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */