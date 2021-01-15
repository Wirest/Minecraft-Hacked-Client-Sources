package ch.qos.logback.classic.db.names;

public abstract interface DBNameResolver
{
  public abstract <N extends Enum<?>> String getTableName(N paramN);
  
  public abstract <N extends Enum<?>> String getColumnName(N paramN);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\db\names\DBNameResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */