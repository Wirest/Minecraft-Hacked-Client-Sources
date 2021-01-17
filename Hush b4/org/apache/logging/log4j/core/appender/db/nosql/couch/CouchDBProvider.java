// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.nosql.couch;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLConnection;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.lang.reflect.Method;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.lightcouch.CouchDbProperties;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.lightcouch.CouchDbClient;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLProvider;

@Plugin(name = "CouchDb", category = "Core", printObject = true)
public final class CouchDBProvider implements NoSQLProvider<CouchDBConnection>
{
    private static final int HTTP = 80;
    private static final int HTTPS = 443;
    private static final Logger LOGGER;
    private final CouchDbClient client;
    private final String description;
    
    private CouchDBProvider(final CouchDbClient client, final String description) {
        this.client = client;
        this.description = "couchDb{ " + description + " }";
    }
    
    @Override
    public CouchDBConnection getConnection() {
        return new CouchDBConnection(this.client);
    }
    
    @Override
    public String toString() {
        return this.description;
    }
    
    @PluginFactory
    public static CouchDBProvider createNoSQLProvider(@PluginAttribute("databaseName") final String databaseName, @PluginAttribute("protocol") String protocol, @PluginAttribute("server") String server, @PluginAttribute("port") final String port, @PluginAttribute("username") final String username, @PluginAttribute("password") final String password, @PluginAttribute("factoryClassName") final String factoryClassName, @PluginAttribute("factoryMethodName") final String factoryMethodName) {
        if (factoryClassName != null && factoryClassName.length() > 0 && factoryMethodName != null && factoryMethodName.length() > 0) {
            try {
                final Class<?> factoryClass = Class.forName(factoryClassName);
                final Method method = factoryClass.getMethod(factoryMethodName, (Class<?>[])new Class[0]);
                final Object object = method.invoke(null, new Object[0]);
                CouchDbClient client;
                String description;
                if (object instanceof CouchDbClient) {
                    client = (CouchDbClient)object;
                    description = "uri=" + client.getDBUri();
                }
                else if (object instanceof CouchDbProperties) {
                    final CouchDbProperties properties = (CouchDbProperties)object;
                    client = new CouchDbClient(properties);
                    description = "uri=" + client.getDBUri() + ", username=" + properties.getUsername() + ", passwordHash=" + NameUtil.md5(password + CouchDBProvider.class.getName()) + ", maxConnections=" + properties.getMaxConnections() + ", connectionTimeout=" + properties.getConnectionTimeout() + ", socketTimeout=" + properties.getSocketTimeout();
                }
                else {
                    if (object == null) {
                        CouchDBProvider.LOGGER.error("The factory method [{}.{}()] returned null.", factoryClassName, factoryMethodName);
                        return null;
                    }
                    CouchDBProvider.LOGGER.error("The factory method [{}.{}()] returned an unsupported type [{}].", factoryClassName, factoryMethodName, object.getClass().getName());
                    return null;
                }
                return new CouchDBProvider(client, description);
            }
            catch (ClassNotFoundException e) {
                CouchDBProvider.LOGGER.error("The factory class [{}] could not be loaded.", factoryClassName, e);
                return null;
            }
            catch (NoSuchMethodException e2) {
                CouchDBProvider.LOGGER.error("The factory class [{}] does not have a no-arg method named [{}].", factoryClassName, factoryMethodName, e2);
                return null;
            }
            catch (Exception e3) {
                CouchDBProvider.LOGGER.error("The factory method [{}.{}()] could not be invoked.", factoryClassName, factoryMethodName, e3);
                return null;
            }
        }
        if (databaseName == null || databaseName.length() <= 0) {
            CouchDBProvider.LOGGER.error("No factory method was provided so the database name is required.");
            return null;
        }
        if (protocol != null && protocol.length() > 0) {
            protocol = protocol.toLowerCase();
            if (!protocol.equals("http") && !protocol.equals("https")) {
                CouchDBProvider.LOGGER.error("Only protocols [http] and [https] are supported, [{}] specified.", protocol);
                return null;
            }
        }
        else {
            protocol = "http";
            CouchDBProvider.LOGGER.warn("No protocol specified, using default port [http].");
        }
        final int portInt = AbstractAppender.parseInt(port, protocol.equals("https") ? 443 : 80);
        if (Strings.isEmpty(server)) {
            server = "localhost";
            CouchDBProvider.LOGGER.warn("No server specified, using default server localhost.");
        }
        if (Strings.isEmpty(username) || Strings.isEmpty(password)) {
            CouchDBProvider.LOGGER.error("You must provide a username and password for the CouchDB provider.");
            return null;
        }
        CouchDbClient client = new CouchDbClient(databaseName, false, protocol, server, portInt, username, password);
        String description = "uri=" + client.getDBUri() + ", username=" + username + ", passwordHash=" + NameUtil.md5(password + CouchDBProvider.class.getName());
        return new CouchDBProvider(client, description);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
