/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import net.minecraft.util.EnumWorldBlockLayer;
/*    */ 
/*    */ public class RegionRenderCacheBuilder
/*    */ {
/*  7 */   private final WorldRenderer[] worldRenderers = new WorldRenderer[EnumWorldBlockLayer.values().length];
/*    */   
/*    */   public RegionRenderCacheBuilder()
/*    */   {
/* 11 */     this.worldRenderers[EnumWorldBlockLayer.SOLID.ordinal()] = new WorldRenderer(2097152);
/* 12 */     this.worldRenderers[EnumWorldBlockLayer.CUTOUT.ordinal()] = new WorldRenderer(131072);
/* 13 */     this.worldRenderers[EnumWorldBlockLayer.CUTOUT_MIPPED.ordinal()] = new WorldRenderer(131072);
/* 14 */     this.worldRenderers[EnumWorldBlockLayer.TRANSLUCENT.ordinal()] = new WorldRenderer(262144);
/*    */   }
/*    */   
/*    */   public WorldRenderer getWorldRendererByLayer(EnumWorldBlockLayer layer)
/*    */   {
/* 19 */     return this.worldRenderers[layer.ordinal()];
/*    */   }
/*    */   
/*    */   public WorldRenderer getWorldRendererByLayerId(int id)
/*    */   {
/* 24 */     return this.worldRenderers[id];
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\RegionRenderCacheBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */