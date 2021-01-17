// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import java.util.Iterator;
import org.apache.http.util.Args;
import java.util.HashMap;
import org.apache.http.annotation.GuardedBy;
import java.util.Map;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public class UriPatternMatcher<T>
{
    @GuardedBy("this")
    private final Map<String, T> map;
    
    public UriPatternMatcher() {
        this.map = new HashMap<String, T>();
    }
    
    public synchronized void register(final String pattern, final T obj) {
        Args.notNull(pattern, "URI request pattern");
        this.map.put(pattern, obj);
    }
    
    public synchronized void unregister(final String pattern) {
        if (pattern == null) {
            return;
        }
        this.map.remove(pattern);
    }
    
    @Deprecated
    public synchronized void setHandlers(final Map<String, T> map) {
        Args.notNull(map, "Map of handlers");
        this.map.clear();
        this.map.putAll((Map<? extends String, ? extends T>)map);
    }
    
    @Deprecated
    public synchronized void setObjects(final Map<String, T> map) {
        Args.notNull(map, "Map of handlers");
        this.map.clear();
        this.map.putAll((Map<? extends String, ? extends T>)map);
    }
    
    @Deprecated
    public synchronized Map<String, T> getObjects() {
        return this.map;
    }
    
    public synchronized T lookup(final String path) {
        Args.notNull(path, "Request path");
        T obj = this.map.get(path);
        if (obj == null) {
            String bestMatch = null;
            for (final String pattern : this.map.keySet()) {
                if (this.matchUriRequestPattern(pattern, path) && (bestMatch == null || bestMatch.length() < pattern.length() || (bestMatch.length() == pattern.length() && pattern.endsWith("*")))) {
                    obj = this.map.get(pattern);
                    bestMatch = pattern;
                }
            }
        }
        return obj;
    }
    
    protected boolean matchUriRequestPattern(final String pattern, final String path) {
        return pattern.equals("*") || (pattern.endsWith("*") && path.startsWith(pattern.substring(0, pattern.length() - 1))) || (pattern.startsWith("*") && path.endsWith(pattern.substring(1, pattern.length())));
    }
    
    @Override
    public String toString() {
        return this.map.toString();
    }
}
