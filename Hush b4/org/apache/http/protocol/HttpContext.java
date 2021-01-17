// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

public interface HttpContext
{
    public static final String RESERVED_PREFIX = "http.";
    
    Object getAttribute(final String p0);
    
    void setAttribute(final String p0, final Object p1);
    
    Object removeAttribute(final String p0);
}
