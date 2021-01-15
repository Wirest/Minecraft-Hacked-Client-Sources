// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.util.MathHelper;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.item.ItemBow;
import me.CheerioFX.FusionX.events.EventUpdate;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class FastBow extends Module
{
    public FastBow() {
        super("FastBow", Category.COMBAT);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (!this.getState()) {
            return;
        }
        if (FastBow.mc.thePlayer.onGround && FastBow.mc.thePlayer.getCurrentEquippedItem() != null && FastBow.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow && FastBow.mc.gameSettings.keyBindUseItem.pressed) {
            FastBow.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(FastBow.mc.thePlayer.getHeldItem()));
            FastBow.mc.thePlayer.setItemInUse(FastBow.mc.thePlayer.getHeldItem(), 7199);
            if (FastBow.mc.thePlayer.ticksExisted % 2 == 0) {
                FastBow.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(FastBow.mc.thePlayer.posX, FastBow.mc.thePlayer.posY - 0.2, FastBow.mc.thePlayer.posZ, FastBow.mc.thePlayer.onGround));
                for (int i = 0; i < 20; ++i) {
                    FastBow.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(FastBow.mc.thePlayer.posX, FastBow.mc.thePlayer.posY + 1.0E-9, FastBow.mc.thePlayer.posZ, FastBow.mc.thePlayer.rotationYaw, FastBow.mc.thePlayer.rotationPitch, true));
                }
            }
            FastBow.mc.playerController.onStoppedUsingItem(FastBow.mc.thePlayer);
        }
    }
    
    public void setSpeed(final double speed) {
        FastBow.mc.thePlayer.motionX = -MathHelper.sin(this.getDirection()) * speed;
        FastBow.mc.thePlayer.motionZ = MathHelper.cos(this.getDirection()) * speed;
    }
    
    public float getDirection() {
        float yaw = FastBow.mc.thePlayer.rotationYawHead;
        final float forward = FastBow.mc.thePlayer.moveForward;
        final float strafe = FastBow.mc.thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        return yaw * 0.017453292f;
    }
}
