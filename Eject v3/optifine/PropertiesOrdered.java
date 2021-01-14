package optifine;

import java.util.*;

public class PropertiesOrdered
        extends Properties {
    private Set<Object> keysOrdered = new LinkedHashSet();

    public synchronized Object put(Object paramObject1, Object paramObject2) {
        this.keysOrdered.add(paramObject1);
        return super.put(paramObject1, paramObject2);
    }

    public Set<Object> keySet() {
        Set localSet = super.keySet();
        this.keysOrdered.retainAll(localSet);
        return Collections.unmodifiableSet(this.keysOrdered);
    }

    public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(keySet());
    }
}




