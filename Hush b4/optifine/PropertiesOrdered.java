// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Enumeration;
import java.util.Collections;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Properties;

public class PropertiesOrdered extends Properties
{
    private Set<Object> keysOrdered;
    
    public PropertiesOrdered() {
        this.keysOrdered = new LinkedHashSet<Object>();
    }
    
    @Override
    public synchronized Object put(final Object p_put_1_, final Object p_put_2_) {
        this.keysOrdered.add(p_put_1_);
        return super.put(p_put_1_, p_put_2_);
    }
    
    @Override
    public Set<Object> keySet() {
        final Set<Object> set = super.keySet();
        this.keysOrdered.retainAll(set);
        return Collections.unmodifiableSet((Set<?>)this.keysOrdered);
    }
    
    @Override
    public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(this.keySet());
    }
}
