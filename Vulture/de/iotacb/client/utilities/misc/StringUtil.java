package de.iotacb.client.utilities.misc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import de.iotacb.client.Client;
import de.iotacb.client.file.files.ClientConfigFile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

public class StringUtil {
	
	public String removeFormattingCodes(String text) {
		return Pattern.compile("(?i)§[0-9A-FK-OR]").matcher(text).replaceAll("");
	}
	
	public String downloadPage(String url) {
		String alt = "";
		final HttpClient httpClient = HttpClients.createDefault();
		final HttpGet get = new HttpGet(url);
		try {
			try {
				final HttpResponse response = httpClient.execute(get);
				
				final Scanner scanner = new Scanner(response.getEntity().getContent());
				
				alt = scanner.nextLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
		}
		return alt;

	}

}
