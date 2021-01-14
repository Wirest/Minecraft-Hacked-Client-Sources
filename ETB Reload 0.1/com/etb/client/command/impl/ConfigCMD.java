package com.etb.client.command.impl;

import java.io.IOException;

import com.etb.client.Client;
import com.etb.client.command.Command;
import com.etb.client.config.Config;
import com.etb.client.utils.Printer;

public class ConfigCMD extends Command {

    public ConfigCMD() {
        super("Config",new String[]{"c","config","configs"},"Loads and saves configs for the client");
    }

    @Override
    public void onRun(final String[] s) {
        switch (s[1]) {
            case "list":
                if (!Client.INSTANCE.getConfigManager().getConfigs().isEmpty()) {
                    Printer.print("Current Configs:");
                    Client.INSTANCE.getConfigManager().getConfigs().forEach(cfg -> {
                        Printer.print(cfg.getName());
                    });
                } else {
                    Printer.print("You have no saved configs!");
                }
                break;
            case "help":
                Printer.print("config list - shows all configs.");
                Printer.print("config create/save configname - saves a config.");
                Printer.print("config delete/remove configname - removes a config.");    
                break;
            case "create":
            case "save":
                if (!Client.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Client.INSTANCE.getConfigManager().saveConfig(s[2]);
                    Client.INSTANCE.getConfigManager().getConfigs().add(new Config(s[2]));
                    Printer.print("Created a config named " + s[2] + "!");
                } else {
                    Printer.print(s[2] + " is already a saved config!");
                }
                break;
            case "delete":
            case "remove":
                if (Client.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Client.INSTANCE.getConfigManager().deleteConfig(s[2]);
                    Printer.print("Deleted the config named " + s[2] + "!");
                } else {
                    Printer.print(s[2] + " is not a saved config!");
                }
                break;
            case "reload":
                Client.INSTANCE.getConfigManager().getConfigs().clear();
                Client.INSTANCE.getConfigManager().load();
                Printer.print("Reloaded saved configs. Current number of configs: " + Client.INSTANCE.getConfigManager().getConfigs().size() + "!");
                break;
            case "clear":
                try {
                    if (!Client.INSTANCE.getConfigManager().getConfigs().isEmpty()) {
                        Client.INSTANCE.getConfigManager().clear();
                        Client.INSTANCE.getConfigManager().getConfigs().clear();
                        Printer.print("Cleared all saved configs!");
                    } else {
                        Printer.print("You have no saved configs!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "override":
                if (Client.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Client.INSTANCE.getConfigManager().saveConfig(s[2]);
                    Printer.print("Overrode the config named " + s[2] + "!");
                } else {
                    Printer.print(s[2] + " is not a saved config!");
                }
                break;
            case "load":
                if (Client.INSTANCE.getConfigManager().isConfig(s[2])) {
                    Client.INSTANCE.getConfigManager().loadModules(s[2]);
                    Printer.print("Loaded the config named " + s[2] + "!");
                } else {
                    Printer.print(s[2] + " is not a saved config!");
                }
                break;
        }
    }
}
