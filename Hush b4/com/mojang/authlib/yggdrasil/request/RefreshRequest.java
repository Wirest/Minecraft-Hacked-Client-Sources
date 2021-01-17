// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.yggdrasil.request;

import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.GameProfile;

public class RefreshRequest
{
    private String clientToken;
    private String accessToken;
    private GameProfile selectedProfile;
    private boolean requestUser;
    
    public RefreshRequest(final YggdrasilUserAuthentication authenticationService) {
        this(authenticationService, null);
    }
    
    public RefreshRequest(final YggdrasilUserAuthentication authenticationService, final GameProfile profile) {
        this.requestUser = true;
        this.clientToken = authenticationService.getAuthenticationService().getClientToken();
        this.accessToken = authenticationService.getAuthenticatedToken();
        this.selectedProfile = profile;
    }
}
