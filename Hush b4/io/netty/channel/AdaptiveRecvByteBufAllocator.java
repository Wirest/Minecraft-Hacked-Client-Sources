// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.util.ArrayList;

public class AdaptiveRecvByteBufAllocator implements RecvByteBufAllocator
{
    static final int DEFAULT_MINIMUM = 64;
    static final int DEFAULT_INITIAL = 1024;
    static final int DEFAULT_MAXIMUM = 65536;
    private static final int INDEX_INCREMENT = 4;
    private static final int INDEX_DECREMENT = 1;
    private static final int[] SIZE_TABLE;
    public static final AdaptiveRecvByteBufAllocator DEFAULT;
    private final int minIndex;
    private final int maxIndex;
    private final int initial;
    
    private static int getSizeTableIndex(final int size) {
        int low = 0;
        int high = AdaptiveRecvByteBufAllocator.SIZE_TABLE.length - 1;
        while (high >= low) {
            if (high == low) {
                return high;
            }
            final int mid = low + high >>> 1;
            final int a = AdaptiveRecvByteBufAllocator.SIZE_TABLE[mid];
            final int b = AdaptiveRecvByteBufAllocator.SIZE_TABLE[mid + 1];
            if (size > b) {
                low = mid + 1;
            }
            else if (size < a) {
                high = mid - 1;
            }
            else {
                if (size == a) {
                    return mid;
                }
                return mid + 1;
            }
        }
        return low;
    }
    
    private AdaptiveRecvByteBufAllocator() {
        this(64, 1024, 65536);
    }
    
    public AdaptiveRecvByteBufAllocator(final int minimum, final int initial, final int maximum) {
        if (minimum <= 0) {
            throw new IllegalArgumentException("minimum: " + minimum);
        }
        if (initial < minimum) {
            throw new IllegalArgumentException("initial: " + initial);
        }
        if (maximum < initial) {
            throw new IllegalArgumentException("maximum: " + maximum);
        }
        final int minIndex = getSizeTableIndex(minimum);
        if (AdaptiveRecvByteBufAllocator.SIZE_TABLE[minIndex] < minimum) {
            this.minIndex = minIndex + 1;
        }
        else {
            this.minIndex = minIndex;
        }
        final int maxIndex = getSizeTableIndex(maximum);
        if (AdaptiveRecvByteBufAllocator.SIZE_TABLE[maxIndex] > maximum) {
            this.maxIndex = maxIndex - 1;
        }
        else {
            this.maxIndex = maxIndex;
        }
        this.initial = initial;
    }
    
    @Override
    public Handle newHandle() {
        return new HandleImpl(this.minIndex, this.maxIndex, this.initial);
    }
    
    static {
        final List<Integer> sizeTable = new ArrayList<Integer>();
        for (int i = 16; i < 512; i += 16) {
            sizeTable.add(i);
        }
        for (int i = 512; i > 0; i <<= 1) {
            sizeTable.add(i);
        }
        SIZE_TABLE = new int[sizeTable.size()];
        for (int i = 0; i < AdaptiveRecvByteBufAllocator.SIZE_TABLE.length; ++i) {
            AdaptiveRecvByteBufAllocator.SIZE_TABLE[i] = sizeTable.get(i);
        }
        DEFAULT = new AdaptiveRecvByteBufAllocator();
    }
    
    private static final class HandleImpl implements Handle
    {
        private final int minIndex;
        private final int maxIndex;
        private int index;
        private int nextReceiveBufferSize;
        private boolean decreaseNow;
        
        HandleImpl(final int minIndex, final int maxIndex, final int initial) {
            this.minIndex = minIndex;
            this.maxIndex = maxIndex;
            this.index = getSizeTableIndex(initial);
            this.nextReceiveBufferSize = AdaptiveRecvByteBufAllocator.SIZE_TABLE[this.index];
        }
        
        @Override
        public ByteBuf allocate(final ByteBufAllocator alloc) {
            return alloc.ioBuffer(this.nextReceiveBufferSize);
        }
        
        @Override
        public int guess() {
            return this.nextReceiveBufferSize;
        }
        
        @Override
        public void record(final int actualReadBytes) {
            if (actualReadBytes <= AdaptiveRecvByteBufAllocator.SIZE_TABLE[Math.max(0, this.index - 1 - 1)]) {
                if (this.decreaseNow) {
                    this.index = Math.max(this.index - 1, this.minIndex);
                    this.nextReceiveBufferSize = AdaptiveRecvByteBufAllocator.SIZE_TABLE[this.index];
                    this.decreaseNow = false;
                }
                else {
                    this.decreaseNow = true;
                }
            }
            else if (actualReadBytes >= this.nextReceiveBufferSize) {
                this.index = Math.min(this.index + 4, this.maxIndex);
                this.nextReceiveBufferSize = AdaptiveRecvByteBufAllocator.SIZE_TABLE[this.index];
                this.decreaseNow = false;
            }
        }
    }
}
