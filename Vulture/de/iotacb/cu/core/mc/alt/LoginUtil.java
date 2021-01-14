package de.iotacb.cu.core.mc.alt;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class LoginUtil {

	private static final Minecraft MC = Minecraft.getMinecraft();
	public static final LoginUtil INSTANCE = new LoginUtil();
	
	public final void login(final String email, final String password) {
		final YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
		
		authentication.setUsername(email);
		authentication.setPassword(password);
		
		try {
			authentication.logIn();
			
			MC.setSession(new Session(authentication.getSelectedProfile().getName(), authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(), "mojang"));
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
