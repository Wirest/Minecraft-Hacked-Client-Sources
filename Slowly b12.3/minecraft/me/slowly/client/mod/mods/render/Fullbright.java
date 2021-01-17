/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.render;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class Fullbright
extends Mod {
    public Fullbright() {
        super("Fullbright", Mod.Category.RENDER, Colors.YELLOW.c);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        this.setColor(Colors.GREY.c);
        this.mc.gameSettings.gammaSetting = 10.0f;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.gameSettings.gammaSetting = 1.0f;
        ClientUtil.sendClientMessage("Fullbright Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Fullbright Enable", ClientNotification.Type.SUCCESS);
    }
}

