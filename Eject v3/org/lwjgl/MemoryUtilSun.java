package org.lwjgl;

import sun.misc.Unsafe;
import sun.reflect.FieldAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.Buffer;

final class MemoryUtilSun {
    private static class AccessorReflectFast
            implements MemoryUtil.Accessor {
        private final FieldAccessor addressAccessor;

        AccessorReflectFast() {
            Field localField;
            try {
                localField = MemoryUtil.getAddressField();
            } catch (NoSuchFieldException localNoSuchFieldException) {
                throw new UnsupportedOperationException(localNoSuchFieldException);
            }
            localField.setAccessible(true);
            try {
                Method localMethod = Field.class.getDeclaredMethod("acquireFieldAccessor", new Class[]{Boolean.TYPE});
                localMethod.setAccessible(true);
                this.addressAccessor = ((FieldAccessor) localMethod.invoke(localField, new Object[]{Boolean.valueOf(true)}));
            } catch (Exception localException) {
                throw new UnsupportedOperationException(localException);
            }
        }

        public long getAddress(Buffer paramBuffer) {
            return this.addressAccessor.getLong(paramBuffer);
        }
    }

    private static class AccessorUnsafe
            implements MemoryUtil.Accessor {
        private final Unsafe unsafe;
        private final long address;

        AccessorUnsafe() {
            try {
                this.unsafe = getUnsafeInstance();
                this.address = this.unsafe.objectFieldOffset(MemoryUtil.getAddressField());
            } catch (Exception localException) {
                throw new UnsupportedOperationException(localException);
            }
        }

        private static Unsafe getUnsafeInstance() {
            Field[] arrayOfField1 = Unsafe.class.getDeclaredFields();
            for (Field localField : arrayOfField1) {
                if (localField.getType().equals(Unsafe.class)) {
                    int k = localField.getModifiers();
                    if ((Modifier.isStatic(k)) && (Modifier.isFinal(k))) {
                        localField.setAccessible(true);
                        try {
                            return (Unsafe) localField.get(null);
                        } catch (IllegalAccessException localIllegalAccessException) {
                        }
                    }
                }
            }
            throw new UnsupportedOperationException();
        }

        public long getAddress(Buffer paramBuffer) {
            return this.unsafe.getLong(paramBuffer, this.address);
        }
    }
}




