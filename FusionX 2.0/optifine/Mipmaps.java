package optifine;

import java.awt.Dimension;
import java.nio.IntBuffer;
import java.util.ArrayList;
import net.minecraft.client.renderer.GLAllocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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

    public Mipmaps(String iconName, int width, int height, int[] data, boolean direct)
    {
        this.iconName = iconName;
        this.width = width;
        this.height = height;
        this.data = data;
        this.direct = direct;
        this.mipmapDimensions = makeMipmapDimensions(width, height, iconName);
        this.mipmapDatas = generateMipMapData(data, width, height, this.mipmapDimensions);

        if (direct)
        {
            this.mipmapBuffers = makeMipmapBuffers(this.mipmapDimensions, this.mipmapDatas);
        }
    }

    public static Dimension[] makeMipmapDimensions(int width, int height, String iconName)
    {
        int texWidth = TextureUtils.ceilPowerOfTwo(width);
        int texHeight = TextureUtils.ceilPowerOfTwo(height);

        if (texWidth == width && texHeight == height)
        {
            ArrayList listDims = new ArrayList();
            int mipWidth = texWidth;
            int mipHeight = texHeight;

            while (true)
            {
                mipWidth /= 2;
                mipHeight /= 2;

                if (mipWidth <= 0 && mipHeight <= 0)
                {
                    Dimension[] mipmapDimensions1 = (Dimension[])((Dimension[])listDims.toArray(new Dimension[listDims.size()]));
                    return mipmapDimensions1;
                }

                if (mipWidth <= 0)
                {
                    mipWidth = 1;
                }

                if (mipHeight <= 0)
                {
                    mipHeight = 1;
                }

                int mipmapDimensions = mipWidth * mipHeight * 4;
                Dimension dim = new Dimension(mipWidth, mipHeight);
                listDims.add(dim);
            }
        }
        else
        {
            Config.warn("Mipmaps not possible (power of 2 dimensions needed), texture: " + iconName + ", dim: " + width + "x" + height);
            return new Dimension[0];
        }
    }

    public static int[][] generateMipMapData(int[] data, int width, int height, Dimension[] mipmapDimensions)
    {
        int[] parMipData = data;
        int parWidth = width;
        boolean scale = true;
        int[][] mipmapDatas = new int[mipmapDimensions.length][];

        for (int i = 0; i < mipmapDimensions.length; ++i)
        {
            Dimension dim = mipmapDimensions[i];
            int mipWidth = dim.width;
            int mipHeight = dim.height;
            int[] mipData = new int[mipWidth * mipHeight];
            mipmapDatas[i] = mipData;
            int level = i + 1;

            if (scale)
            {
                for (int mipX = 0; mipX < mipWidth; ++mipX)
                {
                    for (int mipY = 0; mipY < mipHeight; ++mipY)
                    {
                        int p1 = parMipData[mipX * 2 + 0 + (mipY * 2 + 0) * parWidth];
                        int p2 = parMipData[mipX * 2 + 1 + (mipY * 2 + 0) * parWidth];
                        int p3 = parMipData[mipX * 2 + 1 + (mipY * 2 + 1) * parWidth];
                        int p4 = parMipData[mipX * 2 + 0 + (mipY * 2 + 1) * parWidth];
                        int pixel = alphaBlend(p1, p2, p3, p4);
                        mipData[mipX + mipY * mipWidth] = pixel;
                    }
                }
            }

            parMipData = mipData;
            parWidth = mipWidth;

            if (mipWidth <= 1 || mipHeight <= 1)
            {
                scale = false;
            }
        }

        return mipmapDatas;
    }

    public static int alphaBlend(int c1, int c2, int c3, int c4)
    {
        int cx1 = alphaBlend(c1, c2);
        int cx2 = alphaBlend(c3, c4);
        int cx = alphaBlend(cx1, cx2);
        return cx;
    }

    private static int alphaBlend(int c1, int c2)
    {
        int a1 = (c1 & -16777216) >> 24 & 255;
        int a2 = (c2 & -16777216) >> 24 & 255;
        int ax = (a1 + a2) / 2;

        if (a1 == 0 && a2 == 0)
        {
            a1 = 1;
            a2 = 1;
        }
        else
        {
            if (a1 == 0)
            {
                c1 = c2;
                ax /= 2;
            }

            if (a2 == 0)
            {
                c2 = c1;
                ax /= 2;
            }
        }

        int r1 = (c1 >> 16 & 255) * a1;
        int g1 = (c1 >> 8 & 255) * a1;
        int b1 = (c1 & 255) * a1;
        int r2 = (c2 >> 16 & 255) * a2;
        int g2 = (c2 >> 8 & 255) * a2;
        int b2 = (c2 & 255) * a2;
        int rx = (r1 + r2) / (a1 + a2);
        int gx = (g1 + g2) / (a1 + a2);
        int bx = (b1 + b2) / (a1 + a2);
        return ax << 24 | rx << 16 | gx << 8 | bx;
    }

    private int averageColor(int i, int j)
    {
        int k = (i & -16777216) >> 24 & 255;
        int l = (j & -16777216) >> 24 & 255;
        return (k + l >> 1 << 24) + ((i & 16711422) + (j & 16711422) >> 1);
    }

    public static IntBuffer[] makeMipmapBuffers(Dimension[] mipmapDimensions, int[][] mipmapDatas)
    {
        if (mipmapDimensions == null)
        {
            return null;
        }
        else
        {
            IntBuffer[] mipmapBuffers = new IntBuffer[mipmapDimensions.length];

            for (int i = 0; i < mipmapDimensions.length; ++i)
            {
                Dimension dim = mipmapDimensions[i];
                int bufLen = dim.width * dim.height;
                IntBuffer buf = GLAllocation.createDirectIntBuffer(bufLen);
                int[] data = mipmapDatas[i];
                buf.clear();
                buf.put(data);
                buf.clear();
                mipmapBuffers[i] = buf;
            }

            return mipmapBuffers;
        }
    }

    public static void allocateMipmapTextures(int width, int height, String name)
    {
        Dimension[] dims = makeMipmapDimensions(width, height, name);

        for (int i = 0; i < dims.length; ++i)
        {
            Dimension dim = dims[i];
            int mipWidth = dim.width;
            int mipHeight = dim.height;
            int level = i + 1;
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, level, GL11.GL_RGBA, mipWidth, mipHeight, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, (IntBuffer)null);
        }
    }
}
