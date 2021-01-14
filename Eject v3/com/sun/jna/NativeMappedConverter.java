package com.sun.jna;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.WeakHashMap;

public class NativeMappedConverter
        implements TypeConverter {
    private static final Map converters = new WeakHashMap();
    private final Class type;
    private final Class nativeType;
    private final NativeMapped instance;

    public NativeMappedConverter(Class paramClass) {
        if (!NativeMapped.class.isAssignableFrom(paramClass)) {
            throw new IllegalArgumentException("Type must derive from " + NativeMapped.class);
        }
        this.type = paramClass;
        this.instance = defaultValue();
        this.nativeType = this.instance.nativeType();
    }

    public static NativeMappedConverter getInstance(Class paramClass) {
        synchronized (converters) {
            Reference localReference = (Reference) converters.get(paramClass);
            NativeMappedConverter localNativeMappedConverter = localReference != null ? (NativeMappedConverter) localReference.get() : null;
            if (localNativeMappedConverter == null) {
                localNativeMappedConverter = new NativeMappedConverter(paramClass);
                converters.put(paramClass, new SoftReference(localNativeMappedConverter));
            }
            return localNativeMappedConverter;
        }
    }

    public NativeMapped defaultValue() {
        try {
            return (NativeMapped) this.type.newInstance();
        } catch (InstantiationException localInstantiationException) {
            str = "Can't create an instance of " + this.type + ", requires a no-arg constructor: " + localInstantiationException;
            throw new IllegalArgumentException(str);
        } catch (IllegalAccessException localIllegalAccessException) {
            String str = "Not allowed to create an instance of " + this.type + ", requires a public, no-arg constructor: " + localIllegalAccessException;
            throw new IllegalArgumentException(str);
        }
    }

    public Object fromNative(Object paramObject, FromNativeContext paramFromNativeContext) {
        return this.instance.fromNative(paramObject, paramFromNativeContext);
    }

    public Class nativeType() {
        return this.nativeType;
    }

    public Object toNative(Object paramObject, ToNativeContext paramToNativeContext) {
        if (paramObject == null) {
            if (Pointer.class.isAssignableFrom(this.nativeType)) {
                return null;
            }
            paramObject = defaultValue();
        }
        return ((NativeMapped) paramObject).toNative();
    }
}




