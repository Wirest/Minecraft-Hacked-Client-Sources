package com.ihl.client;

import com.ihl.client.accounts.Account;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;

public class Auth {

    public static void login(Account account) {
        setSessionData(account.email, account.password);
    }

    public static int setSessionData(String user, String pass) {
        if (pass.length() != 0) {
            YggdrasilAuthenticationService authentication = new YggdrasilAuthenticationService(Proxy.NO_PROXY,
                    "");
            YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) authentication
                    .createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(user);
            auth.setPassword(pass);
            try {
                auth.logIn();
                Minecraft.getMinecraft().session = new Session(auth.getSelectedProfile().getName(),
                        auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "legacy");
                return 1;
            } catch (Exception e) {
            }
            return 0;
        } else {
            Minecraft.getMinecraft().session = new Session(user, "0", "0", "legacy");
            return 2;
        }
    }

}
