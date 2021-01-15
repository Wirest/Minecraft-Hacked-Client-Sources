package dev.astroclient.client.command.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.command.Command;
import dev.astroclient.client.configuration.IConfiguration;
import dev.astroclient.client.configuration.impl.BasicConfiguration;
import dev.astroclient.client.util.ChatUtil;

/**
 * made by Xen for Astro
 * at 12/8/2019
 **/
public class ConfigCommand extends Command {

    public ConfigCommand() {
        super(new String[]{"config", "cfg", "cf", "preset"}, "Load and save Astro configs.");
    }

    @Override
    public void execute(String[] args) {
        super.execute(args);
        if (args.length == 1) return;

        switch (args[1]) {
            case "list":
                if (!Client.INSTANCE.configManager.loadConfigs.isEmpty()) {
                    ChatUtil.tellPlayer("Current Configs: ");
                    for(IConfiguration configuration : Client.INSTANCE.configManager.loadConfigs) {
                        if(configuration instanceof BasicConfiguration)
                            ChatUtil.tellPlayer(((BasicConfiguration) configuration).getName());
                    }
                } else {
                    ChatUtil.tellPlayer("You have no saved configs!");
                }
                break;
            case "help":
                ChatUtil.tellPlayer("config list - shows all configs.");
                ChatUtil.tellPlayer("config create/save <name> - saves a config.");
                ChatUtil.tellPlayer("config delete/remove <name> - removes a config.");
                ChatUtil.tellPlayer("config reload/rl - reloads configs.");
                ChatUtil.tellPlayer("config load/l <name> - loads the specified config.");
                ChatUtil.tellPlayer("config override <name> - overrides a saved config.");
                break;
            case "create":
            case "save":
                if (!Client.INSTANCE.configManager.isConfig(args[2])) {
                    Client.INSTANCE.configManager.save(args[2]);
                    ChatUtil.tellPlayer("Created a config named " + args[2]);
                    reload();
                } else {
                    ChatUtil.tellPlayer(args[2] + " is already a saved config!");
                }
                break;
            case "remove":
            case "delete":
                if (Client.INSTANCE.configManager.isConfig(args[2])) {
                    Client.INSTANCE.configManager.delete(args[2]);
                    ChatUtil.tellPlayer("Deleted the config named " + args[2] + "!");
                } else {
                    ChatUtil.tellPlayer(args[2] + " is not a saved config!");
                }
                break;
            case "rl":
            case "reload":
                reload();
                ChatUtil.tellPlayer("Reloaded all saved configs.");
                break;
            case "l":
            case "load":
                if (Client.INSTANCE.configManager.isConfig(args[2])) {
                    Client.INSTANCE.configManager.load(args[2]);
                    ChatUtil.tellPlayer("Loaded the config named " + args[2] + "!");
                } else {
                    ChatUtil.tellPlayer(args[2] + " is not a saved config!");
                }
                break;
            case "override":
                if (Client.INSTANCE.configManager.isConfig(args[2])) {
                    Client.INSTANCE.configManager.save(args[2]);
                    ChatUtil.tellPlayer("Overrode the config named " + args[2] + "!");
                } else {
                    ChatUtil.tellPlayer(args[2] + " is not a saved config!");
                }

                break;
        }

    }

    private void reload() {
        Client.INSTANCE.configManager.loadConfigs.clear();
        Client.INSTANCE.configManager.load();
    }
}
