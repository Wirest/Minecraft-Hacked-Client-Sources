/*    */ package ch.qos.logback.core.db;
/*    */ 
/*    */ import ch.qos.logback.core.joran.action.Action;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.joran.util.PropertySetter;
/*    */ import ch.qos.logback.core.util.OptionHelper;
/*    */ import javax.naming.Context;
/*    */ import javax.naming.InitialContext;
/*    */ import javax.sql.DataSource;
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
/*    */ 
/*    */ 
/*    */ public class BindDataSourceToJNDIAction
/*    */   extends Action
/*    */ {
/*    */   static final String DATA_SOURCE_CLASS = "dataSourceClass";
/*    */   static final String URL = "url";
/*    */   static final String USER = "user";
/*    */   static final String PASSWORD = "password";
/*    */   
/*    */   public void begin(InterpretationContext ec, String localName, Attributes attributes)
/*    */   {
/* 46 */     String dsClassName = ec.getProperty("dataSourceClass");
/*    */     
/* 48 */     if (OptionHelper.isEmpty(dsClassName)) {
/* 49 */       addWarn("dsClassName is a required parameter");
/* 50 */       ec.addError("dsClassName is a required parameter");
/*    */       
/* 52 */       return;
/*    */     }
/*    */     
/* 55 */     String urlStr = ec.getProperty("url");
/* 56 */     String userStr = ec.getProperty("user");
/* 57 */     String passwordStr = ec.getProperty("password");
/*    */     try
/*    */     {
/* 60 */       DataSource ds = (DataSource)OptionHelper.instantiateByClassName(dsClassName, DataSource.class, this.context);
/*    */       
/*    */ 
/* 63 */       PropertySetter setter = new PropertySetter(ds);
/* 64 */       setter.setContext(this.context);
/*    */       
/* 66 */       if (!OptionHelper.isEmpty(urlStr)) {
/* 67 */         setter.setProperty("url", urlStr);
/*    */       }
/*    */       
/* 70 */       if (!OptionHelper.isEmpty(userStr)) {
/* 71 */         setter.setProperty("user", userStr);
/*    */       }
/*    */       
/* 74 */       if (!OptionHelper.isEmpty(passwordStr)) {
/* 75 */         setter.setProperty("password", passwordStr);
/*    */       }
/*    */       
/* 78 */       Context ctx = new InitialContext();
/* 79 */       ctx.rebind("dataSource", ds);
/*    */     } catch (Exception oops) {
/* 81 */       addError("Could not bind  datasource. Reported error follows.", oops);
/*    */       
/* 83 */       ec.addError("Could not not bind  datasource of type [" + dsClassName + "].");
/*    */     }
/*    */   }
/*    */   
/*    */   public void end(InterpretationContext ec, String name) {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\db\BindDataSourceToJNDIAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */