// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.entity;

import java.io.OutputStream;
import org.apache.http.Header;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;

public class DeflateDecompressingEntity extends DecompressingEntity
{
    public DeflateDecompressingEntity(final HttpEntity entity) {
        super(entity);
    }
    
    @Override
    InputStream decorate(final InputStream wrapped) throws IOException {
        return new DeflateInputStream(wrapped);
    }
    
    @Override
    public Header getContentEncoding() {
        return null;
    }
    
    @Override
    public long getContentLength() {
        return -1L;
    }
}
