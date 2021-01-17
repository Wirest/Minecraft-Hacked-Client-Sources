package me.ihaq.iClient;

import java.awt.Color;
import java.net.Proxy;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.realmsclient.gui.ChatFormatting;

import me.ihaq.iClient.command.CommandManager;
import me.ihaq.iClient.event.EventManager;
import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventKeyboard;
import me.ihaq.iClient.file.FileManager;
import me.ihaq.iClient.gui.GuiWindows.altmanager.AltManager;
import me.ihaq.iClient.modules.Module;
import me.ihaq.iClient.modules.ModuleManager;
import me.ihaq.iClient.modules.Render.HUD;
import me.ihaq.iClient.ttf.FontManager;
import me.ihaq.iClient.utils.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Session;

public class Envy {

	public static final String Client_Name = "Envy";
	public static final double Client_Version = 0.3;

	public static final Envy INSTANCE = new Envy();
	public static final FontManager FONT_MANAGER = new FontManager();
	public static final FileManager FILE_MANAGER = new FileManager();
	public static final AltManager ALT_MANAGER = new AltManager();
	public static final CommandManager COMMANDS = new CommandManager();
	public static final ModuleManager MODULE_MANAGER = new ModuleManager();

	public void onEnable() {

		Display.setTitle(Client_Name + " | " + Client_Version + " | " + "iHaq");

		EventManager.register(this);

		FONT_MANAGER.loadFonts();
		MODULE_MANAGER.loadMods();
		FILE_MANAGER.loadFiles();
		COMMANDS.loadCommands();

		//////////////////////////
		YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
		YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service
				.createUserAuthentication(Agent.MINECRAFT);
		auth.setUsername("likegoldandfaceted@yahoo.com");
		auth.setPassword("jlprai5gdib");
		try {
			auth.logIn();
			Minecraft.getMinecraft().session = new Session(auth.getSelectedProfile().getName(),
					auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
		} catch (Exception e) {
			e.printStackTrace();
		}
		///////////////////////////

		Runtime.getRuntime().addShutdownHook(new Thread(() -> onDisable()));
	}

	public void onDisable() {
	}

	@EventTarget
	public static void onKeyPressed(EventKeyboard e) {
		if (e.getKey() == 28) {
			HUD.tab.enter();
		}
		if (e.getKey() == Keyboard.KEY_DOWN) {
			HUD.tab.down();
		}
		if (e.getKey() == Keyboard.KEY_UP) {
			HUD.tab.up();
		}
		if (e.getKey() == Keyboard.KEY_RIGHT) {
			HUD.tab.right();
		}
		if (e.getKey() == Keyboard.KEY_LEFT) {
			HUD.tab.left();
		}
		for (Module module : MODULE_MANAGER.getModules()) {
			if (module.getKeyCode() == e.getKey()) {
				module.toggle();
			}
		}
	}

	public static void tellPlayer(String message) {
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(
				new ChatComponentText(Color.GREEN + "Envy Client > " + ChatFormatting.WHITE + message));
	}

}
