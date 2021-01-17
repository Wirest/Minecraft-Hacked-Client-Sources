package me.rigamortis.faurax.module.modules.player;

import me.rigamortis.faurax.module.*;
import com.mojang.authlib.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.client.entity.*;

public class FreeCam extends Module
{
    private EntityOtherPlayerMP fakePlayer;
    private float posX;
    private float posY;
    private float posZ;
    public static boolean enabled;
    
    public FreeCam() {
        this.setName("FreeCam");
        this.setKey("NUMPAD5");
        this.setType(ModuleType.PLAYER);
        this.setColor(-15104089);
        this.setModInfo("");
        this.setVisible(false);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        this.posX = (float)FreeCam.mc.thePlayer.posX;
        this.posY = (float)FreeCam.mc.thePlayer.posY;
        this.posZ = (float)FreeCam.mc.thePlayer.posZ;
        (this.fakePlayer = new EntityOtherPlayerMP(FreeCam.mc.theWorld, new GameProfile(FreeCam.mc.session.getProfile().getId(), FreeCam.mc.thePlayer.getName()))).setPositionAndRotation(FreeCam.mc.thePlayer.posX, FreeCam.mc.thePlayer.posY, FreeCam.mc.thePlayer.posZ, FreeCam.mc.thePlayer.rotationYaw, FreeCam.mc.thePlayer.rotationPitch);
        this.fakePlayer.rotationYawHead = FreeCam.mc.thePlayer.rotationYawHead;
        this.fakePlayer.inventory = FreeCam.mc.thePlayer.inventory;
        this.fakePlayer.setSneaking(FreeCam.mc.thePlayer.isSneaking());
        FreeCam.mc.theWorld.addEntityToWorld(1337, this.fakePlayer);
        FreeCam.enabled = true;
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        try {
            if (this.fakePlayer != null) {
                FreeCam.mc.theWorld.removeEntity(this.fakePlayer);
                FreeCam.mc.thePlayer.setPosition(this.posX, this.posY, this.posZ);
                FreeCam.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(FreeCam.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }
        }
        catch (Exception ex) {}
        FreeCam.enabled = false;
        FreeCam.mc.thePlayer.capabilities.isFlying = false;
    }
    
    @EventTarget
    public void pushOutOfBlocks(final EventPushOutOfBlock e) {
        if (this.isToggled()) {
            e.setCancelled(true);
        }
    }
    
    @EventTarget
    public void blockBoundingbox(final EventBlockBoundingbox e) {
        if (this.isToggled()) {
            e.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onMove(final EventMove event) {
        if (this.isToggled() && event.motionX == 0.0) {
            final double motionZ = event.motionZ;
        }
    }
    
    @EventTarget(4)
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            FreeCam.mc.thePlayer.capabilities.isFlying = true;
            final EntityPlayerSP thePlayer = FreeCam.mc.thePlayer;
            thePlayer.renderArmPitch += 700.0f;
            final EntityPlayerSP thePlayer2 = FreeCam.mc.thePlayer;
            thePlayer2.renderArmYaw += 180.0f;
            FreeCam.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(FreeCam.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            if (FreeCam.mc.thePlayer.isSprinting()) {
                FreeCam.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(FreeCam.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }
            e.setCancelled(true);
        }
    }
}
