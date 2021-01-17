// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.nosql;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseAppender;

@Plugin(name = "NoSql", category = "Core", elementType = "appender", printObject = true)
public final class NoSQLAppender extends AbstractDatabaseAppender<NoSQLDatabaseManager<?>>
{
    private final String description;
    
    private NoSQLAppender(final String name, final Filter filter, final boolean ignoreExceptions, final NoSQLDatabaseManager<?> manager) {
        super(name, filter, ignoreExceptions, manager);
        this.description = this.getName() + "{ manager=" + ((AbstractDatabaseAppender<Object>)this).getManager() + " }";
    }
    
    @Override
    public String toString() {
        return this.description;
    }
    
    @PluginFactory
    public static NoSQLAppender createAppender(@PluginAttribute("name") final String name, @PluginAttribute("ignoreExceptions") final String ignore, @PluginElement("Filter") final Filter filter, @PluginAttribute("bufferSize") final String bufferSize, @PluginElement("NoSqlProvider") final NoSQLProvider<?> provider) {
        if (provider == null) {
            NoSQLAppender.LOGGER.error("NoSQL provider not specified for appender [{}].", name);
            return null;
        }
        final int bufferSizeInt = AbstractAppender.parseInt(bufferSize, 0);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final String managerName = "noSqlManager{ description=" + name + ", bufferSize=" + bufferSizeInt + ", provider=" + provider + " }";
        final NoSQLDatabaseManager<?> manager = NoSQLDatabaseManager.getNoSQLDatabaseManager(managerName, bufferSizeInt, provider);
        if (manager == null) {
            return null;
        }
        return new NoSQLAppender(name, filter, ignoreExceptions, manager);
    }
}
