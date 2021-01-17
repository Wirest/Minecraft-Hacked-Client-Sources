// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.input;

import java.io.IOException;
import org.apache.commons.io.TaggedIOException;
import java.util.UUID;
import java.io.InputStream;
import java.io.Serializable;

public class TaggedInputStream extends ProxyInputStream
{
    private final Serializable tag;
    
    public TaggedInputStream(final InputStream proxy) {
        super(proxy);
        this.tag = UUID.randomUUID();
    }
    
    public boolean isCauseOf(final Throwable exception) {
        return TaggedIOException.isTaggedWith(exception, this.tag);
    }
    
    public void throwIfCauseOf(final Throwable throwable) throws IOException {
        TaggedIOException.throwCauseIfTaggedWith(throwable, this.tag);
    }
    
    @Override
    protected void handleIOException(final IOException e) throws IOException {
        throw new TaggedIOException(e, this.tag);
    }
}
