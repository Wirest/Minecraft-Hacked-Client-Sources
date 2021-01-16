/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 */
package me.razerboy420.weepcraft.alts;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import java.io.PrintStream;
import java.net.Proxy;
import java.util.UUID;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.alts.Alt;
import me.razerboy420.weepcraft.irc.IrcManager;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.util.Session;

public class YggdrasilLoginBridge {
    public static Session loginWithPassword(String username, String password) {
        Session session = null;
        YggdrasilUserAuthentication auth = new YggdrasilUserAuthentication(new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString()), Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            String userName = auth.getSelectedProfile().getName();
            UUID playerUUID = auth.getSelectedProfile().getId();
            String accessToken = auth.getAuthenticatedToken();
            System.out.println(String.valueOf(String.valueOf(userName)) + "'s (UUID: '" + playerUUID.toString() + "') accessToken: " + accessToken);
            session = new Session(userName, playerUUID.toString(), accessToken, username.contains("@") ? "mojang" : "legacy");
            Wrapper.mc().setSession(session);
            Weepcraft.ircManager.changeNick(session.getUsername());
            playerUUID.toString().equalsIgnoreCase("df2523ab-f33c-48e8-bc2d-acfd2fbc8016");
            return session;
        }
        catch (AuthenticationException userName) {
            return null;
        }
    }

    public static Session loginWithoutPassword(String username) {
        Wrapper.mc().setSession(new Session(username, "", "", "legacy"));
        return Wrapper.mc().getSession();
    }

    public static Session loginWithAlt(Alt alt) {
        try {
            if (!alt.isCracked()) {
                return YggdrasilLoginBridge.loginWithPassword(alt.isMigrated() ? alt.email : alt.name, alt.password);
            }
            return YggdrasilLoginBridge.loginWithoutPassword(alt.name);
        }
        catch (Exception e) {
            return null;
        }
    }
}

