package io.netty.buffer;

import io.netty.util.ReferenceCounted;

public abstract interface ByteBufHolder
        extends ReferenceCounted {
    public abstract ByteBuf content();

    public abstract ByteBufHolder copy();

    public abstract ByteBufHolder duplicate();

    public abstract ByteBufHolder retain();

    public abstract ByteBufHolder retain(int paramInt);
}




