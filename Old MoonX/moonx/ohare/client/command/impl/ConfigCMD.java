package moonx.ohare.client.command.impl;


import moonx.ohare.client.Moonx;
import moonx.ohare.client.command.Command;
import moonx.ohare.client.config.Config;
import moonx.ohare.client.utils.Printer;

import java.io.IOException;

public class ConfigCMD extends Command {

    public ConfigCMD() {
        super("Config", new String[]{"c", "config", "configs"});
    }

    @Override
    public void onRun(final String[] s) {
    	if (s.length == 1) {
            Printer.print("config create/save configname ((keys) if you want to save with keybinds) - saves a config.");
            Printer.print("config override configname ((keys) if you want to override with keybinds) - overrides existing configs.");
            Printer.print("config delete/remove configname - removes a config.");
    		return;
    	}
        switch (s[1]) {
            case "list":
                if (!Moonx.INSTANCE.getConfigManager().getConfigs().isEmpty()) {
                    Printer.print("Current Configs:");
                    Moonx.INSTANCE.getConfigManager().getConfigs().forEach(cfg -> {
                        Printer.print(cfg.getName());
                    });
                } else {
                    Printer.print("You have no saved configs!");
                }
                break;
            case "help":
                Printer.print("config list - shows all configs.");
                Printer.print("config create/save configname ((keys) if you want to save with keybinds) - saves a config.");
                Printer.print("config override configname ((keys) if you want to override with keybinds) - overrides existing configs.");
                Printer.print("config delete/remove configname - removes a config.");
                break;
            case "create":
            case "save":
                if (!Moonx.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Moonx.INSTANCE.getConfigManager().saveConfig(s[2], s.length > 3 && s[3].equalsIgnoreCase("keys"));
                    Moonx.INSTANCE.getConfigManager().getConfigs().add(new Config(s[2]));
                    Printer.print("Created a config named " + s[2] + (s.length > 3 && s[3].equalsIgnoreCase("keys") ? " with keys included" : "") + "!");
                    Moonx.INSTANCE.getNotificationManager().addNotification("Created a config named " + s[2] + (s.length > 3 && s[3].equalsIgnoreCase("keys") ? " with keys included" : "") + "!",2000);
                } else {
                    Printer.print(s[2] + " is already a saved config!");
                    Moonx.INSTANCE.getNotificationManager().addNotification( s[2] + " is already a saved config!",2000);
                }
                break;
            case "delete":
            case "remove":
                if (Moonx.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Moonx.INSTANCE.getConfigManager().deleteConfig(s[2]);
                    Printer.print("Deleted the config named " + s[2] + "!");
                    Moonx.INSTANCE.getNotificationManager().addNotification( "Deleted the config named " + s[2] + "!",2000);
                } else {
                    Printer.print(s[2] + " is not a saved config!");
                    Moonx.INSTANCE.getNotificationManager().addNotification( s[2] + " is not a saved config!",2000);
                }
                break;
            case "reload":
                Moonx.INSTANCE.getConfigManager().getConfigs().clear();
                Moonx.INSTANCE.getConfigManager().load();
                Printer.print("Reloaded all saved configs. Current number of configs: " + Moonx.INSTANCE.getConfigManager().getConfigs().size() + "!");
                Moonx.INSTANCE.getNotificationManager().addNotification( "Reloaded all saved configs. Current number of configs: " + Moonx.INSTANCE.getConfigManager().getConfigs().size() + "!",2000);
                break;
            case "clear":
                try {
                    if (!Moonx.INSTANCE.getConfigManager().getConfigs().isEmpty()) {
                        Moonx.INSTANCE.getConfigManager().clear();
                        Moonx.INSTANCE.getConfigManager().getConfigs().clear();
                        Printer.print("Cleared all saved configs!");
                        Moonx.INSTANCE.getNotificationManager().addNotification( "Cleared all saved configs!",2000);
                    } else {
                        Printer.print("You have no saved configs!");
                        Moonx.INSTANCE.getNotificationManager().addNotification( "You have no saved configs!",2000);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "override":
                if (Moonx.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Moonx.INSTANCE.getConfigManager().saveConfig(s[2], s.length > 3 && s[3].equalsIgnoreCase("keys"));
                    Printer.print("Overrode the config named " + s[2] + (s.length > 3 && s[3].equalsIgnoreCase("keys") ? " with keys included" : "") + "!");
                    Moonx.INSTANCE.getNotificationManager().addNotification( "Overrode the config named " + s[2] + (s.length > 3 && s[3].equalsIgnoreCase("keys") ? " with keys included" : "") + "!",2000);
                } else {
                    Printer.print(s[2] + " is not a saved config!");
                    Moonx.INSTANCE.getNotificationManager().addNotification( s[2] + " is not a saved config!",2000);
                }
                break;
            case "load":
                if (Moonx.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Moonx.INSTANCE.getConfigManager().loadConfig(s[2]);
                    Printer.print("Loaded the config named " + s[2] + "!");
                    Moonx.INSTANCE.getNotificationManager().addNotification( "Loaded the config named " + s[2] + "!",2000);
                } else {
                    Printer.print(s[2] + " is not a saved config!");
                    Moonx.INSTANCE.getNotificationManager().addNotification( s[2] + " is not a saved config!",2000);
                }
                break;
        }
    }
}
