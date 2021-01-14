/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.UserAuthentication
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 */
package me.memewaredevs.altmanager;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.auth.SSLController;
import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;

import java.net.Proxy;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

public final class AltLoginThread
        extends Thread {
    private final String password;
    private String status;
    private final String username;
    private Minecraft mc = Minecraft.getMinecraft();
    private SSLController ssl = new SSLController();
    private TheAlteningAuthentication serviceSwitch = TheAlteningAuthentication.mojang();

    public AltLoginThread(String username, String password) {
        super("Alt Login Thread");
        this.username = username;
        this.password = password;
        this.status = (Object) ((Object) EnumChatFormatting.GRAY) + "Waiting...";
    }

    private Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            if (username.endsWith("@alt.com")) {
                this.ssl.disableCertificateValidation();
                this.serviceSwitch.updateService(AlteningServiceType.THEALTENING);
            } else if (this.serviceSwitch.getService() == AlteningServiceType.THEALTENING) {
                this.ssl.enableCertificateValidation();
                this.serviceSwitch.updateService(AlteningServiceType.MOJANG);
            }
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        } catch (AuthenticationException localAuthenticationException) {
            localAuthenticationException.printStackTrace();
            return null;
        } catch (NullPointerException XD3) {
            try {
                auth.logIn();
            } catch (AuthenticationException e) {
                e.printStackTrace();
            }
            System.out.print(auth.getAuthenticatedToken());
            return new Session(username.split("@")[0], UUID.randomUUID().toString(), auth.getAuthenticatedToken(), "mojang");
        }
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public void run() {
        if (this.password.equals("")) {
            this.mc.session = new Session(this.username.replace("&", "\u00a7"), "", "", "mojang");
            this.status = (Object) ((Object) EnumChatFormatting.GREEN) + "Logged in. (" + this.username + " - offline name)";
            return;
        }
        this.status = (Object) ((Object) EnumChatFormatting.AQUA) + "Logging in...";
        Session auth = this.createSession(this.username, this.password);
        if (auth == null) {
            this.status = (Object) ((Object) EnumChatFormatting.RED) + "Login failed!";
        } else {
            AltManager.lastAlt = new Alt(this.username, this.password);
            this.status = (Object) ((Object) EnumChatFormatting.GREEN) + "Logged in. (" + auth.getUsername() + ")";
            this.mc.session = auth;
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

