// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.account;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import net.minecraft.util.Session;
import me.aristhena.client.gui.account.AccountScreen;
import me.aristhena.utils.ClientUtils;
import net.minecraft.client.Minecraft;

public class LoginThread extends Thread
{
    private Minecraft mc;
    private String pass;
    private String email;
    
    public LoginThread(final Alt alt) {
        super("LoginThread");
        this.mc = ClientUtils.mc();
        AccountScreen.getInstance().lastAlt = alt;
        this.email = alt.getEmail();
        this.pass = alt.getPassword();
        AccountScreen.getInstance().info = "§aLogging In...";
    }
    
    private Session createSession(final String email, final String pass) {
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        authentication.setUsername(email);
        authentication.setPassword(pass);
        try {
            authentication.logIn();
            return new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), "legacy");
        }
        catch (AuthenticationException e) {
            return null;
        }
    }
    
    @Override
    public void run() {
        if (this.pass.equals("")) {
            this.mc.session = new Session(this.email, "", "", "legacy");
            AccountScreen.getInstance().info = "§eLogged In: " + this.email;
            return;
        }
        final Session session = this.createSession(this.email, this.pass);
        if (session == null) {
            AccountScreen.getInstance().info = "§cLogin Failed";
        }
        else {
            this.mc.session = session;
            AccountScreen.getInstance().info = "§aLogged In: " + session.getUsername();
        }
    }
}
