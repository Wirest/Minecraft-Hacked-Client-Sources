// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.core.LogEvent;
import java.nio.charset.Charset;

public abstract class AbstractStringLayout extends AbstractLayout<String>
{
    private final Charset charset;
    
    protected AbstractStringLayout(final Charset charset) {
        this.charset = charset;
    }
    
    @Override
    public byte[] toByteArray(final LogEvent event) {
        return this.toSerializable(event).getBytes(this.charset);
    }
    
    @Override
    public String getContentType() {
        return "text/plain";
    }
    
    protected Charset getCharset() {
        return this.charset;
    }
}
