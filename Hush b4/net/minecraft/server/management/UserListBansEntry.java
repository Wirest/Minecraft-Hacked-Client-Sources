// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import java.util.UUID;
import com.google.gson.JsonObject;
import java.util.Date;
import com.mojang.authlib.GameProfile;

public class UserListBansEntry extends BanEntry<GameProfile>
{
    public UserListBansEntry(final GameProfile profile) {
        this(profile, null, null, null, null);
    }
    
    public UserListBansEntry(final GameProfile profile, final Date startDate, final String banner, final Date endDate, final String banReason) {
        super(profile, endDate, banner, endDate, banReason);
    }
    
    public UserListBansEntry(final JsonObject p_i1136_1_) {
        super(func_152648_b(p_i1136_1_), p_i1136_1_);
    }
    
    @Override
    protected void onSerialization(final JsonObject data) {
        if (this.getValue() != null) {
            data.addProperty("uuid", (this.getValue().getId() == null) ? "" : this.getValue().getId().toString());
            data.addProperty("name", this.getValue().getName());
            super.onSerialization(data);
        }
    }
    
    private static GameProfile func_152648_b(final JsonObject p_152648_0_) {
        if (p_152648_0_.has("uuid") && p_152648_0_.has("name")) {
            final String s = p_152648_0_.get("uuid").getAsString();
            UUID uuid;
            try {
                uuid = UUID.fromString(s);
            }
            catch (Throwable var4) {
                return null;
            }
            return new GameProfile(uuid, p_152648_0_.get("name").getAsString());
        }
        return null;
    }
}
