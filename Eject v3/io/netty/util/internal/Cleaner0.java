package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import sun.misc.Cleaner;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

final class Cleaner0 {
    private static final long CLEANER_FIELD_OFFSET;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(Cleaner0.class);

    static {
        ByteBuffer localByteBuffer = ByteBuffer.allocateDirect(1);
        long l = -1L;
        if (PlatformDependent0.hasUnsafe()) {
            try {
                Field localField = localByteBuffer.getClass().getDeclaredField("cleaner");
                localField.setAccessible(true);
                Cleaner localCleaner = (Cleaner) localField.get(localByteBuffer);
                localCleaner.clean();
                l = PlatformDependent0.objectFieldOffset(localField);
            } catch (Throwable localThrowable) {
                l = -1L;
            }
        }
        logger.debug("java.nio.ByteBuffer.cleaner(): {}", l != -1L ? "available" : "unavailable");
        CLEANER_FIELD_OFFSET = l;
        freeDirectBuffer(localByteBuffer);
    }

    static void freeDirectBuffer(ByteBuffer paramByteBuffer) {
        if ((CLEANER_FIELD_OFFSET == -1L) || (!paramByteBuffer.isDirect())) {
            return;
        }
        try {
            Cleaner localCleaner = (Cleaner) PlatformDependent0.getObject(paramByteBuffer, CLEANER_FIELD_OFFSET);
            if (localCleaner != null) {
                localCleaner.clean();
            }
        } catch (Throwable localThrowable) {
        }
    }
}




