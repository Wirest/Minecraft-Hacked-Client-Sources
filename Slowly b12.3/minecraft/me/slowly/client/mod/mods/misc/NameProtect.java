/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.misc;

import java.io.IOException;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.command.cmds.CommandNameProtect;

public class NameProtect
extends Mod {
    public static String name = "Slowly";

    public NameProtect() {
        super("NameProtect", Mod.Category.RENDER, Colors.YELLOW.c);
        try {
            CommandNameProtect.loadName();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("NameProtect Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("NameProtect Enable", ClientNotification.Type.SUCCESS);
    }

}

