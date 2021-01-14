package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import optfine.Config;
import optfine.Mipmaps;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TextureUtil {
   private static final Logger logger = LogManager.getLogger();
   private static final IntBuffer dataBuffer = GLAllocation.createDirectIntBuffer(4194304);
   public static final DynamicTexture missingTexture = new DynamicTexture(16, 16);
   public static final int[] missingTextureData;
   private static final int[] mipmapBuffer;
   private static final String __OBFID = "CL_00001067";

   public static int glGenTextures() {
      return GlStateManager.generateTexture();
   }

   public static void deleteTexture(int textureId) {
      GlStateManager.deleteTexture(textureId);
   }

   public static int uploadTextureImage(int p_110987_0_, BufferedImage p_110987_1_) {
      return uploadTextureImageAllocate(p_110987_0_, p_110987_1_, false, false);
   }

   public static void uploadTexture(int textureId, int[] p_110988_1_, int p_110988_2_, int p_110988_3_) {
      bindTexture(textureId);
      uploadTextureSub(0, p_110988_1_, p_110988_2_, p_110988_3_, 0, 0, false, false, false);
   }

   public static int[][] generateMipmapData(int p_147949_0_, int p_147949_1_, int[][] p_147949_2_) {
      int[][] aint = new int[p_147949_0_ + 1][];
      aint[0] = p_147949_2_[0];
      if (p_147949_0_ > 0) {
         boolean flag = false;

         int l1;
         for(l1 = 0; l1 < p_147949_2_.length; ++l1) {
            if (p_147949_2_[0][l1] >> 24 == 0) {
               flag = true;
               break;
            }
         }

         for(l1 = 1; l1 <= p_147949_0_; ++l1) {
            if (p_147949_2_[l1] != null) {
               aint[l1] = p_147949_2_[l1];
            } else {
               int[] aint1 = aint[l1 - 1];
               int[] aint2 = new int[aint1.length >> 2];
               int j = p_147949_1_ >> l1;
               int k = aint2.length / j;
               int l = j << 1;

               for(int i1 = 0; i1 < j; ++i1) {
                  for(int j1 = 0; j1 < k; ++j1) {
                     int k1 = 2 * (i1 + j1 * l);
                     aint2[i1 + j1 * j] = blendColors(aint1[k1 + 0], aint1[k1 + 1], aint1[k1 + 0 + l], aint1[k1 + 1 + l], flag);
                  }
               }

               aint[l1] = aint2;
            }
         }
      }

      return aint;
   }

   private static int blendColors(int p_147943_0_, int p_147943_1_, int p_147943_2_, int p_147943_3_, boolean p_147943_4_) {
      return Mipmaps.alphaBlend(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_);
   }

   private static int blendColorComponent(int p_147944_0_, int p_147944_1_, int p_147944_2_, int p_147944_3_, int p_147944_4_) {
      float f = (float)Math.pow((double)((float)(p_147944_0_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
      float f1 = (float)Math.pow((double)((float)(p_147944_1_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
      float f2 = (float)Math.pow((double)((float)(p_147944_2_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
      float f3 = (float)Math.pow((double)((float)(p_147944_3_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
      float f4 = (float)Math.pow((double)(f + f1 + f2 + f3) * 0.25D, 0.45454545454545453D);
      return (int)((double)f4 * 255.0D);
   }

   public static void uploadTextureMipmap(int[][] p_147955_0_, int p_147955_1_, int p_147955_2_, int p_147955_3_, int p_147955_4_, boolean p_147955_5_, boolean p_147955_6_) {
      for(int i = 0; i < p_147955_0_.length; ++i) {
         int[] aint = p_147955_0_[i];
         uploadTextureSub(i, aint, p_147955_1_ >> i, p_147955_2_ >> i, p_147955_3_ >> i, p_147955_4_ >> i, p_147955_5_, p_147955_6_, p_147955_0_.length > 1);
      }

   }

   private static void uploadTextureSub(int p_147947_0_, int[] p_147947_1_, int p_147947_2_, int p_147947_3_, int p_147947_4_, int p_147947_5_, boolean p_147947_6_, boolean p_147947_7_, boolean p_147947_8_) {
      int i = 4194304 / p_147947_2_;
      setTextureBlurMipmap(p_147947_6_, p_147947_8_);
      setTextureClamped(p_147947_7_);

      int j;
      for(int k = 0; k < p_147947_2_ * p_147947_3_; k += p_147947_2_ * j) {
         int l = k / p_147947_2_;
         j = Math.min(i, p_147947_3_ - l);
         int i1 = p_147947_2_ * j;
         copyToBufferPos(p_147947_1_, k, i1);
         GL11.glTexSubImage2D(3553, p_147947_0_, p_147947_4_, p_147947_5_ + l, p_147947_2_, j, 32993, 33639, dataBuffer);
      }

   }

   public static int uploadTextureImageAllocate(int p_110989_0_, BufferedImage p_110989_1_, boolean p_110989_2_, boolean p_110989_3_) {
      allocateTexture(p_110989_0_, p_110989_1_.getWidth(), p_110989_1_.getHeight());
      return uploadTextureImageSub(p_110989_0_, p_110989_1_, 0, 0, p_110989_2_, p_110989_3_);
   }

   public static void allocateTexture(int p_110991_0_, int p_110991_1_, int p_110991_2_) {
      allocateTextureImpl(p_110991_0_, 0, p_110991_1_, p_110991_2_);
   }

   public static void allocateTextureImpl(int p_180600_0_, int p_180600_1_, int p_180600_2_, int p_180600_3_) {
      deleteTexture(p_180600_0_);
      bindTexture(p_180600_0_);
      if (p_180600_1_ >= 0) {
         GL11.glTexParameteri(3553, 33085, p_180600_1_);
         GL11.glTexParameterf(3553, 33082, 0.0F);
         GL11.glTexParameterf(3553, 33083, (float)p_180600_1_);
         GL11.glTexParameterf(3553, 34049, 0.0F);
      }

      for(int i = 0; i <= p_180600_1_; ++i) {
         GL11.glTexImage2D(3553, i, 6408, p_180600_2_ >> i, p_180600_3_ >> i, 0, 32993, 33639, (IntBuffer)null);
      }

   }

   public static int uploadTextureImageSub(int textureId, BufferedImage p_110995_1_, int p_110995_2_, int p_110995_3_, boolean p_110995_4_, boolean p_110995_5_) {
      bindTexture(textureId);
      uploadTextureImageSubImpl(p_110995_1_, p_110995_2_, p_110995_3_, p_110995_4_, p_110995_5_);
      return textureId;
   }

   private static void uploadTextureImageSubImpl(BufferedImage p_110993_0_, int p_110993_1_, int p_110993_2_, boolean p_110993_3_, boolean p_110993_4_) {
      int i = p_110993_0_.getWidth();
      int j = p_110993_0_.getHeight();
      int k = 4194304 / i;
      int[] aint = new int[k * i];
      setTextureBlurred(p_110993_3_);
      setTextureClamped(p_110993_4_);

      for(int l = 0; l < i * j; l += i * k) {
         int i1 = l / i;
         int j1 = Math.min(k, j - i1);
         int k1 = i * j1;
         p_110993_0_.getRGB(0, i1, i, j1, aint, 0, i);
         copyToBuffer(aint, k1);
         GL11.glTexSubImage2D(3553, 0, p_110993_1_, p_110993_2_ + i1, i, j1, 32993, 33639, dataBuffer);
      }

   }

   private static void setTextureClamped(boolean p_110997_0_) {
      if (p_110997_0_) {
         GL11.glTexParameteri(3553, 10242, 33071);
         GL11.glTexParameteri(3553, 10243, 33071);
      } else {
         GL11.glTexParameteri(3553, 10242, 10497);
         GL11.glTexParameteri(3553, 10243, 10497);
      }

   }

   private static void setTextureBlurred(boolean p_147951_0_) {
      setTextureBlurMipmap(p_147951_0_, false);
   }

   private static void setTextureBlurMipmap(boolean p_147954_0_, boolean p_147954_1_) {
      if (p_147954_0_) {
         GL11.glTexParameteri(3553, 10241, p_147954_1_ ? 9987 : 9729);
         GL11.glTexParameteri(3553, 10240, 9729);
      } else {
         int i = Config.getMipmapType();
         GL11.glTexParameteri(3553, 10241, p_147954_1_ ? i : 9728);
         GL11.glTexParameteri(3553, 10240, 9728);
      }

   }

   private static void copyToBuffer(int[] p_110990_0_, int p_110990_1_) {
      copyToBufferPos(p_110990_0_, 0, p_110990_1_);
   }

   private static void copyToBufferPos(int[] p_110994_0_, int p_110994_1_, int p_110994_2_) {
      int[] aint = p_110994_0_;
      if (Minecraft.getMinecraft().gameSettings.anaglyph) {
         aint = updateAnaglyph(p_110994_0_);
      }

      dataBuffer.clear();
      dataBuffer.put(aint, p_110994_1_, p_110994_2_);
      dataBuffer.position(0).limit(p_110994_2_);
   }

   static void bindTexture(int p_94277_0_) {
      GlStateManager.bindTexture(p_94277_0_);
   }

   public static int[] readImageData(IResourceManager resourceManager, ResourceLocation imageLocation) throws IOException {
      BufferedImage bufferedimage = readBufferedImage(resourceManager.getResource(imageLocation).getInputStream());
      int i = bufferedimage.getWidth();
      int j = bufferedimage.getHeight();
      int[] aint = new int[i * j];
      bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
      return aint;
   }

   public static BufferedImage readBufferedImage(InputStream imageStream) throws IOException {
      BufferedImage bufferedimage;
      try {
         bufferedimage = ImageIO.read(imageStream);
      } finally {
         IOUtils.closeQuietly(imageStream);
      }

      return bufferedimage;
   }

   public static int[] updateAnaglyph(int[] p_110985_0_) {
      int[] aint = new int[p_110985_0_.length];

      for(int i = 0; i < p_110985_0_.length; ++i) {
         aint[i] = anaglyphColor(p_110985_0_[i]);
      }

      return aint;
   }

   public static int anaglyphColor(int p_177054_0_) {
      int i = p_177054_0_ >> 24 & 255;
      int j = p_177054_0_ >> 16 & 255;
      int k = p_177054_0_ >> 8 & 255;
      int l = p_177054_0_ & 255;
      int i1 = (j * 30 + k * 59 + l * 11) / 100;
      int j1 = (j * 30 + k * 70) / 100;
      int k1 = (j * 30 + l * 70) / 100;
      return i << 24 | i1 << 16 | j1 << 8 | k1;
   }

   public static void saveGlTexture(String p_saveGlTexture_0_, int p_saveGlTexture_1_, int p_saveGlTexture_2_, int p_saveGlTexture_3_, int p_saveGlTexture_4_) {
      bindTexture(p_saveGlTexture_1_);
      GL11.glPixelStorei(3333, 1);
      GL11.glPixelStorei(3317, 1);

      for(int i = 0; i <= p_saveGlTexture_2_; ++i) {
         File file1 = new File(p_saveGlTexture_0_ + "_" + i + ".png");
         int j = p_saveGlTexture_3_ >> i;
         int k = p_saveGlTexture_4_ >> i;
         int l = j * k;
         IntBuffer intbuffer = BufferUtils.createIntBuffer(l);
         int[] aint = new int[l];
         GL11.glGetTexImage(3553, i, 32993, 33639, intbuffer);
         intbuffer.get(aint);
         BufferedImage bufferedimage = new BufferedImage(j, k, 2);
         bufferedimage.setRGB(0, 0, j, k, aint, 0, j);

         try {
            ImageIO.write(bufferedimage, "png", file1);
            logger.debug("Exported png to: {}", new Object[]{file1.getAbsolutePath()});
         } catch (Exception var14) {
            logger.debug("Unable to write: ", var14);
         }
      }

   }

   public static void processPixelValues(int[] p_147953_0_, int p_147953_1_, int p_147953_2_) {
      int[] aint = new int[p_147953_1_];
      int i = p_147953_2_ / 2;

      for(int j = 0; j < i; ++j) {
         System.arraycopy(p_147953_0_, j * p_147953_1_, aint, 0, p_147953_1_);
         System.arraycopy(p_147953_0_, (p_147953_2_ - 1 - j) * p_147953_1_, p_147953_0_, j * p_147953_1_, p_147953_1_);
         System.arraycopy(aint, 0, p_147953_0_, (p_147953_2_ - 1 - j) * p_147953_1_, p_147953_1_);
      }

   }

   static {
      missingTextureData = missingTexture.getTextureData();
      int i = -16777216;
      int j = -524040;
      int[] aint = new int[]{-524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040};
      int[] aint1 = new int[]{-16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216};
      int k = aint.length;

      for(int l = 0; l < 16; ++l) {
         System.arraycopy(l < k ? aint : aint1, 0, missingTextureData, 16 * l, k);
         System.arraycopy(l < k ? aint1 : aint, 0, missingTextureData, 16 * l + k, k);
      }

      missingTexture.updateDynamicTexture();
      mipmapBuffer = new int[4];
   }
}
