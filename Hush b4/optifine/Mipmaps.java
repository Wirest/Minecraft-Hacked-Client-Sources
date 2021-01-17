// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GLAllocation;
import java.util.List;
import java.util.ArrayList;
import java.awt.Dimension;
import java.nio.IntBuffer;

public class Mipmaps
{
    private final String iconName;
    private final int width;
    private final int height;
    private final int[] data;
    private final boolean direct;
    private int[][] mipmapDatas;
    private IntBuffer[] mipmapBuffers;
    private Dimension[] mipmapDimensions;
    
    public Mipmaps(final String p_i66_1_, final int p_i66_2_, final int p_i66_3_, final int[] p_i66_4_, final boolean p_i66_5_) {
        this.iconName = p_i66_1_;
        this.width = p_i66_2_;
        this.height = p_i66_3_;
        this.data = p_i66_4_;
        this.direct = p_i66_5_;
        this.mipmapDimensions = makeMipmapDimensions(p_i66_2_, p_i66_3_, p_i66_1_);
        this.mipmapDatas = generateMipMapData(p_i66_4_, p_i66_2_, p_i66_3_, this.mipmapDimensions);
        if (p_i66_5_) {
            this.mipmapBuffers = makeMipmapBuffers(this.mipmapDimensions, this.mipmapDatas);
        }
    }
    
    public static Dimension[] makeMipmapDimensions(final int p_makeMipmapDimensions_0_, final int p_makeMipmapDimensions_1_, final String p_makeMipmapDimensions_2_) {
        final int i = TextureUtils.ceilPowerOfTwo(p_makeMipmapDimensions_0_);
        final int j = TextureUtils.ceilPowerOfTwo(p_makeMipmapDimensions_1_);
        if (i == p_makeMipmapDimensions_0_ && j == p_makeMipmapDimensions_1_) {
            final List list = new ArrayList();
            int k = i;
            int l = j;
            while (true) {
                k /= 2;
                l /= 2;
                if (k <= 0 && l <= 0) {
                    break;
                }
                if (k <= 0) {
                    k = 1;
                }
                if (l <= 0) {
                    l = 1;
                }
                final int i2 = k * l * 4;
                final Dimension dimension = new Dimension(k, l);
                list.add(dimension);
            }
            final Dimension[] adimension = list.toArray(new Dimension[list.size()]);
            return adimension;
        }
        Config.warn("Mipmaps not possible (power of 2 dimensions needed), texture: " + p_makeMipmapDimensions_2_ + ", dim: " + p_makeMipmapDimensions_0_ + "x" + p_makeMipmapDimensions_1_);
        return new Dimension[0];
    }
    
    public static int[][] generateMipMapData(final int[] p_generateMipMapData_0_, final int p_generateMipMapData_1_, final int p_generateMipMapData_2_, final Dimension[] p_generateMipMapData_3_) {
        int[] aint = p_generateMipMapData_0_;
        int i = p_generateMipMapData_1_;
        boolean flag = true;
        final int[][] aint2 = new int[p_generateMipMapData_3_.length][];
        for (int j = 0; j < p_generateMipMapData_3_.length; ++j) {
            final Dimension dimension = p_generateMipMapData_3_[j];
            final int k = dimension.width;
            final int l = dimension.height;
            final int[] aint3 = new int[k * l];
            aint2[j] = aint3;
            final int i2 = j + 1;
            if (flag) {
                for (int j2 = 0; j2 < k; ++j2) {
                    for (int k2 = 0; k2 < l; ++k2) {
                        final int l2 = aint[j2 * 2 + 0 + (k2 * 2 + 0) * i];
                        final int i3 = aint[j2 * 2 + 1 + (k2 * 2 + 0) * i];
                        final int j3 = aint[j2 * 2 + 1 + (k2 * 2 + 1) * i];
                        final int k3 = aint[j2 * 2 + 0 + (k2 * 2 + 1) * i];
                        final int l3 = alphaBlend(l2, i3, j3, k3);
                        aint3[j2 + k2 * k] = l3;
                    }
                }
            }
            aint = aint3;
            i = k;
            if (k <= 1 || l <= 1) {
                flag = false;
            }
        }
        return aint2;
    }
    
    public static int alphaBlend(final int p_alphaBlend_0_, final int p_alphaBlend_1_, final int p_alphaBlend_2_, final int p_alphaBlend_3_) {
        final int i = alphaBlend(p_alphaBlend_0_, p_alphaBlend_1_);
        final int j = alphaBlend(p_alphaBlend_2_, p_alphaBlend_3_);
        final int k = alphaBlend(i, j);
        return k;
    }
    
    private static int alphaBlend(int p_alphaBlend_0_, int p_alphaBlend_1_) {
        int i = (p_alphaBlend_0_ & 0xFF000000) >> 24 & 0xFF;
        int j = (p_alphaBlend_1_ & 0xFF000000) >> 24 & 0xFF;
        int k = (i + j) / 2;
        if (i == 0 && j == 0) {
            i = 1;
            j = 1;
        }
        else {
            if (i == 0) {
                p_alphaBlend_0_ = p_alphaBlend_1_;
                k /= 2;
            }
            if (j == 0) {
                p_alphaBlend_1_ = p_alphaBlend_0_;
                k /= 2;
            }
        }
        final int l = (p_alphaBlend_0_ >> 16 & 0xFF) * i;
        final int i2 = (p_alphaBlend_0_ >> 8 & 0xFF) * i;
        final int j2 = (p_alphaBlend_0_ & 0xFF) * i;
        final int k2 = (p_alphaBlend_1_ >> 16 & 0xFF) * j;
        final int l2 = (p_alphaBlend_1_ >> 8 & 0xFF) * j;
        final int i3 = (p_alphaBlend_1_ & 0xFF) * j;
        final int j3 = (l + k2) / (i + j);
        final int k3 = (i2 + l2) / (i + j);
        final int l3 = (j2 + i3) / (i + j);
        return k << 24 | j3 << 16 | k3 << 8 | l3;
    }
    
    private int averageColor(final int p_averageColor_1_, final int p_averageColor_2_) {
        final int i = (p_averageColor_1_ & 0xFF000000) >> 24 & 0xFF;
        final int j = (p_averageColor_2_ & 0xFF000000) >> 24 & 0xFF;
        return (i + j >> 1 << 24) + ((p_averageColor_1_ & 0xFEFEFE) + (p_averageColor_2_ & 0xFEFEFE) >> 1);
    }
    
    public static IntBuffer[] makeMipmapBuffers(final Dimension[] p_makeMipmapBuffers_0_, final int[][] p_makeMipmapBuffers_1_) {
        if (p_makeMipmapBuffers_0_ == null) {
            return null;
        }
        final IntBuffer[] aintbuffer = new IntBuffer[p_makeMipmapBuffers_0_.length];
        for (int i = 0; i < p_makeMipmapBuffers_0_.length; ++i) {
            final Dimension dimension = p_makeMipmapBuffers_0_[i];
            final int j = dimension.width * dimension.height;
            final IntBuffer intbuffer = GLAllocation.createDirectIntBuffer(j);
            final int[] aint = p_makeMipmapBuffers_1_[i];
            intbuffer.clear();
            intbuffer.put(aint);
            intbuffer.clear();
            aintbuffer[i] = intbuffer;
        }
        return aintbuffer;
    }
    
    public static void allocateMipmapTextures(final int p_allocateMipmapTextures_0_, final int p_allocateMipmapTextures_1_, final String p_allocateMipmapTextures_2_) {
        final Dimension[] adimension = makeMipmapDimensions(p_allocateMipmapTextures_0_, p_allocateMipmapTextures_1_, p_allocateMipmapTextures_2_);
        for (int i = 0; i < adimension.length; ++i) {
            final Dimension dimension = adimension[i];
            final int j = dimension.width;
            final int k = dimension.height;
            final int l = i + 1;
            GL11.glTexImage2D(3553, l, 6408, j, k, 0, 32993, 33639, (IntBuffer)null);
        }
    }
}
