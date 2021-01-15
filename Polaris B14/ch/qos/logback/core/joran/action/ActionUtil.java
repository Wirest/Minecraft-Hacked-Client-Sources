/*    */ package ch.qos.logback.core.joran.action;
/*    */ 
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.util.ContextUtil;
/*    */ import ch.qos.logback.core.util.OptionHelper;
/*    */ import java.util.Properties;
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
/*    */ public class ActionUtil
/*    */ {
/*    */   public static enum Scope
/*    */   {
/* 25 */     LOCAL,  CONTEXT,  SYSTEM;
/*    */     
/*    */ 
/*    */     private Scope() {}
/*    */   }
/*    */   
/*    */ 
/*    */   public static Scope stringToScope(String scopeStr)
/*    */   {
/* 34 */     if (Scope.SYSTEM.toString().equalsIgnoreCase(scopeStr))
/* 35 */       return Scope.SYSTEM;
/* 36 */     if (Scope.CONTEXT.toString().equalsIgnoreCase(scopeStr)) {
/* 37 */       return Scope.CONTEXT;
/*    */     }
/* 39 */     return Scope.LOCAL;
/*    */   }
/*    */   
/*    */   public static void setProperty(InterpretationContext ic, String key, String value, Scope scope) {
/* 43 */     switch (scope) {
/*    */     case LOCAL: 
/* 45 */       ic.addSubstitutionProperty(key, value);
/* 46 */       break;
/*    */     case CONTEXT: 
/* 48 */       ic.getContext().putProperty(key, value);
/* 49 */       break;
/*    */     case SYSTEM: 
/* 51 */       OptionHelper.setSystemProperty(ic, key, value);
/*    */     }
/*    */     
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void setProperties(InterpretationContext ic, Properties props, Scope scope)
/*    */   {
/* 61 */     switch (scope) {
/*    */     case LOCAL: 
/* 63 */       ic.addSubstitutionProperties(props);
/* 64 */       break;
/*    */     case CONTEXT: 
/* 66 */       ContextUtil cu = new ContextUtil(ic.getContext());
/* 67 */       cu.addProperties(props);
/* 68 */       break;
/*    */     case SYSTEM: 
/* 70 */       OptionHelper.setSystemProperties(ic, props);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\ActionUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */