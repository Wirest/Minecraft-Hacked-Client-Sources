package de.iotacb.client.utilities.misc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.AltService;
import com.thealtening.AltService.EnumAltService;

import de.iotacb.client.Client;
import de.iotacb.client.file.files.ClientConfigFile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.Session;

public class LoginUtil {
	
	private static final AltService SERVICE = new AltService();

	public final boolean login(String input) {
		final String username = input.split(":")[0];
		final String pw = input.split(":").length > 1 ? input.split(":")[1] : "";

		try {
			System.out.println("Trying to login via mojang service");
			
			SERVICE.switchService(EnumAltService.MOJANG);
			
			if (pw.isEmpty()) {
				Minecraft.getMinecraft().setSession(new Session(username, "", "", "legacy"));
				System.out.println("Successfully logged in via mojang service (cracked)");
				return true;
			}
			final YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(
					Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
			authentication.setUsername(username);
			authentication.setPassword(pw);

			authentication.logIn();
			
			Minecraft.getMinecraft()
					.setSession(new Session(authentication.getSelectedProfile().getName(),
							authentication.getSelectedProfile().getId().toString(),
							authentication.getAuthenticatedToken(), "mojang"));
			System.out.println("Successfully logged in via mojang service");
		} catch (Exception e) {
			System.out.println("Failed to login via mojang service");
			return false;
		}
		return true;
	}
	
	public final boolean loginAltening(String input) {
		try {
			System.out.println("Trying to login via the altening service");
			SERVICE.switchService(EnumAltService.THEALTENING);
			
			final YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(
					Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
			authentication.setUsername(input);
			authentication.setPassword(Client.INSTANCE.getClientName());
			
			authentication.logIn();
			
			Minecraft.getMinecraft()
					.setSession(new Session(authentication.getSelectedProfile().getName(),
							authentication.getSelectedProfile().getId().toString(),
							authentication.getAuthenticatedToken(), "mojang"));
			
			System.out.println("Successfully logged in via altening service");
		} catch (AuthenticationException | NoSuchFieldException | IllegalAccessException e) {
			System.out.println("Failed to login via altening service");
			return false;
		}
		return true;
	}
	
	public final boolean loginVultureAlts(String input) {
		if (!input.endsWith("=")) return false;
		
//		String unhashedAlt = new String(Base64.getDecoder().decode(input.getBytes())).concat("@alt.com");
		String unhashedAlt = new String(Base64.getDecoder().decode(input.getBytes()));
		
		char[] result = new char[unhashedAlt.length()];
		for (int i = 0; i < result.length; i++) {
		    result[i] = (char) (unhashedAlt.charAt(i) - 5);
		}
		String destString = new String(result);
		destString = destString.replaceAll("^(.{5})", "$1-");
		unhashedAlt = destString;
		
		input = unhashedAlt.concat("@alt.com");
		
		if (input.isEmpty() || !input.contains("@")) return false;
		
		try {
			System.out.println("Trying to login via the vulture service");
			SERVICE.switchService(EnumAltService.THEALTENING);
			
			final YggdrasilUserAuthentication authentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(
					Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
			authentication.setUsername(input);
			authentication.setPassword(Client.INSTANCE.getClientName());
			
			authentication.logIn();
			
			Minecraft.getMinecraft()
					.setSession(new Session(authentication.getSelectedProfile().getName(),
							authentication.getSelectedProfile().getId().toString(),
							authentication.getAuthenticatedToken(), "mojang"));
			
			System.out.println("Successfully logged in via vulture service");
		} catch (AuthenticationException | NoSuchFieldException | IllegalAccessException e) {
			System.out.println("Failed to login via vulture service");
			return false;
		}
		return true;
	}

	public final void loginClient(String discordTag) {
		String alt = "";
		final HttpClient httpClient = HttpClients.createDefault();
		HttpGet get;
		try {
			get = new HttpGet(String.format("http://vulture.bplaced.net/?dtag=%s&hwid=%s", discordTag.replace("#", ":"), getHWID()));
			try {
				final HttpResponse response = httpClient.execute(get);
				
				final Scanner scanner = new Scanner(response.getEntity().getContent());
				
				alt = scanner.nextLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if (alt.equals("300")) {
			Client.INSTANCE.startIRC(discordTag);
			((ClientConfigFile) Client.INSTANCE.getFileManager().getFileByClass(ClientConfigFile.class)).saveConfig();
			Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
		}
		
//		final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//		final String date = format.format(new Date());
//		String hwid = "";
//		try {
//			hwid = getHWID();
//		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e2) {
//			e2.printStackTrace();
//		}
//		
//		if (hwid.isEmpty()) return;
//		
//		String ip = "";
//		final HttpClient httpClient = HttpClients.custom().setUserAgent("VULTURE").build();
//		HttpGet get;
//		try {
//			get = new HttpGet("https://dream-launcher.de/vulture/?action=get_ip");
//			try {
//				final HttpResponse response = httpClient.execute(get);
//				
//				final Scanner scanner = new Scanner(response.getEntity().getContent());
//				
//				ip = scanner.nextLine();
//			} catch (IOException e) {
//				e.printStackTrace();      
//			}
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		
//		String shit = "";
//		try {
//			get = new HttpGet(String.format("https://dream-launcher.de/vulture/?action=check_user&hwid=%s&dtag=%s", hwid, discordTag.replace("#", ":")));
//			try {
//				final HttpResponse response = httpClient.execute(get);
//				
//				final Scanner scanner = new Scanner(response.getEntity().getContent());
//				
//				shit = scanner.nextLine();
//			} catch (IOException e) {
//				e.printStackTrace();      
//			}
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		
//		String sha = DigestUtils.sha1Hex(ip.concat(date).concat(hwid).concat(discordTag.replace("#", ":")));
//		
//		if (shit.equals(sha)) {
//			Client.INSTANCE.setLoggedIn(true);
//			((ClientConfigFile) Client.INSTANCE.getFileManager().getFileByClass(ClientConfigFile.class)).saveConfig();
//			Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
//		}
	}
	
    public static final String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String s = "";
        final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
        final byte[] bytes = main.getBytes("UTF-8");
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        final byte[] md5 = messageDigest.digest(bytes);
        int i = 0;
        for (final byte b : md5) {
            s += Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
            if (i != md5.length - 1) {
                s += "-";
            }
            i++;
        }
        return s;
    }
}
