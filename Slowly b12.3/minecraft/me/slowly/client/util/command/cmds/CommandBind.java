/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.slowly.client.util.command.cmds;

import me.slowly.client.Client;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.FileUtil;
import me.slowly.client.util.command.Command;
import org.lwjgl.input.Keyboard;

public class CommandBind
extends Command {
    public CommandBind(String[] command) {
        super(command);
        this.setArgs("Args: <bind> <mod> <key>");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length < 3) {
            ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.WARNING);
        } else {
            String mod = args[1];
            int key = Keyboard.getKeyIndex((String)args[2].toUpperCase());
            for (Mod m : ModManager.getModList()) {
                if (!m.getName().equalsIgnoreCase(mod)) continue;
                m.setKey(key);
                ClientUtil.sendClientMessage(String.valueOf(m.getName()) + " was bound to " + Keyboard.getKeyName((int)key), Keyboard.getKeyName((int)key).equals("NONE") ? ClientNotification.Type.ERROR : ClientNotification.Type.SUCCESS);
                Client.getInstance().getFileUtil().saveKeys();
                return;
            }
            ClientUtil.sendClientMessage("Cannot find Module : " + mod, ClientNotification.Type.ERROR);
        }
    }
}

