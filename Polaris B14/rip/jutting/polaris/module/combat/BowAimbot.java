/*    */ package rip.jutting.polaris.module.combat;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class BowAimbot extends Module
/*    */ {
/*    */   private Entity bowtarget;
/*    */   private float velocity;
/*    */   
/*    */   public BowAimbot()
/*    */   {
/* 18 */     super("BowAimbot", 0, rip.jutting.polaris.module.Category.COMBAT);
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 23 */     this.bowtarget = null;
/* 24 */     if ((mc.thePlayer.inventory.getCurrentItem() != null) && 
/* 25 */       ((mc.thePlayer.inventory.getCurrentItem().getItem() instanceof net.minecraft.item.ItemBow)) && (mc.gameSettings.keyBindUseItem.pressed))
/*    */     {
/* 27 */       this.bowtarget = rip.jutting.polaris.utils.EntityUtils.getClosestEntity(true, true);
/* 28 */       aimAtbowbowbowtarget();
/*    */     }
/*    */   }
/*    */   
/*    */   private void aimAtbowbowbowtarget()
/*    */   {
/* 34 */     if (this.bowtarget == null) {
/* 35 */       return;
/*    */     }
/* 37 */     int bowCharge = mc.thePlayer.getItemInUseDuration();
/* 38 */     this.velocity = (bowCharge / 20);
/* 39 */     this.velocity = ((this.velocity * this.velocity + this.velocity * 2.0F) / 3.0F);
/*    */     
/* 41 */     this.velocity = 1.0F;
/* 42 */     if (this.velocity < 0.1D)
/*    */     {
/* 44 */       if ((this.bowtarget instanceof EntityLivingBase)) {
/* 45 */         rip.jutting.polaris.utils.EntityUtils.faceEntityClient((EntityLivingBase)this.bowtarget);
/*    */       }
/* 47 */       return;
/*    */     }
/* 49 */     if (this.velocity > 1.0F) {
/* 50 */       this.velocity = 1.0F;
/*    */     }
/* 52 */     double posX = 
/* 53 */       this.bowtarget.posX + (this.bowtarget.posX - this.bowtarget.prevPosX) * 5.0D - 
/* 54 */       mc.thePlayer.posX;
/* 55 */     double posY = 
/* 56 */       this.bowtarget.posY + (this.bowtarget.posY - this.bowtarget.prevPosY) * 5.0D + 
/* 57 */       this.bowtarget.getEyeHeight() - 0.15D - mc.thePlayer.posY - 
/* 58 */       mc.thePlayer.getEyeHeight();
/* 59 */     double posZ = 
/* 60 */       this.bowtarget.posZ + (this.bowtarget.posZ - this.bowtarget.prevPosZ) * 5.0D - 
/* 61 */       mc.thePlayer.posZ;
/* 62 */     float yaw = (float)(Math.atan2(posZ, posX) * 180.0D / 3.141592653589793D) - 90.0F;
/* 63 */     double y2 = Math.sqrt(posX * posX + posZ * posZ);
/* 64 */     float g = 0.006F;
/* 65 */     float tmp = 
/* 66 */       (float)(this.velocity * this.velocity * this.velocity * this.velocity - g * (
/* 67 */       g * (y2 * y2) + 2.0D * posY * (this.velocity * this.velocity)));
/* 68 */     float pitch = 
/* 69 */       (float)-Math.toDegrees(Math.atan((this.velocity * this.velocity - 
/* 70 */       Math.sqrt(tmp)) / (g * y2)));
/* 71 */     mc.thePlayer.rotationPitch = pitch;
/* 72 */     mc.thePlayer.rotationYaw = yaw;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\combat\BowAimbot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */