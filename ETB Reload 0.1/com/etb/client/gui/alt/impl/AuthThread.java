package com.etb.client.gui.alt.impl;

import com.etb.client.Client;
import com.etb.client.gui.alt.account.Account;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;


public class AuthThread extends Thread {
    private final String password;
    private String status;
    private final String username;
    private Minecraft mc = Minecraft.getMinecraft();

    public AuthThread(String username, String password) {
        super("Alt Login Thread");
        this.username = username;
        this.password = password;
        status = (ChatFormatting.GRAY + "Waiting...");
    }

    private Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(java.net.Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();

            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        } catch (AuthenticationException localAuthenticationException) {
            localAuthenticationException.printStackTrace();
        }
        return null;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public void run() {
        if (mc.currentScreen instanceof GuiAlteningLogin) Client.INSTANCE.switchToTheAltening();
        else Client.INSTANCE.switchToMojang();
        if (password.equals("")) {
            mc.session = new Session(username, "", "", "mojang");
            status = (ChatFormatting.GREEN + "Logged in. (" + username + " - offline name)");
            Client.INSTANCE.getAccountManager().getAltSaving().saveLastAltFile();
            return;
        }
        status = (ChatFormatting.AQUA + "Logging in...");
        Session auth = createSession(username, password);
        if (auth == null) {
            status = (ChatFormatting.RED + "Login failed!");
        } else {
            Client.INSTANCE.getAccountManager().setLastAlt((new Account(username, password)));
            status = (ChatFormatting.GREEN + "Logged in. (" + auth.getUsername() + ")");
            mc.session = auth;
            Client.INSTANCE.getAccountManager().getAltSaving().saveLastAltFile();
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
