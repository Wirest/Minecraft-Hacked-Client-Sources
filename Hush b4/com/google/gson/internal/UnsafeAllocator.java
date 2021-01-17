// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.internal;

import java.lang.reflect.Field;
import java.io.ObjectStreamClass;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;

public abstract class UnsafeAllocator
{
    public abstract <T> T newInstance(final Class<T> p0) throws Exception;
    
    public static UnsafeAllocator create() {
        try {
            final Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            final Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            final Object unsafe = f.get(null);
            final Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
            return new UnsafeAllocator() {
                @Override
                public <T> T newInstance(final Class<T> c) throws Exception {
                    return (T)allocateInstance.invoke(unsafe, c);
                }
            };
        }
        catch (Exception ignored) {
            try {
                final Method newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
                newInstance.setAccessible(true);
                return new UnsafeAllocator() {
                    @Override
                    public <T> T newInstance(final Class<T> c) throws Exception {
                        return (T)newInstance.invoke(null, c, Object.class);
                    }
                };
            }
            catch (Exception ignored) {
                try {
                    final Method getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
                    getConstructorId.setAccessible(true);
                    final int constructorId = (int)getConstructorId.invoke(null, Object.class);
                    final Method newInstance2 = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
                    newInstance2.setAccessible(true);
                    return new UnsafeAllocator() {
                        @Override
                        public <T> T newInstance(final Class<T> c) throws Exception {
                            return (T)newInstance2.invoke(null, c, constructorId);
                        }
                    };
                }
                catch (Exception ignored) {
                    return new UnsafeAllocator() {
                        @Override
                        public <T> T newInstance(final Class<T> c) {
                            throw new UnsupportedOperationException("Cannot allocate " + c);
                        }
                    };
                }
            }
        }
    }
}
