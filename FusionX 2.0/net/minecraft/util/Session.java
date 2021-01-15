package net.minecraft.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import java.util.Map;
import java.util.UUID;

public class Session
{
    private final String username;
    private final String playerID;
    private final String token;
    private final Session.Type sessionType;
    private static final String __OBFID = "CL_00000659";

    public Session(String p_i1098_1_, String p_i1098_2_, String p_i1098_3_, String p_i1098_4_)
    {
        this.username = p_i1098_1_;
        this.playerID = p_i1098_2_;
        this.token = p_i1098_3_;
        this.sessionType = Session.Type.setSessionType(p_i1098_4_);
    }

    public String getSessionID()
    {
        return "token:" + this.token + ":" + this.playerID;
    }

    public String getPlayerID()
    {
        return this.playerID;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getToken()
    {
        return this.token;
    }

    public GameProfile getProfile()
    {
        try
        {
            UUID var1 = UUIDTypeAdapter.fromString(this.getPlayerID());
            return new GameProfile(var1, this.getUsername());
        }
        catch (IllegalArgumentException var2)
        {
            return new GameProfile((UUID)null, this.getUsername());
        }
    }

    /**
     * Returns either 'legacy' or 'mojang' whether the account is migrated or not
     */
    public Session.Type getSessionType()
    {
        return this.sessionType;
    }

    public static enum Type
    {
        LEGACY("LEGACY", 0, "legacy"),
        MOJANG("MOJANG", 1, "mojang");
        private static final Map field_152425_c = Maps.newHashMap();
        private final String sessionType;

        private static final Session.Type[] $VALUES = new Session.Type[]{LEGACY, MOJANG};
        private static final String __OBFID = "CL_00001851";

        private Type(String p_i1096_1_, int p_i1096_2_, String p_i1096_3_)
        {
            this.sessionType = p_i1096_3_;
        }

        public static Session.Type setSessionType(String p_152421_0_)
        {
            return (Session.Type)field_152425_c.get(p_152421_0_.toLowerCase());
        }

        static {
            Session.Type[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                Session.Type var3 = var0[var2];
                field_152425_c.put(var3.sessionType, var3);
            }
        }
    }
}
