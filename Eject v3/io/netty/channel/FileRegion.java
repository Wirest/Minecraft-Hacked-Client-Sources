package io.netty.channel;

import io.netty.util.ReferenceCounted;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

public abstract interface FileRegion
        extends ReferenceCounted {
    public abstract long position();

    public abstract long transfered();

    public abstract long count();

    public abstract long transferTo(WritableByteChannel paramWritableByteChannel, long paramLong)
            throws IOException;
}




