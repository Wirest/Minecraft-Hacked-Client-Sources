package me.Corbis.Execution.ui.AltManager;

import java.io.IOException;
import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;


import com.thealtening.auth.service.AlteningServiceType;
import me.Corbis.Execution.Execution;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

public final class AltLoginThread extends Thread {
    private Alt alt;
    private String status;
    private Minecraft mc = Minecraft.getMinecraft();
    boolean mojang;
    String username;
    String password;

    public AltLoginThread(Alt alt) {
        super("Alt Login Thread");
        this.mc = Minecraft.getMinecraft();
        this.alt = alt;
        this.status = EnumChatFormatting.GRAY + "Waiting...";
        mojang = true;
    }

    public AltLoginThread(String username, String password, boolean moijang){
        this.username = username;
        this.password = password;
        this.mojang = moijang;
        this.alt = new Alt(username, password);
    }

    private Session createSession(final String username, final String password) {
        if(mojang){
            Execution.instance.switchService(AlteningServiceType.MOJANG);
        }else {
            Execution.instance.switchService(AlteningServiceType.THEALTENING);
        }
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
                Execution.instance.getFileManager().getFile(Alts.class).saveFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setStatus(final String status) {
        this.status = status;
    }
}

