/*     */ package ch.qos.logback.classic.db;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.db.names.DBNameResolver;
/*     */ import ch.qos.logback.classic.db.names.DefaultDBNameResolver;
/*     */ import ch.qos.logback.classic.spi.CallerData;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.classic.spi.IThrowableProxy;
/*     */ import ch.qos.logback.classic.spi.LoggerContextVO;
/*     */ import ch.qos.logback.classic.spi.StackTraceElementProxy;
/*     */ import ch.qos.logback.classic.spi.ThrowableProxyUtil;
/*     */ import ch.qos.logback.core.db.DBAppenderBase;
/*     */ import java.lang.reflect.Method;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class DBAppender
/*     */   extends DBAppenderBase<ILoggingEvent>
/*     */ {
/*     */   protected String insertPropertiesSQL;
/*     */   protected String insertExceptionSQL;
/*     */   protected String insertSQL;
/*     */   protected static final Method GET_GENERATED_KEYS_METHOD;
/*     */   private DBNameResolver dbNameResolver;
/*     */   static final int TIMESTMP_INDEX = 1;
/*     */   static final int FORMATTED_MESSAGE_INDEX = 2;
/*     */   static final int LOGGER_NAME_INDEX = 3;
/*     */   static final int LEVEL_STRING_INDEX = 4;
/*     */   static final int THREAD_NAME_INDEX = 5;
/*     */   static final int REFERENCE_FLAG_INDEX = 6;
/*     */   static final int ARG0_INDEX = 7;
/*     */   static final int ARG1_INDEX = 8;
/*     */   static final int ARG2_INDEX = 9;
/*     */   static final int ARG3_INDEX = 10;
/*     */   static final int CALLER_FILENAME_INDEX = 11;
/*     */   static final int CALLER_CLASS_INDEX = 12;
/*     */   static final int CALLER_METHOD_INDEX = 13;
/*     */   static final int CALLER_LINE_INDEX = 14;
/*     */   static final int EVENT_ID_INDEX = 15;
/*  67 */   static final StackTraceElement EMPTY_CALLER_DATA = ;
/*     */   
/*     */   static
/*     */   {
/*     */     Method getGeneratedKeysMethod;
/*     */     try
/*     */     {
/*  74 */       getGeneratedKeysMethod = PreparedStatement.class.getMethod("getGeneratedKeys", (Class[])null);
/*     */     }
/*     */     catch (Exception ex) {
/*  77 */       getGeneratedKeysMethod = null;
/*     */     }
/*  79 */     GET_GENERATED_KEYS_METHOD = getGeneratedKeysMethod;
/*     */   }
/*     */   
/*     */   public void setDbNameResolver(DBNameResolver dbNameResolver) {
/*  83 */     this.dbNameResolver = dbNameResolver;
/*     */   }
/*     */   
/*     */   public void start()
/*     */   {
/*  88 */     if (this.dbNameResolver == null)
/*  89 */       this.dbNameResolver = new DefaultDBNameResolver();
/*  90 */     this.insertExceptionSQL = SQLBuilder.buildInsertExceptionSQL(this.dbNameResolver);
/*  91 */     this.insertPropertiesSQL = SQLBuilder.buildInsertPropertiesSQL(this.dbNameResolver);
/*  92 */     this.insertSQL = SQLBuilder.buildInsertSQL(this.dbNameResolver);
/*  93 */     super.start();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void subAppend(ILoggingEvent event, Connection connection, PreparedStatement insertStatement)
/*     */     throws Throwable
/*     */   {
/* 100 */     bindLoggingEventWithInsertStatement(insertStatement, event);
/* 101 */     bindLoggingEventArgumentsWithPreparedStatement(insertStatement, event.getArgumentArray());
/*     */     
/*     */ 
/* 104 */     bindCallerDataWithPreparedStatement(insertStatement, event.getCallerData());
/*     */     
/* 106 */     int updateCount = insertStatement.executeUpdate();
/* 107 */     if (updateCount != 1) {
/* 108 */       addWarn("Failed to insert loggingEvent");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void secondarySubAppend(ILoggingEvent event, Connection connection, long eventId) throws Throwable
/*     */   {
/* 114 */     Map<String, String> mergedMap = mergePropertyMaps(event);
/* 115 */     insertProperties(mergedMap, connection, eventId);
/*     */     
/* 117 */     if (event.getThrowableProxy() != null) {
/* 118 */       insertThrowable(event.getThrowableProxy(), connection, eventId);
/*     */     }
/*     */   }
/*     */   
/*     */   void bindLoggingEventWithInsertStatement(PreparedStatement stmt, ILoggingEvent event) throws SQLException
/*     */   {
/* 124 */     stmt.setLong(1, event.getTimeStamp());
/* 125 */     stmt.setString(2, event.getFormattedMessage());
/* 126 */     stmt.setString(3, event.getLoggerName());
/* 127 */     stmt.setString(4, event.getLevel().toString());
/* 128 */     stmt.setString(5, event.getThreadName());
/* 129 */     stmt.setShort(6, DBHelper.computeReferenceMask(event));
/*     */   }
/*     */   
/*     */   void bindLoggingEventArgumentsWithPreparedStatement(PreparedStatement stmt, Object[] argArray)
/*     */     throws SQLException
/*     */   {
/* 135 */     int arrayLen = argArray != null ? argArray.length : 0;
/*     */     
/* 137 */     for (int i = 0; (i < arrayLen) && (i < 4); i++) {
/* 138 */       stmt.setString(7 + i, asStringTruncatedTo254(argArray[i]));
/*     */     }
/* 140 */     if (arrayLen < 4) {
/* 141 */       for (int i = arrayLen; i < 4; i++) {
/* 142 */         stmt.setString(7 + i, null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   String asStringTruncatedTo254(Object o) {
/* 148 */     String s = null;
/* 149 */     if (o != null) {
/* 150 */       s = o.toString();
/*     */     }
/*     */     
/* 153 */     if (s == null) {
/* 154 */       return null;
/*     */     }
/* 156 */     if (s.length() <= 254) {
/* 157 */       return s;
/*     */     }
/* 159 */     return s.substring(0, 254);
/*     */   }
/*     */   
/*     */ 
/*     */   void bindCallerDataWithPreparedStatement(PreparedStatement stmt, StackTraceElement[] callerDataArray)
/*     */     throws SQLException
/*     */   {
/* 166 */     StackTraceElement caller = extractFirstCaller(callerDataArray);
/*     */     
/* 168 */     stmt.setString(11, caller.getFileName());
/* 169 */     stmt.setString(12, caller.getClassName());
/* 170 */     stmt.setString(13, caller.getMethodName());
/* 171 */     stmt.setString(14, Integer.toString(caller.getLineNumber()));
/*     */   }
/*     */   
/*     */   private StackTraceElement extractFirstCaller(StackTraceElement[] callerDataArray) {
/* 175 */     StackTraceElement caller = EMPTY_CALLER_DATA;
/* 176 */     if (hasAtLeastOneNonNullElement(callerDataArray))
/* 177 */       caller = callerDataArray[0];
/* 178 */     return caller;
/*     */   }
/*     */   
/*     */   private boolean hasAtLeastOneNonNullElement(StackTraceElement[] callerDataArray) {
/* 182 */     return (callerDataArray != null) && (callerDataArray.length > 0) && (callerDataArray[0] != null);
/*     */   }
/*     */   
/*     */   Map<String, String> mergePropertyMaps(ILoggingEvent event) {
/* 186 */     Map<String, String> mergedMap = new HashMap();
/*     */     
/*     */ 
/*     */ 
/* 190 */     Map<String, String> loggerContextMap = event.getLoggerContextVO().getPropertyMap();
/*     */     
/* 192 */     Map<String, String> mdcMap = event.getMDCPropertyMap();
/* 193 */     if (loggerContextMap != null) {
/* 194 */       mergedMap.putAll(loggerContextMap);
/*     */     }
/* 196 */     if (mdcMap != null) {
/* 197 */       mergedMap.putAll(mdcMap);
/*     */     }
/*     */     
/* 200 */     return mergedMap;
/*     */   }
/*     */   
/*     */   protected Method getGeneratedKeysMethod()
/*     */   {
/* 205 */     return GET_GENERATED_KEYS_METHOD;
/*     */   }
/*     */   
/*     */   protected String getInsertSQL()
/*     */   {
/* 210 */     return this.insertSQL;
/*     */   }
/*     */   
/*     */   protected void insertProperties(Map<String, String> mergedMap, Connection connection, long eventId) throws SQLException
/*     */   {
/* 215 */     Set<String> propertiesKeys = mergedMap.keySet();
/* 216 */     if (propertiesKeys.size() > 0) {
/* 217 */       PreparedStatement insertPropertiesStatement = null;
/*     */       try {
/* 219 */         insertPropertiesStatement = connection.prepareStatement(this.insertPropertiesSQL);
/*     */         
/*     */ 
/* 222 */         for (String key : propertiesKeys) {
/* 223 */           String value = (String)mergedMap.get(key);
/*     */           
/* 225 */           insertPropertiesStatement.setLong(1, eventId);
/* 226 */           insertPropertiesStatement.setString(2, key);
/* 227 */           insertPropertiesStatement.setString(3, value);
/*     */           
/* 229 */           if (this.cnxSupportsBatchUpdates) {
/* 230 */             insertPropertiesStatement.addBatch();
/*     */           } else {
/* 232 */             insertPropertiesStatement.execute();
/*     */           }
/*     */         }
/*     */         
/* 236 */         if (this.cnxSupportsBatchUpdates) {
/* 237 */           insertPropertiesStatement.executeBatch();
/*     */         }
/*     */       } finally {
/* 240 */         ch.qos.logback.core.db.DBHelper.closeStatement(insertPropertiesStatement);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void updateExceptionStatement(PreparedStatement exceptionStatement, String txt, short i, long eventId)
/*     */     throws SQLException
/*     */   {
/* 251 */     exceptionStatement.setLong(1, eventId);
/* 252 */     exceptionStatement.setShort(2, i);
/* 253 */     exceptionStatement.setString(3, txt);
/* 254 */     if (this.cnxSupportsBatchUpdates) {
/* 255 */       exceptionStatement.addBatch();
/*     */     } else {
/* 257 */       exceptionStatement.execute();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   short buildExceptionStatement(IThrowableProxy tp, short baseIndex, PreparedStatement insertExceptionStatement, long eventId)
/*     */     throws SQLException
/*     */   {
/* 265 */     StringBuilder buf = new StringBuilder();
/* 266 */     ThrowableProxyUtil.subjoinFirstLine(buf, tp);
/* 267 */     baseIndex = (short)(baseIndex + 1);updateExceptionStatement(insertExceptionStatement, buf.toString(), baseIndex, eventId);
/*     */     
/*     */ 
/* 270 */     int commonFrames = tp.getCommonFrames();
/* 271 */     StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
/* 272 */     for (int i = 0; i < stepArray.length - commonFrames; i++) {
/* 273 */       StringBuilder sb = new StringBuilder();
/* 274 */       sb.append('\t');
/* 275 */       ThrowableProxyUtil.subjoinSTEP(sb, stepArray[i]);
/* 276 */       baseIndex = (short)(baseIndex + 1);updateExceptionStatement(insertExceptionStatement, sb.toString(), baseIndex, eventId);
/*     */     }
/*     */     
/*     */ 
/* 280 */     if (commonFrames > 0) {
/* 281 */       StringBuilder sb = new StringBuilder();
/* 282 */       sb.append('\t').append("... ").append(commonFrames).append(" common frames omitted");
/*     */       
/* 284 */       baseIndex = (short)(baseIndex + 1);updateExceptionStatement(insertExceptionStatement, sb.toString(), baseIndex, eventId);
/*     */     }
/*     */     
/*     */ 
/* 288 */     return baseIndex;
/*     */   }
/*     */   
/*     */   protected void insertThrowable(IThrowableProxy tp, Connection connection, long eventId)
/*     */     throws SQLException
/*     */   {
/* 294 */     PreparedStatement exceptionStatement = null;
/*     */     try {
/* 296 */       exceptionStatement = connection.prepareStatement(this.insertExceptionSQL);
/*     */       
/* 298 */       short baseIndex = 0;
/* 299 */       while (tp != null) {
/* 300 */         baseIndex = buildExceptionStatement(tp, baseIndex, exceptionStatement, eventId);
/*     */         
/* 302 */         tp = tp.getCause();
/*     */       }
/*     */       
/* 305 */       if (this.cnxSupportsBatchUpdates) {
/* 306 */         exceptionStatement.executeBatch();
/*     */       }
/*     */     } finally {
/* 309 */       ch.qos.logback.core.db.DBHelper.closeStatement(exceptionStatement);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\db\DBAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */