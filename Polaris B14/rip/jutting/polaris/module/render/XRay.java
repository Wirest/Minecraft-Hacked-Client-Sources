/*    */ package rip.jutting.polaris.module.render;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.RenderGlobal;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import rip.jutting.polaris.module.Category;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class XRay extends Module
/*    */ {
/* 13 */   public ArrayList<Block> xrayblocks = new ArrayList();
/*    */   public static boolean enabled;
/*    */   private float oldgamma;
/*    */   private Object shouldxrayblocks;
/*    */   
/*    */   public XRay() {
/* 19 */     super("XRay", 0, Category.RENDER);
/*    */   }
/*    */   
/*    */   public void onEnable()
/*    */   {
/* 24 */     super.onEnable();
/*    */     
/* 26 */     enabled = true;
/* 27 */     this.oldgamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
/* 28 */     Minecraft.getMinecraft().gameSettings.gammaSetting = 10.0F;
/* 29 */     Minecraft.getMinecraft().gameSettings.ambientOcclusion = 0;
/* 30 */     Minecraft.getMinecraft().renderGlobal.loadRenderers();
/*    */   }
/*    */   
/*    */   public void onDisable()
/*    */   {
/* 35 */     super.onDisable();
/*    */     
/* 37 */     enabled = false;
/* 38 */     Minecraft.getMinecraft().gameSettings.gammaSetting = this.oldgamma;
/* 39 */     Minecraft.getMinecraft().gameSettings.ambientOcclusion = 1;
/* 40 */     Minecraft.getMinecraft().renderGlobal.loadRenderers();
/*    */   }
/*    */   
/*    */   public boolean XrayFunction(int blockid)
/*    */   {
/* 45 */     blockid = 54;
/* 46 */     blockid = 16;
/* 47 */     if (((ArrayList)this.shouldxrayblocks).contains(Integer.valueOf(blockid))) {
/* 48 */       return true;
/*    */     }
/* 50 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\render\XRay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */