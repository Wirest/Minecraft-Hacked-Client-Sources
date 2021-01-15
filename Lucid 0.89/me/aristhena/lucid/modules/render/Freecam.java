package me.aristhena.lucid.modules.render;

import me.aristhena.lucid.management.module.*;
import net.minecraft.client.entity.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import me.aristhena.lucid.eventapi.*;
import me.aristhena.lucid.eventapi.events.*;

@Mod
public class Freecam extends Module
{
    private EntityOtherPlayerMP playerCopy;
    private double startX;
    private double startY;
    private double startZ;
    private float startYaw;
    private float startPitch;
    
    @Override
    public void onEnable() {
        if (this.mc.thePlayer != null) {
            this.mc.thePlayer.noClip = true;
            this.startX = this.mc.thePlayer.posX;
            this.startY = this.mc.thePlayer.posY;
            this.startZ = this.mc.thePlayer.posZ;
            this.startYaw = this.mc.thePlayer.rotationYaw;
            this.startPitch = this.mc.thePlayer.rotationPitch;
            this.playerCopy = new EntityOtherPlayerMP((World)this.mc.theWorld, this.mc.thePlayer.getGameProfile());
            this.playerCopy.inventory = this.mc.thePlayer.inventory;
            this.playerCopy.inventoryContainer = this.mc.thePlayer.inventoryContainer;
            this.playerCopy.setPositionAndRotation(this.startX, this.startY, this.startZ, this.startYaw, this.startPitch);
            this.playerCopy.rotationYawHead = this.mc.thePlayer.rotationYawHead;
            this.mc.theWorld.addEntityToWorld(-1, (Entity)this.playerCopy);
        }
        super.onEnable();
    }
    
    @EventTarget
    private void onPreUpdate(final UpdateEvent event) {
        if (event.state == Event.State.PRE) {
            event.setCancelled(this.mc.thePlayer.capabilities.isFlying = true);
        }
    }
    
    @EventTarget
    private void onBoundingBox(final BoundingBoxEvent event) {
        event.boundingBox = null;
    }
    
    @Override
    public void onDisable() {
        this.mc.thePlayer.setPositionAndRotation(this.startX, this.startY, this.startZ, this.startYaw, this.startPitch);
        this.mc.thePlayer.noClip = false;
        this.mc.theWorld.removeEntityFromWorld(-1);
        this.mc.thePlayer.capabilities.isFlying = false;
        super.onDisable();
    }
}
