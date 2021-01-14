package io.netty.buffer;

import io.netty.util.ResourceLeak;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;

public abstract class AbstractByteBufAllocator
        implements ByteBufAllocator {
    private static final int DEFAULT_INITIAL_CAPACITY = 256;
    private static final int DEFAULT_MAX_COMPONENTS = 16;
    private final boolean directByDefault;
    private final ByteBuf emptyBuf;

    protected AbstractByteBufAllocator() {
        this(false);
    }

    protected AbstractByteBufAllocator(boolean paramBoolean) {
        this.directByDefault = ((paramBoolean) && (PlatformDependent.hasUnsafe()));
        this.emptyBuf = new EmptyByteBuf(this);
    }

    protected static ByteBuf toLeakAwareBuffer(ByteBuf paramByteBuf) {
        ResourceLeak localResourceLeak;
        switch (ResourceLeakDetector.getLevel()) {
            case SIMPLE:
                localResourceLeak = AbstractByteBuf.leakDetector.open(paramByteBuf);
                if (localResourceLeak != null) {
                    paramByteBuf = new SimpleLeakAwareByteBuf(paramByteBuf, localResourceLeak);
                }
                break;
            case ADVANCED:
            case PARANOID:
                localResourceLeak = AbstractByteBuf.leakDetector.open(paramByteBuf);
                if (localResourceLeak != null) {
                    paramByteBuf = new AdvancedLeakAwareByteBuf(paramByteBuf, localResourceLeak);
                }
                break;
        }
        return paramByteBuf;
    }

    private static void validate(int paramInt1, int paramInt2) {
        if (paramInt1 < 0) {
            throw new IllegalArgumentException("initialCapacity: " + paramInt1 + " (expectd: 0+)");
        }
        if (paramInt1 > paramInt2) {
            throw new IllegalArgumentException(String.format("initialCapacity: %d (expected: not greater than maxCapacity(%d)", new Object[]{Integer.valueOf(paramInt1), Integer.valueOf(paramInt2)}));
        }
    }

    public ByteBuf buffer() {
        if (this.directByDefault) {
            return directBuffer();
        }
        return heapBuffer();
    }

    public ByteBuf buffer(int paramInt) {
        if (this.directByDefault) {
            return directBuffer(paramInt);
        }
        return heapBuffer(paramInt);
    }

    public ByteBuf buffer(int paramInt1, int paramInt2) {
        if (this.directByDefault) {
            return directBuffer(paramInt1, paramInt2);
        }
        return heapBuffer(paramInt1, paramInt2);
    }

    public ByteBuf ioBuffer() {
        if (PlatformDependent.hasUnsafe()) {
            return directBuffer(256);
        }
        return heapBuffer(256);
    }

    public ByteBuf ioBuffer(int paramInt) {
        if (PlatformDependent.hasUnsafe()) {
            return directBuffer(paramInt);
        }
        return heapBuffer(paramInt);
    }

    public ByteBuf ioBuffer(int paramInt1, int paramInt2) {
        if (PlatformDependent.hasUnsafe()) {
            return directBuffer(paramInt1, paramInt2);
        }
        return heapBuffer(paramInt1, paramInt2);
    }

    public ByteBuf heapBuffer() {
        return heapBuffer(256, Integer.MAX_VALUE);
    }

    public ByteBuf heapBuffer(int paramInt) {
        return heapBuffer(paramInt, Integer.MAX_VALUE);
    }

    public ByteBuf heapBuffer(int paramInt1, int paramInt2) {
        if ((paramInt1 == 0) && (paramInt2 == 0)) {
            return this.emptyBuf;
        }
        validate(paramInt1, paramInt2);
        return newHeapBuffer(paramInt1, paramInt2);
    }

    public ByteBuf directBuffer() {
        return directBuffer(256, Integer.MAX_VALUE);
    }

    public ByteBuf directBuffer(int paramInt) {
        return directBuffer(paramInt, Integer.MAX_VALUE);
    }

    public ByteBuf directBuffer(int paramInt1, int paramInt2) {
        if ((paramInt1 == 0) && (paramInt2 == 0)) {
            return this.emptyBuf;
        }
        validate(paramInt1, paramInt2);
        return newDirectBuffer(paramInt1, paramInt2);
    }

    public CompositeByteBuf compositeBuffer() {
        if (this.directByDefault) {
            return compositeDirectBuffer();
        }
        return compositeHeapBuffer();
    }

    public CompositeByteBuf compositeBuffer(int paramInt) {
        if (this.directByDefault) {
            return compositeDirectBuffer(paramInt);
        }
        return compositeHeapBuffer(paramInt);
    }

    public CompositeByteBuf compositeHeapBuffer() {
        return compositeHeapBuffer(16);
    }

    public CompositeByteBuf compositeHeapBuffer(int paramInt) {
        return new CompositeByteBuf(this, false, paramInt);
    }

    public CompositeByteBuf compositeDirectBuffer() {
        return compositeDirectBuffer(16);
    }

    public CompositeByteBuf compositeDirectBuffer(int paramInt) {
        return new CompositeByteBuf(this, true, paramInt);
    }

    protected abstract ByteBuf newHeapBuffer(int paramInt1, int paramInt2);

    protected abstract ByteBuf newDirectBuffer(int paramInt1, int paramInt2);

    public String toString() {
        return StringUtil.simpleClassName(this) + "(directByDefault: " + this.directByDefault + ')';
    }
}




