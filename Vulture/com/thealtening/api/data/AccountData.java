package com.thealtening.api.data;

import com.thealtening.api.data.info.*;

public class AccountData
{
    private String token;
    private String password;
    private String username;
    private boolean limit;
    private AccountInfo info;
    
    public String getToken() {
        return this.token;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public boolean isLimit() {
        return this.limit;
    }
    
    public AccountInfo getInfo() {
        return this.info;
    }
    
    @Override
    public String toString() {
        return String.format("AccountData[%s:%s:%s:%s]", this.token, this.username, this.password, this.limit);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof AccountData)) {
            return false;
        }
        final AccountData castedAccountInfo = (AccountData)obj;
        return castedAccountInfo.getUsername().equals(this.username) && castedAccountInfo.getToken().equals(this.token);
    }
}
