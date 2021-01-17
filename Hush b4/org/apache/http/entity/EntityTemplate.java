// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.entity;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.apache.http.util.Args;

public class EntityTemplate extends AbstractHttpEntity
{
    private final ContentProducer contentproducer;
    
    public EntityTemplate(final ContentProducer contentproducer) {
        this.contentproducer = Args.notNull(contentproducer, "Content producer");
    }
    
    public long getContentLength() {
        return -1L;
    }
    
    public InputStream getContent() throws IOException {
        final ByteArrayOutputStream buf = new ByteArrayOutputStream();
        this.writeTo(buf);
        return new ByteArrayInputStream(buf.toByteArray());
    }
    
    public boolean isRepeatable() {
        return true;
    }
    
    public void writeTo(final OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        this.contentproducer.writeTo(outstream);
    }
    
    public boolean isStreaming() {
        return false;
    }
}
