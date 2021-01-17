/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.combat;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventAttackEntity;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;

public class Criticals
extends Mod {
    public Criticals() {
        super("Criticals", Mod.Category.COMBAT, Colors.RED.c);
    }

    @EventTarget
    public void onUpdate(EventAttackEntity event) {
        this.mc.thePlayer.onCriticalHit(event.getTarget());
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Criticlas Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Critclas Enable", ClientNotification.Type.SUCCESS);
    }
}

