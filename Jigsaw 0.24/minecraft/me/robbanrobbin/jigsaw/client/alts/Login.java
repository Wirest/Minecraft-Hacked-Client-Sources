package me.robbanrobbin.jigsaw.client.alts;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.gui.GuiJigsawAltLogin;
import me.robbanrobbin.jigsaw.gui.GuiJigsawAltManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class Login {

	public static boolean login(String email, String password) throws AuthenticationException {
		login(email, password, Proxy.NO_PROXY);
		Jigsaw.loggedInName = null;
		return true;
	}

	public static void login(String email, String password, Proxy proxy) throws AuthenticationException {
		YggdrasilAuthenticationService authService = new YggdrasilAuthenticationService(proxy, "");
		UserAuthentication auth = authService.createUserAuthentication(Agent.MINECRAFT);

		auth.setUsername(email);
		auth.setPassword(password);
		auth.logIn();
		Minecraft.getMinecraft().session = new Session(auth.getSelectedProfile().getName(),
				auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
	}

	public static void changeName(String newName) {
		if (newName.equals("NoHaxJustJigsaw") || newName.equals("Robin146") || newName.equals("JigsawClient")
				|| newName.equals("JigsawDev")) {
			newName = "succ";
		}
		Minecraft.getMinecraft().session = new Session(newName, "", "", "mojang");
		Jigsaw.loggedInName = null;
	}

}
