package com.minimap;

import com.minimap.settings.*;
import com.minimap.events.*;
import net.minecraft.client.*;
import com.minimap.interfaces.*;
import java.nio.file.*;
import com.minimap.mods.*;
import java.net.*;
import java.io.*;

public class XaeroMinimap
{
    public static XaeroMinimap instance;
    public static final String versionID = "1.8_1.9.1.1";
    public static int newestUpdateID;
    public static boolean isOutdated;
    public static ModSettings settings;
    public static String message;
    public static ControlsHandler ch;
    public static Events events;
    public static FMLEvents fmlEvents;
    public static File old_optionsFile;
    public static File old_waypointsFile;
    public static File optionsFile;
    public static File waypointsFile;
    public static Minecraft mc;
    
    public static ModSettings getSettings() {
        return XaeroMinimap.settings;
    }
    
    
    public void load() throws IOException {
        InterfaceHandler.loadPresets();
        InterfaceHandler.load();
        XaeroMinimap.settings = new ModSettings();
        if (XaeroMinimap.old_optionsFile.exists() && !XaeroMinimap.optionsFile.exists()) {
            new File("./config").mkdirs();
            Files.move(XaeroMinimap.old_optionsFile.toPath(), XaeroMinimap.optionsFile.toPath(), new CopyOption[0]);
        }
        if (XaeroMinimap.old_waypointsFile.exists() && !XaeroMinimap.waypointsFile.exists()) {
            new File("./config").mkdirs();
            Files.move(XaeroMinimap.old_waypointsFile.toPath(), XaeroMinimap.waypointsFile.toPath(), new CopyOption[0]);
        }
       // XaeroMinimap.settings.loadSettings();
        XaeroMinimap.events = new Events();
        XaeroMinimap.fmlEvents = new FMLEvents();
        this.checkModVersion();
        //MinecraftForge.EVENT_BUS.register((Object)XaeroMinimap.events);
        //FMLCommonHandler.instance().bus().register((Object)XaeroMinimap.fmlEvents);
       // SupportMods.load();
    }
    
    public void checkModVersion() {
        String s = "https://dl.dropboxusercontent.com/u/13413474/Versions/Minimap.txt";
        s = s.replaceAll(" ", "%20");
        try {
            final URL url = new URL(s);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = reader.readLine();
            if (line != null) {
                XaeroMinimap.message = "§e§l" + line;
            }
            line = reader.readLine();
            if (line != null) {
                XaeroMinimap.newestUpdateID = Integer.parseInt(line);
                if (!ModSettings.updateNotification || XaeroMinimap.newestUpdateID == ModSettings.ignoreUpdate) {
                    XaeroMinimap.isOutdated = false;
                    reader.close();
                    return;
                }
            }
            while ((line = reader.readLine()) != null) {
                if (line.equals("1.8_1.9.1.1")) {
                    XaeroMinimap.isOutdated = false;
                    reader.close();
                }
            }
        }
        catch (Exception e) {
            XaeroMinimap.isOutdated = false;
        }
    }
    
    static {
        XaeroMinimap.isOutdated = true;
        XaeroMinimap.message = "";
        XaeroMinimap.old_optionsFile = new File("./xaerominimap.txt");
        XaeroMinimap.old_waypointsFile = new File("./xaerowaypoints.txt");
        XaeroMinimap.optionsFile = new File("./config/xaerominimap.txt");
        XaeroMinimap.waypointsFile = new File("./config/xaerowaypoints.txt");
        XaeroMinimap.mc = Minecraft.getMinecraft();
    }
}
