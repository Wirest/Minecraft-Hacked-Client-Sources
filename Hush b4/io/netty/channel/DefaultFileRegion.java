// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.FileChannel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.AbstractReferenceCounted;

public class DefaultFileRegion extends AbstractReferenceCounted implements FileRegion
{
    private static final InternalLogger logger;
    private final FileChannel file;
    private final long position;
    private final long count;
    private long transfered;
    
    public DefaultFileRegion(final FileChannel file, final long position, final long count) {
        if (file == null) {
            throw new NullPointerException("file");
        }
        if (position < 0L) {
            throw new IllegalArgumentException("position must be >= 0 but was " + position);
        }
        if (count < 0L) {
            throw new IllegalArgumentException("count must be >= 0 but was " + count);
        }
        this.file = file;
        this.position = position;
        this.count = count;
    }
    
    @Override
    public long position() {
        return this.position;
    }
    
    @Override
    public long count() {
        return this.count;
    }
    
    @Override
    public long transfered() {
        return this.transfered;
    }
    
    @Override
    public long transferTo(final WritableByteChannel target, final long position) throws IOException {
        final long count = this.count - position;
        if (count < 0L || position < 0L) {
            throw new IllegalArgumentException("position out of range: " + position + " (expected: 0 - " + (this.count - 1L) + ')');
        }
        if (count == 0L) {
            return 0L;
        }
        final long written = this.file.transferTo(this.position + position, count, target);
        if (written > 0L) {
            this.transfered += written;
        }
        return written;
    }
    
    @Override
    protected void deallocate() {
        try {
            this.file.close();
        }
        catch (IOException e) {
            if (DefaultFileRegion.logger.isWarnEnabled()) {
                DefaultFileRegion.logger.warn("Failed to close a file.", e);
            }
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(DefaultFileRegion.class);
    }
}
