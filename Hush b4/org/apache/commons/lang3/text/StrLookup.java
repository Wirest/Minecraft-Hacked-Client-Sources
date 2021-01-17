// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.text;

import java.util.Map;

public abstract class StrLookup<V>
{
    private static final StrLookup<String> NONE_LOOKUP;
    private static final StrLookup<String> SYSTEM_PROPERTIES_LOOKUP;
    
    public static StrLookup<?> noneLookup() {
        return StrLookup.NONE_LOOKUP;
    }
    
    public static StrLookup<String> systemPropertiesLookup() {
        return StrLookup.SYSTEM_PROPERTIES_LOOKUP;
    }
    
    public static <V> StrLookup<V> mapLookup(final Map<String, V> map) {
        return new MapStrLookup<V>(map);
    }
    
    protected StrLookup() {
    }
    
    public abstract String lookup(final String p0);
    
    static {
        NONE_LOOKUP = new MapStrLookup<String>(null);
        StrLookup<String> lookup = null;
        try {
            final Map<String, String> properties;
            final Map<?, ?> propMap = properties = (Map<String, String>)System.getProperties();
            lookup = new MapStrLookup<String>(properties);
        }
        catch (SecurityException ex) {
            lookup = StrLookup.NONE_LOOKUP;
        }
        SYSTEM_PROPERTIES_LOOKUP = lookup;
    }
    
    static class MapStrLookup<V> extends StrLookup<V>
    {
        private final Map<String, V> map;
        
        MapStrLookup(final Map<String, V> map) {
            this.map = map;
        }
        
        @Override
        public String lookup(final String key) {
            if (this.map == null) {
                return null;
            }
            final Object obj = this.map.get(key);
            if (obj == null) {
                return null;
            }
            return obj.toString();
        }
    }
}
