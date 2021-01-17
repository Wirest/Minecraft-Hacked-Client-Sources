// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.nosql;

public interface NoSQLProvider<C extends NoSQLConnection<?, ? extends NoSQLObject<?>>>
{
    C getConnection();
    
    String toString();
}
