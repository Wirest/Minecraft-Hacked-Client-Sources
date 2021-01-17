// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush;

import me.nico.hush.utils.Fonts;
import org.lwjgl.opengl.Display;
import me.nico.hush.utils.MathUtilsSpeed;
import de.Hero.settings.SettingsManager;
import me.nico.hush.utils.FileManager;
import java.io.File;
import me.nico.hush.commands.CommandManager;
import me.nico.hush.modules.ModuleManager;
import de.Hero.clickgui.ClickGUI;
import me.nico.hush.utils.Logger;
import net.minecraft.client.Minecraft;

public class Client
{
    public static Client instance;
    public String ClientName;
    public String ClientVersion;
    public String ClientCoder;
    public String ClientPrefix;
    public Minecraft mc;
    public Logger logger;
    public ClickGUI clickGUI;
    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public File directory;
    public FileManager fileManager;
    public SettingsManager settingManager;
    public MathUtilsSpeed mathUtils;
    
    public Client() {
        this.ClientName = "Z\u03a6diac";
        this.ClientVersion = "b4";
        this.ClientCoder = "NicoKush420";
        this.ClientPrefix = "§8[§9Z\u03a6diac§8] ";
        this.mc = Minecraft.getMinecraft();
    }
    
    public final void startClient() {
        Client.instance = this;
        Display.setTitle("Zodiac " + this.ClientVersion);
        System.out.println(String.valueOf(this.ClientName) + " " + this.ClientVersion + " is ready!");
        System.out.println("Made by " + this.ClientCoder);
        System.out.println("Load settings...");
        Fonts.loadFonts();
        this.settingManager = new SettingsManager();
        this.directory = new File(Minecraft.getMinecraft().mcDataDir, this.ClientName);
        if (!this.directory.exists()) {
            this.directory.mkdir();
        }
        this.logger = new Logger();
        this.moduleManager = new ModuleManager();
        this.commandManager = new CommandManager();
        (this.fileManager = new FileManager()).load();
        this.fileManager.loadKeyBinds();
        this.clickGUI = new ClickGUI();
        this.mathUtils = new MathUtilsSpeed();
    }
    
    public Minecraft MC() {
        return this.mc;
    }
    
    public FileManager FileManager() {
        return this.fileManager;
    }
    
    public MathUtilsSpeed getMathUtils() {
        return this.mathUtils;
    }
}
