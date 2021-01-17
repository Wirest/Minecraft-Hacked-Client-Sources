// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.cookie;

import java.util.Locale;
import java.util.HashMap;
import org.apache.http.util.Args;
import java.util.Date;
import java.util.Map;
import org.apache.http.annotation.NotThreadSafe;
import java.io.Serializable;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.SetCookie;

@NotThreadSafe
public class BasicClientCookie implements SetCookie, ClientCookie, Cloneable, Serializable
{
    private static final long serialVersionUID = -3869795591041535538L;
    private final String name;
    private Map<String, String> attribs;
    private String value;
    private String cookieComment;
    private String cookieDomain;
    private Date cookieExpiryDate;
    private String cookiePath;
    private boolean isSecure;
    private int cookieVersion;
    
    public BasicClientCookie(final String name, final String value) {
        Args.notNull(name, "Name");
        this.name = name;
        this.attribs = new HashMap<String, String>();
        this.value = value;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public String getComment() {
        return this.cookieComment;
    }
    
    public void setComment(final String comment) {
        this.cookieComment = comment;
    }
    
    public String getCommentURL() {
        return null;
    }
    
    public Date getExpiryDate() {
        return this.cookieExpiryDate;
    }
    
    public void setExpiryDate(final Date expiryDate) {
        this.cookieExpiryDate = expiryDate;
    }
    
    public boolean isPersistent() {
        return null != this.cookieExpiryDate;
    }
    
    public String getDomain() {
        return this.cookieDomain;
    }
    
    public void setDomain(final String domain) {
        if (domain != null) {
            this.cookieDomain = domain.toLowerCase(Locale.ENGLISH);
        }
        else {
            this.cookieDomain = null;
        }
    }
    
    public String getPath() {
        return this.cookiePath;
    }
    
    public void setPath(final String path) {
        this.cookiePath = path;
    }
    
    public boolean isSecure() {
        return this.isSecure;
    }
    
    public void setSecure(final boolean secure) {
        this.isSecure = secure;
    }
    
    public int[] getPorts() {
        return null;
    }
    
    public int getVersion() {
        return this.cookieVersion;
    }
    
    public void setVersion(final int version) {
        this.cookieVersion = version;
    }
    
    public boolean isExpired(final Date date) {
        Args.notNull(date, "Date");
        return this.cookieExpiryDate != null && this.cookieExpiryDate.getTime() <= date.getTime();
    }
    
    public void setAttribute(final String name, final String value) {
        this.attribs.put(name, value);
    }
    
    public String getAttribute(final String name) {
        return this.attribs.get(name);
    }
    
    public boolean containsAttribute(final String name) {
        return this.attribs.get(name) != null;
    }
    
    public Object clone() throws CloneNotSupportedException {
        final BasicClientCookie clone = (BasicClientCookie)super.clone();
        clone.attribs = new HashMap<String, String>(this.attribs);
        return clone;
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("[version: ");
        buffer.append(Integer.toString(this.cookieVersion));
        buffer.append("]");
        buffer.append("[name: ");
        buffer.append(this.name);
        buffer.append("]");
        buffer.append("[value: ");
        buffer.append(this.value);
        buffer.append("]");
        buffer.append("[domain: ");
        buffer.append(this.cookieDomain);
        buffer.append("]");
        buffer.append("[path: ");
        buffer.append(this.cookiePath);
        buffer.append("]");
        buffer.append("[expiry: ");
        buffer.append(this.cookieExpiryDate);
        buffer.append("]");
        return buffer.toString();
    }
}
