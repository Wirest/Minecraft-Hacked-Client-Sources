// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.nosql.couch;

import org.lightcouch.Response;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;
import org.lightcouch.CouchDbClient;
import java.util.Map;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLConnection;

public final class CouchDBConnection implements NoSQLConnection<Map<String, Object>, CouchDBObject>
{
    private final CouchDbClient client;
    private boolean closed;
    
    public CouchDBConnection(final CouchDbClient client) {
        this.closed = false;
        this.client = client;
    }
    
    @Override
    public CouchDBObject createObject() {
        return new CouchDBObject();
    }
    
    @Override
    public CouchDBObject[] createList(final int length) {
        return new CouchDBObject[length];
    }
    
    @Override
    public void insertObject(final NoSQLObject<Map<String, Object>> object) {
        try {
            final Response response = this.client.save((Object)object.unwrap());
            if (response.getError() != null && response.getError().length() > 0) {
                throw new AppenderLoggingException("Failed to write log event to CouchDB due to error: " + response.getError() + ".");
            }
        }
        catch (Exception e) {
            throw new AppenderLoggingException("Failed to write log event to CouchDB due to error: " + e.getMessage(), e);
        }
    }
    
    @Override
    public synchronized void close() {
        this.closed = true;
        this.client.shutdown();
    }
    
    @Override
    public synchronized boolean isClosed() {
        return this.closed;
    }
}
