package me.rigamortis.faurax.login;

import net.minecraft.client.*;
import net.minecraft.util.*;
import java.net.*;
import com.mojang.authlib.yggdrasil.*;
import com.mojang.authlib.*;
import com.mojang.authlib.exceptions.*;

public class Login
{
    public Login(final String name, final String password, final boolean online) {
        if (online) {
            if (name != null && password != null) {
                new Thread() {
                    @Override
                    public void run() {
                        Login.loginPassword(name, password);
                    }
                }.start();
            }
            else {
                System.out.println("Username and/or password is incorect.");
            }
        }
        else {
            loginPasswordOffline(name);
        }
    }
    
    public static void loginPasswordOffline(final String username) {
        Minecraft.getMinecraft().session = new Session(username, username, username, "MOJANG");
    }
    
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
            Minecraft.getMinecraft().session = new Session(b.getSelectedProfile().getName(), b.getSelectedProfile().getId().toString(), b.getAuthenticatedToken(), "MOJANG");
        }
        catch (AuthenticationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
