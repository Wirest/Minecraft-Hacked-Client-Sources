package skyline.specc.utils;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import net.minecraft.client.Mineman;
import net.minecraft.util.Session;

public class LoginUtils {


	public static void login(String name, String password){
		YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(
				Proxy.NO_PROXY, "");
		YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) authenticationService
				.createUserAuthentication(Agent.MINECRAFT);
		authentication.setUsername(name);
		authentication.setPassword(password);

		try{
			authentication.logIn();
			Mineman.getMinecraft().session = new Session(authentication
					.getSelectedProfile().getName(), authentication
					.getSelectedProfile().getId().toString(),
					authentication.getAuthenticatedToken(), "mojang");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
