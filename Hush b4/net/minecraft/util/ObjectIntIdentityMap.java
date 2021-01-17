// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.base.Predicates;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.IdentityHashMap;

public class ObjectIntIdentityMap implements IObjectIntIterable
{
    private final IdentityHashMap identityMap;
    private final List objectList;
    private static final String __OBFID = "CL_00001203";
    
    public ObjectIntIdentityMap() {
        this.identityMap = new IdentityHashMap(512);
        this.objectList = Lists.newArrayList();
    }
    
    public void put(final Object key, final int value) {
        this.identityMap.put(key, value);
        while (this.objectList.size() <= value) {
            this.objectList.add(null);
        }
        this.objectList.set(value, key);
    }
    
    public int get(final Object key) {
        final Integer integer = this.identityMap.get(key);
        return (integer == null) ? -1 : integer;
    }
    
    public final Object getByValue(final int value) {
        return (value >= 0 && value < this.objectList.size()) ? this.objectList.get(value) : null;
    }
    
    @Override
    public Iterator iterator() {
        return Iterators.filter(this.objectList.iterator(), Predicates.notNull());
    }
    
    public List getObjectList() {
        return this.objectList;
    }
}
