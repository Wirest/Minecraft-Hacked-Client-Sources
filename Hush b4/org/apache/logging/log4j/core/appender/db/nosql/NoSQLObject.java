// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.nosql;

public interface NoSQLObject<W>
{
    void set(final String p0, final Object p1);
    
    void set(final String p0, final NoSQLObject<W> p1);
    
    void set(final String p0, final Object[] p1);
    
    void set(final String p0, final NoSQLObject<W>[] p1);
    
    W unwrap();
}
