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
import optifine.Config;
import optifine.TextureUtils;
import shadersmod.client.ShadersTex;

public class TextureAtlasSprite
{
    private final String iconName;
    protected List framesTextureData = Lists.newArrayList();
    protected int[][] field_176605_b;
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
    private static String field_176607_p = "builtin/clock";
    private static String field_176606_q = "builtin/compass";
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

    private TextureAtlasSprite(TextureAtlasSprite parent)
    {
        this.iconName = parent.iconName;
        this.isSpriteSingle = true;
    }

    protected TextureAtlasSprite(String p_i1282_1_)
    {
        this.iconName = p_i1282_1_;

        if (Config.isMultiTexture())
        {
            this.spriteSingle = new TextureAtlasSprite(this);
        }
    }

    protected static TextureAtlasSprite func_176604_a(ResourceLocation p_176604_0_)
    {
        String var1 = p_176604_0_.toString();
        return (TextureAtlasSprite)(field_176607_p.equals(var1) ? new TextureClock(var1) : (field_176606_q.equals(var1) ? new TextureCompass(var1) : new TextureAtlasSprite(var1)));
    }

    public static void func_176602_a(String p_176602_0_)
    {
        field_176607_p = p_176602_0_;
    }

    public static void func_176603_b(String p_176603_0_)
    {
        field_176606_q = p_176603_0_;
    }

    public void initSprite(int p_110971_1_, int p_110971_2_, int p_110971_3_, int p_110971_4_, boolean p_110971_5_)
    {
        this.originX = p_110971_3_;
        this.originY = p_110971_4_;
        this.rotated = p_110971_5_;
        float var6 = (float)(0.009999999776482582D / (double)p_110971_1_);
        float var7 = (float)(0.009999999776482582D / (double)p_110971_2_);
        this.minU = (float)p_110971_3_ / (float)((double)p_110971_1_) + var6;
        this.maxU = (float)(p_110971_3_ + this.width) / (float)((double)p_110971_1_) - var6;
        this.minV = (float)p_110971_4_ / (float)p_110971_2_ + var7;
        this.maxV = (float)(p_110971_4_ + this.height) / (float)p_110971_2_ - var7;
        this.baseU = Math.min(this.minU, this.maxU);
        this.baseV = Math.min(this.minV, this.maxV);

        if (this.spriteSingle != null)
        {
            this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
        }
    }

    public void copyFrom(TextureAtlasSprite p_94217_1_)
    {
        this.originX = p_94217_1_.originX;
        this.originY = p_94217_1_.originY;
        this.width = p_94217_1_.width;
        this.height = p_94217_1_.height;
        this.rotated = p_94217_1_.rotated;
        this.minU = p_94217_1_.minU;
        this.maxU = p_94217_1_.maxU;
        this.minV = p_94217_1_.minV;
        this.maxV = p_94217_1_.maxV;

        if (this.spriteSingle != null)
        {
            this.spriteSingle.initSprite(this.width, this.height, 0, 0, false);
        }
    }

    /**
     * Returns the X position of this icon on its texture sheet, in pixels.
     */
    public int getOriginX()
    {
        return this.originX;
    }

    /**
     * Returns the Y position of this icon on its texture sheet, in pixels.
     */
    public int getOriginY()
    {
        return this.originY;
    }

    /**
     * Returns the width of the icon, in pixels.
     */
    public int getIconWidth()
    {
        return this.width;
    }

    /**
     * Returns the height of the icon, in pixels.
     */
    public int getIconHeight()
    {
        return this.height;
    }

    /**
     * Returns the minimum U coordinate to use when rendering with this icon.
     */
    public float getMinU()
    {
        return this.minU;
    }

    /**
     * Returns the maximum U coordinate to use when rendering with this icon.
     */
    public float getMaxU()
    {
        return this.maxU;
    }

    /**
     * Gets a U coordinate on the icon. 0 returns uMin and 16 returns uMax. Other arguments return in-between values.
     */
    public float getInterpolatedU(double p_94214_1_)
    {
        float var3 = this.maxU - this.minU;
        return this.minU + var3 * (float)p_94214_1_ / 16.0F;
    }

    /**
     * Returns the minimum V coordinate to use when rendering with this icon.
     */
    public float getMinV()
    {
        return this.minV;
    }

    /**
     * Returns the maximum V coordinate to use when rendering with this icon.
     */
    public float getMaxV()
    {
        return this.maxV;
    }

    /**
     * Gets a V coordinate on the icon. 0 returns vMin and 16 returns vMax. Other arguments return in-between values.
     */
    public float getInterpolatedV(double p_94207_1_)
    {
        float var3 = this.maxV - this.minV;
        return this.minV + var3 * ((float)p_94207_1_ / 16.0F);
    }

    public String getIconName()
    {
        return this.iconName;
    }

    public void updateAnimation()
    {
        ++this.tickCounter;

        if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter))
        {
            int var1 = this.animationMetadata.getFrameIndex(this.frameCounter);
            int var2 = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
            this.frameCounter = (this.frameCounter + 1) % var2;
            this.tickCounter = 0;
            int var3 = this.animationMetadata.getFrameIndex(this.frameCounter);
            boolean texBlur = false;
            boolean texClamp = this.isSpriteSingle;

            if (var1 != var3 && var3 >= 0 && var3 < this.framesTextureData.size())
            {
                if (Config.isShaders())
                {
                    ShadersTex.uploadTexSub((int[][])((int[][])this.framesTextureData.get(var3)), this.width, this.height, this.originX, this.originY, texBlur, texClamp);
                }
                else
                {
                    TextureUtil.uploadTextureMipmap((int[][])((int[][])this.framesTextureData.get(var3)), this.width, this.height, this.originX, this.originY, texBlur, texClamp);
                }
            }
        }
        else if (this.animationMetadata.func_177219_e())
        {
            this.func_180599_n();
        }
    }

    private void func_180599_n()
    {
        double var1 = 1.0D - (double)this.tickCounter / (double)this.animationMetadata.getFrameTimeSingle(this.frameCounter);
        int var3 = this.animationMetadata.getFrameIndex(this.frameCounter);
        int var4 = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
        int var5 = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % var4);

        if (var3 != var5 && var5 >= 0 && var5 < this.framesTextureData.size())
        {
            int[][] var6 = (int[][])((int[][])this.framesTextureData.get(var3));
            int[][] var7 = (int[][])((int[][])this.framesTextureData.get(var5));

            if (this.field_176605_b == null || this.field_176605_b.length != var6.length)
            {
                this.field_176605_b = new int[var6.length][];
            }

            for (int var8 = 0; var8 < var6.length; ++var8)
            {
                if (this.field_176605_b[var8] == null)
                {
                    this.field_176605_b[var8] = new int[var6[var8].length];
                }

                if (var8 < var7.length && var7[var8].length == var6[var8].length)
                {
                    for (int var9 = 0; var9 < var6[var8].length; ++var9)
                    {
                        int var10 = var6[var8][var9];
                        int var11 = var7[var8][var9];
                        int var12 = (int)((double)((var10 & 16711680) >> 16) * var1 + (double)((var11 & 16711680) >> 16) * (1.0D - var1));
                        int var13 = (int)((double)((var10 & 65280) >> 8) * var1 + (double)((var11 & 65280) >> 8) * (1.0D - var1));
                        int var14 = (int)((double)(var10 & 255) * var1 + (double)(var11 & 255) * (1.0D - var1));
                        this.field_176605_b[var8][var9] = var10 & -16777216 | var12 << 16 | var13 << 8 | var14;
                    }
                }
            }

            TextureUtil.uploadTextureMipmap(this.field_176605_b, this.width, this.height, this.originX, this.originY, false, false);
        }
    }

    public int[][] getFrameTextureData(int p_147965_1_)
    {
        return (int[][])((int[][])this.framesTextureData.get(p_147965_1_));
    }

    public int getFrameCount()
    {
        return this.framesTextureData.size();
    }

    public void setIconWidth(int p_110966_1_)
    {
        this.width = p_110966_1_;

        if (this.spriteSingle != null)
        {
            this.spriteSingle.setIconWidth(this.width);
        }
    }

    public void setIconHeight(int p_110969_1_)
    {
        this.height = p_110969_1_;

        if (this.spriteSingle != null)
        {
            this.spriteSingle.setIconHeight(this.height);
        }
    }

    public void func_180598_a(BufferedImage[] p_180598_1_, AnimationMetadataSection p_180598_2_) throws IOException
    {
        this.resetSprite();
        int var3 = p_180598_1_[0].getWidth();
        int var4 = p_180598_1_[0].getHeight();
        this.width = var3;
        this.height = var4;
        int[][] var5 = new int[p_180598_1_.length][];
        int var6;

        for (var6 = 0; var6 < p_180598_1_.length; ++var6)
        {
            BufferedImage i = p_180598_1_[var6];

            if (i != null)
            {
                if (var6 > 0 && (i.getWidth() != var3 >> var6 || i.getHeight() != var4 >> var6))
                {
                    throw new RuntimeException(String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d", new Object[] {Integer.valueOf(var6), Integer.valueOf(i.getWidth()), Integer.valueOf(i.getHeight()), Integer.valueOf(var3 >> var6), Integer.valueOf(var4 >> var6)}));
                }

                var5[var6] = new int[i.getWidth() * i.getHeight()];
                i.getRGB(0, 0, i.getWidth(), i.getHeight(), var5[var6], 0, i.getWidth());
            }
        }

        int di;
        int var11;

        if (p_180598_2_ == null)
        {
            if (var4 != var3)
            {
                throw new RuntimeException("broken aspect ratio and not an animation");
            }

            this.framesTextureData.add(var5);
        }
        else
        {
            var6 = var4 / var3;
            var11 = var3;
            int datas = var3;
            this.height = this.width;

            if (p_180598_2_.getFrameCount() > 0)
            {
                Iterator data = p_180598_2_.getFrameIndexSet().iterator();

                while (data.hasNext())
                {
                    di = ((Integer)data.next()).intValue();

                    if (di >= var6)
                    {
                        throw new RuntimeException("invalid frameindex " + di);
                    }

                    this.allocateFrameTextureData(di);
                    this.framesTextureData.set(di, getFrameTextureData(var5, var11, datas, di));
                }

                this.animationMetadata = p_180598_2_;
            }
            else
            {
                ArrayList var13 = Lists.newArrayList();

                for (di = 0; di < var6; ++di)
                {
                    this.framesTextureData.add(getFrameTextureData(var5, var11, datas, di));
                    var13.add(new AnimationFrame(di, -1));
                }

                this.animationMetadata = new AnimationMetadataSection(var13, this.width, this.height, p_180598_2_.getFrameTime(), p_180598_2_.func_177219_e());
            }
        }

        for (var11 = 0; var11 < this.framesTextureData.size(); ++var11)
        {
            int[][] var12 = (int[][])((int[][])this.framesTextureData.get(var11));

            if (var12 != null && !this.iconName.startsWith("minecraft:blocks/leaves_"))
            {
                for (di = 0; di < var12.length; ++di)
                {
                    int[] var14 = var12[di];
                    this.fixTransparentColor(var14);
                }
            }
        }

        if (this.spriteSingle != null)
        {
            this.spriteSingle.func_180598_a(p_180598_1_, p_180598_2_);
        }
    }

    public void generateMipmaps(int p_147963_1_)
    {
        ArrayList var2 = Lists.newArrayList();

        for (int var3 = 0; var3 < this.framesTextureData.size(); ++var3)
        {
            final int[][] var4 = (int[][])((int[][])this.framesTextureData.get(var3));

            if (var4 != null)
            {
                try
                {
                    var2.add(TextureUtil.generateMipmapData(p_147963_1_, this.width, var4));
                }
                catch (Throwable var8)
                {
                    CrashReport var6 = CrashReport.makeCrashReport(var8, "Generating mipmaps for frame");
                    CrashReportCategory var7 = var6.makeCategory("Frame being iterated");
                    var7.addCrashSection("Frame index", Integer.valueOf(var3));
                    var7.addCrashSectionCallable("Frame sizes", new Callable()
                    {
                        private static final String __OBFID = "CL_00001063";
                        public String call()
                        {
                            StringBuilder var1 = new StringBuilder();
                            int[][] var2 = var4;
                            int var3 = var2.length;

                            for (int var4x = 0; var4x < var3; ++var4x)
                            {
                                int[] var5 = var2[var4x];

                                if (var1.length() > 0)
                                {
                                    var1.append(", ");
                                }

                                var1.append(var5 == null ? "null" : Integer.valueOf(var5.length));
                            }

                            return var1.toString();
                        }
                    });
                    throw new ReportedException(var6);
                }
            }
        }

        this.setFramesTextureData(var2);

        if (this.spriteSingle != null)
        {
            this.spriteSingle.generateMipmaps(p_147963_1_);
        }
    }

    private void allocateFrameTextureData(int p_130099_1_)
    {
        if (this.framesTextureData.size() <= p_130099_1_)
        {
            for (int var2 = this.framesTextureData.size(); var2 <= p_130099_1_; ++var2)
            {
                this.framesTextureData.add((Object)null);
            }
        }

        if (this.spriteSingle != null)
        {
            this.spriteSingle.allocateFrameTextureData(p_130099_1_);
        }
    }

    private static int[][] getFrameTextureData(int[][] p_147962_0_, int p_147962_1_, int p_147962_2_, int p_147962_3_)
    {
        int[][] var4 = new int[p_147962_0_.length][];

        for (int var5 = 0; var5 < p_147962_0_.length; ++var5)
        {
            int[] var6 = p_147962_0_[var5];

            if (var6 != null)
            {
                var4[var5] = new int[(p_147962_1_ >> var5) * (p_147962_2_ >> var5)];
                System.arraycopy(var6, p_147962_3_ * var4[var5].length, var4[var5], 0, var4[var5].length);
            }
        }

        return var4;
    }

    public void clearFramesTextureData()
    {
        this.framesTextureData.clear();

        if (this.spriteSingle != null)
        {
            this.spriteSingle.clearFramesTextureData();
        }
    }

    public boolean hasAnimationMetadata()
    {
        return this.animationMetadata != null;
    }

    public void setFramesTextureData(List p_110968_1_)
    {
        this.framesTextureData = p_110968_1_;

        if (this.spriteSingle != null)
        {
            this.spriteSingle.setFramesTextureData(p_110968_1_);
        }
    }

    private void resetSprite()
    {
        this.animationMetadata = null;
        this.setFramesTextureData(Lists.newArrayList());
        this.frameCounter = 0;
        this.tickCounter = 0;

        if (this.spriteSingle != null)
        {
            this.spriteSingle.resetSprite();
        }
    }

    public String toString()
    {
        return "TextureAtlasSprite{name=\'" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
    }

    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location)
    {
        return false;
    }

    public boolean load(IResourceManager manager, ResourceLocation location)
    {
        return true;
    }

    public int getIndexInMap()
    {
        return this.indexInMap;
    }

    public void setIndexInMap(int indexInMap)
    {
        this.indexInMap = indexInMap;
    }

    private void fixTransparentColor(int[] data)
    {
        if (data != null)
        {
            long redSum = 0L;
            long greenSum = 0L;
            long blueSum = 0L;
            long count = 0L;
            int redAvg;
            int greenAvg;
            int blueAvg;
            int colAvg;
            int i;
            int col;

            for (redAvg = 0; redAvg < data.length; ++redAvg)
            {
                greenAvg = data[redAvg];
                blueAvg = greenAvg >> 24 & 255;

                if (blueAvg >= 16)
                {
                    colAvg = greenAvg >> 16 & 255;
                    i = greenAvg >> 8 & 255;
                    col = greenAvg & 255;
                    redSum += (long)colAvg;
                    greenSum += (long)i;
                    blueSum += (long)col;
                    ++count;
                }
            }

            if (count > 0L)
            {
                redAvg = (int)(redSum / count);
                greenAvg = (int)(greenSum / count);
                blueAvg = (int)(blueSum / count);
                colAvg = redAvg << 16 | greenAvg << 8 | blueAvg;

                for (i = 0; i < data.length; ++i)
                {
                    col = data[i];
                    int alpha = col >> 24 & 255;

                    if (alpha <= 16)
                    {
                        data[i] = colAvg;
                    }
                }
            }
        }
    }

    public double getSpriteU16(float atlasU)
    {
        float dU = this.maxU - this.minU;
        return (double)((atlasU - this.minU) / dU * 16.0F);
    }

    public double getSpriteV16(float atlasV)
    {
        float dV = this.maxV - this.minV;
        return (double)((atlasV - this.minV) / dV * 16.0F);
    }

    public void bindSpriteTexture()
    {
        if (this.glSpriteTextureId < 0)
        {
            this.glSpriteTextureId = TextureUtil.glGenTextures();
            TextureUtil.func_180600_a(this.glSpriteTextureId, this.mipmapLevels, this.width, this.height);
            TextureUtils.applyAnisotropicLevel();
        }

        TextureUtils.bindTexture(this.glSpriteTextureId);
    }

    public void deleteSpriteTexture()
    {
        if (this.glSpriteTextureId >= 0)
        {
            TextureUtil.deleteTexture(this.glSpriteTextureId);
            this.glSpriteTextureId = -1;
        }
    }

    public float toSingleU(float u)
    {
        u -= this.baseU;
        float ku = (float)this.sheetWidth / (float)this.width;
        u *= ku;
        return u;
    }

    public float toSingleV(float v)
    {
        v -= this.baseV;
        float kv = (float)this.sheetHeight / (float)this.height;
        v *= kv;
        return v;
    }
}
