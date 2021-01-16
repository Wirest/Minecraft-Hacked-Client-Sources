/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft;

import java.io.File;
import java.util.ArrayList;

import darkmagician6.EventManager;
import darkmagician6.EventTarget;
import darkmagician6.events.EventKeyPressed;
import me.razerboy420.weepcraft.capes.PlayerCapes;
import me.razerboy420.weepcraft.commands.CommandManager;
import me.razerboy420.weepcraft.files.AltsFile;
import me.razerboy420.weepcraft.files.ColorFile;
import me.razerboy420.weepcraft.files.FriendsFile;
import me.razerboy420.weepcraft.files.GuiFile;
import me.razerboy420.weepcraft.files.Keybinds;
import me.razerboy420.weepcraft.files.ToggledMods;
import me.razerboy420.weepcraft.files.ValuesFile;
import me.razerboy420.weepcraft.gui.click.Click;
import me.razerboy420.weepcraft.gui.newclick.WeepcraftClick;
import me.razerboy420.weepcraft.irc.IrcManager;
import me.razerboy420.weepcraft.keybinds.Keybind;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.module.ModuleManager;
import me.razerboy420.weepcraft.module.modules.combat.aura.AuraType;
import me.razerboy420.weepcraft.radar.Radar;
import me.razerboy420.weepcraft.settings.EnumColor;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.util.RainbowUtil;
import me.razerboy420.weepcraft.util.RenderUtils2D;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Weepcraft {
    public static String name = "WeepCraft";
    public static String version = "0.2";
    public static String motd;
    public static ArrayList<Module> mods;
    public static File weepcraftDir;
    public static File configDir;
    public static File accDir;
    public static Radar radar;
    public static String ip;
    public static int port;
    public static ModuleManager modulemanager;
    public static CommandManager cmdman;
    public static IrcManager ircManager;
    public static boolean firstLoad;
    private static Click click;
    private static WeepcraftClick weepclick;
    public static AuraType auratype;
    public static RainbowUtil rainbowutil;
    public static ArrayList<Keybind> keybinds;
    public static ArrayList<String> tablist;
    public static EnumColor primaryColor;
    public static EnumColor secondaryColor;
    public static EnumColor normalColor;
    public static EnumColor enabledColor;
    public static EnumColor disabledColor;
    public static EnumColor borderColor;
    public static EnumColor panlColor;
    public static EnumColor whiteColor;

    static {
        mods = new ArrayList();
        ip = "";
        port = 25565;
        firstLoad = false;
        auratype = new AuraType();
        keybinds = new ArrayList();
        tablist = new ArrayList();
        whiteColor = EnumColor.White;
        primaryColor = EnumColor.Red;
        secondaryColor = EnumColor.DarkRed;
        normalColor = EnumColor.Yellow;
        enabledColor = EnumColor.DarkGray;
        disabledColor = EnumColor.DarkRed;
        borderColor = EnumColor.DarkGray;
        panlColor = EnumColor.Black;
    }

    public Weepcraft() {
        this.start();
    }

    public void start() {
        EventManager.register(this);
        weepcraftDir = new File(Wrapper.mc().mcDataDir + File.separator + "Weepcraft");
        configDir = new File(weepcraftDir + File.separator + "Config");
        accDir = new File(weepcraftDir + File.separator + "Accounts");
        modulemanager = new ModuleManager();
        rainbowutil = new RainbowUtil();
        if (!weepcraftDir.exists()) {
            firstLoad = true;
            weepcraftDir.mkdir();
        }
        if (!configDir.exists()) {
            configDir.mkdir();
        }
        if (!accDir.exists()) {
            accDir.mkdir();
        }
        ModuleManager.hud.togglenosave();
        ModuleManager.hud.setVisible(false);
        cmdman = new CommandManager();
        click = new Click();
        ircManager = new IrcManager(Minecraft.getMinecraft().getSession().getUsername());
        ircManager.connect();
        String ricesfirstcode = "{ boolean, kill yourself.hey;";
        try {
            AltsFile.load();
            Keybinds.load();
            ToggledMods.load();
            ValuesFile.load();
            FriendsFile.load();
            ColorFile.load();
            GuiFile.load();
        }
        catch (Exception exception) {
            // empty catch block
        }
        PlayerCapes.load();
        System.out.println("-------------------------");
        System.out.println("Welcome to Weepcraft.");
        System.out.println("-------------------------");
    }

    public static ArrayList<Module> getMods() {
        return mods;
    }

    @EventTarget
    public void onKey(EventKeyPressed event) {
        for (Keybind k : keybinds) {
            if (event.getEventKey() != k.getKey()) continue;
            k.getMod().toggle();
        }
    }

    public static void drawWeepString() {
        String weepcraftString = String.valueOf(String.valueOf(ColorUtil.getColor(primaryColor))) + "\u00a7lWeep" + ColorUtil.getColor(secondaryColor) + "\u00a7lCraft";
        Gui.drawCenteredString(Wrapper.fr(), String.valueOf(String.valueOf(weepcraftString)) + " " + ColorUtil.getColor(normalColor) + version + "v", RenderUtils2D.newScaledResolution().getScaledWidth() / 2, 2.0f, -1);
    }

    public static Click getClick() {
        if (click == null) {
            click = new Click();
        }
        return click;
    }

    public static WeepcraftClick getWeepClick() {
        if (weepclick == null) {
            weepclick = new WeepcraftClick();
        }
        return weepclick;
    }

    public static AuraType getAura() {
        return auratype;
    }
}

