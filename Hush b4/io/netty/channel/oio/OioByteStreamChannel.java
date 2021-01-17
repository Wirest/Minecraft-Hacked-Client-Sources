// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.oio;

import java.nio.channels.ClosedChannelException;
import java.io.EOFException;
import java.nio.channels.Channels;
import io.netty.channel.FileRegion;
import java.nio.channels.NotYetConnectedException;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import io.netty.channel.Channel;
import java.nio.channels.WritableByteChannel;
import java.io.OutputStream;
import java.io.InputStream;

public abstract class OioByteStreamChannel extends AbstractOioByteChannel
{
    private static final InputStream CLOSED_IN;
    private static final OutputStream CLOSED_OUT;
    private InputStream is;
    private OutputStream os;
    private WritableByteChannel outChannel;
    
    protected OioByteStreamChannel(final Channel parent) {
        super(parent);
    }
    
    protected final void activate(final InputStream is, final OutputStream os) {
        if (this.is != null) {
            throw new IllegalStateException("input was set already");
        }
        if (this.os != null) {
            throw new IllegalStateException("output was set already");
        }
        if (is == null) {
            throw new NullPointerException("is");
        }
        if (os == null) {
            throw new NullPointerException("os");
        }
        this.is = is;
        this.os = os;
    }
    
    @Override
    public boolean isActive() {
        final InputStream is = this.is;
        if (is == null || is == OioByteStreamChannel.CLOSED_IN) {
            return false;
        }
        final OutputStream os = this.os;
        return os != null && os != OioByteStreamChannel.CLOSED_OUT;
    }
    
    @Override
    protected int available() {
        try {
            return this.is.available();
        }
        catch (IOException ignored) {
            return 0;
        }
    }
    
    @Override
    protected int doReadBytes(final ByteBuf buf) throws Exception {
        final int length = Math.max(1, Math.min(this.available(), buf.maxWritableBytes()));
        return buf.writeBytes(this.is, length);
    }
    
    @Override
    protected void doWriteBytes(final ByteBuf buf) throws Exception {
        final OutputStream os = this.os;
        if (os == null) {
            throw new NotYetConnectedException();
        }
        buf.readBytes(os, buf.readableBytes());
    }
    
    @Override
    protected void doWriteFileRegion(final FileRegion region) throws Exception {
        final OutputStream os = this.os;
        if (os == null) {
            throw new NotYetConnectedException();
        }
        if (this.outChannel == null) {
            this.outChannel = Channels.newChannel(os);
        }
        long written = 0L;
        while (true) {
            final long localWritten = region.transferTo(this.outChannel, written);
            if (localWritten == -1L) {
                checkEOF(region);
                return;
            }
            written += localWritten;
            if (written >= region.count()) {
                return;
            }
        }
    }
    
    private static void checkEOF(final FileRegion region) throws IOException {
        if (region.transfered() < region.count()) {
            throw new EOFException("Expected to be able to write " + region.count() + " bytes, " + "but only wrote " + region.transfered());
        }
    }
    
    @Override
    protected void doClose() throws Exception {
        final InputStream is = this.is;
        final OutputStream os = this.os;
        this.is = OioByteStreamChannel.CLOSED_IN;
        this.os = OioByteStreamChannel.CLOSED_OUT;
        try {
            if (is != null) {
                is.close();
            }
        }
        finally {
            if (os != null) {
                os.close();
            }
        }
    }
    
    static {
        CLOSED_IN = new InputStream() {
            @Override
            public int read() {
                return -1;
            }
        };
        CLOSED_OUT = new OutputStream() {
            @Override
            public void write(final int b) throws IOException {
                throw new ClosedChannelException();
            }
        };
    }
}
