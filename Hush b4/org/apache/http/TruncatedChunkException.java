// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http;

public class TruncatedChunkException extends MalformedChunkCodingException
{
    private static final long serialVersionUID = -23506263930279460L;
    
    public TruncatedChunkException(final String message) {
        super(message);
    }
}
