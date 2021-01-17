// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn;

import org.apache.http.HttpHost;

public interface SchemePortResolver
{
    int resolve(final HttpHost p0) throws UnsupportedSchemeException;
}
