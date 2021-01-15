/*     */ package ch.qos.logback.core.db;
/*     */ 
/*     */ import ch.qos.logback.core.db.dialect.DBUtil;
/*     */ import ch.qos.logback.core.db.dialect.SQLDialectCode;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ConnectionSourceBase
/*     */   extends ContextAwareBase
/*     */   implements ConnectionSource
/*     */ {
/*     */   private boolean started;
/*  32 */   private String user = null;
/*  33 */   private String password = null;
/*     */   
/*     */ 
/*  36 */   private SQLDialectCode dialectCode = SQLDialectCode.UNKNOWN_DIALECT;
/*  37 */   private boolean supportsGetGeneratedKeys = false;
/*  38 */   private boolean supportsBatchUpdates = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void discoverConnectionProperties()
/*     */   {
/*  46 */     Connection connection = null;
/*     */     try {
/*  48 */       connection = getConnection();
/*  49 */       if (connection == null) {
/*  50 */         addWarn("Could not get a connection");
/*     */       }
/*     */       else {
/*  53 */         DatabaseMetaData meta = connection.getMetaData();
/*  54 */         DBUtil util = new DBUtil();
/*  55 */         util.setContext(getContext());
/*  56 */         this.supportsGetGeneratedKeys = util.supportsGetGeneratedKeys(meta);
/*  57 */         this.supportsBatchUpdates = util.supportsBatchUpdates(meta);
/*  58 */         this.dialectCode = DBUtil.discoverSQLDialect(meta);
/*  59 */         addInfo("Driver name=" + meta.getDriverName());
/*  60 */         addInfo("Driver version=" + meta.getDriverVersion());
/*  61 */         addInfo("supportsGetGeneratedKeys=" + this.supportsGetGeneratedKeys);
/*     */       }
/*     */     } catch (SQLException se) {
/*  64 */       addWarn("Could not discover the dialect to use.", se);
/*     */     } finally {
/*  66 */       DBHelper.closeConnection(connection);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final boolean supportsGetGeneratedKeys()
/*     */   {
/*  74 */     return this.supportsGetGeneratedKeys;
/*     */   }
/*     */   
/*     */   public final SQLDialectCode getSQLDialectCode() {
/*  78 */     return this.dialectCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final String getPassword()
/*     */   {
/*  85 */     return this.password;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setPassword(String password)
/*     */   {
/*  93 */     this.password = password;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final String getUser()
/*     */   {
/* 100 */     return this.user;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setUser(String username)
/*     */   {
/* 108 */     this.user = username;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final boolean supportsBatchUpdates()
/*     */   {
/* 115 */     return this.supportsBatchUpdates;
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/* 119 */     return this.started;
/*     */   }
/*     */   
/*     */   public void start() {
/* 123 */     this.started = true;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 127 */     this.started = false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\db\ConnectionSourceBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */