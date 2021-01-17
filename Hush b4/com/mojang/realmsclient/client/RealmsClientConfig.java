// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.client;

import java.net.Proxy;

public class RealmsClientConfig
{
    private static Proxy proxy;
    
    public static Proxy getProxy() {
        return RealmsClientConfig.proxy;
    }
    
    public static void setProxy(final Proxy proxy) {
        if (RealmsClientConfig.proxy == null) {
            RealmsClientConfig.proxy = proxy;
        }
    }
}
