package dev.astroclient.client.command.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.command.Command;
import dev.astroclient.client.util.ChatUtil;

public class HelpCommand extends Command {

    public HelpCommand() {
        super(new String[]{"help"}, "Lists commands descriptions.");
    }

    @Override
    public void execute(String... args) {
        for (Command command : Client.INSTANCE.commandManager.commands) {
            ChatUtil.tellPlayer(command.getUsages()[0] + ": " + command.getDescription());
        }
    }
}
