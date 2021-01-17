package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.*;

public class CommandConfig extends Command
{
    public CommandConfig() {
        super("Config", new String[] { "cfg" });
    }
    
    @Argument
    protected String configHelp() {
        final String help = "Load, Save, LoadPreset <fileName>, SavePreset <fileName>";
        return help;
    }
    
    @Argument(handles = { "load", "l" })
    protected String loadCFG() {
        Client.getConfig().loadConfig();
        Client.getConfig().loadGUI();
        Client.getConfig().loadBinds();
        Client.getConfig().loadXray();
        Client.getConfig().loadFriends();
        Client.getConfig().loadMods();
        Client.getConfig().loadGUITheme();
        return "Done.";
    }
    
    @Argument(handles = { "save", "s" })
    protected String saveCFG() {
        Client.getConfig().saveConfig();
        Client.getConfig().saveGUI();
        Client.getConfig().saveBinds();
        Client.getConfig().saveXray();
        Client.getConfig().saveFriends();
        Client.getConfig().saveMods();
        Client.getConfig().saveGUITheme();
        return "Done.";
    }
    
    @Argument(handles = { "loadpreset", "lp" })
    protected String loadPresetCFG(final String fileName) {
        Client.getConfig().loadPresetConfig(fileName);
        Client.getConfig().loadGUI();
        Client.getConfig().loadBinds();
        Client.getConfig().loadXray();
        Client.getConfig().loadFriends();
        Client.getConfig().loadMods();
        Client.getConfig().loadModsPreset(fileName);
        Client.getConfig().loadGUITheme();
        return "Done.";
    }
    
    @Argument(handles = { "savepreset", "sp" })
    protected String savePresetCFG(final String fileName) {
        Client.getConfig().savePresetConfig(fileName);
        Client.getConfig().saveGUI();
        Client.getConfig().saveBinds();
        Client.getConfig().saveXray();
        Client.getConfig().saveFriends();
        Client.getConfig().saveMods();
        Client.getConfig().saveModsPreset(fileName);
        Client.getConfig().saveGUITheme();
        return "Done.";
    }
}
