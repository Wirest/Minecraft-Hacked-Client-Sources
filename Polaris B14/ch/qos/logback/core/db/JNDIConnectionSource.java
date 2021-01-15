/*     */ package ch.qos.logback.core.db;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ import javax.sql.DataSource;
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
/*     */ public class JNDIConnectionSource
/*     */   extends ConnectionSourceBase
/*     */ {
/*  43 */   private String jndiLocation = null;
/*  44 */   private DataSource dataSource = null;
/*     */   
/*     */   public void start() {
/*  47 */     if (this.jndiLocation == null) {
/*  48 */       addError("No JNDI location specified for JNDIConnectionSource.");
/*     */     }
/*  50 */     discoverConnectionProperties();
/*     */   }
/*     */   
/*     */   public Connection getConnection() throws SQLException
/*     */   {
/*  55 */     Connection conn = null;
/*     */     try {
/*  57 */       if (this.dataSource == null) {
/*  58 */         this.dataSource = lookupDataSource();
/*     */       }
/*  60 */       if (getUser() != null) {
/*  61 */         addWarn("Ignoring property [user] with value [" + getUser() + "] for obtaining a connection from a DataSource.");
/*     */       }
/*  63 */       conn = this.dataSource.getConnection();
/*     */     } catch (NamingException ne) {
/*  65 */       addError("Error while getting data source", ne);
/*  66 */       throw new SQLException("NamingException while looking up DataSource: " + ne.getMessage());
/*     */     }
/*     */     catch (ClassCastException cce) {
/*  69 */       addError("ClassCastException while looking up DataSource.", cce);
/*  70 */       throw new SQLException("ClassCastException while looking up DataSource: " + cce.getMessage());
/*     */     }
/*     */     
/*     */ 
/*  74 */     return conn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getJndiLocation()
/*     */   {
/*  83 */     return this.jndiLocation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setJndiLocation(String jndiLocation)
/*     */   {
/*  93 */     this.jndiLocation = jndiLocation;
/*     */   }
/*     */   
/*     */   private DataSource lookupDataSource() throws NamingException, SQLException {
/*  97 */     addInfo("Looking up [" + this.jndiLocation + "] in JNDI");
/*     */     
/*  99 */     Context initialContext = new InitialContext();
/* 100 */     Object obj = initialContext.lookup(this.jndiLocation);
/*     */     
/*     */ 
/*     */ 
/* 104 */     DataSource ds = (DataSource)obj;
/*     */     
/* 106 */     if (ds == null) {
/* 107 */       throw new SQLException("Failed to obtain data source from JNDI location " + this.jndiLocation);
/*     */     }
/*     */     
/* 110 */     return ds;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\db\JNDIConnectionSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */