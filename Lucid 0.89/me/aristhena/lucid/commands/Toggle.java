/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.commands;

import me.aristhena.lucid.management.command.Com;
import me.aristhena.lucid.management.command.Command;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.util.ChatUtils;

@Com(names={"toggle", "t", "tog"})
public class Toggle
extends Command {
    @Override
    public void runCommand(String[] args) {
        String modName = "";
        if (args.length > 1) {
            modName = args[1];
        }
        Module module = ModuleManager.getModule(modName);
        if (module.name.equalsIgnoreCase("null")) {
            ChatUtils.sendClientMessage("Invalid Module.");
            return;
        }
        module.toggle();
        ChatUtils.sendClientMessage(String.valueOf(module.name) + " is now " + (module.enabled ? "enabled" : "disabled"));
    }

    @Override
    public String getHelp() {
        return "Toggle - toggle <t, tog> (module) - Toggles the module's enabled state.";
    }
}

