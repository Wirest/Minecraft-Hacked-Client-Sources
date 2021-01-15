/*     */ package ch.qos.logback.core.db;
/*     */ 
/*     */ import ch.qos.logback.core.UnsynchronizedAppenderBase;
/*     */ import ch.qos.logback.core.db.dialect.DBUtil;
/*     */ import ch.qos.logback.core.db.dialect.SQLDialect;
/*     */ import ch.qos.logback.core.db.dialect.SQLDialectCode;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
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
/*     */ public abstract class DBAppenderBase<E>
/*     */   extends UnsynchronizedAppenderBase<E>
/*     */ {
/*     */   protected ConnectionSource connectionSource;
/*  37 */   protected boolean cnxSupportsGetGeneratedKeys = false;
/*  38 */   protected boolean cnxSupportsBatchUpdates = false;
/*     */   
/*     */   protected SQLDialect sqlDialect;
/*     */   
/*     */   protected abstract Method getGeneratedKeysMethod();
/*     */   
/*     */   protected abstract String getInsertSQL();
/*     */   
/*     */   public void start()
/*     */   {
/*  48 */     if (this.connectionSource == null) {
/*  49 */       throw new IllegalStateException("DBAppender cannot function without a connection source");
/*     */     }
/*     */     
/*     */ 
/*  53 */     this.sqlDialect = DBUtil.getDialectFromCode(this.connectionSource.getSQLDialectCode());
/*     */     
/*  55 */     if (getGeneratedKeysMethod() != null) {
/*  56 */       this.cnxSupportsGetGeneratedKeys = this.connectionSource.supportsGetGeneratedKeys();
/*     */     } else {
/*  58 */       this.cnxSupportsGetGeneratedKeys = false;
/*     */     }
/*  60 */     this.cnxSupportsBatchUpdates = this.connectionSource.supportsBatchUpdates();
/*  61 */     if ((!this.cnxSupportsGetGeneratedKeys) && (this.sqlDialect == null)) {
/*  62 */       throw new IllegalStateException("DBAppender cannot function if the JDBC driver does not support getGeneratedKeys method *and* without a specific SQL dialect");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  67 */     super.start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ConnectionSource getConnectionSource()
/*     */   {
/*  74 */     return this.connectionSource;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectionSource(ConnectionSource connectionSource)
/*     */   {
/*  82 */     this.connectionSource = connectionSource;
/*     */   }
/*     */   
/*     */   public void append(E eventObject)
/*     */   {
/*  87 */     Connection connection = null;
/*  88 */     PreparedStatement insertStatement = null;
/*     */     try {
/*  90 */       connection = this.connectionSource.getConnection();
/*  91 */       connection.setAutoCommit(false);
/*     */       
/*  93 */       if (this.cnxSupportsGetGeneratedKeys) {
/*  94 */         String EVENT_ID_COL_NAME = "EVENT_ID";
/*     */         
/*  96 */         if (this.connectionSource.getSQLDialectCode() == SQLDialectCode.POSTGRES_DIALECT) {
/*  97 */           EVENT_ID_COL_NAME = EVENT_ID_COL_NAME.toLowerCase();
/*     */         }
/*  99 */         insertStatement = connection.prepareStatement(getInsertSQL(), new String[] { EVENT_ID_COL_NAME });
/*     */       }
/*     */       else {
/* 102 */         insertStatement = connection.prepareStatement(getInsertSQL());
/*     */       }
/*     */       
/*     */       long eventId;
/*     */       
/* 107 */       synchronized (this) {
/* 108 */         subAppend(eventObject, connection, insertStatement);
/* 109 */         eventId = selectEventId(insertStatement, connection);
/*     */       }
/* 111 */       secondarySubAppend(eventObject, connection, eventId);
/*     */       
/* 113 */       connection.commit();
/*     */     } catch (Throwable sqle) {
/* 115 */       addError("problem appending event", sqle);
/*     */     } finally {
/* 117 */       DBHelper.closeStatement(insertStatement);
/* 118 */       DBHelper.closeConnection(connection);
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract void subAppend(E paramE, Connection paramConnection, PreparedStatement paramPreparedStatement)
/*     */     throws Throwable;
/*     */   
/*     */   protected abstract void secondarySubAppend(E paramE, Connection paramConnection, long paramLong)
/*     */     throws Throwable;
/*     */   
/*     */   protected long selectEventId(PreparedStatement insertStatement, Connection connection) throws SQLException, InvocationTargetException
/*     */   {
/* 130 */     ResultSet rs = null;
/* 131 */     Statement idStatement = null;
/*     */     try {
/* 133 */       boolean gotGeneratedKeys = false;
/* 134 */       if (this.cnxSupportsGetGeneratedKeys) {
/*     */         try {
/* 136 */           rs = (ResultSet)getGeneratedKeysMethod().invoke(insertStatement, (Object[])null);
/*     */           
/* 138 */           gotGeneratedKeys = true;
/*     */         } catch (InvocationTargetException ex) {
/* 140 */           Throwable target = ex.getTargetException();
/* 141 */           if ((target instanceof SQLException)) {
/* 142 */             throw ((SQLException)target);
/*     */           }
/* 144 */           throw ex;
/*     */         } catch (IllegalAccessException ex) {
/* 146 */           addWarn("IllegalAccessException invoking PreparedStatement.getGeneratedKeys", ex);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 152 */       if (!gotGeneratedKeys) {
/* 153 */         idStatement = connection.createStatement();
/* 154 */         idStatement.setMaxRows(1);
/* 155 */         String selectInsertIdStr = this.sqlDialect.getSelectInsertId();
/* 156 */         rs = idStatement.executeQuery(selectInsertIdStr);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 161 */       rs.next();
/* 162 */       long eventId = rs.getLong(1);
/* 163 */       return eventId;
/*     */     } finally {
/* 165 */       if (rs != null) {
/*     */         try {
/* 167 */           rs.close();
/*     */         }
/*     */         catch (SQLException e) {}
/*     */       }
/* 171 */       DBHelper.closeStatement(idStatement);
/*     */     }
/*     */   }
/*     */   
/*     */   public void stop()
/*     */   {
/* 177 */     super.stop();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\db\DBAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */