/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.misc;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventSafeWalk;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;

public class SafeWalk
extends Mod {
    public SafeWalk() {
        super("SafeWalk", Mod.Category.MISCELLANEOUS, Colors.DARKAQUA.c);
    }

    @EventTarget
    public void onSafeWalk(EventSafeWalk event) {
        event.setSafe(true);
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("SafeWalk Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("SafeWalk Enable", ClientNotification.Type.SUCCESS);
    }
}

