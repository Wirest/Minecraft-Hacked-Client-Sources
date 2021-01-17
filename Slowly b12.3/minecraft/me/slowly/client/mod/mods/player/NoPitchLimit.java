/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.player;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class NoPitchLimit
extends Mod {
    private Value<String> mode = new Value("NoPitchLimit", "Mode", 0);

    public NoPitchLimit() {
        super("NoPitchLimit", Mod.Category.PLAYER, Colors.DARKYELLOW.c);
        this.mode.mode.add("Normal");
        this.mode.mode.add("AAC");
    }

    @Override
    public void onDisable() {
        if (this.mc.thePlayer.rotationPitch >= 90.0f) {
            this.mc.thePlayer.rotationPitch = 90.0f;
        } else if (this.mc.thePlayer.rotationPitch <= -90.0f) {
            this.mc.thePlayer.rotationPitch = -90.0f;
        }
        super.onDisable();
        ClientUtil.sendClientMessage("NoFall Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("NoFall Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        if (this.mode.isCurrentMode("AAC")) {
            if (this.mc.thePlayer.rotationPitch >= 90.0f) {
                event.pitch = 90.0f;
            } else if (this.mc.thePlayer.rotationPitch <= -90.0f) {
                event.pitch = -90.0f;
            }
        }
    }
    
}

