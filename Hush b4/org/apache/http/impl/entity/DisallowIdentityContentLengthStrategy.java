// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.entity;

import org.apache.http.HttpException;
import org.apache.http.ProtocolException;
import org.apache.http.HttpMessage;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentLengthStrategy;

@Immutable
public class DisallowIdentityContentLengthStrategy implements ContentLengthStrategy
{
    public static final DisallowIdentityContentLengthStrategy INSTANCE;
    private final ContentLengthStrategy contentLengthStrategy;
    
    public DisallowIdentityContentLengthStrategy(final ContentLengthStrategy contentLengthStrategy) {
        this.contentLengthStrategy = contentLengthStrategy;
    }
    
    public long determineLength(final HttpMessage message) throws HttpException {
        final long result = this.contentLengthStrategy.determineLength(message);
        if (result == -1L) {
            throw new ProtocolException("Identity transfer encoding cannot be used");
        }
        return result;
    }
    
    static {
        INSTANCE = new DisallowIdentityContentLengthStrategy(new LaxContentLengthStrategy(0));
    }
}
