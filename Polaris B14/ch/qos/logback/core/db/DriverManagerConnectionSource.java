/*    */ package ch.qos.logback.core.db;
/*    */ 
/*    */ import java.sql.Connection;
/*    */ import java.sql.DriverManager;
/*    */ import java.sql.SQLException;
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
/*    */ public class DriverManagerConnectionSource
/*    */   extends ConnectionSourceBase
/*    */ {
/* 31 */   private String driverClass = null;
/* 32 */   private String url = null;
/*    */   
/*    */   public void start() {
/*    */     try {
/* 36 */       if (this.driverClass != null) {
/* 37 */         Class.forName(this.driverClass);
/* 38 */         discoverConnectionProperties();
/*    */       } else {
/* 40 */         addError("WARNING: No JDBC driver specified for logback DriverManagerConnectionSource.");
/*    */       }
/*    */     } catch (ClassNotFoundException cnfe) {
/* 43 */       addError("Could not load JDBC driver class: " + this.driverClass, cnfe);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public Connection getConnection()
/*    */     throws SQLException
/*    */   {
/* 51 */     if (getUser() == null) {
/* 52 */       return DriverManager.getConnection(this.url);
/*    */     }
/* 54 */     return DriverManager.getConnection(this.url, getUser(), getPassword());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getUrl()
/*    */   {
/* 64 */     return this.url;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setUrl(String url)
/*    */   {
/* 74 */     this.url = url;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDriverClass()
/*    */   {
/* 83 */     return this.driverClass;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setDriverClass(String driverClass)
/*    */   {
/* 93 */     this.driverClass = driverClass;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\db\DriverManagerConnectionSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */