package de.iotacb.client;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.iotacb.client.file.files.AltManagerFile;
import de.iotacb.client.file.files.ClickGuiFile;
import de.iotacb.client.file.files.ClientConfigFile;
import de.iotacb.client.file.files.ConfigFile;
import de.iotacb.client.file.files.FriendsFile;
import de.iotacb.client.file.files.ModuleFile;
import de.iotacb.client.file.files.PrixGenFile;
import de.iotacb.client.gui.alt.util.AltLogin;
import de.iotacb.client.gui.click.GuiClick;
import de.iotacb.client.irc.IRC;
import de.iotacb.client.manager.AltManager;
import de.iotacb.client.manager.CommandManager;
import de.iotacb.client.manager.FileManager;
import de.iotacb.client.manager.FontManager;
import de.iotacb.client.manager.FriendManager;
import de.iotacb.client.manager.ModuleManager;
import de.iotacb.client.manager.NotificationManager;
import de.iotacb.client.module.modules.movement.flies.McCentral;
import de.iotacb.client.module.modules.render.HUD;
import de.iotacb.client.utilities.combat.RaycastUtil;
import de.iotacb.client.utilities.combat.RotationUtil;
import de.iotacb.client.utilities.math.MathUtil;
import de.iotacb.client.utilities.misc.ClipboardUtil;
import de.iotacb.client.utilities.misc.DiscordUtil;
import de.iotacb.client.utilities.misc.FritzBoxUtil;
import de.iotacb.client.utilities.misc.LoginUtil;
import de.iotacb.client.utilities.misc.Printer;
import de.iotacb.client.utilities.misc.StringUtil;
import de.iotacb.client.utilities.player.EntityUtil;
import de.iotacb.client.utilities.player.InventoryUtil;
import de.iotacb.client.utilities.player.MovementUtil;
import de.iotacb.client.utilities.render.BlurUtil;
import de.iotacb.client.utilities.render.DeltaUtil;
import de.iotacb.client.utilities.render.OutlineUtil;
import de.iotacb.client.utilities.render.Render2D;
import de.iotacb.client.utilities.render.Render3D;
import de.iotacb.client.utilities.render.Stencil;
import de.iotacb.client.utilities.render.color.BetterColor;
import de.iotacb.client.utilities.render.color.ColorUtil;
import de.iotacb.client.utilities.render.image.ExternalImageDrawer;
import de.iotacb.client.utilities.render.shader.ShaderUtil;
import net.minecraft.client.Minecraft;

public class Client {
	
	public static Client INSTANCE = new Client();
	
	private final String clientName = "Vulture";
	private final String clientAuthor = "chris.";
	private final String clientVersion = "Stable Build 1.8";
	
	private ModuleManager moduleManager;
	private CommandManager commandManager;
	private FileManager fileManager;
	private NotificationManager notificationManager;
	private FontManager fontManager;
	private AltManager altManager;
	private FriendManager friendManager;
	
	private BetterColor clientColor;
	
	private GuiClick clickGui;
	
	private DiscordUtil discord;
	
	private String clientFont;
	private String nameProtectName;
	
	private String backgroundPath;
	
	private boolean isPrixGenLoggedIn, isVultureGenLoggedIn;
	
	private ExternalImageDrawer customBackground;
	
	public static final RaycastUtil RAYCAST_UTIL = new RaycastUtil();
	public static final RotationUtil ROTATION_UTIL = new RotationUtil();
	public static final EntityUtil ENTITY_UTIL = new EntityUtil();
	public static final InventoryUtil INVENTORY_UTIL = new InventoryUtil();
	public static final MovementUtil MOVEMENT_UTIL = new MovementUtil();
	public static final DeltaUtil DELTA_UTIL = new DeltaUtil();
	public static final Render2D RENDER2D = new Render2D();
	public static final Render3D RENDER3D = new Render3D();
	public static final FritzBoxUtil FRITZ_BOX_UTIL = new FritzBoxUtil();
	public static final LoginUtil LOGIN_UTIL = new LoginUtil();
	public static final Printer PRINTER = new Printer();
	public static final MathUtil MATH_UTIL = new MathUtil();
	public static final BlurUtil BLUR_UTIL = new BlurUtil();
	public static final Stencil STENCIL = new Stencil();
	public static final StringUtil STRING_UTIL = new StringUtil();
	public static final ColorUtil COLOR_UTIL = new ColorUtil();
	public static final ShaderUtil SHADER_UTIL = new ShaderUtil();
	
	private IRC irc;
	
	public void startup() {
		System.out.println(String.format("Initializing %s...", getClientName()));
		
		this.moduleManager = new ModuleManager();
		this.commandManager = new CommandManager();
		this.altManager = new AltManager();
		this.fileManager = new FileManager();
		this.notificationManager = new NotificationManager();
		this.friendManager = new FriendManager();
		this.fontManager = new FontManager();
		
		this.discord = new DiscordUtil("697475974421282909");
		
		this.nameProtectName = "Vulture";
		this.backgroundPath = "client/designs/default/background.png";
		this.clientColor = new BetterColor(100, 50, 130);
		
		((ClientConfigFile) fileManager.getFileByClass(ClientConfigFile.class)).readConfig();
		((ModuleFile) fileManager.getFileByClass(ModuleFile.class)).readModules();
		((AltManagerFile) fileManager.getFileByClass(AltManagerFile.class)).readAlts();
		((FriendsFile) fileManager.getFileByClass(FriendsFile.class)).readFriends();
		((ConfigFile) fileManager.getFileByClass(ConfigFile.class)).readConfig(false);
		((ClickGuiFile) fileManager.getFileByClass(ClickGuiFile.class)).readClick();
		
		this.clickGui = new GuiClick();
		
		this.clientFont = Client.INSTANCE.getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDFonts").getComboValue();
		
		Minecraft.getMinecraft().gameSettings.ofFastRender = false;
		Minecraft.getMinecraft().gameSettings.guiScale = 2;
	}
	
	public void shutdown() {
		System.out.println(String.format("Closing %s...", getClientName()));
		
		((ModuleFile) fileManager.getFileByClass(ModuleFile.class)).saveModules();
		((ConfigFile) fileManager.getFileByClass(ConfigFile.class)).saveConfig();
		((ClientConfigFile) fileManager.getFileByClass(ClientConfigFile.class)).saveConfig();
		
		this.discord.getRpc().Discord_Shutdown();
	}
	
	public void startIRC(String username) {
		this.irc = new IRC(username, "45.88.110.160", 6969);
//		this.irc = new IRC(username, "127.0.0.1", 6969);
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public String getClientAuthor() {
		return clientAuthor;
	}
	
	public String getClientVersion() {
		return clientVersion;
	}
	
	public ModuleManager getModuleManager() {
		return moduleManager;
	}
	
	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	public FileManager getFileManager() {
		return fileManager;
	}
	
	public NotificationManager getNotificationManager() {
		return notificationManager;
	}
	
	public FontManager getFontManager() {
		return fontManager;
	}
	
	public AltManager getAltManager() {
		return altManager;
	}
	
	public FriendManager getFriendManager() {
		return friendManager;
	}
	
	public BetterColor getClientColor() {
		return getModuleManager().getModuleByClass(HUD.class).getValueByName("HUDColoring").getComboValue().equalsIgnoreCase("Rainbow") ? BetterColor.getRainbow(0) : clientColor;
	}
	
	public GuiClick getClickGui() {
		return clickGui;
	}
	
	public IRC getIrc() {
		return irc;
	}
	
	public String getClientFont() {
		return clientFont;
	}
	
	public String getNameProtectName() {
		return nameProtectName;
	}
	
	public String getBackgroundPath() {
		return backgroundPath;
	}
	
	public ExternalImageDrawer getCustomBackground() {
		return customBackground;
	}
	
	public void setNameProtectName(String nameProtectName) {
		this.nameProtectName = nameProtectName;
	}
	
	public void setBackgroundPath(String backgroundPath) {
		this.backgroundPath = backgroundPath;
	}
	
	public void setCustomBackground(ExternalImageDrawer customBackground) {
		this.customBackground = customBackground;
	}
	
	public void setClientColor(BetterColor clientColor) {
		this.clientColor = clientColor;
	}
	
	public void setClientFont(String clientFont) {
		this.clientFont = clientFont;
	}
	
	public void setPrixGenLoggedIn(boolean isPrixGenLoggedIn) {
		this.isPrixGenLoggedIn = isPrixGenLoggedIn;
	}
	
	public void setVultureGenLoggedIn(boolean isVultureGenLoggedIn) {
		this.isVultureGenLoggedIn = isVultureGenLoggedIn;
	}
	
	public String getClientColorCode() {
		return "§8";
	}
	
	public boolean isPrixGenLoggedIn() {
		return isPrixGenLoggedIn;
	}
	
	public boolean isVultureGenLoggedIn() {
		return isVultureGenLoggedIn;
	}
	
}
