// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.util;

import java.util.HashMap;
import java.util.Map;

public class UploadTokenCache
{
    private static Map<Long, String> tokenCache;
    
    public static String get(final long worldId) {
        return UploadTokenCache.tokenCache.get(worldId);
    }
    
    public static void invalidate(final long world) {
        UploadTokenCache.tokenCache.remove(world);
    }
    
    public static void put(final long wid, final String token) {
        UploadTokenCache.tokenCache.put(wid, token);
    }
    
    static {
        UploadTokenCache.tokenCache = new HashMap<Long, String>();
    }
}
