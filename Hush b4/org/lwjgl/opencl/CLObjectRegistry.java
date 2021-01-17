// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.LWJGLUtil;

class CLObjectRegistry<T extends CLObjectChild>
{
    private FastLongMap<T> registry;
    
    final boolean isEmpty() {
        return this.registry == null || this.registry.isEmpty();
    }
    
    final T getObject(final long id) {
        return (T)((this.registry == null) ? null : ((T)this.registry.get(id)));
    }
    
    final boolean hasObject(final long id) {
        return this.registry != null && this.registry.containsKey(id);
    }
    
    final Iterable<FastLongMap.Entry<T>> getAll() {
        return this.registry;
    }
    
    void registerObject(final T object) {
        final FastLongMap<T> map = this.getMap();
        final Long key = object.getPointer();
        if (LWJGLUtil.DEBUG && map.containsKey(key)) {
            throw new IllegalStateException("Duplicate object found: " + object.getClass() + " - " + key);
        }
        this.getMap().put(object.getPointer(), object);
    }
    
    void unregisterObject(final T object) {
        this.getMap().remove(object.getPointerUnsafe());
    }
    
    private FastLongMap<T> getMap() {
        if (this.registry == null) {
            this.registry = new FastLongMap<T>();
        }
        return this.registry;
    }
}
