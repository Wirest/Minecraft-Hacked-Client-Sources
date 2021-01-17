
package Blizzard;

import org.lwjgl.opengl.Display;

import Blizzard.Alts.AltManager;
import Blizzard.Commands.CommandManager;
import Blizzard.Event.EventManager;
import Blizzard.Files.FileManager;
import Blizzard.Friends.FriendManager;
import Blizzard.Mod.Mod;
import Blizzard.Mod.ModManager;
import Blizzard.UI.Font.FontManager;
import Blizzard.UI.tab.TabGui;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;

// Client  leaked By Hope and FinalException

public class Blizzard {
	public static final Blizzard instance = new Blizzard();
	public static Blizzard Blizzard = new Blizzard();
	public static ModManager modManager = new ModManager();
	public static FontManager fontManager = new FontManager();
	public static final String Name = "Blizzard";
	public static final String Version = "B1";
	public static String prefix = "-";
	public static TabGui tabGui;
	public static AltManager altManager;
	public static FileManager.CustomFile customFile;
	public static FileManager fileManager;
	public static FriendManager friendManager;
	public static CommandManager cmdManager;
	public static final String tag = "Leaked by Hope and FinalException";

	public void start() {
		Display.setTitle("Blizzard | " + Version);
		EventManager.register(this);
		fontManager.loadFonts();
		cmdManager = new CommandManager();
		fileManager = new FileManager();
		friendManager = new FriendManager();
		fileManager.loadFiles();
		tabGui = new TabGui();
		ModManager.getModbyName("HUD").setToggled(true);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			this.end();
		}));
	}

	public void end() {
	}

	public static Blizzard getInstance() {
		return Blizzard;
	}

	public static void addChatMessage(String s) {
		Minecraft.getMinecraft().thePlayer
				.addChatMessage(new ChatComponentText("Leaked by Hope and FinalExpection"
	}

	public static boolean onSendChatMessage(String s) {
		if (s.startsWith(prefix)) {
			cmdManager.callCommand(s.substring(1));
			return false;
		}
		for (Mod m : ModManager.getMods()) {
			if (!m.isToggled())
				continue;
			return m.onSendChatMessage(s);
		}
		return true;
	}

	public static boolean onReciveChatMessage(S02PacketChat packet) {
		for (Mod m : ModManager.getMods()) {
			if (!m.isToggled())
				continue;
			return m.onReciveChatMessage(packet);
		}
		return true;
	}
}
