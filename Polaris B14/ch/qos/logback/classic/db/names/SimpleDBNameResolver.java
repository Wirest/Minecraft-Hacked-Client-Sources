/*    */ package ch.qos.logback.classic.db.names;
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
/*    */ public class SimpleDBNameResolver
/*    */   implements DBNameResolver
/*    */ {
/* 24 */   private String tableNamePrefix = "";
/*    */   
/* 26 */   private String tableNameSuffix = "";
/*    */   
/* 28 */   private String columnNamePrefix = "";
/*    */   
/* 30 */   private String columnNameSuffix = "";
/*    */   
/*    */   public <N extends Enum<?>> String getTableName(N tableName) {
/* 33 */     return this.tableNamePrefix + tableName.name().toLowerCase() + this.tableNameSuffix;
/*    */   }
/*    */   
/*    */   public <N extends Enum<?>> String getColumnName(N columnName) {
/* 37 */     return this.columnNamePrefix + columnName.name().toLowerCase() + this.columnNameSuffix;
/*    */   }
/*    */   
/*    */   public void setTableNamePrefix(String tableNamePrefix) {
/* 41 */     this.tableNamePrefix = (tableNamePrefix != null ? tableNamePrefix : "");
/*    */   }
/*    */   
/*    */   public void setTableNameSuffix(String tableNameSuffix) {
/* 45 */     this.tableNameSuffix = (tableNameSuffix != null ? tableNameSuffix : "");
/*    */   }
/*    */   
/*    */   public void setColumnNamePrefix(String columnNamePrefix) {
/* 49 */     this.columnNamePrefix = (columnNamePrefix != null ? columnNamePrefix : "");
/*    */   }
/*    */   
/*    */   public void setColumnNameSuffix(String columnNameSuffix) {
/* 53 */     this.columnNameSuffix = (columnNameSuffix != null ? columnNameSuffix : "");
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\db\names\SimpleDBNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */