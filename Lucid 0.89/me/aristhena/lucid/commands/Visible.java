/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.commands;

import me.aristhena.lucid.management.command.Com;
import me.aristhena.lucid.management.command.Command;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.util.ChatUtils;

@Com(names={"visible", "v", "show"})
public class Visible
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
        module.shown = !module.shown;
        ChatUtils.sendClientMessage(String.valueOf(module.name) + " is now " + (module.enabled ? "shown" : "hidden"));
    }

    @Override
    public String getHelp() {
        return "Visible - visible <v, show> (module) - Shows or hides the module on the arraylist.";
    }
}

