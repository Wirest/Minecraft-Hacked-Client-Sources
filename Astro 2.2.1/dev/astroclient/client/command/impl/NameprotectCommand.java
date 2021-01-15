package dev.astroclient.client.command.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.command.Command;

public class NameprotectCommand extends Command {

    public NameprotectCommand() {
        super(new String[]{"nameprotect", "name", "streamermode"}, "Change your nameprotect name.");
    }

    @Override
    public void execute(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++)
            stringBuilder.append(args[i]).append(" ");
        Client.INSTANCE.featureManager.streamerMode.name = stringBuilder.toString().trim();
    }
}
