/*    */ package rip.jutting.polaris.module.player;
/*    */ 
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.utils.AutoUtils;
/*    */ 
/*    */ public class AntiAim extends Module
/*    */ {
/*    */   public AntiAim()
/*    */   {
/* 11 */     super("AntiAim", 0, rip.jutting.polaris.module.Category.PLAYER);
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 16 */     event.setYaw(AutoUtils.setRandom(1.0F, 360.0F));
/* 17 */     event.setPitch(180.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\player\AntiAim.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */