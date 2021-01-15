// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX;

import org.darkstorm.minecraft.gui.theme.simple.SimpleTheme;
import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;
import org.hero.clickgui.ClickGUI;
import org.hero.settings.SettingsManager;
import org.lwjgl.opengl.Display;

import me.CheerioFX.FusionX.AltManager.AltManager;
import me.CheerioFX.FusionX.GUI.tabGui.TabGui;
import me.CheerioFX.FusionX.command.CommandManager;
import me.CheerioFX.FusionX.managers.GuiManager;
import me.CheerioFX.FusionX.managers.fileManager.FileManager;
import me.CheerioFX.FusionX.module.Module;
import me.CheerioFX.FusionX.module.ModuleManager;
import me.CheerioFX.FusionX.module.modules.GhostClient;
import me.CheerioFX.FusionX.utils.ModeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;

public class FusionX {
	public String Client_Name;
	public double Client_Version;
	public String GAME_VERSION;
	public static boolean countingHits;
	public String More_Info;
	public static final FusionX theClient;
	public static boolean renderTheGui;
	public ModuleManager moduleManager;
	public FileManager fileManager;
	public TabGui tabGui;
	public static boolean updateChecked;
	public static String prefix;
	public static String spammessage;
	public static float td;
	private GuiManagerDisplayScreen gui;
	private GuiManager guiManager;
	private CommandManager cmdManager;
	public GuiMainMenu gmm;
	public AltManager altManager;
	public boolean ServerCategories;
	public boolean checked4Updates;
	public SettingsManager setmgr;
	public ClickGUI clickgui;

	static {
		FusionX.countingHits = false;
		theClient = new FusionX();
		FusionX.renderTheGui = true;
		FusionX.updateChecked = false;
		FusionX.prefix = ".";
		FusionX.spammessage = "Subscribe to CheerioFX on Youtube! Want to change this spam message? Type "
				+ FusionX.prefix + "spam set [MESSAGE] to change it.";
		FusionX.td = 3.1f;
	}

	public FusionX() {
		this.Client_Name = "FusionX";
		this.Client_Version = 2.0;
		this.GAME_VERSION = "1.8.X";
		this.More_Info = "Premium";
		this.ServerCategories = false;
		this.checked4Updates = false;
	}

	public void StartClient() {
		Display.setTitle(String.valueOf(this.Client_Name) + " " + this.More_Info);
		this.cmdManager = new CommandManager();
		new ModeUtils();
		this.setmgr = new SettingsManager();
		this.moduleManager = new ModuleManager();
		this.tabGui = new TabGui();
		this.clickgui = new ClickGUI();
		this.fileManager = new FileManager();
		this.getGuiManager();
		try {
			this.fileManager.loadValues();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public GuiManager getGuiManager() {
		if (this.guiManager == null) {
			(this.guiManager = new GuiManager()).setTheme(new SimpleTheme());
			this.guiManager.setup();
		}
		return this.guiManager;
	}

	public GuiManagerDisplayScreen getGui() {
		if (this.gui == null) {
			this.gui = new GuiManagerDisplayScreen(this.getGuiManager());
		}
		return this.gui;
	}

	public String getClient_Name() {
		return this.Client_Name;
	}

	public void setClient_Name(final String client_Name) {
		this.Client_Name = client_Name;
	}

	public double getClient_Version() {
		return this.Client_Version;
	}

	public void setClient_Version(final double client_Version) {
		this.Client_Version = client_Version;
	}

	public static void addChatMessage(final String msg) {
		final String m = msg.replaceAll("", "§c");
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§4§lFusion§f§lX §c§l» §r§c" + m));
	}

	public boolean onSendChatMessage(final String s) {
		if (!GhostClient.enabled) {
			if (s.startsWith(FusionX.prefix)) {
				this.cmdManager.callCommand(s.substring(1));
				return false;
			}
			for (final Module m : ModuleManager.getModules()) {
				if (m.getState()) {
					return m.onSendChatMessage(s);
				}
			}
		}
		return true;
	}

	public boolean onRecieveChatMessage(final S02PacketChat packet) {
		for (final Module m : ModuleManager.getModules()) {
			if (m.getState()) {
				return m.onRecieveChatMessage(packet);
			}
		}
		return true;
	}
}
