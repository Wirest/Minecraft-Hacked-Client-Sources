package de.iotacb.client.minimap;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;

import de.iotacb.client.minimap.events.Events;
import de.iotacb.client.minimap.interfaces.InterfaceHandler;
import de.iotacb.client.minimap.settings.ModSettings;
import net.minecraft.client.Minecraft;

public class XaeroMinimap
{
    public static XaeroMinimap instance;
    public static final String versionID = "1.8_1.9.1.1";
    public static int newestUpdateID;
    public static boolean isOutdated;
    public static ModSettings settings;
    public static String message;
    public static Events events;
    public static File old_optionsFile;
    public static File old_waypointsFile;
    public static File optionsFile;
    public static File waypointsFile;
    public static Minecraft mc;
    
    public static ModSettings getSettings() {
        return XaeroMinimap.settings;
    }
    
    
    public void load() {
        InterfaceHandler.loadPresets();
        InterfaceHandler.load();
        XaeroMinimap.settings = new ModSettings();
        XaeroMinimap.events = new Events();
    }
    
    static {
        XaeroMinimap.message = "";
        XaeroMinimap.old_optionsFile = new File("./xaerominimap.txt");
        XaeroMinimap.old_waypointsFile = new File("./xaerowaypoints.txt");
        XaeroMinimap.optionsFile = new File("./config/xaerominimap.txt");
        XaeroMinimap.waypointsFile = new File("./config/xaerowaypoints.txt");
        XaeroMinimap.mc = Minecraft.getMinecraft();
    }
}
