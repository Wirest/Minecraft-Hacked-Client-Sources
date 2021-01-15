/*    */ package ch.qos.logback.core.sift;
/*    */ 
/*    */ import ch.qos.logback.core.Appender;
/*    */ import ch.qos.logback.core.joran.GenericConfigurator;
/*    */ import ch.qos.logback.core.joran.action.DefinePropertyAction;
/*    */ import ch.qos.logback.core.joran.action.NestedBasicPropertyIA;
/*    */ import ch.qos.logback.core.joran.action.NestedComplexPropertyIA;
/*    */ import ch.qos.logback.core.joran.action.PropertyAction;
/*    */ import ch.qos.logback.core.joran.action.TimestampAction;
/*    */ import ch.qos.logback.core.joran.event.SaxEvent;
/*    */ import ch.qos.logback.core.joran.spi.ElementSelector;
/*    */ import ch.qos.logback.core.joran.spi.Interpreter;
/*    */ import ch.qos.logback.core.joran.spi.JoranException;
/*    */ import ch.qos.logback.core.joran.spi.RuleStore;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ public abstract class SiftingJoranConfiguratorBase<E>
/*    */   extends GenericConfigurator
/*    */ {
/*    */   protected final String key;
/*    */   protected final String value;
/*    */   protected final Map<String, String> parentPropertyMap;
/*    */   static final String ONE_AND_ONLY_ONE_URL = "http://logback.qos.ch/codes.html#1andOnly1";
/*    */   
/*    */   protected SiftingJoranConfiguratorBase(String key, String value, Map<String, String> parentPropertyMap)
/*    */   {
/* 38 */     this.key = key;
/* 39 */     this.value = value;
/* 40 */     this.parentPropertyMap = parentPropertyMap;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void addImplicitRules(Interpreter interpreter)
/*    */   {
/* 48 */     NestedComplexPropertyIA nestedComplexIA = new NestedComplexPropertyIA();
/* 49 */     nestedComplexIA.setContext(this.context);
/* 50 */     interpreter.addImplicitAction(nestedComplexIA);
/*    */     
/* 52 */     NestedBasicPropertyIA nestedSimpleIA = new NestedBasicPropertyIA();
/* 53 */     nestedSimpleIA.setContext(this.context);
/* 54 */     interpreter.addImplicitAction(nestedSimpleIA);
/*    */   }
/*    */   
/*    */   protected void addInstanceRules(RuleStore rs)
/*    */   {
/* 59 */     rs.addRule(new ElementSelector("configuration/property"), new PropertyAction());
/* 60 */     rs.addRule(new ElementSelector("configuration/timestamp"), new TimestampAction());
/* 61 */     rs.addRule(new ElementSelector("configuration/define"), new DefinePropertyAction());
/*    */   }
/*    */   
/*    */   public abstract Appender<E> getAppender();
/*    */   
/* 66 */   int errorEmmissionCount = 0;
/*    */   
/*    */   protected void oneAndOnlyOneCheck(Map<?, ?> appenderMap) {
/* 69 */     String errMsg = null;
/* 70 */     if (appenderMap.size() == 0) {
/* 71 */       this.errorEmmissionCount += 1;
/* 72 */       errMsg = "No nested appenders found within the <sift> element in SiftingAppender.";
/* 73 */     } else if (appenderMap.size() > 1) {
/* 74 */       this.errorEmmissionCount += 1;
/* 75 */       errMsg = "Only and only one appender can be nested the <sift> element in SiftingAppender. See also http://logback.qos.ch/codes.html#1andOnly1";
/*    */     }
/*    */     
/*    */ 
/* 79 */     if ((errMsg != null) && (this.errorEmmissionCount < 4)) {
/* 80 */       addError(errMsg);
/*    */     }
/*    */   }
/*    */   
/*    */   public void doConfigure(List<SaxEvent> eventList) throws JoranException {
/* 85 */     super.doConfigure(eventList);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 90 */     return getClass().getName() + "{" + this.key + "=" + this.value + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\sift\SiftingJoranConfiguratorBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */