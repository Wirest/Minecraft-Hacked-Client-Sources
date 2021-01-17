/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.player;

import com.darkmagician6.eventapi.EventTarget;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import me.slowly.client.util.TimeHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.FoodStats;

public class Regen
extends Mod {
    private Value<Double> packet;
    TimeHelper delay = new TimeHelper();
	private Value<Double> packetdelay;

    public Regen() {
        super("Regen", Mod.Category.PLAYER, Colors.RED.c);
        this.showValue = this.packet = new Value<Double>("Regen_Packets", 500.0, 1.0, 1000.0, 1.0);
        this.showValue = this.packetdelay = new Value<Double>("Regen_PacketsDelay", 500.0, 1.0, 1000.0, 1.0);
    }

    @EventTarget
    public void onMotion(EventPreMotion event) {
    	if (this.delay.isDelayComplete(this.packetdelay.getValueState().longValue())) {
        if (this.mc.thePlayer.getHealth() < 20.0f && this.mc.thePlayer.getFoodStats().getFoodLevel() >= 19) {
            int i = 0;
            while ((double)i < this.packet.getValueState()) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                ++i;
            }
        }
        this.delay.reset();
    	}
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Regen Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Regen Enable", ClientNotification.Type.SUCCESS);
    }
}

