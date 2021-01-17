/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.misc;

import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;

public class NoHurtcam
extends Mod {
    public NoHurtcam() {
        super("NoHurtcam", Mod.Category.MISCELLANEOUS, Colors.DARKORANGE.c);
        this.setColor(-6697780);
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("NoHurtcam Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("NoHurtcam Enable", ClientNotification.Type.SUCCESS);
    }
}

