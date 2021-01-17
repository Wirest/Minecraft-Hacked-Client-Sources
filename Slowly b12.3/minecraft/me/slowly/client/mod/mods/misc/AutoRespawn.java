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
import net.minecraft.client.entity.EntityPlayerSP;

public class AutoRespawn
extends Mod {
    public AutoRespawn() {
        super("Respawn", Mod.Category.MISCELLANEOUS, Colors.DARKORANGE.c);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        this.setColor(-6697780);
        if (!this.mc.thePlayer.isEntityAlive()) {
            this.mc.thePlayer.respawnPlayer();
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("AutoRespawn Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("AutoRespawn Enable", ClientNotification.Type.SUCCESS);
    }
}

