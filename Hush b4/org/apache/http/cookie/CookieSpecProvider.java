// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.cookie;

import org.apache.http.protocol.HttpContext;

public interface CookieSpecProvider
{
    CookieSpec create(final HttpContext p0);
}
