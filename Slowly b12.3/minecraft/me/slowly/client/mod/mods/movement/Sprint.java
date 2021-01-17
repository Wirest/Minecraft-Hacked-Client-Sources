/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.mod.mods.movement.ScaffoldWalk;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class Sprint
extends Mod {
    public Sprint() {
        super("Sprint", Mod.Category.MOVEMENT, Colors.GREY.c);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        this.setColor(-6710887);
        if (!(!this.mc.gameSettings.keyBindForward.pressed || this.mc.thePlayer.isCollidedHorizontally || ScaffoldWalk.mode.isCurrentMode("Gomme") && ModManager.getModByName("ScaffoldWalk").isEnabled())) {
            this.mc.thePlayer.setSprinting(true);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.thePlayer.setSprinting(false);
        ClientUtil.sendClientMessage("Sprint Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Sprint Enable", ClientNotification.Type.SUCCESS);
    }
}

