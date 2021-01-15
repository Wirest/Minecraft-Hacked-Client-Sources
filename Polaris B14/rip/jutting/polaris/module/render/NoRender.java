/*    */ package rip.jutting.polaris.module.render;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.entity.item.EntityItem;
/*    */ import rip.jutting.polaris.event.events.EventUpdate;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class NoRender extends Module
/*    */ {
/*    */   public NoRender()
/*    */   {
/* 13 */     super("NoRender", 0, rip.jutting.polaris.module.Category.RENDER);
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(EventUpdate event)
/*    */   {
/* 19 */     for (Object o : mc.theWorld.loadedEntityList) {
/* 20 */       if ((o instanceof EntityItem)) {
/* 21 */         EntityItem i = (EntityItem)o;
/* 22 */         mc.theWorld.removeEntity(i);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\render\NoRender.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */