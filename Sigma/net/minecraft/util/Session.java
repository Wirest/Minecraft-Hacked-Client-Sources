package net.minecraft.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.util.UUIDTypeAdapter;

import java.net.Proxy;
import java.util.Map;
import java.util.UUID;

public class Session {
    private final String username;
    private final String playerID;
    private final String token;
    private final Session.Type sessionType;
    private static final String __OBFID = "CL_00000659";

    public Session(String p_i1098_1_, String p_i1098_2_, String p_i1098_3_, String p_i1098_4_) {
        username = p_i1098_1_;
        playerID = p_i1098_2_;
        token = p_i1098_3_;
        sessionType = Session.Type.setSessionType(p_i1098_4_);
    }

    public String getSessionID() {
        return "token:" + token + ":" + playerID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public GameProfile getProfile() {
        try {
            UUID var1 = UUIDTypeAdapter.fromString(getPlayerID());
            return new GameProfile(var1, getUsername());
        } catch (IllegalArgumentException var2) {
            return new GameProfile((UUID) null, getUsername());
        }
    }

    /**
     * Returns either 'legacy' or 'mojang' whether the account is migrated or
     * not
     */
    public Session.Type getSessionType() {
        return sessionType;
    }

    public static enum Type {
        LEGACY("LEGACY", 0, "legacy"), MOJANG("MOJANG", 1, "mojang");
        private static final Map field_152425_c = Maps.newHashMap();
        private final String sessionType;

        private static final Session.Type[] $VALUES = new Session.Type[]{LEGACY, MOJANG};
        private static final String __OBFID = "CL_00001851";

        private Type(String p_i1096_1_, int p_i1096_2_, String p_i1096_3_) {
            sessionType = p_i1096_3_;
        }

        public static Session.Type setSessionType(String p_152421_0_) {
            return (Session.Type) Type.field_152425_c.get(p_152421_0_.toLowerCase());
        }

        static {
            Session.Type[] var0 = Type.values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                Session.Type var3 = var0[var2];
                Type.field_152425_c.put(var3.sessionType, var3);
            }
        }
    }

    public static Session loginPassword(String username, String password, Proxy p) {
        if (username == null || username.length() <= 0 || password == null || password.length() <= 0) {
            return null;
        }
        YggdrasilAuthenticationService a = new YggdrasilAuthenticationService(p, "");
        YggdrasilUserAuthentication b = (YggdrasilUserAuthentication) a.createUserAuthentication(Agent.MINECRAFT);
        b.setUsername(username);
        b.setPassword(password);
        try {
            b.logIn();
            return new Session(b.getSelectedProfile().getName(), b.getSelectedProfile().getId().toString(), b.getAuthenticatedToken(), "legacy");
        } catch (final InvalidCredentialsException e) {
            e.printStackTrace();
        } catch (final AuthenticationException e) {
            e.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
