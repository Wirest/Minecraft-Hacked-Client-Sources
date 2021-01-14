package org.apache.logging.log4j.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultThreadContextMap
        implements ThreadContextMap {
    private final boolean useMap;
    private final ThreadLocal<Map<String, String>> localMap = new InheritableThreadLocal() {
        protected Map<String, String> childValue(Map<String, String> paramAnonymousMap) {
            return (paramAnonymousMap == null) || (!DefaultThreadContextMap.this.useMap) ? null : Collections.unmodifiableMap(new HashMap(paramAnonymousMap));
        }
    };

    public DefaultThreadContextMap(boolean paramBoolean) {
        this.useMap = paramBoolean;
    }

    public void put(String paramString1, String paramString2) {
        if (!this.useMap) {
            return;
        }
        Object localObject = (Map) this.localMap.get();
        localObject = localObject == null ? new HashMap() : new HashMap((Map) localObject);
        ((Map) localObject).put(paramString1, paramString2);
        this.localMap.set(Collections.unmodifiableMap((Map) localObject));
    }

    public String get(String paramString) {
        Map localMap1 = (Map) this.localMap.get();
        return localMap1 == null ? null : (String) localMap1.get(paramString);
    }

    public void remove(String paramString) {
        Map localMap1 = (Map) this.localMap.get();
        if (localMap1 != null) {
            HashMap localHashMap = new HashMap(localMap1);
            localHashMap.remove(paramString);
            this.localMap.set(Collections.unmodifiableMap(localHashMap));
        }
    }

    public void clear() {
        this.localMap.remove();
    }

    public boolean containsKey(String paramString) {
        Map localMap1 = (Map) this.localMap.get();
        return (localMap1 != null) && (localMap1.containsKey(paramString));
    }

    public Map<String, String> getCopy() {
        Map localMap1 = (Map) this.localMap.get();
        return localMap1 == null ? new HashMap() : new HashMap(localMap1);
    }

    public Map<String, String> getImmutableMapOrNull() {
        return (Map) this.localMap.get();
    }

    public boolean isEmpty() {
        Map localMap1 = (Map) this.localMap.get();
        return (localMap1 == null) || (localMap1.size() == 0);
    }

    public String toString() {
        Map localMap1 = (Map) this.localMap.get();
        return localMap1 == null ? "{}" : localMap1.toString();
    }
}




