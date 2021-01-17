// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.sql.Driver;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "DriverManager", category = "Core", elementType = "connectionSource", printObject = true)
public final class DriverManagerConnectionSource implements ConnectionSource
{
    private static final Logger LOGGER;
    private final String databasePassword;
    private final String databaseUrl;
    private final String databaseUsername;
    private final String description;
    
    private DriverManagerConnectionSource(final String databaseUrl, final String databaseUsername, final String databasePassword) {
        this.databaseUrl = databaseUrl;
        this.databaseUsername = databaseUsername;
        this.databasePassword = databasePassword;
        this.description = "driverManager{ url=" + this.databaseUrl + ", username=" + this.databaseUsername + ", passwordHash=" + NameUtil.md5(this.databasePassword + this.getClass().getName()) + " }";
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        if (this.databaseUsername == null) {
            return DriverManager.getConnection(this.databaseUrl);
        }
        return DriverManager.getConnection(this.databaseUrl, this.databaseUsername, this.databasePassword);
    }
    
    @Override
    public String toString() {
        return this.description;
    }
    
    @PluginFactory
    public static DriverManagerConnectionSource createConnectionSource(@PluginAttribute("url") final String url, @PluginAttribute("username") String username, @PluginAttribute("password") String password) {
        if (Strings.isEmpty(url)) {
            DriverManagerConnectionSource.LOGGER.error("No JDBC URL specified for the database.");
            return null;
        }
        Driver driver;
        try {
            driver = DriverManager.getDriver(url);
        }
        catch (SQLException e) {
            DriverManagerConnectionSource.LOGGER.error("No matching driver found for database URL [" + url + "].", e);
            return null;
        }
        if (driver == null) {
            DriverManagerConnectionSource.LOGGER.error("No matching driver found for database URL [" + url + "].");
            return null;
        }
        if (username == null || username.trim().isEmpty()) {
            username = null;
            password = null;
        }
        return new DriverManagerConnectionSource(url, username, password);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
