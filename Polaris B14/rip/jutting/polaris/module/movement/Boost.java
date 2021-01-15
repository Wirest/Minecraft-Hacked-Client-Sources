/*    */ package rip.jutting.polaris.module.movement;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import rip.jutting.polaris.event.EventTarget;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Category;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.utils.Timer;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Boost
/*    */   extends Module
/*    */ {
/*    */   public Boost()
/*    */   {
/* 18 */     super("Boost", 0, Category.MOVEMENT);
/*    */   }
/*    */   
/*    */ 
/* 22 */   public final Timer timer = new Timer();
/*    */   
/*    */   @EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 26 */     setDisplayName("Boost §7- Hypixel");
/* 27 */     mc.timer.timerSpeed = 3.0F;
/* 28 */     if (mc.thePlayer.ticksExisted % 15 == 0) {
/* 29 */       toggle();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void onEnable()
/*    */   {
/* 36 */     super.onEnable();
/*    */   }
/*    */   
/*    */   public void disableModule() {
/* 40 */     onDisable();
/*    */   }
/*    */   
/*    */   public void onDisable()
/*    */   {
/* 45 */     mc.timer.timerSpeed = 1.0F;
/* 46 */     super.onDisable();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\movement\Boost.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */