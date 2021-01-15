package ch.qos.logback.core.db;

import ch.qos.logback.core.db.dialect.SQLDialectCode;
import ch.qos.logback.core.spi.LifeCycle;
import java.sql.Connection;
import java.sql.SQLException;

public abstract interface ConnectionSource
  extends LifeCycle
{
  public abstract Connection getConnection()
    throws SQLException;
  
  public abstract SQLDialectCode getSQLDialectCode();
  
  public abstract boolean supportsGetGeneratedKeys();
  
  public abstract boolean supportsBatchUpdates();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\db\ConnectionSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */