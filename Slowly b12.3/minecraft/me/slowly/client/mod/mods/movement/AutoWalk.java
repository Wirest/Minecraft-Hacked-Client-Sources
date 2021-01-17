/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class AutoWalk
extends Mod {
    public AutoWalk() {
        super("AutoWalk", Mod.Category.MOVEMENT, Colors.ORANGE.c);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        this.mc.gameSettings.keyBindForward.pressed = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("AutoWalk Disable", ClientNotification.Type.ERROR);
        this.mc.gameSettings.keyBindForward.pressed = false;
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("AutoWalk Enable", ClientNotification.Type.SUCCESS);
    }
}

