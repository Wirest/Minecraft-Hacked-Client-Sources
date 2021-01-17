// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.auth;

import org.apache.http.util.LangUtils;
import java.security.Principal;
import org.apache.http.util.Args;
import org.apache.http.annotation.Immutable;
import java.io.Serializable;

@Immutable
public class UsernamePasswordCredentials implements Credentials, Serializable
{
    private static final long serialVersionUID = 243343858802739403L;
    private final BasicUserPrincipal principal;
    private final String password;
    
    public UsernamePasswordCredentials(final String usernamePassword) {
        Args.notNull(usernamePassword, "Username:password string");
        final int atColon = usernamePassword.indexOf(58);
        if (atColon >= 0) {
            this.principal = new BasicUserPrincipal(usernamePassword.substring(0, atColon));
            this.password = usernamePassword.substring(atColon + 1);
        }
        else {
            this.principal = new BasicUserPrincipal(usernamePassword);
            this.password = null;
        }
    }
    
    public UsernamePasswordCredentials(final String userName, final String password) {
        Args.notNull(userName, "Username");
        this.principal = new BasicUserPrincipal(userName);
        this.password = password;
    }
    
    public Principal getUserPrincipal() {
        return this.principal;
    }
    
    public String getUserName() {
        return this.principal.getName();
    }
    
    public String getPassword() {
        return this.password;
    }
    
    @Override
    public int hashCode() {
        return this.principal.hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof UsernamePasswordCredentials) {
            final UsernamePasswordCredentials that = (UsernamePasswordCredentials)o;
            if (LangUtils.equals(this.principal, that.principal)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return this.principal.toString();
    }
}
