/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.commands;

import me.aristhena.lucid.management.command.Command;
import me.aristhena.lucid.management.command.CommandManager;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.management.option.Option;
import me.aristhena.lucid.management.option.OptionManager;
import me.aristhena.lucid.management.value.Value;
import me.aristhena.lucid.management.value.ValueManager;
import me.aristhena.lucid.util.ChatUtils;

public class OptionCommand
extends Command {
    @Override
    public void runCommand(String[] args) {
        if (args.length < 2) {
            ChatUtils.sendClientMessage(OptionCommand.getHelpString());
            return;
        }
        Module mod = ModuleManager.getModule(args[0].replaceFirst(CommandManager.commandPrefix, ""));
        if (mod != ModuleManager.MODULE_EMPTY) {
            Option option = OptionManager.getOption(args[1], mod);
            Value value = ValueManager.getValue(args[1], mod);
            if (option != null) {
                option.setValue(!option.value);
                ChatUtils.sendClientMessage(String.valueOf(option.name) + " Set To " + option.value);
            } else if (value != null) {
                try {
                    value.setValue(Double.parseDouble(args[2]));
                    ChatUtils.sendClientMessage(String.valueOf(value.name) + " Set To " + args[2]);
                }
                catch (NumberFormatException e) {
                    ChatUtils.sendClientMessage("Value input error.");
                }
            } else {
                ChatUtils.sendClientMessage("Option or value not recognized.");
            }
        } else {
            ChatUtils.sendClientMessage(OptionCommand.getHelpString());
        }
    }

    public static String getHelpString() {
        return "Toggle Option / Set Value - (modname) (option/value name) <double>";
    }
}

