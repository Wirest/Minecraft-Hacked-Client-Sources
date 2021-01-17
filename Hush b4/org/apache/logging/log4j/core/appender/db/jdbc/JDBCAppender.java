// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;

@Plugin(name = "JDBC", category = "Core", elementType = "appender", printObject = true)
public final class JDBCAppender extends AbstractDatabaseAppender<JDBCDatabaseManager>
{
    private final String description;
    
    private JDBCAppender(final String name, final Filter filter, final boolean ignoreExceptions, final JDBCDatabaseManager manager) {
        super(name, filter, ignoreExceptions, manager);
        this.description = this.getName() + "{ manager=" + ((AbstractDatabaseAppender<Object>)this).getManager() + " }";
    }
    
    @Override
    public String toString() {
        return this.description;
    }
    
    @PluginFactory
    public static JDBCAppender createAppender(@PluginAttribute("name") final String name, @PluginAttribute("ignoreExceptions") final String ignore, @PluginElement("Filter") final Filter filter, @PluginElement("ConnectionSource") final ConnectionSource connectionSource, @PluginAttribute("bufferSize") final String bufferSize, @PluginAttribute("tableName") final String tableName, @PluginElement("ColumnConfigs") final ColumnConfig[] columnConfigs) {
        final int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final StringBuilder managerName = new StringBuilder("jdbcManager{ description=").append(name).append(", bufferSize=").append(bufferSizeInt).append(", connectionSource=").append(connectionSource.toString()).append(", tableName=").append(tableName).append(", columns=[ ");
        int i = 0;
        for (final ColumnConfig column : columnConfigs) {
            if (i++ > 0) {
                managerName.append(", ");
            }
            managerName.append(column.toString());
        }
        managerName.append(" ] }");
        final JDBCDatabaseManager manager = JDBCDatabaseManager.getJDBCDatabaseManager(managerName.toString(), bufferSizeInt, connectionSource, tableName, columnConfigs);
        if (manager == null) {
            return null;
        }
        return new JDBCAppender(name, filter, ignoreExceptions, manager);
    }
}
