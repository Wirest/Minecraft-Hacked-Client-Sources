package info.sigmaclient.management.command.impl;

import java.util.ArrayList;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.util.EnumChatFormatting;

public class Help extends Command {

    public Help(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        // Intended arguements
        // 0 - List commands
        // 1 - List details for command
        int i = 1;

        if (args == null) {
            // Due to commands having being registered multiple times via aliases,
            // take only the first instance (Determined by the names in the alias
            // String[])
            ArrayList<String> used = new ArrayList<String>();
            for (Command command : Client.commandManager.getCommands()) {
                if (used.contains(command.getName())) {
                    continue;
                }
                used.add(command.getName());
                ChatUtil.printChat(chatPrefix + i + ". " + command.getName() + " - " + command.getDescription());
                i++;
            }
            ChatUtil.printChat(chatPrefix + "Specify a name of a command for details about it.");
        } else if (args.length > 0) {
            String name = args[0];
            Command command = Client.commandManager.getCommand(name);
            if (command == null) {
                ChatUtil.printChat(chatPrefix + "Could not find: " + name);
                return;
            }
            ChatUtil.printChat(chatPrefix + command.getName() + ": " + command.getDescription());
            ChatUtil.printChat(chatPrefix + "Usage: " + command.getUsage());
        }
    }

    @Override
    public String getUsage() {
        return "Help " + EnumChatFormatting.ITALIC + " [optional] " + EnumChatFormatting.RESET + "<Command>";
    }

    @Override
    public void onEvent(Event event) {

    }
}
