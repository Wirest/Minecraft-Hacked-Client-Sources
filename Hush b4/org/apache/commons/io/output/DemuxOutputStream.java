// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class DemuxOutputStream extends OutputStream
{
    private final InheritableThreadLocal<OutputStream> m_streams;
    
    public DemuxOutputStream() {
        this.m_streams = new InheritableThreadLocal<OutputStream>();
    }
    
    public OutputStream bindStream(final OutputStream output) {
        final OutputStream stream = this.m_streams.get();
        this.m_streams.set(output);
        return stream;
    }
    
    @Override
    public void close() throws IOException {
        final OutputStream output = this.m_streams.get();
        if (null != output) {
            output.close();
        }
    }
    
    @Override
    public void flush() throws IOException {
        final OutputStream output = this.m_streams.get();
        if (null != output) {
            output.flush();
        }
    }
    
    @Override
    public void write(final int ch) throws IOException {
        final OutputStream output = this.m_streams.get();
        if (null != output) {
            output.write(ch);
        }
    }
}
