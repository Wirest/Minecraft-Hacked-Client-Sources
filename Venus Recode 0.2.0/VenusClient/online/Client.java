package VenusClient.online;

import VenusClient.online.Auth.VenusAuthAPI;
import VenusClient.online.DiscordRPC.DiscordRP;
import VenusClient.online.DiscordRPC.VENUSRP;
import VenusClient.online.Event.EventManager;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventKey;
import VenusClient.online.Module.ModuleManager;
import VenusClient.online.Ui.font.GlyphPage;
import VenusClient.online.Ui.font.GlyphPageFontRenderer;
import VenusClient.online.Ui.notification.NotificationManager;
import de.Hero.clickgui.ClickGUI;
import de.Hero.settings.SettingsManager;
import me.tireman.hexa.alts.AltManager;
import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.Display;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;

public class Client {

    //instance
    public static Client instance;


    //Client info
    public static String client_Name = "VenusClient", client_Version = "Recode 0.2.0", client_Build = "Developer Build";
    public static boolean isPremium;
    
    //Files
    public static File fanDir = new File(Minecraft.getMinecraft().mcDataDir, "VenusClient");
    
    //Register Stuff
    public EventManager eventManager;
    public ModuleManager moduleManager;
    public SettingsManager setmgr;
    public ClickGUI clickgui;
    public AltManager altManager;
    public NotificationManager notificationManager;

    //Start Client
    public void startClient() throws UnsupportedEncodingException, NoSuchAlgorithmException, MalformedURLException {
    	

    	instance = this;
    	
    	setmgr = new SettingsManager();

        startChecking();
    	
        eventManager = new EventManager();

        moduleManager = new ModuleManager();

        Display.setTitle(client_Name + " | " + client_Version);

        clickgui = new ClickGUI();
        
        eventManager.register(this);

        try {
            VENUSRP.Instance.init();
            VENUSRP.Instance.getDiscordRP().update("User : " + VenusAuthAPI.getUserName(), "Chilling in Venus Client");
        } catch (IOException e) {
            System.err.println("Discord RPC Failed");
        }

    }

    public void stopClient() {
        eventManager.unregister(this);
    }

    @EventTarget
    public void onKey(EventKey event) {
        moduleManager.getModules().stream().filter(module -> module.getKeyCode() == event.getKey()).forEach(module -> module.toggle());
    }

    public static void startChecking() {
        try {
            VenusAuthAPI.authAutoHWID();
            if (VenusAuthAPI.authAutoHWID()) {
                isPremium = true;
                System.out.println("Login Complete ! User : " + VenusAuthAPI.getUserName() + " |  Rank : " + VenusAuthAPI.getRole() + " | HWID : " + VenusAuthAPI.getHWID());
                Display.setTitle(Client.client_Name + " | " + Client.client_Version + " | " + VenusAuthAPI.getUserName() + " |  Rank : " + VenusAuthAPI.getRole() + " | HWID : " + VenusAuthAPI.getHWID());
            } else {
                isPremium = false;
                System.out.println("Login Compleate (NON PREM)");
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            isPremium = false;
            System.out.println("Login ERROR");
            e.printStackTrace();
        }
    }
}