// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.cookie;

import java.util.Date;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.cookie.SetCookie2;

@NotThreadSafe
public class BasicClientCookie2 extends BasicClientCookie implements SetCookie2
{
    private static final long serialVersionUID = -7744598295706617057L;
    private String commentURL;
    private int[] ports;
    private boolean discard;
    
    public BasicClientCookie2(final String name, final String value) {
        super(name, value);
    }
    
    @Override
    public int[] getPorts() {
        return this.ports;
    }
    
    public void setPorts(final int[] ports) {
        this.ports = ports;
    }
    
    @Override
    public String getCommentURL() {
        return this.commentURL;
    }
    
    public void setCommentURL(final String commentURL) {
        this.commentURL = commentURL;
    }
    
    public void setDiscard(final boolean discard) {
        this.discard = discard;
    }
    
    @Override
    public boolean isPersistent() {
        return !this.discard && super.isPersistent();
    }
    
    @Override
    public boolean isExpired(final Date date) {
        return this.discard || super.isExpired(date);
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        final BasicClientCookie2 clone = (BasicClientCookie2)super.clone();
        if (this.ports != null) {
            clone.ports = this.ports.clone();
        }
        return clone;
    }
}
