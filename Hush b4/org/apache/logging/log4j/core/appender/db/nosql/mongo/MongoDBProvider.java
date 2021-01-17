// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.nosql.mongo;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLConnection;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.lang.reflect.Method;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import com.mongodb.ServerAddress;
import com.mongodb.MongoClient;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLProvider;

@Plugin(name = "MongoDb", category = "Core", printObject = true)
public final class MongoDBProvider implements NoSQLProvider<MongoDBConnection>
{
    private static final Logger LOGGER;
    private final String collectionName;
    private final DB database;
    private final String description;
    private final WriteConcern writeConcern;
    
    private MongoDBProvider(final DB database, final WriteConcern writeConcern, final String collectionName, final String description) {
        this.database = database;
        this.writeConcern = writeConcern;
        this.collectionName = collectionName;
        this.description = "mongoDb{ " + description + " }";
    }
    
    @Override
    public MongoDBConnection getConnection() {
        return new MongoDBConnection(this.database, this.writeConcern, this.collectionName);
    }
    
    @Override
    public String toString() {
        return this.description;
    }
    
    @PluginFactory
    public static MongoDBProvider createNoSQLProvider(@PluginAttribute("collectionName") final String collectionName, @PluginAttribute("writeConcernConstant") final String writeConcernConstant, @PluginAttribute("writeConcernConstantClass") final String writeConcernConstantClassName, @PluginAttribute("databaseName") final String databaseName, @PluginAttribute("server") final String server, @PluginAttribute("port") final String port, @PluginAttribute("username") final String username, @PluginAttribute("password") final String password, @PluginAttribute("factoryClassName") final String factoryClassName, @PluginAttribute("factoryMethodName") final String factoryMethodName) {
        DB database = null;
        String description = null;
        Label_0742: {
            if (factoryClassName != null && factoryClassName.length() > 0 && factoryMethodName != null && factoryMethodName.length() > 0) {
                try {
                    final Class<?> factoryClass = Class.forName(factoryClassName);
                    final Method method = factoryClass.getMethod(factoryMethodName, (Class<?>[])new Class[0]);
                    final Object object = method.invoke(null, new Object[0]);
                    if (object instanceof DB) {
                        database = (DB)object;
                    }
                    else if (object instanceof MongoClient) {
                        if (databaseName == null || databaseName.length() <= 0) {
                            MongoDBProvider.LOGGER.error("The factory method [{}.{}()] returned a MongoClient so the database name is required.", factoryClassName, factoryMethodName);
                            return null;
                        }
                        database = ((MongoClient)object).getDB(databaseName);
                    }
                    else {
                        if (object == null) {
                            MongoDBProvider.LOGGER.error("The factory method [{}.{}()] returned null.", factoryClassName, factoryMethodName);
                            return null;
                        }
                        MongoDBProvider.LOGGER.error("The factory method [{}.{}()] returned an unsupported type [{}].", factoryClassName, factoryMethodName, object.getClass().getName());
                        return null;
                    }
                    description = "database=" + database.getName();
                    final List<ServerAddress> addresses = (List<ServerAddress>)database.getMongo().getAllAddress();
                    if (addresses.size() == 1) {
                        description = description + ", server=" + addresses.get(0).getHost() + ", port=" + addresses.get(0).getPort();
                    }
                    else {
                        description += ", servers=[";
                        for (final ServerAddress address : addresses) {
                            description = description + " { " + address.getHost() + ", " + address.getPort() + " } ";
                        }
                        description += "]";
                    }
                    break Label_0742;
                }
                catch (ClassNotFoundException e) {
                    MongoDBProvider.LOGGER.error("The factory class [{}] could not be loaded.", factoryClassName, e);
                    return null;
                }
                catch (NoSuchMethodException e2) {
                    MongoDBProvider.LOGGER.error("The factory class [{}] does not have a no-arg method named [{}].", factoryClassName, factoryMethodName, e2);
                    return null;
                }
                catch (Exception e3) {
                    MongoDBProvider.LOGGER.error("The factory method [{}.{}()] could not be invoked.", factoryClassName, factoryMethodName, e3);
                    return null;
                }
            }
            if (databaseName != null && databaseName.length() > 0) {
                description = "database=" + databaseName;
                try {
                    if (server != null && server.length() > 0) {
                        final int portInt = AbstractAppender.parseInt(port, 0);
                        description = description + ", server=" + server;
                        if (portInt > 0) {
                            description = description + ", port=" + portInt;
                            database = new MongoClient(server, portInt).getDB(databaseName);
                        }
                        else {
                            database = new MongoClient(server).getDB(databaseName);
                        }
                    }
                    else {
                        database = new MongoClient().getDB(databaseName);
                    }
                    break Label_0742;
                }
                catch (Exception e3) {
                    MongoDBProvider.LOGGER.error("Failed to obtain a database instance from the MongoClient at server [{}] and port [{}].", server, port);
                    return null;
                }
            }
            MongoDBProvider.LOGGER.error("No factory method was provided so the database name is required.");
            return null;
        }
        if (!database.isAuthenticated()) {
            if (username == null || username.length() <= 0 || password == null || password.length() <= 0) {
                MongoDBProvider.LOGGER.error("The database is not already authenticated so you must supply a username and password for the MongoDB provider.");
                return null;
            }
            description = description + ", username=" + username + ", passwordHash=" + NameUtil.md5(password + MongoDBProvider.class.getName());
            MongoDBConnection.authenticate(database, username, password);
        }
        WriteConcern writeConcern;
        if (writeConcernConstant != null && writeConcernConstant.length() > 0) {
            if (writeConcernConstantClassName != null && writeConcernConstantClassName.length() > 0) {
                try {
                    final Class<?> writeConcernConstantClass = Class.forName(writeConcernConstantClassName);
                    final Field field = writeConcernConstantClass.getField(writeConcernConstant);
                    writeConcern = (WriteConcern)field.get(null);
                }
                catch (Exception e4) {
                    MongoDBProvider.LOGGER.error("Write concern constant [{}.{}] not found, using default.", writeConcernConstantClassName, writeConcernConstant);
                    writeConcern = WriteConcern.ACKNOWLEDGED;
                }
            }
            else {
                writeConcern = WriteConcern.valueOf(writeConcernConstant);
                if (writeConcern == null) {
                    MongoDBProvider.LOGGER.warn("Write concern constant [{}] not found, using default.", writeConcernConstant);
                    writeConcern = WriteConcern.ACKNOWLEDGED;
                }
            }
        }
        else {
            writeConcern = WriteConcern.ACKNOWLEDGED;
        }
        return new MongoDBProvider(database, writeConcern, collectionName, description);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
