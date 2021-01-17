/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.misc;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;

public class NoBob
extends Mod {
    public NoBob() {
        super("NoBob", Mod.Category.MISCELLANEOUS, Colors.ORANGE.c);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        this.setColor(Colors.DARKYELLOW.c);
        this.mc.gameSettings.viewBobbing = true;
        this.mc.thePlayer.distanceWalkedModified = 0.0f;
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("NoBob Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("NoBob Enable", ClientNotification.Type.SUCCESS);
    }
}

