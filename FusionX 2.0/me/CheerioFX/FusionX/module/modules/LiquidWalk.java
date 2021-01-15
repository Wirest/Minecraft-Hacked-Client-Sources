// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.network.Packet;
import net.minecraft.entity.player.EntityPlayer;
import me.CheerioFX.FusionX.utils.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import me.CheerioFX.FusionX.utils.BlockUtils;
import me.CheerioFX.FusionX.events.EventPostMotionUpdates;
import net.minecraft.block.material.Material;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import net.minecraft.network.play.client.C03PacketPlayer;
import me.CheerioFX.FusionX.events.EventPacketSent;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import me.CheerioFX.FusionX.utils.LiquidUtils;
import me.CheerioFX.FusionX.events.EventBoundingBox;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class LiquidWalk extends Module
{
    public static int mode;
    boolean canStart;
    public static boolean randomVar;
    public static int Delay;
    public static boolean shouldOffsetPacket;
    
    static {
        LiquidWalk.mode = 4;
    }
    
    public LiquidWalk() {
        super("LiquidWalk", 0, Category.MOVEMENT);
        this.canStart = false;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        if (LiquidWalk.mode == 4) {
            LiquidWalk.shouldOffsetPacket = false;
        }
        super.onDisable();
    }
    
    private boolean shouldSetBoundingBox() {
        return !LiquidWalk.mc.thePlayer.isSneaking() && LiquidWalk.mc.thePlayer.fallDistance < 4.0f;
    }
    
    @EventTarget
    private void onBoundingBox(final EventBoundingBox event) {
        if (LiquidWalk.mode == 4 && !LiquidUtils.isInLiquid() && event.getBlock() instanceof BlockLiquid && LiquidWalk.mc.theWorld.getBlockState(event.getBlockPos()).getBlock() instanceof BlockLiquid && (int)LiquidWalk.mc.theWorld.getBlockState(event.getBlockPos()).getValue(BlockLiquid.LEVEL) == 0 && this.shouldSetBoundingBox() && event.getBlockPos().getY() + 1 <= LiquidWalk.mc.thePlayer.boundingBox.minY) {
            event.setBoundingBox(new AxisAlignedBB(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ(), event.getBlockPos().getX() + 1, event.getBlockPos().getY() + 1, event.getBlockPos().getZ() + 1));
        }
    }
    
    @EventTarget
    private void onPacketSend(final EventPacketSent event) {
        if (LiquidWalk.mode == 4 && event.getPacket() instanceof C03PacketPlayer && LiquidUtils.isOnLiquid()) {
            final C03PacketPlayer packet = (C03PacketPlayer)event.getPacket();
            final boolean bl = LiquidWalk.shouldOffsetPacket = !LiquidWalk.shouldOffsetPacket;
            if (LiquidWalk.shouldOffsetPacket) {
                final C03PacketPlayer c03PacketPlayer = packet;
                c03PacketPlayer.y -= 1.0E-6;
            }
        }
    }
    
    public void onPreMotionUpdates(final EventPreMotionUpdates event) {
        LiquidWalk.mode = 1;
        if (LiquidWalk.mode == 4) {
            this.canStart = true;
            if (LiquidUtils.isInLiquid() && LiquidWalk.mc.thePlayer.isInsideOfMaterial(Material.air) && !LiquidWalk.mc.thePlayer.isSneaking()) {
                LiquidWalk.mc.thePlayer.motionY = 0.085;
            }
            if (!LiquidUtils.isOnLiquid() || LiquidUtils.isInLiquid() || !this.shouldSetBoundingBox()) {
                LiquidWalk.shouldOffsetPacket = false;
            }
        }
    }
    
    @EventTarget
    public void onPostMotionUpdates(final EventPostMotionUpdates event) {
        LiquidWalk.mode = 2;
        if (LiquidWalk.mode == 2 && LiquidWalk.mc.gameSettings.keyBindJump.pressed && LiquidUtils.isInLiquid()) {
            LiquidWalk.mc.thePlayer.setSprinting(false);
            LiquidWalk.mc.thePlayer.jump();
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            LiquidWalk.mode = 2;
            if (LiquidWalk.mode == 1) {
                if (BlockUtils.isInLiquid() && !LiquidWalk.mc.thePlayer.isSneaking()) {
                    LiquidWalk.mc.thePlayer.motionY = 0.1;
                    final EntityPlayerSP thePlayer = LiquidWalk.mc.thePlayer;
                    thePlayer.jumpMovementFactor *= 1.12f;
                }
                else {
                    LiquidWalk.mc.thePlayer.jumpMovementFactor = 0.0f;
                }
            }
        }
    }
    
    @EventTarget
    public void onPreMotionUpdate(final EventPacketSent event) {
        if (LiquidWalk.mode == 2 || LiquidWalk.mode == 3) {
            final double posX = Wrapper.mc.thePlayer.posX;
            final double posY = Wrapper.mc.thePlayer.posY;
            final double posZ = Wrapper.mc.thePlayer.posZ;
            if (Wrapper.liquidCollision().equalsIgnoreCase("positive") && !Wrapper.mc.thePlayer.isInWater() && Wrapper.mc.thePlayer.onGround) {
                Wrapper.mc.thePlayer.setPosition(posX - 1.0E-10, posY, posZ - 1.0E-10);
            }
            else if (Wrapper.liquidCollision().equalsIgnoreCase("negative") && !Wrapper.mc.thePlayer.isInWater() && Wrapper.mc.thePlayer.onGround) {
                Wrapper.mc.thePlayer.setPosition(posX + 1.0E-10, posY, posZ + 1.0E-10);
            }
            LiquidWalk.randomVar = true;
            if (Wrapper.isOnLiquid() && !Wrapper.getBlockUnderPlayer2(Wrapper.mc.thePlayer, -1.0E-5).getMaterial().isLiquid()) {
                if (Wrapper.mc.gameSettings.keyBindSneak.isPressed()) {
                    Wrapper.mc.thePlayer.setPosition(posX, posY - 0.1, posZ);
                }
                LiquidWalk.randomVar = true;
                final float yaw = Wrapper.mc.thePlayer.rotationYaw;
                final float pitch = Wrapper.mc.thePlayer.rotationPitch;
                final boolean newMode = LiquidWalk.mode == 3;
                if (event.getPacket() instanceof C03PacketPlayer) {
                    final C03PacketPlayer p = (C03PacketPlayer)event.getPacket();
                    if (Wrapper.mc.thePlayer.ticksExisted % 2 == 0) {
                        if (!Wrapper.mc.thePlayer.isCollidedHorizontally && Wrapper.mc.thePlayer.posY % 1.0 == 0.0) {
                            event.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(posX, Wrapper.mc.thePlayer.posY - (Boolean.valueOf(newMode) ? 0.215 : 0.01), posZ, yaw, pitch, true));
                        }
                    }
                    else if (!Wrapper.mc.thePlayer.isCollidedHorizontally) {
                        event.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(posX, Wrapper.mc.thePlayer.posY - (Boolean.valueOf(newMode) ? 0.195 : -0.01), posZ, yaw, pitch, true));
                    }
                }
            }
            else {
                LiquidWalk.Delay = 0;
                if (Wrapper.getBlockUnderPlayer2(Wrapper.mc.thePlayer, -0.1).getMaterial().isLiquid() || Wrapper.mc.thePlayer.fallDistance >= 3.0f) {
                    LiquidWalk.randomVar = false;
                }
                if (Wrapper.getBlockUnderPlayer2(Wrapper.mc.thePlayer, -0.01).getMaterial().isLiquid()) {
                    if (Wrapper.mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
                        return;
                    }
                    Wrapper.mc.thePlayer.motionY = 0.075;
                }
            }
        }
    }
}
