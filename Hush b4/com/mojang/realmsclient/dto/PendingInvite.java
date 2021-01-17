// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonObject;
import java.util.Date;
import org.apache.logging.log4j.Logger;

public class PendingInvite extends ValueObject
{
    private static final Logger LOGGER;
    public String invitationId;
    public String worldName;
    public String worldOwnerName;
    public String worldOwnerUuid;
    public Date date;
    
    public static PendingInvite parse(final JsonObject json) {
        final PendingInvite invite = new PendingInvite();
        try {
            invite.invitationId = JsonUtils.getStringOr("invitationId", json, "");
            invite.worldName = JsonUtils.getStringOr("worldName", json, "");
            invite.worldOwnerName = JsonUtils.getStringOr("worldOwnerName", json, "");
            invite.worldOwnerUuid = JsonUtils.getStringOr("worldOwnerUuid", json, "");
            invite.date = JsonUtils.getDateOr("date", json);
        }
        catch (Exception e) {
            PendingInvite.LOGGER.error("Could not parse PendingInvite: " + e.getMessage());
        }
        return invite;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
