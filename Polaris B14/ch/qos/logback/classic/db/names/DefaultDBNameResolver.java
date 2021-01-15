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
/*    */ 
/*    */ public class DefaultDBNameResolver
/*    */   implements DBNameResolver
/*    */ {
/*    */   public <N extends Enum<?>> String getTableName(N tableName)
/*    */   {
/* 27 */     return tableName.toString().toLowerCase();
/*    */   }
/*    */   
/*    */   public <N extends Enum<?>> String getColumnName(N columnName) {
/* 31 */     return columnName.toString().toLowerCase();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\db\names\DefaultDBNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */