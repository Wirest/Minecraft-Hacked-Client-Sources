/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import net.minecraft.client.resources.IResource;
/*    */ import net.minecraft.client.resources.IResourceManager;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class LayeredTexture extends AbstractTexture
/*    */ {
/* 16 */   private static final Logger logger = ;
/*    */   public final java.util.List<String> layeredTextureNames;
/*    */   
/*    */   public LayeredTexture(String... textureNames)
/*    */   {
/* 21 */     this.layeredTextureNames = Lists.newArrayList(textureNames);
/*    */   }
/*    */   
/*    */   public void loadTexture(IResourceManager resourceManager) throws IOException
/*    */   {
/* 26 */     deleteGlTexture();
/* 27 */     BufferedImage bufferedimage = null;
/*    */     
/*    */     try
/*    */     {
/* 31 */       for (String s : this.layeredTextureNames)
/*    */       {
/* 33 */         if (s != null)
/*    */         {
/* 35 */           InputStream inputstream = resourceManager.getResource(new ResourceLocation(s)).getInputStream();
/* 36 */           BufferedImage bufferedimage1 = TextureUtil.readBufferedImage(inputstream);
/*    */           
/* 38 */           if (bufferedimage == null)
/*    */           {
/* 40 */             bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), 2);
/*    */           }
/*    */           
/* 43 */           bufferedimage.getGraphics().drawImage(bufferedimage1, 0, 0, null);
/*    */         }
/*    */       }
/*    */     }
/*    */     catch (IOException ioexception)
/*    */     {
/* 49 */       logger.error("Couldn't load layered image", ioexception);
/* 50 */       return;
/*    */     }
/*    */     
/* 53 */     TextureUtil.uploadTextureImage(getGlTextureId(), bufferedimage);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\texture\LayeredTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */