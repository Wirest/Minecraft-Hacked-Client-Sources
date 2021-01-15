/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.commands;

import java.util.List;
import me.aristhena.lucid.commands.OptionCommand;
import me.aristhena.lucid.management.command.Com;
import me.aristhena.lucid.management.command.Command;
import me.aristhena.lucid.management.command.CommandManager;
import me.aristhena.lucid.util.ChatUtils;

@Com(names={"help", "?"})
public class Help
extends Command {
    @Override
    public void runCommand(String[] args) {
        ChatUtils.sendClientMessage("All Commands:");
        for (Command command : CommandManager.commandList) {
            if (command instanceof OptionCommand || command.getHelp() == null) continue;
            ChatUtils.sendMessage(command.getHelp());
        }
        ChatUtils.sendMessage(OptionCommand.getHelpString());
    }

    @Override
    public String getHelp() {
        return "Help - help <?> - Returns a list of commands and their information.";
    }
}

