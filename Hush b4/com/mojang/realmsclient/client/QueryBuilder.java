// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.client;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class QueryBuilder
{
    private Map<String, String> queryParams;
    
    public QueryBuilder() {
        this.queryParams = new HashMap<String, String>();
    }
    
    public static QueryBuilder of(final String key, final String value) {
        final QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.queryParams.put(key, value);
        return queryBuilder;
    }
    
    public static QueryBuilder empty() {
        return new QueryBuilder();
    }
    
    public QueryBuilder with(final String key, final String value) {
        this.queryParams.put(key, value);
        return this;
    }
    
    public QueryBuilder with(final Object key, final Object value) {
        this.queryParams.put(String.valueOf(key), String.valueOf(value));
        return this;
    }
    
    public String toQueryString() {
        final StringBuilder stringBuilder = new StringBuilder();
        final Iterator<String> keyIterator = this.queryParams.keySet().iterator();
        if (!keyIterator.hasNext()) {
            return null;
        }
        final String firstKey = keyIterator.next();
        stringBuilder.append(firstKey).append("=").append(this.queryParams.get(firstKey));
        while (keyIterator.hasNext()) {
            final String key = keyIterator.next();
            stringBuilder.append("&").append(key).append("=").append(this.queryParams.get(key));
        }
        return stringBuilder.toString();
    }
}
