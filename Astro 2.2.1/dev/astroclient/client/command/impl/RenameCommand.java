package dev.astroclient.client.command.impl;

import dev.astroclient.client.Client;
import dev.astroclient.client.command.Command;

public class RenameCommand extends Command {

    public RenameCommand() {
        super(new String[]{"rename", "clientname"}, "Rename Astro like you've always wanted to!");
    }

    @Override
    public void execute(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++)
            stringBuilder.append(args[i]).append(" ");
        Client.INSTANCE.featureManager.hud.displayName = stringBuilder.toString().trim();
    }
}
