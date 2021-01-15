/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.aristhena.lucid.commands;

import me.aristhena.lucid.management.command.Com;
import me.aristhena.lucid.management.command.Command;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.util.ChatUtils;
import org.lwjgl.input.Keyboard;

@Com(names={"bind", "b"})
public class Bind
extends Command {
    @Override
    public void runCommand(String[] args) {
        String modName = "";
        String keyName = "";
        if (args.length > 1) {
            modName = args[1];
            if (args.length > 2) {
                keyName = args[2];
            }
        }
        Module module = ModuleManager.getModule(modName);
        if (module.name.equalsIgnoreCase("null")) {
            ChatUtils.sendClientMessage("Invalid Module.");
            return;
        }
        if (keyName == "") {
            ChatUtils.sendClientMessage(String.valueOf(module.name) + "'s bind has been cleared.");
            module.keyBind = 0;
            ModuleManager.save();
            return;
        }
        module.keyBind = Keyboard.getKeyIndex((String)keyName.toUpperCase());
        ModuleManager.save();
        if (Keyboard.getKeyIndex((String)keyName.toUpperCase()) == 0) {
            ChatUtils.sendClientMessage("Invalid Key entered, Bind cleared.");
        } else {
            ChatUtils.sendClientMessage(String.valueOf(module.name) + " bound to " + keyName);
        }
    }

    @Override
    public String getHelp() {
        return "Bind - bind <b> (module) (key) - Bind the specified module to a key.";
    }
}

