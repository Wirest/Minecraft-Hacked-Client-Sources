// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.entity;

import org.apache.http.HttpException;
import org.apache.http.HttpMessage;

public interface ContentLengthStrategy
{
    public static final int IDENTITY = -1;
    public static final int CHUNKED = -2;
    
    long determineLength(final HttpMessage p0) throws HttpException;
}
