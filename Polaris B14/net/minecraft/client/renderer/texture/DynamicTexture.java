/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.resources.IResourceManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DynamicTexture
/*    */   extends AbstractTexture
/*    */ {
/*    */   private final int[] dynamicTextureData;
/*    */   private final int width;
/*    */   private final int height;
/*    */   
/*    */   public DynamicTexture(BufferedImage bufferedImage)
/*    */   {
/* 19 */     this(bufferedImage.getWidth(), bufferedImage.getHeight());
/* 20 */     bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), this.dynamicTextureData, 0, bufferedImage.getWidth());
/* 21 */     updateDynamicTexture();
/*    */   }
/*    */   
/*    */   public DynamicTexture(int textureWidth, int textureHeight)
/*    */   {
/* 26 */     this.width = textureWidth;
/* 27 */     this.height = textureHeight;
/* 28 */     this.dynamicTextureData = new int[textureWidth * textureHeight];
/* 29 */     TextureUtil.allocateTexture(getGlTextureId(), textureWidth, textureHeight);
/*    */   }
/*    */   
/*    */   public void loadTexture(IResourceManager resourceManager)
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void updateDynamicTexture()
/*    */   {
/* 38 */     TextureUtil.uploadTexture(getGlTextureId(), this.dynamicTextureData, this.width, this.height);
/*    */   }
/*    */   
/*    */   public int[] getTextureData()
/*    */   {
/* 43 */     return this.dynamicTextureData;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\texture\DynamicTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */