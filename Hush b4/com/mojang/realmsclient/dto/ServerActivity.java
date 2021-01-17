// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonObject;

public class ServerActivity
{
    public String profileUuid;
    public long joinTime;
    public long leaveTime;
    
    public static ServerActivity parse(final JsonObject element) {
        final ServerActivity sa = new ServerActivity();
        try {
            sa.profileUuid = JsonUtils.getStringOr("profileUuid", element, null);
            sa.joinTime = JsonUtils.getLongOr("joinTime", element, Long.MIN_VALUE);
            sa.leaveTime = JsonUtils.getLongOr("leaveTime", element, Long.MIN_VALUE);
        }
        catch (Exception ex) {}
        return sa;
    }
}
