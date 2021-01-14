package com.etb.client.command.impl;

import com.etb.client.Client;
import com.etb.client.command.Command;
import com.etb.client.utils.Printer;
import com.mojang.realmsclient.gui.ChatFormatting;

public class Help extends Command {

    public Help() {
        super("Help", new String[]{"h", "help"}, "Shows all commands and usage of them");
    }

    @Override
    public void onRun(final String[] s) {
        Printer.printWithoutPrefix("§7§m§l------------------------------");
        Printer.printWithoutPrefix("                      §c§lETB Client");
        Client.INSTANCE.getCommandManager().getCommandMap().values().forEach(command -> {
            String str = "§c." + command.getLabel().toLowerCase();
            StringBuilder stringBuilder = new StringBuilder(str);
            stringBuilder.append(" > " + ChatFormatting.GRAY);
            stringBuilder.append(command.getDescription());
            Printer.printWithoutPrefix(stringBuilder.toString());
        });
        Printer.printWithoutPrefix("§7§m§l------------------------------");
    }
}
