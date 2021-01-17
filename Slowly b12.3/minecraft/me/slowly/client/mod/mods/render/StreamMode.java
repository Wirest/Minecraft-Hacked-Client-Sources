/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.render;

import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;

public class StreamMode
extends Mod {
    public StreamMode() {
        super("StreamMode", Mod.Category.RENDER, Colors.YELLOW.c);
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("StreamMode Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("StreamMode Enable", ClientNotification.Type.SUCCESS);
    }
}

