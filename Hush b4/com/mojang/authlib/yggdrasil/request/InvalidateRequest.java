// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.yggdrasil.request;

import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

public class InvalidateRequest
{
    private String accessToken;
    private String clientToken;
    
    public InvalidateRequest(final YggdrasilUserAuthentication authenticationService) {
        this.accessToken = authenticationService.getAuthenticatedToken();
        this.clientToken = authenticationService.getAuthenticationService().getClientToken();
    }
}
