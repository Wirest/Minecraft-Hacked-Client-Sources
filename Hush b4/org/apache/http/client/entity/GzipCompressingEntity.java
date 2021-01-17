// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.entity;

import java.util.zip.GZIPOutputStream;
import org.apache.http.util.Args;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.message.BasicHeader;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

public class GzipCompressingEntity extends HttpEntityWrapper
{
    private static final String GZIP_CODEC = "gzip";
    
    public GzipCompressingEntity(final HttpEntity entity) {
        super(entity);
    }
    
    @Override
    public Header getContentEncoding() {
        return new BasicHeader("Content-Encoding", "gzip");
    }
    
    @Override
    public long getContentLength() {
        return -1L;
    }
    
    @Override
    public boolean isChunked() {
        return true;
    }
    
    @Override
    public InputStream getContent() throws IOException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        final GZIPOutputStream gzip = new GZIPOutputStream(outstream);
        try {
            this.wrappedEntity.writeTo(gzip);
        }
        finally {
            gzip.close();
        }
    }
}
