package com.etb.client.utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class LoginUtils {

    public static void login(final String username, final String password) {
        if (password == null) {
            Minecraft.getMinecraft().setSession(new Session(username, "", "", "legacy"));
        } else {
            final YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
            authentication.setUsername(username);
            authentication.setPassword(password);
            try {
                authentication.logIn();
                Minecraft.getMinecraft().setSession(new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), "mojang"));
            } catch (AuthenticationException e) {
            }
        }
    }

    public static void loginFromClipboard() {
        try {
            final Toolkit toolkit = Toolkit.getDefaultToolkit();
            final Clipboard clipboard = toolkit.getSystemClipboard();
            final String result = (String) clipboard.getData(DataFlavor.stringFlavor);
            if (result.contains(":")) {
                final String[] combo = result.split(":");
                login(combo[0], combo[1]);
            } else {
                login(result, null);
            }
        } catch (UnsupportedFlavorException e) {
        } catch (IOException e) {
        }
    }
}
