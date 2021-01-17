// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http;

import java.util.Set;

public interface Cookie extends Comparable<Cookie>
{
    String getName();
    
    String getValue();
    
    void setValue(final String p0);
    
    String getDomain();
    
    void setDomain(final String p0);
    
    String getPath();
    
    void setPath(final String p0);
    
    String getComment();
    
    void setComment(final String p0);
    
    long getMaxAge();
    
    void setMaxAge(final long p0);
    
    int getVersion();
    
    void setVersion(final int p0);
    
    boolean isSecure();
    
    void setSecure(final boolean p0);
    
    boolean isHttpOnly();
    
    void setHttpOnly(final boolean p0);
    
    String getCommentUrl();
    
    void setCommentUrl(final String p0);
    
    boolean isDiscard();
    
    void setDiscard(final boolean p0);
    
    Set<Integer> getPorts();
    
    void setPorts(final int... p0);
    
    void setPorts(final Iterable<Integer> p0);
}
