/*    */ package rip.jutting.polaris.module.player;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.potion.Potion;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class SpeedMine extends Module
/*    */ {
/*    */   public SpeedMine()
/*    */   {
/* 13 */     super("SpeedMine", 0, rip.jutting.polaris.module.Category.PLAYER);
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event)
/*    */   {
/* 19 */     mc.playerController.blockHitDelay = 0;
/* 20 */     if (mc.playerController.curBlockDamageMP > 0.8F) {
/* 21 */       mc.playerController.curBlockDamageMP = 1.0F;
/*    */     }
/* 23 */     boolean item = mc.thePlayer.getCurrentEquippedItem() == null;
/* 24 */     mc.thePlayer.addPotionEffect(new net.minecraft.potion.PotionEffect(Potion.digSpeed.getId(), 20, item ? 1 : 0));
/*    */   }
/*    */   
/*    */   public void onDisable() {
/* 28 */     Minecraft.getMinecraft().thePlayer.removePotionEffect(Potion.digSpeed.getId());
/* 29 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\player\SpeedMine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */