// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.auth;

import org.apache.http.util.LangUtils;
import org.apache.http.util.Args;
import org.apache.http.annotation.Immutable;
import java.io.Serializable;
import java.security.Principal;

@Immutable
public final class BasicUserPrincipal implements Principal, Serializable
{
    private static final long serialVersionUID = -2266305184969850467L;
    private final String username;
    
    public BasicUserPrincipal(final String username) {
        Args.notNull(username, "User name");
        this.username = username;
    }
    
    public String getName() {
        return this.username;
    }
    
    @Override
    public int hashCode() {
        int hash = 17;
        hash = LangUtils.hashCode(hash, this.username);
        return hash;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof BasicUserPrincipal) {
            final BasicUserPrincipal that = (BasicUserPrincipal)o;
            if (LangUtils.equals(this.username, that.username)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("[principal: ");
        buffer.append(this.username);
        buffer.append("]");
        return buffer.toString();
    }
}
