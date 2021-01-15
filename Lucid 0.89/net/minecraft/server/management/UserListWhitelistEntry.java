package net.minecraft.server.management;

import java.util.UUID;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

public class UserListWhitelistEntry extends UserListEntry
{

    public UserListWhitelistEntry(GameProfile profile)
    {
        super(profile);
    }

    public UserListWhitelistEntry(JsonObject p_i1130_1_)
    {
        super(gameProfileFromJsonObject(p_i1130_1_), p_i1130_1_);
    }

    @Override
	protected void onSerialization(JsonObject data)
    {
        if (this.getValue() != null)
        {
            data.addProperty("uuid", ((GameProfile)this.getValue()).getId() == null ? "" : ((GameProfile)this.getValue()).getId().toString());
            data.addProperty("name", ((GameProfile)this.getValue()).getName());
            super.onSerialization(data);
        }
    }

    private static GameProfile gameProfileFromJsonObject(JsonObject p_152646_0_)
    {
        if (p_152646_0_.has("uuid") && p_152646_0_.has("name"))
        {
            String var1 = p_152646_0_.get("uuid").getAsString();
            UUID var2;

            try
            {
                var2 = UUID.fromString(var1);
            }
            catch (Throwable var4)
            {
                return null;
            }

            return new GameProfile(var2, p_152646_0_.get("name").getAsString());
        }
        else
        {
            return null;
        }
    }
}
