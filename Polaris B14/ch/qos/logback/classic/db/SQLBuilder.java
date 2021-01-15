/*    */ package ch.qos.logback.classic.db;
/*    */ 
/*    */ import ch.qos.logback.classic.db.names.ColumnName;
/*    */ import ch.qos.logback.classic.db.names.DBNameResolver;
/*    */ import ch.qos.logback.classic.db.names.TableName;
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
/*    */ public class SQLBuilder
/*    */ {
/*    */   static String buildInsertPropertiesSQL(DBNameResolver dbNameResolver)
/*    */   {
/* 25 */     StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
/* 26 */     sqlBuilder.append(dbNameResolver.getTableName(TableName.LOGGING_EVENT_PROPERTY)).append(" (");
/* 27 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.EVENT_ID)).append(", ");
/* 28 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.MAPPED_KEY)).append(", ");
/* 29 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.MAPPED_VALUE)).append(") ");
/* 30 */     sqlBuilder.append("VALUES (?, ?, ?)");
/* 31 */     return sqlBuilder.toString();
/*    */   }
/*    */   
/*    */   static String buildInsertExceptionSQL(DBNameResolver dbNameResolver) {
/* 35 */     StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
/* 36 */     sqlBuilder.append(dbNameResolver.getTableName(TableName.LOGGING_EVENT_EXCEPTION)).append(" (");
/* 37 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.EVENT_ID)).append(", ");
/* 38 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.I)).append(", ");
/* 39 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.TRACE_LINE)).append(") ");
/* 40 */     sqlBuilder.append("VALUES (?, ?, ?)");
/* 41 */     return sqlBuilder.toString();
/*    */   }
/*    */   
/*    */   static String buildInsertSQL(DBNameResolver dbNameResolver) {
/* 45 */     StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
/* 46 */     sqlBuilder.append(dbNameResolver.getTableName(TableName.LOGGING_EVENT)).append(" (");
/* 47 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.TIMESTMP)).append(", ");
/* 48 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.FORMATTED_MESSAGE)).append(", ");
/* 49 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.LOGGER_NAME)).append(", ");
/* 50 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.LEVEL_STRING)).append(", ");
/* 51 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.THREAD_NAME)).append(", ");
/* 52 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.REFERENCE_FLAG)).append(", ");
/* 53 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.ARG0)).append(", ");
/* 54 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.ARG1)).append(", ");
/* 55 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.ARG2)).append(", ");
/* 56 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.ARG3)).append(", ");
/* 57 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.CALLER_FILENAME)).append(", ");
/* 58 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.CALLER_CLASS)).append(", ");
/* 59 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.CALLER_METHOD)).append(", ");
/* 60 */     sqlBuilder.append(dbNameResolver.getColumnName(ColumnName.CALLER_LINE)).append(") ");
/* 61 */     sqlBuilder.append("VALUES (?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
/* 62 */     return sqlBuilder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\db\SQLBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */