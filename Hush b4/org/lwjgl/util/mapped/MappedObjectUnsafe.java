// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.mapped;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import sun.misc.Unsafe;

final class MappedObjectUnsafe
{
    static final Unsafe INSTANCE;
    private static final long BUFFER_ADDRESS_OFFSET;
    private static final long BUFFER_CAPACITY_OFFSET;
    private static final ByteBuffer global;
    
    static ByteBuffer newBuffer(final long address, final int capacity) {
        if (address <= 0L || capacity < 0) {
            throw new IllegalStateException("you almost crashed the jvm");
        }
        final ByteBuffer buffer = MappedObjectUnsafe.global.duplicate().order(ByteOrder.nativeOrder());
        MappedObjectUnsafe.INSTANCE.putLong(buffer, MappedObjectUnsafe.BUFFER_ADDRESS_OFFSET, address);
        MappedObjectUnsafe.INSTANCE.putInt(buffer, MappedObjectUnsafe.BUFFER_CAPACITY_OFFSET, capacity);
        buffer.position(0);
        buffer.limit(capacity);
        return buffer;
    }
    
    private static long getObjectFieldOffset(Class<?> type, final String fieldName) {
        while (type != null) {
            try {
                return MappedObjectUnsafe.INSTANCE.objectFieldOffset(type.getDeclaredField(fieldName));
            }
            catch (Throwable t) {
                type = type.getSuperclass();
                continue;
            }
            break;
        }
        throw new UnsupportedOperationException();
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
    
    static {
        INSTANCE = getUnsafeInstance();
        BUFFER_ADDRESS_OFFSET = getObjectFieldOffset(ByteBuffer.class, "address");
        BUFFER_CAPACITY_OFFSET = getObjectFieldOffset(ByteBuffer.class, "capacity");
        global = ByteBuffer.allocateDirect(4096);
    }
}
