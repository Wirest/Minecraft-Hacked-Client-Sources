// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.util.CharArrayBuffer;
import java.io.IOException;
import org.apache.http.Consts;
import org.apache.http.annotation.Immutable;
import org.apache.http.io.SessionOutputBuffer;

@Deprecated
@Immutable
public class LoggingSessionOutputBuffer implements SessionOutputBuffer
{
    private final SessionOutputBuffer out;
    private final Wire wire;
    private final String charset;
    
    public LoggingSessionOutputBuffer(final SessionOutputBuffer out, final Wire wire, final String charset) {
        this.out = out;
        this.wire = wire;
        this.charset = ((charset != null) ? charset : Consts.ASCII.name());
    }
    
    public LoggingSessionOutputBuffer(final SessionOutputBuffer out, final Wire wire) {
        this(out, wire, null);
    }
    
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.out.write(b, off, len);
        if (this.wire.enabled()) {
            this.wire.output(b, off, len);
        }
    }
    
    public void write(final int b) throws IOException {
        this.out.write(b);
        if (this.wire.enabled()) {
            this.wire.output(b);
        }
    }
    
    public void write(final byte[] b) throws IOException {
        this.out.write(b);
        if (this.wire.enabled()) {
            this.wire.output(b);
        }
    }
    
    public void flush() throws IOException {
        this.out.flush();
    }
    
    public void writeLine(final CharArrayBuffer buffer) throws IOException {
        this.out.writeLine(buffer);
        if (this.wire.enabled()) {
            final String s = new String(buffer.buffer(), 0, buffer.length());
            final String tmp = s + "\r\n";
            this.wire.output(tmp.getBytes(this.charset));
        }
    }
    
    public void writeLine(final String s) throws IOException {
        this.out.writeLine(s);
        if (this.wire.enabled()) {
            final String tmp = s + "\r\n";
            this.wire.output(tmp.getBytes(this.charset));
        }
    }
    
    public HttpTransportMetrics getMetrics() {
        return this.out.getMetrics();
    }
}
