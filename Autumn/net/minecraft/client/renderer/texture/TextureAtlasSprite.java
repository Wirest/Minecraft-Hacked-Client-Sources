package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import optfine.Config;
import optfine.TextureUtils;

public class TextureAtlasSprite {
   private final String iconName;
   protected List framesTextureData = Lists.newArrayList();
   protected int[][] interpolatedFrameData;
   private AnimationMetadataSection animationMetadata;
   protected boolean rotated;
   protected int originX;
   protected int originY;
   protected int width;
   protected int height;
   private float minU;
   private float maxU;
   private float minV;
   private float maxV;
   protected int frameCounter;
   protected int tickCounter;
   private static String locationNameClock = "builtin/clock";
   private static String locationNameCompass = "builtin/compass";
   private static final String __OBFID = "CL_00001062";
   private int indexInMap = -1;
   public float baseU;
   public float baseV;
   public int sheetWidth;
   public int sheetHeight;
   public int glSpriteTextureId = -1;
   public TextureAtlasSprite spriteSingle = null;
   public boolean isSpriteSingle = false;
   public int mipmapLevels = 0;

   private TextureAtlasSprite(TextureAtlasSprite p_i9_1_) {
      this.iconName = p_i9_1_.iconName;
      this.isSpriteSingle = true;
   }

   protected TextureAtlasSprite(String spriteName) {
      this.iconName = spriteName;
      if (Config.isMultiTexture()) {
         this.spriteSingle = new TextureAtlasSprite(this);
      }

   }

   protected static TextureAtlasSprite makeAtlasSprite(ResourceLocation spriteResourceLocation) {
      String s = spriteResourceLocation.toString();
      return (TextureAtlasSprite)(locationNameClock.equals(s) ? new TextureClock(s) : (locationNameCompass.equals(s) ? new TextureCompass(s) : new TextureAtlasSprite(s)));
   }

   public static void setLocationNameClock(String clockName) {
      locationNameClock = clockName;
   }

   public static void setLocationNameCompass(String compassName) {
      locationNameCompass = compassName;
   }

   public void initSprite(int inX, int inY, int originInX, int originInY, boolean rotatedIn) {
      this.originX = originInX;
      this.originY = originInY;
      this.rotated = rotatedIn;
      float f = (float)(0.009999999776482582D / (double)inX);
      float f1 = (float)(0.009999999776482582D / (double)inY);
      this.minU = (float)originInX / (float)((double)inX) + f;
      this.maxU = (float)(originInX + this.width) / (float)((double)inX) - f;
      this.minV = (float)originInY / (float)inY + f1;
      this.maxV = (float)(originInY + this.height) / (float)inY - f1;
      this.baseU = Math.min(this.minU, this.maxU);
      this.baseV = Math.min(this.minV, this.maxV);
      if (this.spriteSingle != null) {
         this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
      }

   }

   public void copyFrom(TextureAtlasSprite atlasSpirit) {
      this.originX = atlasSpirit.originX;
      this.originY = atlasSpirit.originY;
      this.width = atlasSpirit.width;
      this.height = atlasSpirit.height;
      this.rotated = atlasSpirit.rotated;
      this.minU = atlasSpirit.minU;
      this.maxU = atlasSpirit.maxU;
      this.minV = atlasSpirit.minV;
      this.maxV = atlasSpirit.maxV;
      if (this.spriteSingle != null) {
         this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
      }

   }

   public int getOriginX() {
      return this.originX;
   }

   public int getOriginY() {
      return this.originY;
   }

   public int getIconWidth() {
      return this.width;
   }

   public int getIconHeight() {
      return this.height;
   }

   public float getMinU() {
      return this.minU;
   }

   public float getMaxU() {
      return this.maxU;
   }

   public float getInterpolatedU(double u) {
      float f = this.maxU - this.minU;
      return this.minU + f * (float)u / 16.0F;
   }

   public float getMinV() {
      return this.minV;
   }

   public float getMaxV() {
      return this.maxV;
   }

   public float getInterpolatedV(double v) {
      float f = this.maxV - this.minV;
      return this.minV + f * ((float)v / 16.0F);
   }

   public String getIconName() {
      return this.iconName;
   }

   public void updateAnimation() {
      ++this.tickCounter;
      if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
         int i = this.animationMetadata.getFrameIndex(this.frameCounter);
         int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
         this.frameCounter = (this.frameCounter + 1) % j;
         this.tickCounter = 0;
         int k = this.animationMetadata.getFrameIndex(this.frameCounter);
         boolean flag = false;
         boolean flag1 = this.isSpriteSingle;
         if (i != k && k >= 0 && k < this.framesTextureData.size()) {
            TextureUtil.uploadTextureMipmap((int[][])((int[][])((int[][])this.framesTextureData.get(k))), this.width, this.height, this.originX, this.originY, flag, flag1);
         }
      } else if (this.animationMetadata.isInterpolate()) {
         this.updateAnimationInterpolated();
      }

   }

   private void updateAnimationInterpolated() {
      double d0 = 1.0D - (double)this.tickCounter / (double)this.animationMetadata.getFrameTimeSingle(this.frameCounter);
      int i = this.animationMetadata.getFrameIndex(this.frameCounter);
      int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
      int k = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % j);
      if (i != k && k >= 0 && k < this.framesTextureData.size()) {
         int[][] aint = (int[][])((int[][])((int[][])this.framesTextureData.get(i)));
         int[][] aint1 = (int[][])((int[][])((int[][])this.framesTextureData.get(k)));
         if (this.interpolatedFrameData == null || this.interpolatedFrameData.length != aint.length) {
            this.interpolatedFrameData = new int[aint.length][];
         }

         for(int l = 0; l < aint.length; ++l) {
            if (this.interpolatedFrameData[l] == null) {
               this.interpolatedFrameData[l] = new int[aint[l].length];
            }

            if (l < aint1.length && aint1[l].length == aint[l].length) {
               for(int i1 = 0; i1 < aint[l].length; ++i1) {
                  int j1 = aint[l][i1];
                  int k1 = aint1[l][i1];
                  int l1 = (int)((double)((j1 & 16711680) >> 16) * d0 + (double)((k1 & 16711680) >> 16) * (1.0D - d0));
                  int i2 = (int)((double)((j1 & '\uff00') >> 8) * d0 + (double)((k1 & '\uff00') >> 8) * (1.0D - d0));
                  int j2 = (int)((double)(j1 & 255) * d0 + (double)(k1 & 255) * (1.0D - d0));
                  this.interpolatedFrameData[l][i1] = j1 & -16777216 | l1 << 16 | i2 << 8 | j2;
               }
            }
         }

         TextureUtil.uploadTextureMipmap(this.interpolatedFrameData, this.width, this.height, this.originX, this.originY, false, false);
      }

   }

   public int[][] getFrameTextureData(int index) {
      return (int[][])((int[][])((int[][])this.framesTextureData.get(index)));
   }

   public int getFrameCount() {
      return this.framesTextureData.size();
   }

   public void setIconWidth(int newWidth) {
      this.width = newWidth;
      if (this.spriteSingle != null) {
         this.spriteSingle.setIconWidth(this.width);
      }

   }

   public void setIconHeight(int newHeight) {
      this.height = newHeight;
      if (this.spriteSingle != null) {
         this.spriteSingle.setIconHeight(this.height);
      }

   }

   public void loadSprite(BufferedImage[] images, AnimationMetadataSection meta) throws IOException {
      this.resetSprite();
      int i = images[0].getWidth();
      int j = images[0].getHeight();
      this.width = i;
      this.height = j;
      int[][] aint = new int[images.length][];

      int l1;
      for(l1 = 0; l1 < images.length; ++l1) {
         BufferedImage bufferedimage = images[l1];
         if (bufferedimage != null) {
            if (l1 > 0 && (bufferedimage.getWidth() != i >> l1 || bufferedimage.getHeight() != j >> l1)) {
               throw new RuntimeException(String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d", l1, bufferedimage.getWidth(), bufferedimage.getHeight(), i >> l1, j >> l1));
            }

            aint[l1] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[l1], 0, bufferedimage.getWidth());
         }
      }

      int j2;
      if (meta == null) {
         if (j != i) {
            throw new RuntimeException("broken aspect ratio and not an animation");
         }

         this.framesTextureData.add(aint);
      } else {
         l1 = j / i;
         int k1 = i;
         j2 = i;
         this.height = this.width;
         int i1;
         if (meta.getFrameCount() > 0) {
            Iterator iterator = meta.getFrameIndexSet().iterator();

            while(iterator.hasNext()) {
               i1 = (Integer)iterator.next();
               if (i1 >= l1) {
                  throw new RuntimeException("invalid frameindex " + i1);
               }

               this.allocateFrameTextureData(i1);
               this.framesTextureData.set(i1, getFrameTextureData(aint, k1, j2, i1));
            }

            this.animationMetadata = meta;
         } else {
            ArrayList arraylist = Lists.newArrayList();

            for(i1 = 0; i1 < l1; ++i1) {
               this.framesTextureData.add(getFrameTextureData(aint, k1, j2, i1));
               arraylist.add(new AnimationFrame(i1, -1));
            }

            this.animationMetadata = new AnimationMetadataSection(arraylist, this.width, this.height, meta.getFrameTime(), meta.isInterpolate());
         }
      }

      for(l1 = 0; l1 < this.framesTextureData.size(); ++l1) {
         int[][] aint1 = (int[][])((int[][])((int[][])this.framesTextureData.get(l1)));
         if (aint1 != null && !this.iconName.startsWith("minecraft:blocks/leaves_")) {
            for(j2 = 0; j2 < aint1.length; ++j2) {
               int[] aint2 = aint1[j2];
               this.fixTransparentColor(aint2);
            }
         }
      }

      if (this.spriteSingle != null) {
         this.spriteSingle.loadSprite(images, meta);
      }

   }

   public void generateMipmaps(int level) {
      ArrayList arraylist = Lists.newArrayList();

      for(int i = 0; i < this.framesTextureData.size(); ++i) {
         final int[][] aint = (int[][])((int[][])((int[][])this.framesTextureData.get(i)));
         if (aint != null) {
            try {
               arraylist.add(TextureUtil.generateMipmapData(level, this.width, aint));
            } catch (Throwable var8) {
               CrashReport crashreport = CrashReport.makeCrashReport(var8, "Generating mipmaps for frame");
               CrashReportCategory crashreportcategory = crashreport.makeCategory("Frame being iterated");
               crashreportcategory.addCrashSection("Frame index", i);
               crashreportcategory.addCrashSectionCallable("Frame sizes", new Callable() {
                  private static final String __OBFID = "CL_00001063";

                  public String call() throws Exception {
                     StringBuilder stringbuilder = new StringBuilder();
                     int[][] var2 = aint;
                     int var3 = var2.length;

                     for(int var4 = 0; var4 < var3; ++var4) {
                        int[] aint1 = var2[var4];
                        if (stringbuilder.length() > 0) {
                           stringbuilder.append(", ");
                        }

                        stringbuilder.append(aint1 == null ? "null" : aint1.length);
                     }

                     return stringbuilder.toString();
                  }
               });
               throw new ReportedException(crashreport);
            }
         }
      }

      this.setFramesTextureData(arraylist);
      if (this.spriteSingle != null) {
         this.spriteSingle.generateMipmaps(level);
      }

   }

   private void allocateFrameTextureData(int index) {
      if (this.framesTextureData.size() <= index) {
         for(int i = this.framesTextureData.size(); i <= index; ++i) {
            this.framesTextureData.add((Object)null);
         }
      }

      if (this.spriteSingle != null) {
         this.spriteSingle.allocateFrameTextureData(index);
      }

   }

   private static int[][] getFrameTextureData(int[][] data, int rows, int columns, int p_147962_3_) {
      int[][] aint = new int[data.length][];

      for(int i = 0; i < data.length; ++i) {
         int[] aint1 = data[i];
         if (aint1 != null) {
            aint[i] = new int[(rows >> i) * (columns >> i)];
            System.arraycopy(aint1, p_147962_3_ * aint[i].length, aint[i], 0, aint[i].length);
         }
      }

      return aint;
   }

   public void clearFramesTextureData() {
      this.framesTextureData.clear();
      if (this.spriteSingle != null) {
         this.spriteSingle.clearFramesTextureData();
      }

   }

   public boolean hasAnimationMetadata() {
      return this.animationMetadata != null;
   }

   public void setFramesTextureData(List newFramesTextureData) {
      this.framesTextureData = newFramesTextureData;
      if (this.spriteSingle != null) {
         this.spriteSingle.setFramesTextureData(newFramesTextureData);
      }

   }

   private void resetSprite() {
      this.animationMetadata = null;
      this.setFramesTextureData(Lists.newArrayList());
      this.frameCounter = 0;
      this.tickCounter = 0;
      if (this.spriteSingle != null) {
         this.spriteSingle.resetSprite();
      }

   }

   public String toString() {
      return "TextureAtlasSprite{name='" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
   }

   public boolean hasCustomLoader(IResourceManager p_hasCustomLoader_1_, ResourceLocation p_hasCustomLoader_2_) {
      return false;
   }

   public boolean load(IResourceManager p_load_1_, ResourceLocation p_load_2_) {
      return true;
   }

   public int getIndexInMap() {
      return this.indexInMap;
   }

   public void setIndexInMap(int p_setIndexInMap_1_) {
      this.indexInMap = p_setIndexInMap_1_;
   }

   private void fixTransparentColor(int[] p_fixTransparentColor_1_) {
      if (p_fixTransparentColor_1_ != null) {
         long i = 0L;
         long j = 0L;
         long k = 0L;
         long l = 0L;

         int l2;
         int i3;
         int j3;
         int k3;
         int l3;
         int i4;
         for(l2 = 0; l2 < p_fixTransparentColor_1_.length; ++l2) {
            i3 = p_fixTransparentColor_1_[l2];
            j3 = i3 >> 24 & 255;
            if (j3 >= 16) {
               k3 = i3 >> 16 & 255;
               l3 = i3 >> 8 & 255;
               i4 = i3 & 255;
               i += (long)k3;
               j += (long)l3;
               k += (long)i4;
               ++l;
            }
         }

         if (l > 0L) {
            l2 = (int)(i / l);
            i3 = (int)(j / l);
            j3 = (int)(k / l);
            k3 = l2 << 16 | i3 << 8 | j3;

            for(l3 = 0; l3 < p_fixTransparentColor_1_.length; ++l3) {
               i4 = p_fixTransparentColor_1_[l3];
               int k2 = i4 >> 24 & 255;
               if (k2 <= 16) {
                  p_fixTransparentColor_1_[l3] = k3;
               }
            }
         }
      }

   }

   public double getSpriteU16(float p_getSpriteU16_1_) {
      float f = this.maxU - this.minU;
      return (double)((p_getSpriteU16_1_ - this.minU) / f * 16.0F);
   }

   public double getSpriteV16(float p_getSpriteV16_1_) {
      float f = this.maxV - this.minV;
      return (double)((p_getSpriteV16_1_ - this.minV) / f * 16.0F);
   }

   public void bindSpriteTexture() {
      if (this.glSpriteTextureId < 0) {
         this.glSpriteTextureId = TextureUtil.glGenTextures();
         TextureUtil.allocateTextureImpl(this.glSpriteTextureId, this.mipmapLevels, this.width, this.height);
         TextureUtils.applyAnisotropicLevel();
      }

      TextureUtils.bindTexture(this.glSpriteTextureId);
   }

   public void deleteSpriteTexture() {
      if (this.glSpriteTextureId >= 0) {
         TextureUtil.deleteTexture(this.glSpriteTextureId);
         this.glSpriteTextureId = -1;
      }

   }

   public float toSingleU(float p_toSingleU_1_) {
      p_toSingleU_1_ -= this.baseU;
      float f = (float)this.sheetWidth / (float)this.width;
      p_toSingleU_1_ *= f;
      return p_toSingleU_1_;
   }

   public float toSingleV(float p_toSingleV_1_) {
      p_toSingleV_1_ -= this.baseV;
      float f = (float)this.sheetHeight / (float)this.height;
      p_toSingleV_1_ *= f;
      return p_toSingleV_1_;
   }
}
