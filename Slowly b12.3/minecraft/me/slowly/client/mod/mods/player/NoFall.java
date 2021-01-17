/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.player;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall
extends Mod {
    private float b = 1.0f;
    private boolean c = true;
    private int d;
    private Value<String> mode = new Value("NoFall", "Mode", 0);

    public NoFall() {
        super("NoFall", Mod.Category.PLAYER, Colors.YELLOW.c);
        this.mode.mode.add("Packet");
        this.mode.mode.add("AAC3.3.8");
        this.mode.mode.add("AAC3.3.11");
        this.showValue = this.mode;
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (this.mode.isCurrentMode("Packet")) {
            if (event instanceof UpdateEvent && (mc.thePlayer.fallDistance > 2.0F) && (!mc.thePlayer.onGround)){
            	mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }
        } else if (this.mode.isCurrentMode("AAC3.3.8")) {
            if (this.mc.thePlayer.onGround) {
                this.mc.thePlayer.motionY = -9.0;
            }
        } else if (this.mode.isCurrentMode("AAC3.3.11") && this.mc.thePlayer.fallDistance > 2.0f) {
            this.mc.thePlayer.motionZ = 0.0;
            this.mc.thePlayer.motionX = 0.0;
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.001, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("NoFall Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("NoFall Enable", ClientNotification.Type.SUCCESS);
    }
}

