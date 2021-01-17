// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.minecraft;

import com.mojang.authlib.AuthenticationService;

public abstract class BaseMinecraftSessionService implements MinecraftSessionService
{
    private final AuthenticationService authenticationService;
    
    protected BaseMinecraftSessionService(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
    
    public AuthenticationService getAuthenticationService() {
        return this.authenticationService;
    }
}
