/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.renderer.texture.TextureUtil;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.world.ColorizerGrass;
/*    */ 
/*    */ public class GrassColorReloadListener implements IResourceManagerReloadListener
/*    */ {
/* 10 */   private static final ResourceLocation LOC_GRASS_PNG = new ResourceLocation("textures/colormap/grass.png");
/*    */   
/*    */   public void onResourceManagerReload(IResourceManager resourceManager)
/*    */   {
/*    */     try
/*    */     {
/* 16 */       ColorizerGrass.setGrassBiomeColorizer(TextureUtil.readImageData(resourceManager, LOC_GRASS_PNG));
/*    */     }
/*    */     catch (IOException localIOException) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\resources\GrassColorReloadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */