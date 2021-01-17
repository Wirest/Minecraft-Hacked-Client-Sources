package me.rigamortis.faurax;

import me.rigamortis.faurax.config.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.commands.*;
import me.rigamortis.faurax.gui.click.*;
import me.rigamortis.faurax.gui.click.theme.*;
import me.rigamortis.faurax.utils.*;
import me.rigamortis.faurax.values.*;

public class Client
{
    public static ModuleList modules;
    public static CommandManager cmds;
    public static Config cfg;
    public static ExternalModuleLoader plugins;
    public static ExternalCommandLoader commands;
    public static ClickUI clickUI;
    public static ValueManager values;
    public static ThemeManager themes;
    public static ExternalThemeLoader externalThemes;
    public static ModuleHelper modHelper;
    public static Value t;
    
    static {
        Client.t = new Value("GUITheme", String.class, "Themes", "Faurax", new String[] { "Faurax", "Vanity", "Hamzini", "Nodus" });
    }
    
    public Client() {
        try {
            Client.cmds = new CommandManager();
            Client.modules = new ModuleList();
            Client.plugins = new ExternalModuleLoader();
            Client.commands = new ExternalCommandLoader();
            Client.themes = new ThemeManager();
            Client.externalThemes = new ExternalThemeLoader();
            Client.clickUI = new ClickUI();
            Client.cfg = new Config();
            Client.modHelper = new ModuleHelper();
            getValues();
            ValueManager.values.add(Client.t);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void reload() {
        Client.cmds = new CommandManager();
        Client.modules = new ModuleList();
        Client.commands = new ExternalCommandLoader();
        Client.plugins = new ExternalModuleLoader();
    }
    
    public static ModuleList getModules() {
        if (Client.modules == null) {
            Client.modules = new ModuleList();
        }
        return Client.modules;
    }
    
    public static Config getConfig() {
        if (Client.cfg == null) {
            Client.cfg = new Config();
        }
        return Client.cfg;
    }
    
    public static ModuleHelper getClientHelper() {
        if (Client.modHelper == null) {
            Client.modHelper = new ModuleHelper();
        }
        return Client.modHelper;
    }
    
    public static ThemeManager getThemes() {
        if (Client.themes == null) {
            Client.themes = new ThemeManager();
        }
        return Client.themes;
    }
    
    public static ClickUI getGUI() {
        if (Client.clickUI == null) {
            Client.clickUI = new ClickUI();
        }
        return Client.clickUI;
    }
    
    public static ValueManager getValues() {
        if (Client.values == null) {
            Client.values = new ValueManager();
        }
        return Client.values;
    }
    
    public static CommandManager getCMDS() {
        if (Client.cmds == null) {
            Client.cmds = new CommandManager();
        }
        return Client.cmds;
    }
}
