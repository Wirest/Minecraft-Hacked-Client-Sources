// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import net.minecraft.client.Minecraft;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.client.resources.IResource;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import optifine.Config;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import shadersmod.common.SMCLog;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.texture.AbstractTexture;
import java.util.Arrays;
import java.util.HashMap;
import org.lwjgl.BufferUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import java.util.Map;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;

public class ShadersTex
{
    public static final int initialBufferSize = 1048576;
    public static ByteBuffer byteBuffer;
    public static IntBuffer intBuffer;
    public static int[] intArray;
    public static final int defBaseTexColor = 0;
    public static final int defNormTexColor = -8421377;
    public static final int defSpecTexColor = 0;
    public static Map<Integer, MultiTexID> multiTexMap;
    public static TextureMap updatingTextureMap;
    public static TextureAtlasSprite updatingSprite;
    public static MultiTexID updatingTex;
    public static MultiTexID boundTex;
    public static int updatingPage;
    public static String iconName;
    public static IResourceManager resManager;
    static ResourceLocation resLocation;
    static int imageSize;
    
    static {
        ShadersTex.byteBuffer = BufferUtils.createByteBuffer(4194304);
        ShadersTex.intBuffer = ShadersTex.byteBuffer.asIntBuffer();
        ShadersTex.intArray = new int[1048576];
        ShadersTex.multiTexMap = new HashMap<Integer, MultiTexID>();
        ShadersTex.updatingTextureMap = null;
        ShadersTex.updatingSprite = null;
        ShadersTex.updatingTex = null;
        ShadersTex.boundTex = null;
        ShadersTex.updatingPage = 0;
        ShadersTex.iconName = null;
        ShadersTex.resManager = null;
        ShadersTex.resLocation = null;
        ShadersTex.imageSize = 0;
    }
    
    public static IntBuffer getIntBuffer(final int size) {
        if (ShadersTex.intBuffer.capacity() < size) {
            final int i = roundUpPOT(size);
            ShadersTex.byteBuffer = BufferUtils.createByteBuffer(i * 4);
            ShadersTex.intBuffer = ShadersTex.byteBuffer.asIntBuffer();
        }
        return ShadersTex.intBuffer;
    }
    
    public static int[] getIntArray(final int size) {
        if (ShadersTex.intArray == null) {
            ShadersTex.intArray = new int[1048576];
        }
        if (ShadersTex.intArray.length < size) {
            ShadersTex.intArray = new int[roundUpPOT(size)];
        }
        return ShadersTex.intArray;
    }
    
    public static int roundUpPOT(final int x) {
        int i = x - 1;
        i |= i >> 1;
        i |= i >> 2;
        i |= i >> 4;
        i |= i >> 8;
        i |= i >> 16;
        return i + 1;
    }
    
    public static int log2(int x) {
        int i = 0;
        if ((x & 0xFFFF0000) != 0x0) {
            i += 16;
            x >>= 16;
        }
        if ((x & 0xFF00) != 0x0) {
            i += 8;
            x >>= 8;
        }
        if ((x & 0xF0) != 0x0) {
            i += 4;
            x >>= 4;
        }
        if ((x & 0x6) != 0x0) {
            i += 2;
            x >>= 2;
        }
        if ((x & 0x2) != 0x0) {
            ++i;
        }
        return i;
    }
    
    public static IntBuffer fillIntBuffer(final int size, final int value) {
        final int[] aint = getIntArray(size);
        final IntBuffer intbuffer = getIntBuffer(size);
        Arrays.fill(ShadersTex.intArray, 0, size, value);
        ShadersTex.intBuffer.put(ShadersTex.intArray, 0, size);
        return ShadersTex.intBuffer;
    }
    
    public static int[] createAIntImage(final int size) {
        final int[] aint = new int[size * 3];
        Arrays.fill(aint, 0, size, 0);
        Arrays.fill(aint, size, size * 2, -8421377);
        Arrays.fill(aint, size * 2, size * 3, 0);
        return aint;
    }
    
    public static int[] createAIntImage(final int size, final int color) {
        final int[] aint = new int[size * 3];
        Arrays.fill(aint, 0, size, color);
        Arrays.fill(aint, size, size * 2, -8421377);
        Arrays.fill(aint, size * 2, size * 3, 0);
        return aint;
    }
    
    public static MultiTexID getMultiTexID(final AbstractTexture tex) {
        MultiTexID multitexid = tex.multiTex;
        if (multitexid == null) {
            final int i = tex.getGlTextureId();
            multitexid = ShadersTex.multiTexMap.get(i);
            if (multitexid == null) {
                multitexid = new MultiTexID(i, GL11.glGenTextures(), GL11.glGenTextures());
                ShadersTex.multiTexMap.put(i, multitexid);
            }
            tex.multiTex = multitexid;
        }
        return multitexid;
    }
    
    public static void deleteTextures(final AbstractTexture atex, final int texid) {
        final MultiTexID multitexid = atex.multiTex;
        if (multitexid != null) {
            atex.multiTex = null;
            ShadersTex.multiTexMap.remove(multitexid.base);
            GlStateManager.deleteTexture(multitexid.norm);
            GlStateManager.deleteTexture(multitexid.spec);
            if (multitexid.base != texid) {
                SMCLog.warning("Error : MultiTexID.base mismatch: " + multitexid.base + ", texid: " + texid);
                GlStateManager.deleteTexture(multitexid.base);
            }
        }
    }
    
    public static void bindNSTextures(final int normTex, final int specTex) {
        if (Shaders.isRenderingWorld && GlStateManager.getActiveTextureUnit() == 33984) {
            GlStateManager.setActiveTexture(33986);
            GlStateManager.bindTexture(normTex);
            GlStateManager.setActiveTexture(33987);
            GlStateManager.bindTexture(specTex);
            GlStateManager.setActiveTexture(33984);
        }
    }
    
    public static void bindNSTextures(final MultiTexID multiTex) {
        bindNSTextures(multiTex.norm, multiTex.spec);
    }
    
    public static void bindTextures(final int baseTex, final int normTex, final int specTex) {
        if (Shaders.isRenderingWorld && GlStateManager.getActiveTextureUnit() == 33984) {
            GlStateManager.setActiveTexture(33986);
            GlStateManager.bindTexture(normTex);
            GlStateManager.setActiveTexture(33987);
            GlStateManager.bindTexture(specTex);
            GlStateManager.setActiveTexture(33984);
        }
        GlStateManager.bindTexture(baseTex);
    }
    
    public static void bindTextures(final MultiTexID multiTex) {
        ShadersTex.boundTex = multiTex;
        if (Shaders.isRenderingWorld && GlStateManager.getActiveTextureUnit() == 33984) {
            if (Shaders.configNormalMap) {
                GlStateManager.setActiveTexture(33986);
                GlStateManager.bindTexture(multiTex.norm);
            }
            if (Shaders.configSpecularMap) {
                GlStateManager.setActiveTexture(33987);
                GlStateManager.bindTexture(multiTex.spec);
            }
            GlStateManager.setActiveTexture(33984);
        }
        GlStateManager.bindTexture(multiTex.base);
    }
    
    public static void bindTexture(final ITextureObject tex) {
        final int i = tex.getGlTextureId();
        if (tex instanceof TextureMap) {
            Shaders.atlasSizeX = ((TextureMap)tex).atlasWidth;
            Shaders.atlasSizeY = ((TextureMap)tex).atlasHeight;
            bindTextures(tex.getMultiTexID());
        }
        else {
            Shaders.atlasSizeX = 0;
            Shaders.atlasSizeY = 0;
            bindTextures(tex.getMultiTexID());
        }
    }
    
    public static void bindTextureMapForUpdateAndRender(final TextureManager tm, final ResourceLocation resLoc) {
        final TextureMap texturemap = (TextureMap)tm.getTexture(resLoc);
        Shaders.atlasSizeX = texturemap.atlasWidth;
        Shaders.atlasSizeY = texturemap.atlasHeight;
        bindTextures(ShadersTex.updatingTex = texturemap.getMultiTexID());
    }
    
    public static void bindTextures(final int baseTex) {
        final MultiTexID multitexid = ShadersTex.multiTexMap.get(baseTex);
        bindTextures(multitexid);
    }
    
    public static void initDynamicTexture(final int texID, final int width, final int height, final DynamicTexture tex) {
        final MultiTexID multitexid = tex.getMultiTexID();
        final int[] aint = tex.getTextureData();
        final int i = width * height;
        Arrays.fill(aint, i, i * 2, -8421377);
        Arrays.fill(aint, i * 2, i * 3, 0);
        TextureUtil.allocateTexture(multitexid.base, width, height);
        TextureUtil.setTextureBlurMipmap(false, false);
        TextureUtil.setTextureClamped(false);
        TextureUtil.allocateTexture(multitexid.norm, width, height);
        TextureUtil.setTextureBlurMipmap(false, false);
        TextureUtil.setTextureClamped(false);
        TextureUtil.allocateTexture(multitexid.spec, width, height);
        TextureUtil.setTextureBlurMipmap(false, false);
        TextureUtil.setTextureClamped(false);
        GlStateManager.bindTexture(multitexid.base);
    }
    
    public static void updateDynamicTexture(final int texID, final int[] src, final int width, final int height, final DynamicTexture tex) {
        final MultiTexID multitexid = tex.getMultiTexID();
        GlStateManager.bindTexture(multitexid.base);
        updateDynTexSubImage1(src, width, height, 0, 0, 0);
        GlStateManager.bindTexture(multitexid.norm);
        updateDynTexSubImage1(src, width, height, 0, 0, 1);
        GlStateManager.bindTexture(multitexid.spec);
        updateDynTexSubImage1(src, width, height, 0, 0, 2);
        GlStateManager.bindTexture(multitexid.base);
    }
    
    public static void updateDynTexSubImage1(final int[] src, final int width, final int height, final int posX, final int posY, final int page) {
        final int i = width * height;
        final IntBuffer intbuffer = getIntBuffer(i);
        intbuffer.clear();
        final int j = page * i;
        if (src.length >= j + i) {
            intbuffer.put(src, j, i).position(0).limit(i);
            GL11.glTexSubImage2D(3553, 0, posX, posY, width, height, 32993, 33639, intbuffer);
            intbuffer.clear();
        }
    }
    
    public static ITextureObject createDefaultTexture() {
        final DynamicTexture dynamictexture = new DynamicTexture(1, 1);
        dynamictexture.getTextureData()[0] = -1;
        dynamictexture.updateDynamicTexture();
        return dynamictexture;
    }
    
    public static void allocateTextureMap(final int texID, final int mipmapLevels, final int width, final int height, final Stitcher stitcher, final TextureMap tex) {
        SMCLog.info("allocateTextureMap " + mipmapLevels + " " + width + " " + height + " ");
        ShadersTex.updatingTextureMap = tex;
        tex.atlasWidth = width;
        tex.atlasHeight = height;
        final MultiTexID multitexid = ShadersTex.updatingTex = getMultiTexID(tex);
        TextureUtil.allocateTextureImpl(multitexid.base, mipmapLevels, width, height);
        if (Shaders.configNormalMap) {
            TextureUtil.allocateTextureImpl(multitexid.norm, mipmapLevels, width, height);
        }
        if (Shaders.configSpecularMap) {
            TextureUtil.allocateTextureImpl(multitexid.spec, mipmapLevels, width, height);
        }
        GlStateManager.bindTexture(texID);
    }
    
    public static TextureAtlasSprite setSprite(final TextureAtlasSprite tas) {
        return ShadersTex.updatingSprite = tas;
    }
    
    public static String setIconName(final String name) {
        return ShadersTex.iconName = name;
    }
    
    public static void uploadTexSubForLoadAtlas(final int[][] data, final int width, final int height, final int xoffset, final int yoffset, final boolean linear, final boolean clamp) {
        TextureUtil.uploadTextureMipmap(data, width, height, xoffset, yoffset, linear, clamp);
        final boolean flag = false;
        if (Shaders.configNormalMap) {
            final int[][] aint = readImageAndMipmaps(String.valueOf(ShadersTex.iconName) + "_n", width, height, data.length, flag, -8421377);
            GlStateManager.bindTexture(ShadersTex.updatingTex.norm);
            TextureUtil.uploadTextureMipmap(aint, width, height, xoffset, yoffset, linear, clamp);
        }
        if (Shaders.configSpecularMap) {
            final int[][] aint2 = readImageAndMipmaps(String.valueOf(ShadersTex.iconName) + "_s", width, height, data.length, flag, 0);
            GlStateManager.bindTexture(ShadersTex.updatingTex.spec);
            TextureUtil.uploadTextureMipmap(aint2, width, height, xoffset, yoffset, linear, clamp);
        }
        GlStateManager.bindTexture(ShadersTex.updatingTex.base);
    }
    
    public static int[][] readImageAndMipmaps(final String name, final int width, final int height, final int numLevels, final boolean border, final int defColor) {
        int[][] aint = new int[numLevels][];
        final int[] aint2 = aint[0] = new int[width * height];
        boolean flag = false;
        final BufferedImage bufferedimage = readImage(ShadersTex.updatingTextureMap.completeResourceLocation(new ResourceLocation(name), 0));
        if (bufferedimage != null) {
            final int i = bufferedimage.getWidth();
            final int j = bufferedimage.getHeight();
            if (i + (border ? 16 : 0) == width) {
                flag = true;
                bufferedimage.getRGB(0, 0, i, i, aint2, 0, i);
            }
        }
        if (!flag) {
            Arrays.fill(aint2, defColor);
        }
        GlStateManager.bindTexture(ShadersTex.updatingTex.spec);
        aint = genMipmapsSimple(aint.length - 1, width, aint);
        return aint;
    }
    
    public static BufferedImage readImage(final ResourceLocation resLoc) {
        try {
            if (!Config.hasResource(resLoc)) {
                return null;
            }
            final InputStream inputstream = Config.getResourceStream(resLoc);
            if (inputstream == null) {
                return null;
            }
            final BufferedImage bufferedimage = ImageIO.read(inputstream);
            inputstream.close();
            return bufferedimage;
        }
        catch (IOException var3) {
            return null;
        }
    }
    
    public static int[][] genMipmapsSimple(final int maxLevel, final int width, final int[][] data) {
        for (int i = 1; i <= maxLevel; ++i) {
            if (data[i] == null) {
                final int j = width >> i;
                final int k = j * 2;
                final int[] aint = data[i - 1];
                final int n = i;
                final int[] array = new int[j * j];
                data[n] = array;
                final int[] aint2 = array;
                for (int i2 = 0; i2 < j; ++i2) {
                    for (int l = 0; l < j; ++l) {
                        final int j2 = i2 * 2 * k + l * 2;
                        aint2[i2 * j + l] = blend4Simple(aint[j2], aint[j2 + 1], aint[j2 + k], aint[j2 + k + 1]);
                    }
                }
            }
        }
        return data;
    }
    
    public static void uploadTexSub(final int[][] data, final int width, final int height, final int xoffset, final int yoffset, final boolean linear, final boolean clamp) {
        TextureUtil.uploadTextureMipmap(data, width, height, xoffset, yoffset, linear, clamp);
        if (Shaders.configNormalMap || Shaders.configSpecularMap) {
            if (Shaders.configNormalMap) {
                GlStateManager.bindTexture(ShadersTex.updatingTex.norm);
                uploadTexSub1(data, width, height, xoffset, yoffset, 1);
            }
            if (Shaders.configSpecularMap) {
                GlStateManager.bindTexture(ShadersTex.updatingTex.spec);
                uploadTexSub1(data, width, height, xoffset, yoffset, 2);
            }
            GlStateManager.bindTexture(ShadersTex.updatingTex.base);
        }
    }
    
    public static void uploadTexSub1(final int[][] src, final int width, final int height, final int posX, final int posY, final int page) {
        final int i = width * height;
        final IntBuffer intbuffer = getIntBuffer(i);
        for (int j = src.length, k = 0, l = width, i2 = height, j2 = posX, k2 = posY; l > 0 && i2 > 0 && k < j; l >>= 1, i2 >>= 1, j2 >>= 1, k2 >>= 1, ++k) {
            final int l2 = l * i2;
            final int[] aint = src[k];
            intbuffer.clear();
            if (aint.length >= l2 * (page + 1)) {
                intbuffer.put(aint, l2 * page, l2).position(0).limit(l2);
                GL11.glTexSubImage2D(3553, k, j2, k2, l, i2, 32993, 33639, intbuffer);
            }
        }
        intbuffer.clear();
    }
    
    public static int blend4Alpha(final int c0, final int c1, final int c2, final int c3) {
        int i = c0 >>> 24 & 0xFF;
        int j = c1 >>> 24 & 0xFF;
        int k = c2 >>> 24 & 0xFF;
        int l = c3 >>> 24 & 0xFF;
        final int i2 = i + j + k + l;
        final int j2 = (i2 + 2) / 4;
        int k2;
        if (i2 != 0) {
            k2 = i2;
        }
        else {
            k2 = 4;
            i = 1;
            j = 1;
            k = 1;
            l = 1;
        }
        final int l2 = (k2 + 1) / 2;
        final int i3 = j2 << 24 | ((c0 >>> 16 & 0xFF) * i + (c1 >>> 16 & 0xFF) * j + (c2 >>> 16 & 0xFF) * k + (c3 >>> 16 & 0xFF) * l + l2) / k2 << 16 | ((c0 >>> 8 & 0xFF) * i + (c1 >>> 8 & 0xFF) * j + (c2 >>> 8 & 0xFF) * k + (c3 >>> 8 & 0xFF) * l + l2) / k2 << 8 | ((c0 >>> 0 & 0xFF) * i + (c1 >>> 0 & 0xFF) * j + (c2 >>> 0 & 0xFF) * k + (c3 >>> 0 & 0xFF) * l + l2) / k2 << 0;
        return i3;
    }
    
    public static int blend4Simple(final int c0, final int c1, final int c2, final int c3) {
        final int i = ((c0 >>> 24 & 0xFF) + (c1 >>> 24 & 0xFF) + (c2 >>> 24 & 0xFF) + (c3 >>> 24 & 0xFF) + 2) / 4 << 24 | ((c0 >>> 16 & 0xFF) + (c1 >>> 16 & 0xFF) + (c2 >>> 16 & 0xFF) + (c3 >>> 16 & 0xFF) + 2) / 4 << 16 | ((c0 >>> 8 & 0xFF) + (c1 >>> 8 & 0xFF) + (c2 >>> 8 & 0xFF) + (c3 >>> 8 & 0xFF) + 2) / 4 << 8 | ((c0 >>> 0 & 0xFF) + (c1 >>> 0 & 0xFF) + (c2 >>> 0 & 0xFF) + (c3 >>> 0 & 0xFF) + 2) / 4 << 0;
        return i;
    }
    
    public static void genMipmapAlpha(final int[] aint, final int offset, final int width, final int height) {
        Math.min(width, height);
        int o2 = offset;
        int w2 = width;
        int h2 = height;
        int o3 = 0;
        int w3 = 0;
        int h3 = 0;
        int i = 0;
        while (w2 > 1) {
            if (h2 <= 1) {
                break;
            }
            o3 = o2 + w2 * h2;
            w3 = w2 / 2;
            h3 = h2 / 2;
            for (int l1 = 0; l1 < h3; ++l1) {
                final int i2 = o3 + l1 * w3;
                final int j2 = o2 + l1 * 2 * w2;
                for (int k2 = 0; k2 < w3; ++k2) {
                    aint[i2 + k2] = blend4Alpha(aint[j2 + k2 * 2], aint[j2 + k2 * 2 + 1], aint[j2 + w2 + k2 * 2], aint[j2 + w2 + k2 * 2 + 1]);
                }
            }
            ++i;
            w2 = w3;
            h2 = h3;
            o2 = o3;
        }
        while (i > 0) {
            --i;
            w2 = width >> i;
            h2 = height >> i;
            int l2;
            o2 = (l2 = o3 - w2 * h2);
            for (int i3 = 0; i3 < h2; ++i3) {
                for (int j3 = 0; j3 < w2; ++j3) {
                    if (aint[l2] == 0) {
                        aint[l2] = (aint[o3 + i3 / 2 * w3 + j3 / 2] & 0xFFFFFF);
                    }
                    ++l2;
                }
            }
            o3 = o2;
            w3 = w2;
        }
    }
    
    public static void genMipmapSimple(final int[] aint, final int offset, final int width, final int height) {
        Math.min(width, height);
        int o2 = offset;
        int w2 = width;
        int h2 = height;
        int o3 = 0;
        int w3 = 0;
        int h3 = 0;
        int i = 0;
        while (w2 > 1) {
            if (h2 <= 1) {
                break;
            }
            o3 = o2 + w2 * h2;
            w3 = w2 / 2;
            h3 = h2 / 2;
            for (int l1 = 0; l1 < h3; ++l1) {
                final int i2 = o3 + l1 * w3;
                final int j2 = o2 + l1 * 2 * w2;
                for (int k2 = 0; k2 < w3; ++k2) {
                    aint[i2 + k2] = blend4Simple(aint[j2 + k2 * 2], aint[j2 + k2 * 2 + 1], aint[j2 + w2 + k2 * 2], aint[j2 + w2 + k2 * 2 + 1]);
                }
            }
            ++i;
            w2 = w3;
            h2 = h3;
            o2 = o3;
        }
        while (i > 0) {
            --i;
            w2 = width >> i;
            h2 = height >> i;
            int l2;
            o2 = (l2 = o3 - w2 * h2);
            for (int i3 = 0; i3 < h2; ++i3) {
                for (int j3 = 0; j3 < w2; ++j3) {
                    if (aint[l2] == 0) {
                        aint[l2] = (aint[o3 + i3 / 2 * w3 + j3 / 2] & 0xFFFFFF);
                    }
                    ++l2;
                }
            }
            o3 = o2;
            w3 = w2;
        }
    }
    
    public static boolean isSemiTransparent(final int[] aint, final int width, final int height) {
        final int i = width * height;
        if (aint[0] >>> 24 == 255 && aint[i - 1] == 0) {
            return true;
        }
        for (int j = 0; j < i; ++j) {
            final int k = aint[j] >>> 24;
            if (k != 0 && k != 255) {
                return true;
            }
        }
        return false;
    }
    
    public static void updateSubTex1(final int[] src, final int width, final int height, final int posX, final int posY) {
        int i = 0;
        for (int j = width, k = height, l = posX, i2 = posY; j > 0 && k > 0; j /= 2, k /= 2, l /= 2, i2 /= 2) {
            GL11.glCopyTexSubImage2D(3553, i, l, i2, 0, 0, j, k);
            ++i;
        }
    }
    
    public static void setupTexture(final MultiTexID multiTex, final int[] src, final int width, final int height, final boolean linear, final boolean clamp) {
        final int i = linear ? 9729 : 9728;
        final int j = clamp ? 10496 : 10497;
        final int k = width * height;
        final IntBuffer intbuffer = getIntBuffer(k);
        intbuffer.clear();
        intbuffer.put(src, 0, k).position(0).limit(k);
        GlStateManager.bindTexture(multiTex.base);
        GL11.glTexImage2D(3553, 0, 6408, width, height, 0, 32993, 33639, intbuffer);
        GL11.glTexParameteri(3553, 10241, i);
        GL11.glTexParameteri(3553, 10240, i);
        GL11.glTexParameteri(3553, 10242, j);
        GL11.glTexParameteri(3553, 10243, j);
        intbuffer.put(src, k, k).position(0).limit(k);
        GlStateManager.bindTexture(multiTex.norm);
        GL11.glTexImage2D(3553, 0, 6408, width, height, 0, 32993, 33639, intbuffer);
        GL11.glTexParameteri(3553, 10241, i);
        GL11.glTexParameteri(3553, 10240, i);
        GL11.glTexParameteri(3553, 10242, j);
        GL11.glTexParameteri(3553, 10243, j);
        intbuffer.put(src, k * 2, k).position(0).limit(k);
        GlStateManager.bindTexture(multiTex.spec);
        GL11.glTexImage2D(3553, 0, 6408, width, height, 0, 32993, 33639, intbuffer);
        GL11.glTexParameteri(3553, 10241, i);
        GL11.glTexParameteri(3553, 10240, i);
        GL11.glTexParameteri(3553, 10242, j);
        GL11.glTexParameteri(3553, 10243, j);
        GlStateManager.bindTexture(multiTex.base);
    }
    
    public static void updateSubImage(final MultiTexID multiTex, final int[] src, final int width, final int height, final int posX, final int posY, final boolean linear, final boolean clamp) {
        final int i = width * height;
        final IntBuffer intbuffer = getIntBuffer(i);
        intbuffer.clear();
        intbuffer.put(src, 0, i);
        intbuffer.position(0).limit(i);
        GlStateManager.bindTexture(multiTex.base);
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10242, 10497);
        GL11.glTexParameteri(3553, 10243, 10497);
        GL11.glTexSubImage2D(3553, 0, posX, posY, width, height, 32993, 33639, intbuffer);
        if (src.length == i * 3) {
            intbuffer.clear();
            intbuffer.put(src, i, i).position(0);
            intbuffer.position(0).limit(i);
        }
        GlStateManager.bindTexture(multiTex.norm);
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10242, 10497);
        GL11.glTexParameteri(3553, 10243, 10497);
        GL11.glTexSubImage2D(3553, 0, posX, posY, width, height, 32993, 33639, intbuffer);
        if (src.length == i * 3) {
            intbuffer.clear();
            intbuffer.put(src, i * 2, i);
            intbuffer.position(0).limit(i);
        }
        GlStateManager.bindTexture(multiTex.spec);
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10242, 10497);
        GL11.glTexParameteri(3553, 10243, 10497);
        GL11.glTexSubImage2D(3553, 0, posX, posY, width, height, 32993, 33639, intbuffer);
        GlStateManager.setActiveTexture(33984);
    }
    
    public static ResourceLocation getNSMapLocation(final ResourceLocation location, final String mapName) {
        final String s = location.getResourcePath();
        final String[] astring = s.split(".png");
        final String s2 = astring[0];
        return new ResourceLocation(location.getResourceDomain(), String.valueOf(s2) + "_" + mapName + ".png");
    }
    
    public static void loadNSMap(final IResourceManager manager, final ResourceLocation location, final int width, final int height, final int[] aint) {
        if (Shaders.configNormalMap) {
            loadNSMap1(manager, getNSMapLocation(location, "n"), width, height, aint, width * height, -8421377);
        }
        if (Shaders.configSpecularMap) {
            loadNSMap1(manager, getNSMapLocation(location, "s"), width, height, aint, width * height * 2, 0);
        }
    }
    
    public static void loadNSMap1(final IResourceManager manager, final ResourceLocation location, final int width, final int height, final int[] aint, final int offset, final int defaultColor) {
        boolean flag = false;
        try {
            final IResource iresource = manager.getResource(location);
            final BufferedImage bufferedimage = ImageIO.read(iresource.getInputStream());
            if (bufferedimage != null && bufferedimage.getWidth() == width && bufferedimage.getHeight() == height) {
                bufferedimage.getRGB(0, 0, width, height, aint, offset, width);
                flag = true;
            }
        }
        catch (IOException ex) {}
        if (!flag) {
            Arrays.fill(aint, offset, offset + width * height, defaultColor);
        }
    }
    
    public static int loadSimpleTexture(final int textureID, final BufferedImage bufferedimage, final boolean linear, final boolean clamp, final IResourceManager resourceManager, final ResourceLocation location, final MultiTexID multiTex) {
        final int i = bufferedimage.getWidth();
        final int j = bufferedimage.getHeight();
        final int k = i * j;
        final int[] aint = getIntArray(k * 3);
        bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
        loadNSMap(resourceManager, location, i, j, aint);
        setupTexture(multiTex, aint, i, j, linear, clamp);
        return textureID;
    }
    
    public static void mergeImage(final int[] aint, final int dstoff, final int srcoff, final int size) {
    }
    
    public static int blendColor(final int color1, final int color2, final int factor1) {
        final int i = 255 - factor1;
        return ((color1 >>> 24 & 0xFF) * factor1 + (color2 >>> 24 & 0xFF) * i) / 255 << 24 | ((color1 >>> 16 & 0xFF) * factor1 + (color2 >>> 16 & 0xFF) * i) / 255 << 16 | ((color1 >>> 8 & 0xFF) * factor1 + (color2 >>> 8 & 0xFF) * i) / 255 << 8 | ((color1 >>> 0 & 0xFF) * factor1 + (color2 >>> 0 & 0xFF) * i) / 255 << 0;
    }
    
    public static void loadLayeredTexture(final LayeredTexture tex, final IResourceManager manager, final List list) {
        int i = 0;
        int j = 0;
        int k = 0;
        int[] aint = null;
        for (final Object s : list) {
            if (s != null) {
                try {
                    final ResourceLocation resourcelocation = new ResourceLocation((String)s);
                    final InputStream inputstream = manager.getResource(resourcelocation).getInputStream();
                    final BufferedImage bufferedimage = ImageIO.read(inputstream);
                    if (k == 0) {
                        i = bufferedimage.getWidth();
                        j = bufferedimage.getHeight();
                        k = i * j;
                        aint = createAIntImage(k, 0);
                    }
                    final int[] aint2 = getIntArray(k * 3);
                    bufferedimage.getRGB(0, 0, i, j, aint2, 0, i);
                    loadNSMap(manager, resourcelocation, i, j, aint2);
                    for (int l = 0; l < k; ++l) {
                        final int i2 = aint2[l] >>> 24 & 0xFF;
                        aint[k * 0 + l] = blendColor(aint2[k * 0 + l], aint[k * 0 + l], i2);
                        aint[k * 1 + l] = blendColor(aint2[k * 1 + l], aint[k * 1 + l], i2);
                        aint[k * 2 + l] = blendColor(aint2[k * 2 + l], aint[k * 2 + l], i2);
                    }
                }
                catch (IOException ioexception) {
                    ioexception.printStackTrace();
                }
            }
        }
        setupTexture(tex.getMultiTexID(), aint, i, j, false, false);
    }
    
    static void updateTextureMinMagFilter() {
        final TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        final ITextureObject itextureobject = texturemanager.getTexture(TextureMap.locationBlocksTexture);
        if (itextureobject != null) {
            final MultiTexID multitexid = itextureobject.getMultiTexID();
            GlStateManager.bindTexture(multitexid.base);
            GL11.glTexParameteri(3553, 10241, Shaders.texMinFilValue[Shaders.configTexMinFilB]);
            GL11.glTexParameteri(3553, 10240, Shaders.texMagFilValue[Shaders.configTexMagFilB]);
            GlStateManager.bindTexture(multitexid.norm);
            GL11.glTexParameteri(3553, 10241, Shaders.texMinFilValue[Shaders.configTexMinFilN]);
            GL11.glTexParameteri(3553, 10240, Shaders.texMagFilValue[Shaders.configTexMagFilN]);
            GlStateManager.bindTexture(multitexid.spec);
            GL11.glTexParameteri(3553, 10241, Shaders.texMinFilValue[Shaders.configTexMinFilS]);
            GL11.glTexParameteri(3553, 10240, Shaders.texMagFilValue[Shaders.configTexMagFilS]);
            GlStateManager.bindTexture(0);
        }
    }
    
    public static IResource loadResource(final IResourceManager manager, final ResourceLocation location) throws IOException {
        ShadersTex.resManager = manager;
        ShadersTex.resLocation = location;
        return manager.getResource(location);
    }
    
    public static int[] loadAtlasSprite(final BufferedImage bufferedimage, final int startX, final int startY, final int w, final int h, final int[] aint, final int offset, final int scansize) {
        ShadersTex.imageSize = w * h;
        bufferedimage.getRGB(startX, startY, w, h, aint, offset, scansize);
        loadNSMap(ShadersTex.resManager, ShadersTex.resLocation, w, h, aint);
        return aint;
    }
    
    public static int[][] getFrameTexData(final int[][] src, final int width, final int height, final int frameIndex) {
        final int i = src.length;
        final int[][] aint = new int[i][];
        for (int j = 0; j < i; ++j) {
            final int[] aint2 = src[j];
            if (aint2 != null) {
                final int k = (width >> j) * (height >> j);
                final int[] aint3 = new int[k * 3];
                aint[j] = aint3;
                final int l = aint2.length / 3;
                int i2 = k * frameIndex;
                int j2 = 0;
                System.arraycopy(aint2, i2, aint3, j2, k);
                i2 += l;
                j2 += k;
                System.arraycopy(aint2, i2, aint3, j2, k);
                i2 += l;
                j2 += k;
                System.arraycopy(aint2, i2, aint3, j2, k);
            }
        }
        return aint;
    }
    
    public static int[][] prepareAF(final TextureAtlasSprite tas, final int[][] src, final int width, final int height) {
        final boolean flag = true;
        return src;
    }
    
    public static void fixTransparentColor(final TextureAtlasSprite tas, final int[] aint) {
    }
}
