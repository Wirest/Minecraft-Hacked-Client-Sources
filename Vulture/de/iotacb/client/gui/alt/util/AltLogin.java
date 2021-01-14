package de.iotacb.client.gui.alt.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.ibm.icu.text.UFormat;

import de.iotacb.client.Client;
import de.iotacb.client.gui.alt.Alt;
import de.iotacb.client.gui.alt.AltSlot;
import de.iotacb.client.gui.alt.GuiAltManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;

public class AltLogin {

	private static final Minecraft MC = Minecraft.getMinecraft();
	
	public static void login(AltSlot slot, GuiAltManager screen) {
		if (slot.getType().equals("C") || slot.getType().equals("M")) {
			if (Client.LOGIN_UTIL.login(slot.getEmail().concat(":").concat(slot.getPassword()))) {
				MC.displayGuiScreen(new GuiMultiplayer(screen));
			}
		} else if (slot.getType().equals("V")) {
			if (Client.LOGIN_UTIL.loginVultureAlts(slot.getEmail())) {
				MC.displayGuiScreen(new GuiMultiplayer(screen));
			}
		} else if (slot.getType().equals("TA")) {
			if (Client.LOGIN_UTIL.loginAltening(slot.getEmail())) {
				MC.displayGuiScreen(new GuiMultiplayer(screen));
			}
		} else {
			System.out.println("No type like: ".concat(slot.getType()));
		}
	}
	
	public static void login(String email, String password, GuiAltManager screen) {
		final String type = getType(email, password);
		if (type.equals("C") || type.equals("M")) {
			if (Client.LOGIN_UTIL.login(email.concat(":").concat(password))) {
				MC.displayGuiScreen(new GuiMultiplayer(screen));
			}
		} else if (type.equals("V")) {
			if (Client.LOGIN_UTIL.loginVultureAlts(email)) {
				MC.displayGuiScreen(new GuiMultiplayer(screen));
			}
		} else if (type.equals("TA")) {
			if (Client.LOGIN_UTIL.loginAltening(email)) {
				MC.displayGuiScreen(new GuiMultiplayer(screen));
			}
		} else {
			System.out.println("No type like: ".concat(type));
		}
	}
	
	public static String getType(String email, String password) {
		if (email.endsWith("@alt.com") && password.isEmpty()) {
			return "TA";
		} else if (email.endsWith("=") && password.isEmpty()) {
			return "V";
		} else if (email.contains("@") && !password.isEmpty()) {
			return "M";
		} else {
			return "C";
		}
	}
	
	public static String gerateAccountPrixGen(String username, String password) {
		String alt = "";
		final HttpClient httpClient = HttpClients.custom().setUserAgent("API").build();
		final HttpGet get = new HttpGet(String.format("http://cheating-shop.de/datas/accounts/?action=generate&username=%s&password=%s&type=Minecraft", username, password));
		try {
			final HttpResponse response = httpClient.execute(get);
			
			final Scanner scanner = new Scanner(response.getEntity().getContent());
			if (scanner.hasNextLine()) {
				alt = scanner.nextLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return alt;
	}
	
	public static String gerateTokenVultureGen(String username, String password) {
		String alt = "";
		final HttpClient httpClient = HttpClients.createDefault();
		password = DigestUtils.sha512Hex(password);
		final HttpGet get = new HttpGet(String.format("http://vulture.bplaced.net/gen/?did=%s&password=%s", username, password));
		try {
			final HttpResponse response = httpClient.execute(get);
			
			final Scanner scanner = new Scanner(response.getEntity().getContent());
			
			alt = scanner.nextLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return alt;
	}
	
}
