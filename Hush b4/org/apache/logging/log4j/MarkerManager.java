// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class MarkerManager
{
    private static ConcurrentMap<String, Marker> markerMap;
    
    private MarkerManager() {
    }
    
    public static Marker getMarker(final String name) {
        MarkerManager.markerMap.putIfAbsent(name, new Log4jMarker(name));
        return MarkerManager.markerMap.get(name);
    }
    
    public static Marker getMarker(final String name, final String parent) {
        final Marker parentMarker = MarkerManager.markerMap.get(parent);
        if (parentMarker == null) {
            throw new IllegalArgumentException("Parent Marker " + parent + " has not been defined");
        }
        return getMarker(name, parentMarker);
    }
    
    public static Marker getMarker(final String name, final Marker parent) {
        MarkerManager.markerMap.putIfAbsent(name, new Log4jMarker(name, parent));
        return MarkerManager.markerMap.get(name);
    }
    
    static {
        MarkerManager.markerMap = new ConcurrentHashMap<String, Marker>();
    }
    
    private static class Log4jMarker implements Marker
    {
        private static final long serialVersionUID = 100L;
        private final String name;
        private final Marker parent;
        
        public Log4jMarker(final String name) {
            this.name = name;
            this.parent = null;
        }
        
        public Log4jMarker(final String name, final Marker parent) {
            this.name = name;
            this.parent = parent;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        @Override
        public Marker getParent() {
            return this.parent;
        }
        
        @Override
        public boolean isInstanceOf(final Marker m) {
            if (m == null) {
                throw new IllegalArgumentException("A marker parameter is required");
            }
            Marker test = this;
            while (test != m) {
                test = test.getParent();
                if (test == null) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean isInstanceOf(final String name) {
            if (name == null) {
                throw new IllegalArgumentException("A marker name is required");
            }
            Marker toTest = this;
            while (!name.equals(toTest.getName())) {
                toTest = toTest.getParent();
                if (toTest == null) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || !(o instanceof Marker)) {
                return false;
            }
            final Marker marker = (Marker)o;
            if (this.name != null) {
                if (this.name.equals(marker.getName())) {
                    return true;
                }
            }
            else if (marker.getName() == null) {
                return true;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return (this.name != null) ? this.name.hashCode() : 0;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(this.name);
            if (this.parent != null) {
                Marker m = this.parent;
                sb.append("[ ");
                boolean first = true;
                while (m != null) {
                    if (!first) {
                        sb.append(", ");
                    }
                    sb.append(m.getName());
                    first = false;
                    m = m.getParent();
                }
                sb.append(" ]");
            }
            return sb.toString();
        }
    }
}
