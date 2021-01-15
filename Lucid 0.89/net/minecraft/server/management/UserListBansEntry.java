package net.minecraft.server.management;

import java.util.Date;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

public class UserListBansEntry extends BanEntry
{

    public UserListBansEntry(GameProfile profile)
    {
        this(profile, (Date)null, (String)null, (Date)null, (String)null);
    }

    public UserListBansEntry(GameProfile profile, Date startDate, String banner, Date endDate, String banReason)
    {
        super(profile, endDate, banner, endDate, banReason);
    }

    public UserListBansEntry(JsonObject p_i1136_1_)
    {
        super(func_152648_b(p_i1136_1_), p_i1136_1_);
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

    private static GameProfile func_152648_b(JsonObject p_152648_0_)
    {
        if (p_152648_0_.has("uuid") && p_152648_0_.has("name"))
        {
            String var1 = p_152648_0_.get("uuid").getAsString();
            UUID var2;

            try
            {
                var2 = UUID.fromString(var1);
            }
            catch (Throwable var4)
            {
                return null;
            }

            return new GameProfile(var2, p_152648_0_.get("name").getAsString());
        }
        else
        {
            return null;
        }
    }
}
