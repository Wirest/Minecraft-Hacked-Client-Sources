// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.account;

public class Alt
{
    public String email;
    public String username;
    public String password;
    
    public Alt(String email, String name, String pass) {
        this.email = email;
        this.username = name;
        this.password = pass;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setEmail(final String email) {
        this.email = email;
    }
    
    public void setUsername(final String name) {
        this.username = name;
    }
    
    public void setPassword(final String pass) {
        this.password = pass;
    }
}
