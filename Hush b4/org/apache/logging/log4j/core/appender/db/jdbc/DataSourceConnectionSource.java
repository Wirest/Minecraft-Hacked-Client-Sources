// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.sql.SQLException;
import java.sql.Connection;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "DataSource", category = "Core", elementType = "connectionSource", printObject = true)
public final class DataSourceConnectionSource implements ConnectionSource
{
    private static final Logger LOGGER;
    private final DataSource dataSource;
    private final String description;
    
    private DataSourceConnectionSource(final String dataSourceName, final DataSource dataSource) {
        this.dataSource = dataSource;
        this.description = "dataSource{ name=" + dataSourceName + ", value=" + dataSource + " }";
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
    
    @Override
    public String toString() {
        return this.description;
    }
    
    @PluginFactory
    public static DataSourceConnectionSource createConnectionSource(@PluginAttribute("jndiName") final String jndiName) {
        if (Strings.isEmpty(jndiName)) {
            DataSourceConnectionSource.LOGGER.error("No JNDI name provided.");
            return null;
        }
        try {
            final InitialContext context = new InitialContext();
            final DataSource dataSource = (DataSource)context.lookup(jndiName);
            if (dataSource == null) {
                DataSourceConnectionSource.LOGGER.error("No data source found with JNDI name [" + jndiName + "].");
                return null;
            }
            return new DataSourceConnectionSource(jndiName, dataSource);
        }
        catch (NamingException e) {
            DataSourceConnectionSource.LOGGER.error(e.getMessage(), e);
            return null;
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
