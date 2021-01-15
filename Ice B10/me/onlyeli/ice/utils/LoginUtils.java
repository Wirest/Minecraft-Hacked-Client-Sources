package me.onlyeli.ice.utils;

import java.net.Proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserType;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class LoginUtils {
	private static final Logger logger;
	private static String displayText;

	static {
		logger = LogManager.getLogger();
	}

	public static String getMessage() {
		return LoginUtils.displayText;
	}

	public static String login(String email, String password) {
		final YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY,
				"");
		final YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) authenticationService
				.createUserAuthentication(Agent.MINECRAFT);
		authentication.setUsername(email);
		authentication.setPassword(password);
		try {
			authentication.logIn();
			Minecraft.getMinecraft().session = new Session(authentication.getSelectedProfile().getName(),
					authentication.getSelectedProfile().getId().toString(), authentication.getAuthenticatedToken(),
					UserType.MOJANG.getName());
			LoginUtils.displayText = "";
		} catch (AuthenticationUnavailableException e2) {
			LoginUtils.displayText = "Cannot contact authentication server!";
		} catch (AuthenticationException e) {
			if (e.getMessage().contains("Invalid username or password.")
					|| e.getMessage().toLowerCase().contains("account migrated")) {
				LoginUtils.displayText = "Wrong password!";
			} else {
				LoginUtils.displayText = "Cannot contact authentication server!";
			}
			LoginUtils.logger.error(e.getMessage());
		} catch (NullPointerException e3) {
			LoginUtils.displayText = "Wrong password!";
		}
		return LoginUtils.displayText;
	}

	public static void changeCrackedName(final String newName) {
		Minecraft.getMinecraft().session = new Session(newName, "", "", UserType.MOJANG.getName());
	}
}
