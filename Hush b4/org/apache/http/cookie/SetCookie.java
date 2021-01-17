// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.cookie;

import java.util.Date;

public interface SetCookie extends Cookie
{
    void setValue(final String p0);
    
    void setComment(final String p0);
    
    void setExpiryDate(final Date p0);
    
    void setDomain(final String p0);
    
    void setPath(final String p0);
    
    void setSecure(final boolean p0);
    
    void setVersion(final int p0);
}
