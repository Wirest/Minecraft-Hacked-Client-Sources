package optifine;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class PropertiesOrdered extends Properties {
    private Set<Object> keysOrdered = new LinkedHashSet();

    public synchronized Object put(Object key, Object value) {
        this.keysOrdered.add(key);
        return super.put(key, value);
    }

    public Set<Object> keySet() {
        Set keysParent = super.keySet();
        this.keysOrdered.retainAll(keysParent);
        return Collections.unmodifiableSet(this.keysOrdered);
    }

    public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(this.keySet());
    }
}
