/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.misc;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import net.minecraft.client.Minecraft;

public class FastPlace
extends Mod {
    public FastPlace() {
        super("FastPlace", Mod.Category.MISCELLANEOUS, Colors.MAGENTA.c);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        this.mc.rightClickDelayTimer = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.rightClickDelayTimer = 4;
        ClientUtil.sendClientMessage("FastPlace Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("FastPlace Enable", ClientNotification.Type.SUCCESS);
    }
}

