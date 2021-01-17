// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.nosql;

import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.ThreadContext;
import java.util.Iterator;
import org.apache.logging.log4j.Marker;
import java.util.Map;
import java.util.Date;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;

public final class NoSQLDatabaseManager<W> extends AbstractDatabaseManager
{
    private static final NoSQLDatabaseManagerFactory FACTORY;
    private final NoSQLProvider<NoSQLConnection<W, ? extends NoSQLObject<W>>> provider;
    private NoSQLConnection<W, ? extends NoSQLObject<W>> connection;
    
    private NoSQLDatabaseManager(final String name, final int bufferSize, final NoSQLProvider<NoSQLConnection<W, ? extends NoSQLObject<W>>> provider) {
        super(name, bufferSize);
        this.provider = provider;
    }
    
    @Override
    protected void connectInternal() {
        this.connection = this.provider.getConnection();
    }
    
    @Override
    protected void disconnectInternal() {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }
    
    @Override
    protected void writeInternal(final LogEvent event) {
        if (!this.isConnected() || this.connection == null || this.connection.isClosed()) {
            throw new AppenderLoggingException("Cannot write logging event; NoSQL manager not connected to the database.");
        }
        final NoSQLObject<W> entity = (NoSQLObject<W>)this.connection.createObject();
        entity.set("level", event.getLevel());
        entity.set("loggerName", event.getLoggerName());
        entity.set("message", (event.getMessage() == null) ? null : event.getMessage().getFormattedMessage());
        final StackTraceElement source = event.getSource();
        if (source == null) {
            entity.set("source", (Object)null);
        }
        else {
            entity.set("source", this.convertStackTraceElement(source));
        }
        Marker marker = event.getMarker();
        if (marker == null) {
            entity.set("marker", (Object)null);
        }
        else {
            NoSQLObject<W> markerEntity;
            final NoSQLObject<W> originalMarkerEntity = markerEntity = (NoSQLObject<W>)this.connection.createObject();
            markerEntity.set("name", marker.getName());
            while (marker.getParent() != null) {
                marker = marker.getParent();
                final NoSQLObject<W> parentMarkerEntity = (NoSQLObject<W>)this.connection.createObject();
                parentMarkerEntity.set("name", marker.getName());
                markerEntity.set("parent", parentMarkerEntity);
                markerEntity = parentMarkerEntity;
            }
            entity.set("marker", originalMarkerEntity);
        }
        entity.set("threadName", event.getThreadName());
        entity.set("millis", event.getMillis());
        entity.set("date", new Date(event.getMillis()));
        Throwable thrown = event.getThrown();
        if (thrown == null) {
            entity.set("thrown", (Object)null);
        }
        else {
            NoSQLObject<W> exceptionEntity;
            final NoSQLObject<W> originalExceptionEntity = exceptionEntity = (NoSQLObject<W>)this.connection.createObject();
            exceptionEntity.set("type", thrown.getClass().getName());
            exceptionEntity.set("message", thrown.getMessage());
            exceptionEntity.set("stackTrace", this.convertStackTrace(thrown.getStackTrace()));
            while (thrown.getCause() != null) {
                thrown = thrown.getCause();
                final NoSQLObject<W> causingExceptionEntity = (NoSQLObject<W>)this.connection.createObject();
                causingExceptionEntity.set("type", thrown.getClass().getName());
                causingExceptionEntity.set("message", thrown.getMessage());
                causingExceptionEntity.set("stackTrace", this.convertStackTrace(thrown.getStackTrace()));
                exceptionEntity.set("cause", causingExceptionEntity);
                exceptionEntity = causingExceptionEntity;
            }
            entity.set("thrown", originalExceptionEntity);
        }
        final Map<String, String> contextMap = event.getContextMap();
        if (contextMap == null) {
            entity.set("contextMap", (Object)null);
        }
        else {
            final NoSQLObject<W> contextMapEntity = (NoSQLObject<W>)this.connection.createObject();
            for (final Map.Entry<String, String> entry : contextMap.entrySet()) {
                contextMapEntity.set(entry.getKey(), entry.getValue());
            }
            entity.set("contextMap", contextMapEntity);
        }
        final ThreadContext.ContextStack contextStack = event.getContextStack();
        if (contextStack == null) {
            entity.set("contextStack", (Object)null);
        }
        else {
            entity.set("contextStack", contextStack.asList().toArray());
        }
        this.connection.insertObject(entity);
    }
    
    private NoSQLObject<W>[] convertStackTrace(final StackTraceElement[] stackTrace) {
        final NoSQLObject<W>[] stackTraceEntities = (NoSQLObject<W>[])this.connection.createList(stackTrace.length);
        for (int i = 0; i < stackTrace.length; ++i) {
            stackTraceEntities[i] = this.convertStackTraceElement(stackTrace[i]);
        }
        return stackTraceEntities;
    }
    
    private NoSQLObject<W> convertStackTraceElement(final StackTraceElement element) {
        final NoSQLObject<W> elementEntity = (NoSQLObject<W>)this.connection.createObject();
        elementEntity.set("className", element.getClassName());
        elementEntity.set("methodName", element.getMethodName());
        elementEntity.set("fileName", element.getFileName());
        elementEntity.set("lineNumber", element.getLineNumber());
        return elementEntity;
    }
    
    public static NoSQLDatabaseManager<?> getNoSQLDatabaseManager(final String name, final int bufferSize, final NoSQLProvider<?> provider) {
        return AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, provider), (ManagerFactory<NoSQLDatabaseManager<?>, FactoryData>)NoSQLDatabaseManager.FACTORY);
    }
    
    static {
        FACTORY = new NoSQLDatabaseManagerFactory();
    }
    
    private static final class FactoryData extends AbstractFactoryData
    {
        private final NoSQLProvider<?> provider;
        
        protected FactoryData(final int bufferSize, final NoSQLProvider<?> provider) {
            super(bufferSize);
            this.provider = provider;
        }
    }
    
    private static final class NoSQLDatabaseManagerFactory implements ManagerFactory<NoSQLDatabaseManager<?>, FactoryData>
    {
        @Override
        public NoSQLDatabaseManager<?> createManager(final String name, final FactoryData data) {
            return new NoSQLDatabaseManager<Object>(name, data.getBufferSize(), data.provider, null);
        }
    }
}
