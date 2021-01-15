// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Render;

import net.minecraft.client.entity.EntityPlayerSP;
import cf.euphoria.euphorical.Utils.BlockUtils;
import net.minecraft.client.Minecraft;
import cf.euphoria.euphorical.Events.EventUpdate;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C03PacketPlayer;
import cf.euphoria.euphorical.Events.EventPacketSend;
import com.darkmagician6.eventapi.EventTarget;
import cf.euphoria.euphorical.Events.EventBBSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Mod.Category;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import cf.euphoria.euphorical.Mod.Mod;

public class Freecam extends Mod
{
    private double oldX;
    private double oldY;
    private double oldZ;
    private float oldYaw;
    private float oldPitch;
    private EntityOtherPlayerMP player;
    
    public Freecam() {
        super("Freecam", Category.RENDER);
    }
    
    @Override
    public void onEnable() {
        this.mc.thePlayer.noClip = true;
        this.oldX = this.mc.thePlayer.posX;
        this.oldY = this.mc.thePlayer.posY;
        this.oldZ = this.mc.thePlayer.posZ;
        this.oldYaw = this.mc.thePlayer.rotationYaw;
        this.oldPitch = this.mc.thePlayer.rotationPitch;
        EventManager.register(this);
        (this.player = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile())).clonePlayer(this.mc.thePlayer, true);
        this.player.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
        this.player.rotationYawHead = this.mc.thePlayer.rotationYaw;
        this.player.rotationPitch = this.mc.thePlayer.rotationPitch;
        this.player.setSneaking(this.mc.thePlayer.isSneaking());
        this.mc.theWorld.addEntityToWorld(-1337, this.player);
    }
    
    @EventTarget
    public void onBBSet(final EventBBSet event) {
        event.boundingBox = null;
    }
    
    @Override
    public void onDisable() {
        this.mc.thePlayer.noClip = false;
        this.mc.thePlayer.capabilities.isFlying = false;
        EventManager.unregister(this);
        this.mc.thePlayer.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, this.oldYaw, this.oldPitch);
        this.mc.theWorld.removeEntity(this.player);
    }
    
    @EventTarget
    public void onPacketSend(final EventPacketSend event) {
        if (event.packet instanceof C03PacketPlayer || event.packet instanceof C0BPacketEntityAction || event.packet instanceof C0APacketAnimation || event.packet instanceof C02PacketUseEntity || event.packet instanceof C09PacketHeldItemChange || event.packet instanceof C07PacketPlayerDigging) {
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.mc.thePlayer.noClip = true;
        this.mc.thePlayer.motionX = 0.0;
        this.mc.thePlayer.motionY = 0.0;
        this.mc.thePlayer.motionZ = 0.0;
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        thePlayer.jumpMovementFactor *= 5.0f;
        if (Minecraft.gameSettings.keyBindSneak.pressed) {
            final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
            thePlayer2.motionY -= 0.5;
        }
        else if (Minecraft.gameSettings.keyBindJump.pressed) {
            final EntityPlayerSP thePlayer3 = this.mc.thePlayer;
            thePlayer3.motionY += 0.5;
        }
        final double mx2 = Math.cos(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
        final double mz2 = Math.sin(Math.toRadians(this.mc.thePlayer.rotationYaw + 90.0f));
        final double x = this.mc.thePlayer.movementInput.moveForward * 0.3 * mx2 + this.mc.thePlayer.movementInput.moveStrafe * 0.3 * mz2;
        final double z = this.mc.thePlayer.movementInput.moveForward * 0.3 * mz2 - this.mc.thePlayer.movementInput.moveStrafe * 0.3 * mx2;
        if (this.mc.thePlayer.isCollidedHorizontally && !this.mc.thePlayer.isOnLadder() && !BlockUtils.isInsideBlock()) {
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX + x * 10.0, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + z * 10.0);
        }
    }
}
