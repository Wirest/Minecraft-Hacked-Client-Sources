// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.nosql;

import java.io.Closeable;

public interface NoSQLConnection<W, T extends NoSQLObject<W>> extends Closeable
{
    T createObject();
    
    T[] createList(final int p0);
    
    void insertObject(final NoSQLObject<W> p0);
    
    void close();
    
    boolean isClosed();
}
