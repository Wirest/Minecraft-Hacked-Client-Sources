// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.nosql.mongo;

import org.bson.BSON;
import org.bson.Transformer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusLogger;
import com.mongodb.WriteResult;
import com.mongodb.MongoException;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import com.mongodb.DBObject;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;
import com.mongodb.DB;
import com.mongodb.WriteConcern;
import com.mongodb.Mongo;
import com.mongodb.DBCollection;
import org.apache.logging.log4j.Logger;
import com.mongodb.BasicDBObject;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLConnection;

public final class MongoDBConnection implements NoSQLConnection<BasicDBObject, MongoDBObject>
{
    private static final Logger LOGGER;
    private final DBCollection collection;
    private final Mongo mongo;
    private final WriteConcern writeConcern;
    
    public MongoDBConnection(final DB database, final WriteConcern writeConcern, final String collectionName) {
        this.mongo = database.getMongo();
        this.collection = database.getCollection(collectionName);
        this.writeConcern = writeConcern;
    }
    
    @Override
    public MongoDBObject createObject() {
        return new MongoDBObject();
    }
    
    @Override
    public MongoDBObject[] createList(final int length) {
        return new MongoDBObject[length];
    }
    
    @Override
    public void insertObject(final NoSQLObject<BasicDBObject> object) {
        try {
            final WriteResult result = this.collection.insert((DBObject)object.unwrap(), this.writeConcern);
            if (result.getError() != null && result.getError().length() > 0) {
                throw new AppenderLoggingException("Failed to write log event to MongoDB due to error: " + result.getError() + ".");
            }
        }
        catch (MongoException e) {
            throw new AppenderLoggingException("Failed to write log event to MongoDB due to error: " + e.getMessage(), (Throwable)e);
        }
    }
    
    @Override
    public void close() {
        this.mongo.close();
    }
    
    @Override
    public boolean isClosed() {
        return !this.mongo.getConnector().isOpen();
    }
    
    static void authenticate(final DB database, final String username, final String password) {
        try {
            if (!database.authenticate(username, password.toCharArray())) {
                MongoDBConnection.LOGGER.error("Failed to authenticate against MongoDB server. Unknown error.");
            }
        }
        catch (MongoException e) {
            MongoDBConnection.LOGGER.error("Failed to authenticate against MongoDB: " + e.getMessage(), (Throwable)e);
        }
        catch (IllegalStateException e2) {
            MongoDBConnection.LOGGER.error("Factory-supplied MongoDB database connection already authenticated with differentcredentials but lost connection.");
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        BSON.addDecodingHook((Class)Level.class, (Transformer)new Transformer() {
            public Object transform(final Object o) {
                if (o instanceof Level) {
                    return ((Level)o).name();
                }
                return o;
            }
        });
    }
}
