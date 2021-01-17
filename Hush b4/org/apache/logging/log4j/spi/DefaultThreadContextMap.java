// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultThreadContextMap implements ThreadContextMap
{
    private final boolean useMap;
    private final ThreadLocal<Map<String, String>> localMap;
    
    public DefaultThreadContextMap(final boolean useMap) {
        this.localMap = new InheritableThreadLocal<Map<String, String>>() {
            @Override
            protected Map<String, String> childValue(final Map<String, String> parentValue) {
                return (parentValue == null || !DefaultThreadContextMap.this.useMap) ? null : Collections.unmodifiableMap((Map<? extends String, ? extends String>)new HashMap<String, String>(parentValue));
            }
        };
        this.useMap = useMap;
    }
    
    @Override
    public void put(final String key, final String value) {
        if (!this.useMap) {
            return;
        }
        Map<String, String> map = this.localMap.get();
        map = ((map == null) ? new HashMap<String, String>() : new HashMap<String, String>(map));
        map.put(key, value);
        this.localMap.set(Collections.unmodifiableMap((Map<? extends String, ? extends String>)map));
    }
    
    @Override
    public String get(final String key) {
        final Map<String, String> map = this.localMap.get();
        return (map == null) ? null : map.get(key);
    }
    
    @Override
    public void remove(final String key) {
        final Map<String, String> map = this.localMap.get();
        if (map != null) {
            final Map<String, String> copy = new HashMap<String, String>(map);
            copy.remove(key);
            this.localMap.set(Collections.unmodifiableMap((Map<? extends String, ? extends String>)copy));
        }
    }
    
    @Override
    public void clear() {
        this.localMap.remove();
    }
    
    @Override
    public boolean containsKey(final String key) {
        final Map<String, String> map = this.localMap.get();
        return map != null && map.containsKey(key);
    }
    
    @Override
    public Map<String, String> getCopy() {
        final Map<String, String> map = this.localMap.get();
        return (map == null) ? new HashMap<String, String>() : new HashMap<String, String>(map);
    }
    
    @Override
    public Map<String, String> getImmutableMapOrNull() {
        return this.localMap.get();
    }
    
    @Override
    public boolean isEmpty() {
        final Map<String, String> map = this.localMap.get();
        return map == null || map.size() == 0;
    }
    
    @Override
    public String toString() {
        final Map<String, String> map = this.localMap.get();
        return (map == null) ? "{}" : map.toString();
    }
}
