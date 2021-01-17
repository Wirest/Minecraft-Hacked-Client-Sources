/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.util.command.cmds;

import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.command.Command;

public class CommandToggle
extends Command {
    public CommandToggle(String[] commands) {
        super(commands);
        this.setArgs(".toggle <mod>");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length != 2) {
            ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.INFO);
            return;
        }
        boolean found = false;
        for (Mod mod : ModManager.getModList()) {
            if (!args[1].equalsIgnoreCase(mod.getName())) continue;
            mod.set(!mod.isEnabled());
            found = true;
            ClientUtil.sendClientMessage(String.valueOf(mod.getName()) + " was toggled", ClientNotification.Type.SUCCESS);
            break;
        }
        if (!found) {
            ClientUtil.sendClientMessage("Cannot find Module : " + args[1], ClientNotification.Type.WARNING);
        }
    }
}

