/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.commands;

import me.aristhena.lucid.management.command.Com;
import me.aristhena.lucid.management.command.Command;
import me.aristhena.lucid.management.command.CommandManager;
import me.aristhena.lucid.util.ChatUtils;

@Com(names={""})
public class UnknownCommand
extends Command {
    @Override
    public void runCommand(String[] args) {
        ChatUtils.sendClientMessage("Unknown Command. Try " + CommandManager.commandPrefix + "help.");
    }
}

