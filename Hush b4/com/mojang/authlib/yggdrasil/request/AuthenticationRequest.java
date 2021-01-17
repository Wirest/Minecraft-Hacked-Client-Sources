// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.yggdrasil.request;

import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.Agent;

public class AuthenticationRequest
{
    private Agent agent;
    private String username;
    private String password;
    private String clientToken;
    private boolean requestUser;
    
    public AuthenticationRequest(final YggdrasilUserAuthentication authenticationService, final String username, final String password) {
        this.requestUser = true;
        this.agent = authenticationService.getAgent();
        this.username = username;
        this.clientToken = authenticationService.getAuthenticationService().getClientToken();
        this.password = password;
    }
}
