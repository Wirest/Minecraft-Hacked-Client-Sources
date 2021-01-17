// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl;

import java.lang.reflect.Method;
import sun.reflect.FieldAccessor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.Buffer;
import sun.misc.Unsafe;

final class MemoryUtilSun
{
    private MemoryUtilSun() {
    }
    
    private static class AccessorUnsafe implements MemoryUtil.Accessor
    {
        private final Unsafe unsafe;
        private final long address;
        
        AccessorUnsafe() {
            try {
                this.unsafe = getUnsafeInstance();
                this.address = this.unsafe.objectFieldOffset(MemoryUtil.getAddressField());
            }
            catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        }
        
        public long getAddress(final Buffer buffer) {
            return this.unsafe.getLong(buffer, this.address);
        }
        
        private static Unsafe getUnsafeInstance() {
            final Field[] arr$;
            final Field[] fields = arr$ = Unsafe.class.getDeclaredFields();
            for (final Field field : arr$) {
                if (field.getType().equals(Unsafe.class)) {
                    final int modifiers = field.getModifiers();
                    if (Modifier.isStatic(modifiers)) {
                        if (Modifier.isFinal(modifiers)) {
                            field.setAccessible(true);
                            try {
                                return (Unsafe)field.get(null);
                            }
                            catch (IllegalAccessException e) {
                                break;
                            }
                        }
                    }
                }
            }
            throw new UnsupportedOperationException();
        }
    }
    
    private static class AccessorReflectFast implements MemoryUtil.Accessor
    {
        private final FieldAccessor addressAccessor;
        
        AccessorReflectFast() {
            Field address;
            try {
                address = MemoryUtil.getAddressField();
            }
            catch (NoSuchFieldException e) {
                throw new UnsupportedOperationException(e);
            }
            address.setAccessible(true);
            try {
                final Method m = Field.class.getDeclaredMethod("acquireFieldAccessor", Boolean.TYPE);
                m.setAccessible(true);
                this.addressAccessor = (FieldAccessor)m.invoke(address, true);
            }
            catch (Exception e2) {
                throw new UnsupportedOperationException(e2);
            }
        }
        
        public long getAddress(final Buffer buffer) {
            return this.addressAccessor.getLong((Object)buffer);
        }
    }
}
