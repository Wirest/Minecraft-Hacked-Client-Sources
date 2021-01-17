// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import java.awt.image.RenderedImage;
import org.lwjgl.BufferUtils;
import java.io.File;
import org.apache.commons.io.IOUtils;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.IOException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.Minecraft;
import optifine.Config;
import optifine.Reflector;
import org.lwjgl.opengl.GL11;
import optifine.Mipmaps;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GLAllocation;
import org.apache.logging.log4j.LogManager;
import java.nio.IntBuffer;
import org.apache.logging.log4j.Logger;

public class TextureUtil
{
    private static final Logger logger;
    private static final IntBuffer dataBuffer;
    public static final DynamicTexture missingTexture;
    public static final int[] missingTextureData;
    private static final int[] mipmapBuffer;
    private static final String __OBFID = "CL_00001067";
    
    static {
        logger = LogManager.getLogger();
        dataBuffer = GLAllocation.createDirectIntBuffer(4194304);
        missingTexture = new DynamicTexture(16, 16);
        missingTextureData = TextureUtil.missingTexture.getTextureData();
        final int i = -16777216;
        final int j = -524040;
        final int[] aint = { -524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040 };
        final int[] aint2 = { -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216 };
        final int k = aint.length;
        for (int l = 0; l < 16; ++l) {
            System.arraycopy((l < k) ? aint : aint2, 0, TextureUtil.missingTextureData, 16 * l, k);
            System.arraycopy((l < k) ? aint2 : aint, 0, TextureUtil.missingTextureData, 16 * l + k, k);
        }
        TextureUtil.missingTexture.updateDynamicTexture();
        mipmapBuffer = new int[4];
    }
    
    public static int glGenTextures() {
        return GlStateManager.generateTexture();
    }
    
    public static void deleteTexture(final int textureId) {
        GlStateManager.deleteTexture(textureId);
    }
    
    public static int uploadTextureImage(final int p_110987_0_, final BufferedImage p_110987_1_) {
        return uploadTextureImageAllocate(p_110987_0_, p_110987_1_, false, false);
    }
    
    public static void uploadTexture(final int textureId, final int[] p_110988_1_, final int p_110988_2_, final int p_110988_3_) {
        bindTexture(textureId);
        uploadTextureSub(0, p_110988_1_, p_110988_2_, p_110988_3_, 0, 0, false, false, false);
    }
    
    public static int[][] generateMipmapData(final int p_147949_0_, final int p_147949_1_, final int[][] p_147949_2_) {
        final int[][] aint = new int[p_147949_0_ + 1][];
        aint[0] = p_147949_2_[0];
        if (p_147949_0_ > 0) {
            boolean flag = false;
            for (int i = 0; i < p_147949_2_.length; ++i) {
                if (p_147949_2_[0][i] >> 24 == 0) {
                    flag = true;
                    break;
                }
            }
            for (int l1 = 1; l1 <= p_147949_0_; ++l1) {
                if (p_147949_2_[l1] != null) {
                    aint[l1] = p_147949_2_[l1];
                }
                else {
                    final int[] aint2 = aint[l1 - 1];
                    final int[] aint3 = new int[aint2.length >> 2];
                    final int j = p_147949_1_ >> l1;
                    final int k = aint3.length / j;
                    final int m = j << 1;
                    for (int i2 = 0; i2 < j; ++i2) {
                        for (int j2 = 0; j2 < k; ++j2) {
                            final int k2 = 2 * (i2 + j2 * m);
                            aint3[i2 + j2 * j] = blendColors(aint2[k2 + 0], aint2[k2 + 1], aint2[k2 + 0 + m], aint2[k2 + 1 + m], flag);
                        }
                    }
                    aint[l1] = aint3;
                }
            }
        }
        return aint;
    }
    
    private static int blendColors(final int p_147943_0_, final int p_147943_1_, final int p_147943_2_, final int p_147943_3_, final boolean p_147943_4_) {
        return Mipmaps.alphaBlend(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_);
    }
    
    private static int blendColorComponent(final int p_147944_0_, final int p_147944_1_, final int p_147944_2_, final int p_147944_3_, final int p_147944_4_) {
        final float f = (float)Math.pow((p_147944_0_ >> p_147944_4_ & 0xFF) / 255.0f, 2.2);
        final float f2 = (float)Math.pow((p_147944_1_ >> p_147944_4_ & 0xFF) / 255.0f, 2.2);
        final float f3 = (float)Math.pow((p_147944_2_ >> p_147944_4_ & 0xFF) / 255.0f, 2.2);
        final float f4 = (float)Math.pow((p_147944_3_ >> p_147944_4_ & 0xFF) / 255.0f, 2.2);
        final float f5 = (float)Math.pow((f + f2 + f3 + f4) * 0.25, 0.45454545454545453);
        return (int)(f5 * 255.0);
    }
    
    public static void uploadTextureMipmap(final int[][] p_147955_0_, final int p_147955_1_, final int p_147955_2_, final int p_147955_3_, final int p_147955_4_, final boolean p_147955_5_, final boolean p_147955_6_) {
        for (int i = 0; i < p_147955_0_.length; ++i) {
            final int[] aint = p_147955_0_[i];
            uploadTextureSub(i, aint, p_147955_1_ >> i, p_147955_2_ >> i, p_147955_3_ >> i, p_147955_4_ >> i, p_147955_5_, p_147955_6_, p_147955_0_.length > 1);
        }
    }
    
    private static void uploadTextureSub(final int p_147947_0_, final int[] p_147947_1_, final int p_147947_2_, final int p_147947_3_, final int p_147947_4_, final int p_147947_5_, final boolean p_147947_6_, final boolean p_147947_7_, final boolean p_147947_8_) {
        final int i = 4194304 / p_147947_2_;
        setTextureBlurMipmap(p_147947_6_, p_147947_8_);
        setTextureClamped(p_147947_7_);
        int j;
        for (int k = 0; k < p_147947_2_ * p_147947_3_; k += p_147947_2_ * j) {
            final int l = k / p_147947_2_;
            j = Math.min(i, p_147947_3_ - l);
            final int i2 = p_147947_2_ * j;
            copyToBufferPos(p_147947_1_, k, i2);
            GL11.glTexSubImage2D(3553, p_147947_0_, p_147947_4_, p_147947_5_ + l, p_147947_2_, j, 32993, 33639, TextureUtil.dataBuffer);
        }
    }
    
    public static int uploadTextureImageAllocate(final int p_110989_0_, final BufferedImage p_110989_1_, final boolean p_110989_2_, final boolean p_110989_3_) {
        allocateTexture(p_110989_0_, p_110989_1_.getWidth(), p_110989_1_.getHeight());
        return uploadTextureImageSub(p_110989_0_, p_110989_1_, 0, 0, p_110989_2_, p_110989_3_);
    }
    
    public static void allocateTexture(final int p_110991_0_, final int p_110991_1_, final int p_110991_2_) {
        allocateTextureImpl(p_110991_0_, 0, p_110991_1_, p_110991_2_);
    }
    
    public static void allocateTextureImpl(final int p_180600_0_, final int p_180600_1_, final int p_180600_2_, final int p_180600_3_) {
        Object object = TextureUtil.class;
        if (Reflector.SplashScreen.exists()) {
            object = Reflector.SplashScreen.getTargetClass();
        }
        synchronized (object) {
            deleteTexture(p_180600_0_);
            bindTexture(p_180600_0_);
        }
        if (p_180600_1_ >= 0) {
            GL11.glTexParameteri(3553, 33085, p_180600_1_);
            GL11.glTexParameterf(3553, 33082, 0.0f);
            GL11.glTexParameterf(3553, 33083, (float)p_180600_1_);
            GL11.glTexParameterf(3553, 34049, 0.0f);
        }
        for (int i = 0; i <= p_180600_1_; ++i) {
            GL11.glTexImage2D(3553, i, 6408, p_180600_2_ >> i, p_180600_3_ >> i, 0, 32993, 33639, (IntBuffer)null);
        }
    }
    
    public static int uploadTextureImageSub(final int textureId, final BufferedImage p_110995_1_, final int p_110995_2_, final int p_110995_3_, final boolean p_110995_4_, final boolean p_110995_5_) {
        bindTexture(textureId);
        uploadTextureImageSubImpl(p_110995_1_, p_110995_2_, p_110995_3_, p_110995_4_, p_110995_5_);
        return textureId;
    }
    
    private static void uploadTextureImageSubImpl(final BufferedImage p_110993_0_, final int p_110993_1_, final int p_110993_2_, final boolean p_110993_3_, final boolean p_110993_4_) {
        final int i = p_110993_0_.getWidth();
        final int j = p_110993_0_.getHeight();
        final int k = 4194304 / i;
        final int[] aint = new int[k * i];
        setTextureBlurred(p_110993_3_);
        setTextureClamped(p_110993_4_);
        for (int l = 0; l < i * j; l += i * k) {
            final int i2 = l / i;
            final int j2 = Math.min(k, j - i2);
            final int k2 = i * j2;
            p_110993_0_.getRGB(0, i2, i, j2, aint, 0, i);
            copyToBuffer(aint, k2);
            GL11.glTexSubImage2D(3553, 0, p_110993_1_, p_110993_2_ + i2, i, j2, 32993, 33639, TextureUtil.dataBuffer);
        }
    }
    
    public static void setTextureClamped(final boolean p_110997_0_) {
        if (p_110997_0_) {
            GL11.glTexParameteri(3553, 10242, 33071);
            GL11.glTexParameteri(3553, 10243, 33071);
        }
        else {
            GL11.glTexParameteri(3553, 10242, 10497);
            GL11.glTexParameteri(3553, 10243, 10497);
        }
    }
    
    private static void setTextureBlurred(final boolean p_147951_0_) {
        setTextureBlurMipmap(p_147951_0_, false);
    }
    
    public static void setTextureBlurMipmap(final boolean p_147954_0_, final boolean p_147954_1_) {
        if (p_147954_0_) {
            GL11.glTexParameteri(3553, 10241, p_147954_1_ ? 9987 : 9729);
            GL11.glTexParameteri(3553, 10240, 9729);
        }
        else {
            final int i = Config.getMipmapType();
            GL11.glTexParameteri(3553, 10241, p_147954_1_ ? i : 9728);
            GL11.glTexParameteri(3553, 10240, 9728);
        }
    }
    
    private static void copyToBuffer(final int[] p_110990_0_, final int p_110990_1_) {
        copyToBufferPos(p_110990_0_, 0, p_110990_1_);
    }
    
    private static void copyToBufferPos(final int[] p_110994_0_, final int p_110994_1_, final int p_110994_2_) {
        int[] aint = p_110994_0_;
        if (Minecraft.getMinecraft().gameSettings.anaglyph) {
            aint = updateAnaglyph(p_110994_0_);
        }
        TextureUtil.dataBuffer.clear();
        TextureUtil.dataBuffer.put(aint, p_110994_1_, p_110994_2_);
        TextureUtil.dataBuffer.position(0).limit(p_110994_2_);
    }
    
    static void bindTexture(final int p_94277_0_) {
        GlStateManager.bindTexture(p_94277_0_);
    }
    
    public static int[] readImageData(final IResourceManager resourceManager, final ResourceLocation imageLocation) throws IOException {
        final BufferedImage bufferedimage = readBufferedImage(resourceManager.getResource(imageLocation).getInputStream());
        if (bufferedimage == null) {
            return null;
        }
        final int i = bufferedimage.getWidth();
        final int j = bufferedimage.getHeight();
        final int[] aint = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
        return aint;
    }
    
    public static BufferedImage readBufferedImage(final InputStream imageStream) throws IOException {
        if (imageStream == null) {
            return null;
        }
        BufferedImage bufferedimage;
        try {
            bufferedimage = ImageIO.read(imageStream);
        }
        finally {
            IOUtils.closeQuietly(imageStream);
        }
        IOUtils.closeQuietly(imageStream);
        return bufferedimage;
    }
    
    public static int[] updateAnaglyph(final int[] p_110985_0_) {
        final int[] aint = new int[p_110985_0_.length];
        for (int i = 0; i < p_110985_0_.length; ++i) {
            aint[i] = anaglyphColor(p_110985_0_[i]);
        }
        return aint;
    }
    
    public static int anaglyphColor(final int p_177054_0_) {
        final int i = p_177054_0_ >> 24 & 0xFF;
        final int j = p_177054_0_ >> 16 & 0xFF;
        final int k = p_177054_0_ >> 8 & 0xFF;
        final int l = p_177054_0_ & 0xFF;
        final int i2 = (j * 30 + k * 59 + l * 11) / 100;
        final int j2 = (j * 30 + k * 70) / 100;
        final int k2 = (j * 30 + l * 70) / 100;
        return i << 24 | i2 << 16 | j2 << 8 | k2;
    }
    
    public static void saveGlTexture(final String p_saveGlTexture_0_, final int p_saveGlTexture_1_, final int p_saveGlTexture_2_, final int p_saveGlTexture_3_, final int p_saveGlTexture_4_) {
        bindTexture(p_saveGlTexture_1_);
        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);
        for (int i = 0; i <= p_saveGlTexture_2_; ++i) {
            final File file1 = new File(String.valueOf(p_saveGlTexture_0_) + "_" + i + ".png");
            final int j = p_saveGlTexture_3_ >> i;
            final int k = p_saveGlTexture_4_ >> i;
            final int l = j * k;
            final IntBuffer intbuffer = BufferUtils.createIntBuffer(l);
            final int[] aint = new int[l];
            GL11.glGetTexImage(3553, i, 32993, 33639, intbuffer);
            intbuffer.get(aint);
            final BufferedImage bufferedimage = new BufferedImage(j, k, 2);
            bufferedimage.setRGB(0, 0, j, k, aint, 0, j);
            try {
                ImageIO.write(bufferedimage, "png", file1);
                TextureUtil.logger.debug("Exported png to: {}", file1.getAbsolutePath());
            }
            catch (Exception exception) {
                TextureUtil.logger.debug("Unable to write: ", exception);
            }
        }
    }
    
    public static void processPixelValues(final int[] p_147953_0_, final int p_147953_1_, final int p_147953_2_) {
        final int[] aint = new int[p_147953_1_];
        for (int i = p_147953_2_ / 2, j = 0; j < i; ++j) {
            System.arraycopy(p_147953_0_, j * p_147953_1_, aint, 0, p_147953_1_);
            System.arraycopy(p_147953_0_, (p_147953_2_ - 1 - j) * p_147953_1_, p_147953_0_, j * p_147953_1_, p_147953_1_);
            System.arraycopy(aint, 0, p_147953_0_, (p_147953_2_ - 1 - j) * p_147953_1_, p_147953_1_);
        }
    }
}
