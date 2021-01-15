// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.AltManager;

import me.CheerioFX.FusionX.FusionX;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import net.minecraft.util.Session;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.Minecraft;

public final class AltLoginThread extends Thread
{
    private final String password;
    private String status;
    private final String username;
    private Minecraft mc;
    
    public AltLoginThread(final String username, final String password) {
        super("Alt Login Thread");
        this.mc = Minecraft.getMinecraft();
        this.username = username;
        this.password = password;
        this.status = EnumChatFormatting.GRAY + "Waiting...";
    }
    
    private Session createSession(final String username, final String password) {
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        }
        catch (AuthenticationException localAuthenticationException) {
            localAuthenticationException.printStackTrace();
            return null;
        }
    }
    
    public String getStatus() {
        return this.status;
    }
    
    @Override
    public void run() {
        if (this.password.equals("")) {
            this.mc.session = new Session(this.username, "", "", "mojang");
            this.status = EnumChatFormatting.GREEN + "Logged in. (" + this.username + " - cracked)";
            return;
        }
        this.status = EnumChatFormatting.YELLOW + "Logging in...";
        final Session auth = this.createSession(this.username, this.password);
        if (auth == null) {
            this.status = EnumChatFormatting.RED + "Login failed!";
        }
        else {
            final AltManager altManager = FusionX.theClient.altManager;
            AltManager.lastAlt = new Alt(this.username, this.password);
            this.status = EnumChatFormatting.GREEN + "Logged in. (" + auth.getUsername() + ")";
            this.mc.session = auth;
        }
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
}
