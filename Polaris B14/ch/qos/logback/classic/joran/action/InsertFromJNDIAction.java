/*    */ package ch.qos.logback.classic.joran.action;
/*    */ 
/*    */ import ch.qos.logback.classic.util.JNDIUtil;
/*    */ import ch.qos.logback.core.joran.action.Action;
/*    */ import ch.qos.logback.core.joran.action.ActionUtil;
/*    */ import ch.qos.logback.core.joran.action.ActionUtil.Scope;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.util.OptionHelper;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.NamingException;
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
/*    */ public class InsertFromJNDIAction
/*    */   extends Action
/*    */ {
/*    */   public static final String ENV_ENTRY_NAME_ATTR = "env-entry-name";
/*    */   public static final String AS_ATTR = "as";
/*    */   
/*    */   public void begin(InterpretationContext ec, String name, Attributes attributes)
/*    */   {
/* 41 */     int errorCount = 0;
/* 42 */     String envEntryName = ec.subst(attributes.getValue("env-entry-name"));
/* 43 */     String asKey = ec.subst(attributes.getValue("as"));
/*    */     
/* 45 */     String scopeStr = attributes.getValue("scope");
/* 46 */     ActionUtil.Scope scope = ActionUtil.stringToScope(scopeStr);
/*    */     
/*    */ 
/*    */ 
/* 50 */     if (OptionHelper.isEmpty(envEntryName)) {
/* 51 */       String lineColStr = getLineColStr(ec);
/* 52 */       addError("[env-entry-name] missing, around " + lineColStr);
/* 53 */       errorCount++;
/*    */     }
/*    */     
/* 56 */     if (OptionHelper.isEmpty(asKey)) {
/* 57 */       String lineColStr = getLineColStr(ec);
/* 58 */       addError("[as] missing, around " + lineColStr);
/* 59 */       errorCount++;
/*    */     }
/*    */     
/* 62 */     if (errorCount != 0) {
/* 63 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 67 */       Context ctx = JNDIUtil.getInitialContext();
/* 68 */       String envEntryValue = JNDIUtil.lookup(ctx, envEntryName);
/* 69 */       if (OptionHelper.isEmpty(envEntryValue)) {
/* 70 */         addError("[" + envEntryName + "] has null or empty value");
/*    */       } else {
/* 72 */         addInfo("Setting variable [" + asKey + "] to [" + envEntryValue + "] in [" + scope + "] scope");
/* 73 */         ActionUtil.setProperty(ec, asKey, envEntryValue, scope);
/*    */       }
/*    */     } catch (NamingException e) {
/* 76 */       addError("Failed to lookup JNDI env-entry [" + envEntryName + "]");
/*    */     }
/*    */   }
/*    */   
/*    */   public void end(InterpretationContext ec, String name) {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\joran\action\InsertFromJNDIAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */