// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.cookie;

import java.util.Date;

public interface Cookie
{
    String getName();
    
    String getValue();
    
    String getComment();
    
    String getCommentURL();
    
    Date getExpiryDate();
    
    boolean isPersistent();
    
    String getDomain();
    
    String getPath();
    
    int[] getPorts();
    
    boolean isSecure();
    
    int getVersion();
    
    boolean isExpired(final Date p0);
}
