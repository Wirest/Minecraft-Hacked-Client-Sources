/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.renderer.texture.TextureUtil;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.world.ColorizerFoliage;
/*    */ 
/*    */ public class FoliageColorReloadListener implements IResourceManagerReloadListener
/*    */ {
/* 10 */   private static final ResourceLocation LOC_FOLIAGE_PNG = new ResourceLocation("textures/colormap/foliage.png");
/*    */   
/*    */   public void onResourceManagerReload(IResourceManager resourceManager)
/*    */   {
/*    */     try
/*    */     {
/* 16 */       ColorizerFoliage.setFoliageBiomeColorizer(TextureUtil.readImageData(resourceManager, LOC_FOLIAGE_PNG));
/*    */     }
/*    */     catch (IOException localIOException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\FoliageColorReloadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */