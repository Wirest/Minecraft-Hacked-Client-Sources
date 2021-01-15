/*    */ package rip.jutting.polaris.module.movement;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class Sprint extends Module
/*    */ {
/*    */   public Sprint()
/*    */   {
/* 12 */     super("Sprint", 0, rip.jutting.polaris.module.Category.MOVEMENT);
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 17 */     if ((!mc.thePlayer.isCollidedHorizontally) && (mc.thePlayer.moveForward > 0.0F)) {
/* 18 */       mc.thePlayer.setSprinting(true);
/*    */     }
/*    */   }
/*    */   
/*    */   public void onDisable() {
/* 23 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\movement\Sprint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */