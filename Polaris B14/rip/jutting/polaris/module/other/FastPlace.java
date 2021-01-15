/*    */ package rip.jutting.polaris.module.other;
/*    */ 
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class FastPlace extends Module
/*    */ {
/*    */   public FastPlace()
/*    */   {
/* 10 */     super("FastPlace", 0, rip.jutting.polaris.module.Category.OTHER);
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event) {
/* 15 */     mc.rightClickDelayTimer = Math.min(mc.rightClickDelayTimer, 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\FastPlace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */