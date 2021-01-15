/*    */ package rip.jutting.polaris.module.render;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityOtherPlayerMP;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import rip.jutting.polaris.event.EventTarget;
/*    */ import rip.jutting.polaris.event.events.EventBlockCull;
/*    */ import rip.jutting.polaris.event.events.EventBoundingBox;
/*    */ import rip.jutting.polaris.event.events.EventInsideBlockRender;
/*    */ import rip.jutting.polaris.event.events.EventPreMotionUpdate;
/*    */ import rip.jutting.polaris.event.events.EventPushOutOfBlock;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class Freecam extends Module
/*    */ {
/*    */   private double speed;
/*    */   private EntityOtherPlayerMP freecamEntity;
/*    */   
/*    */   public Freecam()
/*    */   {
/* 23 */     super("Freecam", 0, rip.jutting.polaris.module.Category.RENDER);
/* 24 */     this.speed = 1.0D;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onEnable()
/*    */   {
/* 32 */     if (mc.thePlayer == null) {
/* 33 */       return;
/*    */     }
/* 35 */     this.freecamEntity = new EntityOtherPlayerMP(mc.theWorld, new com.mojang.authlib.GameProfile(new java.util.UUID(69L, 96L), Minecraft.getMinecraft().thePlayer.getName()));
/* 36 */     this.freecamEntity.inventory = mc.thePlayer.inventory;
/* 37 */     this.freecamEntity.inventoryContainer = mc.thePlayer.inventoryContainer;
/* 38 */     this.freecamEntity.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
/* 39 */     this.freecamEntity.rotationYawHead = mc.thePlayer.rotationYawHead;
/* 40 */     mc.theWorld.addEntityToWorld(this.freecamEntity.getEntityId(), this.freecamEntity);
/* 41 */     mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.3D, mc.thePlayer.posZ);
/* 42 */     super.onEnable();
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 47 */     if (event.getState() == rip.jutting.polaris.event.Event.State.PRE) {
/* 48 */       event.setCancelled(true);
/*    */     }
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onMove(EventPreMotionUpdate event) {
/* 54 */     if (mc.thePlayer.movementInput.jump) {
/* 55 */       event.setY(mc.thePlayer.motionY = this.speed);
/*    */     }
/* 57 */     else if (mc.thePlayer.movementInput.sneak) {
/* 58 */       event.setY(mc.thePlayer.motionY = -this.speed);
/*    */     }
/*    */     else {
/* 61 */       event.setY(mc.thePlayer.motionY = 0.0D);
/*    */     }
/* 63 */     if (mc.thePlayer.isMoving()) {
/* 64 */       mc.thePlayer.setSpeed(this.speed);
/*    */     } else {
/* 66 */       mc.thePlayer.setSpeed(0.0F);
/*    */     }
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onBoundingBox(EventBoundingBox event) {
/* 72 */     event.setBoundingBox(mc.thePlayer.boundingBox);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onPushOut(EventPushOutOfBlock event) {
/* 77 */     event.setCancelled(true);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onRender(EventInsideBlockRender event) {
/* 82 */     event.setCancelled(true);
/*    */   }
/*    */   
/*    */   @EventTarget
/*    */   public void onCull(EventBlockCull event) {
/* 87 */     event.setCancelled(true);
/*    */   }
/*    */   
/*    */   public void onDisable()
/*    */   {
/* 92 */     mc.thePlayer.setPositionAndRotation(this.freecamEntity.posX, this.freecamEntity.posY, this.freecamEntity.posZ, this.freecamEntity.rotationYaw, this.freecamEntity.rotationPitch);
/* 93 */     mc.theWorld.removeEntityFromWorld(this.freecamEntity.getEntityId());
/* 94 */     mc.renderGlobal.loadRenderers();
/* 95 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\render\Freecam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */