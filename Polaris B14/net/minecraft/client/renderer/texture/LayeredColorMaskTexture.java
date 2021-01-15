/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.List;
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.client.resources.IResource;
/*    */ import net.minecraft.client.resources.IResourceManager;
/*    */ import net.minecraft.item.EnumDyeColor;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.apache.logging.log4j.LogManager;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class LayeredColorMaskTexture
/*    */   extends AbstractTexture
/*    */ {
/* 20 */   private static final Logger LOG = ;
/*    */   
/*    */   private final ResourceLocation textureLocation;
/*    */   
/*    */   private final List<String> field_174949_h;
/*    */   private final List<EnumDyeColor> field_174950_i;
/*    */   
/*    */   public LayeredColorMaskTexture(ResourceLocation textureLocationIn, List<String> p_i46101_2_, List<EnumDyeColor> p_i46101_3_)
/*    */   {
/* 29 */     this.textureLocation = textureLocationIn;
/* 30 */     this.field_174949_h = p_i46101_2_;
/* 31 */     this.field_174950_i = p_i46101_3_;
/*    */   }
/*    */   
/*    */   public void loadTexture(IResourceManager resourceManager) throws IOException
/*    */   {
/* 36 */     deleteGlTexture();
/*    */     
/*    */ 
/*    */     try
/*    */     {
/* 41 */       BufferedImage bufferedimage1 = TextureUtil.readBufferedImage(resourceManager.getResource(this.textureLocation).getInputStream());
/* 42 */       int i = bufferedimage1.getType();
/*    */       
/* 44 */       if (i == 0)
/*    */       {
/* 46 */         i = 6;
/*    */       }
/*    */       
/* 49 */       BufferedImage bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), i);
/* 50 */       Graphics graphics = bufferedimage.getGraphics();
/* 51 */       graphics.drawImage(bufferedimage1, 0, 0, null);
/*    */       
/* 53 */       int j = 0;
/*    */       do {
/* 55 */         String s = (String)this.field_174949_h.get(j);
/* 56 */         MapColor mapcolor = ((EnumDyeColor)this.field_174950_i.get(j)).getMapColor();
/*    */         
/* 58 */         if (s != null)
/*    */         {
/* 60 */           InputStream inputstream = resourceManager.getResource(new ResourceLocation(s)).getInputStream();
/* 61 */           BufferedImage bufferedimage2 = TextureUtil.readBufferedImage(inputstream);
/*    */           
/* 63 */           if ((bufferedimage2.getWidth() == bufferedimage.getWidth()) && (bufferedimage2.getHeight() == bufferedimage.getHeight()) && (bufferedimage2.getType() == 6))
/*    */           {
/* 65 */             for (int k = 0; k < bufferedimage2.getHeight(); k++)
/*    */             {
/* 67 */               for (int l = 0; l < bufferedimage2.getWidth(); l++)
/*    */               {
/* 69 */                 int i1 = bufferedimage2.getRGB(l, k);
/*    */                 
/* 71 */                 if ((i1 & 0xFF000000) != 0)
/*    */                 {
/* 73 */                   int j1 = (i1 & 0xFF0000) << 8 & 0xFF000000;
/* 74 */                   int k1 = bufferedimage1.getRGB(l, k);
/* 75 */                   int l1 = MathHelper.func_180188_d(k1, mapcolor.colorValue) & 0xFFFFFF;
/* 76 */                   bufferedimage2.setRGB(l, k, j1 | l1);
/*    */                 }
/*    */               }
/*    */             }
/*    */             
/* 81 */             bufferedimage.getGraphics().drawImage(bufferedimage2, 0, 0, null);
/*    */           }
/*    */         }
/* 53 */         j++; if ((j >= 17) || (j >= this.field_174949_h.size())) break; } while (j < this.field_174950_i.size());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     }
/*    */     catch (IOException ioexception)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 88 */       LOG.error("Couldn't load layered image", ioexception); return;
/*    */     }
/*    */     
/*    */     BufferedImage bufferedimage;
/* 92 */     TextureUtil.uploadTextureImage(getGlTextureId(), bufferedimage);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\texture\LayeredColorMaskTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */