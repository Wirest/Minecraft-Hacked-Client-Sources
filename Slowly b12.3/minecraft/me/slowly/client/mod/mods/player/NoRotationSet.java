/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.player;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventReceivePacket;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.mods.combat.KillAura;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class NoRotationSet
extends Mod {
    private Value<String> mode = new Value("NoRotate", "Mode", 0);

    public NoRotationSet() {
        super("NoRotate", Mod.Category.PLAYER, Colors.DARKRED.c);
        this.mode.addValue("Standard");
        this.mode.addValue("AAC");
    }

    @EventTarget
    public void onEvent(EventReceivePacket event) {
        this.setColor(-564656);
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
            if (this.mc.thePlayer.rotationYaw > -90.0f && this.mc.thePlayer.rotationPitch < 90.0f && (this.mode.isCurrentMode("Standard") || KillAura.curTarget != null)) {
                packet.yaw = this.mc.thePlayer.rotationYaw;
                packet.pitch = this.mc.thePlayer.rotationPitch;
            }
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("NoRotationSet Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("NoRotationSet Enable", ClientNotification.Type.SUCCESS);
    }
}

