/*    */ package rip.jutting.polaris.module.render;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class Fullbright extends Module
/*    */ {
/*    */   private float oldBrightness;
/*    */   
/*    */   public Fullbright()
/*    */   {
/* 12 */     super("Fullbright", 0, rip.jutting.polaris.module.Category.RENDER);
/*    */   }
/*    */   
/*    */   public void onEnable()
/*    */   {
/* 17 */     super.onEnable();
/*    */     
/* 19 */     this.oldBrightness = mc.gameSettings.gammaSetting;
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event) {
/* 24 */     mc.gameSettings.gammaSetting = 10.0F;
/*    */   }
/*    */   
/*    */   public void onDisable()
/*    */   {
/* 29 */     super.onDisable();
/*    */     
/* 31 */     mc.gameSettings.gammaSetting = this.oldBrightness;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\render\Fullbright.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */