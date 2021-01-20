package cedo.command.commands;

import cedo.Fan;
import cedo.command.Command;
import cedo.util.Logger;

public class ConfigCommand extends Command {

    public static String namenameOfWatermark;

    String configName;

    public ConfigCommand() {
        super("Config", "config");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            Logger.ingameInfo("Config Manager Commands");
            Logger.ingameInfo(".config load <name>");
            Logger.ingameInfo(".config save <name>");
            Logger.ingameInfo(".config delete <name>");
            Logger.ingameInfo(".config list");
            return;
        }

        if (args.length >= 2) {
            configName = args[1];
        }

        if (args[0].equalsIgnoreCase("load")) {
            Fan.cfgManager.load(configName);
            Logger.ingameInfo("Config \"" + configName + "\" was loaded.");
        } else if (args[0].equalsIgnoreCase("save")) {
            Fan.cfgManager.save(configName);
            Logger.ingameInfo("Current config has been saved as \"" + configName + "\"");
        } else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
            Fan.cfgManager.delete(configName);
            Logger.ingameInfo("The config \"" + configName + "\" has been removed");
        } else if (args[0].equalsIgnoreCase("list")) {
            Logger.ingameInfo("List of configs:");
            Fan.cfgManager.list();
        } else {
            Logger.ingameError("Command Not Found!");
            Logger.ingameInfo("Config Manager Commands");
            Logger.ingameInfo(".config load <name>");
            Logger.ingameInfo(".config save <name>");
            Logger.ingameInfo(".config delete <name>");
            Logger.ingameInfo(".config list");
        }
    }
}
