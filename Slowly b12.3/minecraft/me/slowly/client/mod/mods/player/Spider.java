/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.player;

import com.darkmagician6.eventapi.EventTarget;
import java.util.List;
import me.slowly.client.events.BlockCollideEvent;
import me.slowly.client.events.EventSendPacket;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class Spider
extends Mod {
    private int tick = 0;

    public Spider() {
        super("Spider", Mod.Category.PLAYER, Colors.AQUA.c);
    }

    @EventTarget
    public void onEvent(UpdateEvent event) {
        this.setColor(-45656);
        if (this.mc.thePlayer.isCollidedHorizontally && !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isInLava()) {
            if (this.mc.thePlayer.onGround) {
                this.mc.thePlayer.motionY = 0.39;
            } else if (this.mc.thePlayer.motionY < 0.0) {
                this.mc.thePlayer.motionY = -0.24;
            }
        }
    }

    @EventTarget
    public void onBlockCollide(BlockCollideEvent e) {
        if (this.mc.thePlayer.isCollidedHorizontally && !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isInLava()) {
            e.boxes.add(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0).offset(this.mc.thePlayer.posX, (int)this.mc.thePlayer.posY - 1, this.mc.thePlayer.posZ));
        }
    }

    @EventTarget
    public void onPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer p = (C03PacketPlayer)event.getPacket();
            if (this.mc.thePlayer.isCollidedHorizontally && !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isInLava()) {
                double speed = 1.0E-10;
                float f = PlayerUtil.getDirection();
                p.x -= (double)MathHelper.sin(f) * speed;
                p.z += (double)MathHelper.cos(f) * speed;
            }
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Spider Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Spider Enable", ClientNotification.Type.SUCCESS);
    }
}

