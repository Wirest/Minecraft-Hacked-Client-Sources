package me.ihaq.iClient.gui.GuiWindows.altmanager;

import java.net.Proxy;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import me.ihaq.iClient.Envy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

public final class AltLoginThread extends Thread {
	private final String password;
	private String status;
	private final String username;
	private Minecraft mc;

	public AltLoginThread(String username, String password) {
		super("Alt Login Thread");
		this.mc = Minecraft.getMinecraft();
		this.username = username;
		this.password = password;
		this.status = (EnumChatFormatting.GRAY + "Waiting...");
	}

	private Session createSession(String username, String password) {
		YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
		YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service
				.createUserAuthentication(Agent.MINECRAFT);
		auth.setUsername(username);
		auth.setPassword(password);
		try {
			auth.logIn();
			return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(),
					auth.getAuthenticatedToken(), "mojang");
		} catch (AuthenticationException localAuthenticationException) {
			localAuthenticationException.printStackTrace();
		}
		return null;
	}

	public String getStatus() {
		return this.status;
	}

	@Override
	public void run() {
		if (this.password.equals("")) {
			this.mc.session = new Session(this.username, "", "", "mojang");
			this.status = (EnumChatFormatting.GREEN + "Logged in. (" + this.username + " - offline name)");
			return;
		}
		this.status = (EnumChatFormatting.YELLOW + "Logging in...");
		Session auth = createSession(this.username, this.password);
		if (auth == null) {
			this.status = (EnumChatFormatting.RED + "Login failed!");
		} else {
			AltManager altManager = Envy.ALT_MANAGER;
			AltManager.lastAlt = new Alt(this.username, this.password);
			this.status = (EnumChatFormatting.GREEN + "Logged in. (" + auth.getUsername() + ")");
			this.mc.session = auth;
		}
	}

	public void setStatus(String status) {
		this.status = status;
	}
}