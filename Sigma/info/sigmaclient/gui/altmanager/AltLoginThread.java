// 
// Decompiled by Procyon v0.5.30
// 

package info.sigmaclient.gui.altmanager;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import java.io.IOException;
import java.net.Proxy;

import info.sigmaclient.Client;
import net.minecraft.util.Session;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.Minecraft;

public final class AltLoginThread extends Thread {
    private Alt alt;
    private String status;
    private Minecraft mc;

    public AltLoginThread(Alt alt) {
        super("Alt Login Thread");
        this.mc = Minecraft.getMinecraft();
        this.alt = alt;
        this.status = EnumChatFormatting.GRAY + "Waiting...";
    }

    private Session createSession(final String username, final String password) {
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        } catch (AuthenticationException localAuthenticationException) {
            localAuthenticationException.printStackTrace();
            return null;
        }
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public void run() {
        if (alt.getPassword().equals("")) {
            this.mc.session = new Session(alt.getUsername(), "", "", "mojang");
            this.status = EnumChatFormatting.GREEN + "Logged in. (" + alt.getUsername() + " - offline name)";
            return;
        }
        this.status = EnumChatFormatting.AQUA + "Logging in...";
        final Session auth = this.createSession(alt.getUsername(), alt.getPassword());
        if (auth == null) {
            this.status = EnumChatFormatting.RED + "Login failed!";
        } else {
            AltManager.lastAlt = new Alt(alt.getUsername(), alt.getPassword());
            this.status = EnumChatFormatting.GREEN + "Logged in. (" + auth.getUsername() + ")";
            alt.setMask(auth.getUsername());
            this.mc.session = auth;
            try {
                Client.getFileManager().getFile(Alts.class).saveFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setStatus(final String status) {
        this.status = status;
    }
}
