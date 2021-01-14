package splash;

import me.hippo.systems.lwjeb.EventBus;
import me.hippo.systems.lwjeb.annotation.Collect;
import me.kix.stars.in.pornhub.ClickGUIScreen;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import splash.api.command.Command;
import splash.api.module.Module;
import splash.api.user.User;
import splash.client.events.chat.ChatEvent;
import splash.client.events.key.EventKey;
import splash.client.managers.cfont.CFontRenderer;
import splash.client.managers.command.CommandManager;
import splash.client.managers.config.ConfigManager;
import splash.client.managers.friend.FriendManager;
import splash.client.managers.module.ModuleManager;
import splash.client.managers.notifications.NotificationManager;
import splash.client.managers.value.ValueManager;
import splash.client.modules.visual.UI.FontMode;
import splash.utilities.system.ClientLogger;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: Ice Created: 00:19, 30-May-20 Project: Abyss B21 - CBT Edition
 */
public class Splash {

	public static Splash INSTANCE = new Splash();
	private Color clientColor = new Color(255, 80, 80);
	public ClickGUIScreen CLICK_UI;
	private String clientName = "Splash";
	private User currentUser = null;
	private final FriendManager friendManager = new FriendManager();
	private final ModuleManager moduleManager = new ModuleManager();
	private final ValueManager valueManager = new ValueManager();
	private final CommandManager commandManager = new CommandManager();
	private final ConfigManager configManager = new ConfigManager();
	private final EventBus eventBus = new EventBus();
	private final NotificationManager notificationManager = new NotificationManager();

	private final File splashDirectory = new File("C:/splash/");
	private final File splashConfigsDirectory = new File("C:/splash/configs/");
	private GAMEMODE gameMode;
	private CFontRenderer fontRenderer;
	public long lastFlag;
	
	public enum GAMEMODE {
		BEDWARS, SKYWARS, DUELS, PIT, UNSPECIFIED
	}

	public void hookClient() {
		getEventBus().build();
		getEventBus().register(this);
		if (!splashDirectory.exists()) {
			splashDirectory.mkdir();
		}
		if (!splashConfigsDirectory.exists()) {
			splashConfigsDirectory.mkdir();
		}
		CLICK_UI = new ClickGUIScreen();
		ClientLogger.printToConsole("Starting Splash...");
		Display.setTitle(getClientName() + " " + getClientBuild());
		getConfigManager().loadConfigs();
		getModuleManager().getContents().forEach(module -> getValueManager().loadValues(module));
		gameMode = GAMEMODE.UNSPECIFIED;
	}

	public void loadFontRenderer() {
		fontRenderer = new CFontRenderer(new Font("Segoe UI", Font.PLAIN, 18), true, false);
	}

	public void loadFontRenderer(FontMode fontMode) {
		if(fontMode == FontMode.ARIAL) {
			fontRenderer = new CFontRenderer(new Font("Arial", Font.PLAIN, 20), true, false);
		} else {
			fontRenderer = new CFontRenderer(new Font("Segoe UI", Font.PLAIN, 18), true, false);
		}
	}

	public void unhookClient() {
		getEventBus().unregister(this);
		ClientLogger.printToConsole("Stopping Splash...");
	}

	public static Splash getInstance() {
		return INSTANCE;
	}

	public String getClientName() {
		return clientName;
	}
	
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientBuild() {
		return "028620";
	}

	public File getSplashConfigsDirectory() {
		return splashConfigsDirectory;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public FriendManager getFriendManager() {
		return friendManager;
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}

	public ValueManager getValueManager() {
		return valueManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public NotificationManager getNotificationManager() {
		return notificationManager;
	}

	public int getClientColor() {
		return clientColor.getRGB();
	}

	public Color getClientColorNORGB() {
		return clientColor;
	}

	public void setClientColor(Color clientColor) {
		this.clientColor = clientColor;
	}

	public CFontRenderer getFontRenderer() {
		return fontRenderer;
	}
	
	public GAMEMODE getGameMode() {
		return gameMode;
	}

	public void setGameMode(GAMEMODE gameMode) {
		this.gameMode = gameMode;
	}

	@Collect
	public void onChat(ChatEvent eventChat) {
			for (Command command : getCommandManager().getContents()) {
				String chatMessage = eventChat.getChatMessage();
				String formattedMessage = chatMessage.replace(".", "");
				String[] regexFormattedMessage = formattedMessage.split(" ");
				if (regexFormattedMessage[0].equalsIgnoreCase(command.getCommandName())) {
					ArrayList<String> list = new ArrayList<>(Arrays.asList(regexFormattedMessage));
					list.remove(command.getCommandName());
					regexFormattedMessage = list.toArray(new String[0]);
					command.executeCommand(regexFormattedMessage);
				}
			}
	}

	@Collect
	public void onKey(EventKey eventKey) {
		if (eventKey.getPressedKey() == Keyboard.KEY_RSHIFT) {
			Minecraft.getMinecraft().displayGuiScreen(new ClickGUIScreen());
		} else {
			for (Module module : getModuleManager().getContents()) {
				if (module.getModuleMacro() == eventKey.getPressedKey()) {
					module.activateModule();
				}
			}
		}
	}
}
