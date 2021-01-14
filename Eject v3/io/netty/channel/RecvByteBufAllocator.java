package io.netty.channel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public abstract interface RecvByteBufAllocator {
    public abstract Handle newHandle();

    public static abstract interface Handle {
        public abstract ByteBuf allocate(ByteBufAllocator paramByteBufAllocator);

        public abstract int guess();

        public abstract void record(int paramInt);
    }
}




