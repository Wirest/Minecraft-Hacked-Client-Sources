// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jdbc;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.sql.SQLException;
import java.sql.Connection;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ConnectionFactory", category = "Core", elementType = "connectionSource", printObject = true)
public final class FactoryMethodConnectionSource implements ConnectionSource
{
    private static final Logger LOGGER;
    private final DataSource dataSource;
    private final String description;
    
    private FactoryMethodConnectionSource(final DataSource dataSource, final String className, final String methodName, final String returnType) {
        this.dataSource = dataSource;
        this.description = "factory{ public static " + returnType + " " + className + "." + methodName + "() }";
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
    public static FactoryMethodConnectionSource createConnectionSource(@PluginAttribute("class") final String className, @PluginAttribute("method") final String methodName) {
        if (Strings.isEmpty(className) || Strings.isEmpty(methodName)) {
            FactoryMethodConnectionSource.LOGGER.error("No class name or method name specified for the connection factory method.");
            return null;
        }
        Method method;
        try {
            final Class<?> factoryClass = Class.forName(className);
            method = factoryClass.getMethod(methodName, (Class<?>[])new Class[0]);
        }
        catch (Exception e) {
            FactoryMethodConnectionSource.LOGGER.error(e.toString(), e);
            return null;
        }
        final Class<?> returnType = method.getReturnType();
        String returnTypeString = returnType.getName();
        if (returnType == DataSource.class) {
            try {
                final DataSource dataSource = (DataSource)method.invoke(null, new Object[0]);
                returnTypeString = returnTypeString + "[" + dataSource + "]";
                return new FactoryMethodConnectionSource(dataSource, className, methodName, returnTypeString);
            }
            catch (Exception e2) {
                FactoryMethodConnectionSource.LOGGER.error(e2.toString(), e2);
                return null;
            }
        }
        if (returnType != Connection.class) {
            FactoryMethodConnectionSource.LOGGER.error("Method [{}.{}()] returns unsupported type [{}].", className, methodName, returnType.getName());
            return null;
        }
        final DataSource dataSource = new DataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                try {
                    return (Connection)method.invoke(null, new Object[0]);
                }
                catch (Exception e) {
                    throw new SQLException("Failed to obtain connection from factory method.", e);
                }
            }
            
            @Override
            public Connection getConnection(final String username, final String password) throws SQLException {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public int getLoginTimeout() throws SQLException {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public PrintWriter getLogWriter() throws SQLException {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public java.util.logging.Logger getParentLogger() {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public boolean isWrapperFor(final Class<?> iface) throws SQLException {
                return false;
            }
            
            @Override
            public void setLoginTimeout(final int seconds) throws SQLException {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void setLogWriter(final PrintWriter out) throws SQLException {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public <T> T unwrap(final Class<T> iface) throws SQLException {
                return null;
            }
        };
        return new FactoryMethodConnectionSource(dataSource, className, methodName, returnTypeString);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
