// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.Set;
import com.google.common.primitives.Primitives;
import java.util.Map;
import java.util.HashMap;

public final class MutableClassToInstanceMap<B> extends MapConstraints.ConstrainedMap<Class<? extends B>, B> implements ClassToInstanceMap<B>
{
    private static final MapConstraint<Class<?>, Object> VALUE_CAN_BE_CAST_TO_KEY;
    private static final long serialVersionUID = 0L;
    
    public static <B> MutableClassToInstanceMap<B> create() {
        return new MutableClassToInstanceMap<B>(new HashMap<Class<? extends B>, B>());
    }
    
    public static <B> MutableClassToInstanceMap<B> create(final Map<Class<? extends B>, B> backingMap) {
        return new MutableClassToInstanceMap<B>(backingMap);
    }
    
    private MutableClassToInstanceMap(final Map<Class<? extends B>, B> delegate) {
        super(delegate, MutableClassToInstanceMap.VALUE_CAN_BE_CAST_TO_KEY);
    }
    
    @Override
    public <T extends B> T putInstance(final Class<T> type, final T value) {
        return cast(type, this.put((Class<? extends B>)type, value));
    }
    
    @Override
    public <T extends B> T getInstance(final Class<T> type) {
        return cast(type, this.get(type));
    }
    
    private static <B, T extends B> T cast(final Class<T> type, final B value) {
        return Primitives.wrap(type).cast(value);
    }
    
    static {
        VALUE_CAN_BE_CAST_TO_KEY = new MapConstraint<Class<?>, Object>() {
            @Override
            public void checkKeyValue(final Class<?> key, final Object value) {
                cast(key, value);
            }
        };
    }
}
