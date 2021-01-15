/*    */ package ch.qos.logback.core.db;
/*    */ 
/*    */ import ch.qos.logback.core.db.dialect.SQLDialectCode;
/*    */ import java.sql.Connection;
/*    */ import java.sql.SQLException;
/*    */ import javax.sql.DataSource;
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
/*    */ public class DataSourceConnectionSource
/*    */   extends ConnectionSourceBase
/*    */ {
/*    */   private DataSource dataSource;
/*    */   
/*    */   public void start()
/*    */   {
/* 41 */     if (this.dataSource == null) {
/* 42 */       addWarn("WARNING: No data source specified");
/*    */     } else {
/* 44 */       discoverConnectionProperties();
/* 45 */       if ((!supportsGetGeneratedKeys()) && (getSQLDialectCode() == SQLDialectCode.UNKNOWN_DIALECT))
/*    */       {
/* 47 */         addWarn("Connection does not support GetGeneratedKey method and could not discover the dialect.");
/*    */       }
/*    */     }
/* 50 */     super.start();
/*    */   }
/*    */   
/*    */ 
/*    */   public Connection getConnection()
/*    */     throws SQLException
/*    */   {
/* 57 */     if (this.dataSource == null) {
/* 58 */       addError("WARNING: No data source specified");
/* 59 */       return null;
/*    */     }
/*    */     
/* 62 */     if (getUser() == null) {
/* 63 */       return this.dataSource.getConnection();
/*    */     }
/* 65 */     return this.dataSource.getConnection(getUser(), getPassword());
/*    */   }
/*    */   
/*    */   public DataSource getDataSource()
/*    */   {
/* 70 */     return this.dataSource;
/*    */   }
/*    */   
/*    */   public void setDataSource(DataSource dataSource) {
/* 74 */     this.dataSource = dataSource;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\db\DataSourceConnectionSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */