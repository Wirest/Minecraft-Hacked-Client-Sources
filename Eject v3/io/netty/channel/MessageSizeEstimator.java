package io.netty.channel;

public abstract interface MessageSizeEstimator {
    public abstract Handle newHandle();

    public static abstract interface Handle {
        public abstract int size(Object paramObject);
    }
}




