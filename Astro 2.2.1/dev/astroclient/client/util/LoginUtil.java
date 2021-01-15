package dev.astroclient.client.util;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.api.response.Account;
import dev.astroclient.client.ui.menu.account.Status;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;

/**
 * @author Zane2711 for PublicBase
 * @since 10/28/19
 */

public class LoginUtil {

    public static void login(Account account) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
        auth.setPassword(account.getPassword());
        auth.setUsername(account.getUsername());
        try {
            auth.logIn();
            Minecraft.getMinecraft().session = new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
            Status.STATUS = "Session started with username - " + Minecraft.getMinecraft().session.getUsername();
        } catch (Exception e) {
            Status.STATUS = "Login failed";
        }

    }
}
