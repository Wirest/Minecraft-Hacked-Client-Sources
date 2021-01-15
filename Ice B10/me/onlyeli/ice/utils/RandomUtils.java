package me.onlyeli.ice.utils;

import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.util.Session;
import net.minecraft.client.Minecraft;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;

public class RandomUtils
{
    public static String loginPassword(final String username, final String password) {
        if (username == null || username.length() < 0 || password == null || password.length() <= 0) {
            return null;
        }
        final YggdrasilAuthenticationService a = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication b = (YggdrasilUserAuthentication)a.createUserAuthentication(Agent.MINECRAFT);
        b.setUsername(username);
        b.setPassword(password);
        try {
            b.logIn();
            Minecraft.getMinecraft().session = new Session(b.getSelectedProfile().getName(), b.getSelectedProfile().getId().toString(), b.getAuthenticatedToken(), Agent.MINECRAFT.toString());
        }
        catch (AuthenticationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
