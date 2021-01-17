// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.nosql.couch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;

public final class CouchDBObject implements NoSQLObject<Map<String, Object>>
{
    private final Map<String, Object> map;
    
    public CouchDBObject() {
        this.map = new HashMap<String, Object>();
    }
    
    @Override
    public void set(final String field, final Object value) {
        this.map.put(field, value);
    }
    
    @Override
    public void set(final String field, final NoSQLObject<Map<String, Object>> value) {
        this.map.put(field, value.unwrap());
    }
    
    @Override
    public void set(final String field, final Object[] values) {
        this.map.put(field, Arrays.asList(values));
    }
    
    @Override
    public void set(final String field, final NoSQLObject<Map<String, Object>>[] values) {
        final ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (final NoSQLObject<Map<String, Object>> value : values) {
            list.add(value.unwrap());
        }
        this.map.put(field, list);
    }
    
    @Override
    public Map<String, Object> unwrap() {
        return this.map;
    }
}
