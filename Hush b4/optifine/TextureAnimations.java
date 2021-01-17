// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import net.minecraft.client.settings.GameSettings;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import net.minecraft.client.resources.IResourcePack;

public class TextureAnimations
{
    private static TextureAnimation[] textureAnimations;
    
    static {
        TextureAnimations.textureAnimations = null;
    }
    
    public static void reset() {
        TextureAnimations.textureAnimations = null;
    }
    
    public static void update() {
        TextureAnimations.textureAnimations = null;
        final IResourcePack[] airesourcepack = Config.getResourcePacks();
        TextureAnimations.textureAnimations = getTextureAnimations(airesourcepack);
        if (Config.isAnimatedTextures()) {
            updateAnimations();
        }
    }
    
    public static void updateCustomAnimations() {
        if (TextureAnimations.textureAnimations != null && Config.isAnimatedTextures()) {
            updateAnimations();
        }
    }
    
    public static void updateAnimations() {
        if (TextureAnimations.textureAnimations != null) {
            for (int i = 0; i < TextureAnimations.textureAnimations.length; ++i) {
                final TextureAnimation textureanimation = TextureAnimations.textureAnimations[i];
                textureanimation.updateTexture();
            }
        }
    }
    
    public static TextureAnimation[] getTextureAnimations(final IResourcePack[] p_getTextureAnimations_0_) {
        final List list = new ArrayList();
        for (int i = 0; i < p_getTextureAnimations_0_.length; ++i) {
            final IResourcePack iresourcepack = p_getTextureAnimations_0_[i];
            final TextureAnimation[] atextureanimation = getTextureAnimations(iresourcepack);
            if (atextureanimation != null) {
                list.addAll(Arrays.asList(atextureanimation));
            }
        }
        final TextureAnimation[] atextureanimation2 = list.toArray(new TextureAnimation[list.size()]);
        return atextureanimation2;
    }
    
    public static TextureAnimation[] getTextureAnimations(final IResourcePack p_getTextureAnimations_0_) {
        final String[] astring = ResUtils.collectFiles(p_getTextureAnimations_0_, "mcpatcher/anim", ".properties", null);
        if (astring.length <= 0) {
            return null;
        }
        final List list = new ArrayList();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            Config.dbg("Texture animation: " + s);
            try {
                final ResourceLocation resourcelocation = new ResourceLocation(s);
                final InputStream inputstream = p_getTextureAnimations_0_.getInputStream(resourcelocation);
                final Properties properties = new Properties();
                properties.load(inputstream);
                final TextureAnimation textureanimation = makeTextureAnimation(properties, resourcelocation);
                if (textureanimation != null) {
                    final ResourceLocation resourcelocation2 = new ResourceLocation(textureanimation.getDstTex());
                    if (Config.getDefiningResourcePack(resourcelocation2) != p_getTextureAnimations_0_) {
                        Config.dbg("Skipped: " + s + ", target texture not loaded from same resource pack");
                    }
                    else {
                        list.add(textureanimation);
                    }
                }
            }
            catch (FileNotFoundException filenotfoundexception) {
                Config.warn("File not found: " + filenotfoundexception.getMessage());
            }
            catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
        final TextureAnimation[] atextureanimation = list.toArray(new TextureAnimation[list.size()]);
        return atextureanimation;
    }
    
    public static TextureAnimation makeTextureAnimation(final Properties p_makeTextureAnimation_0_, final ResourceLocation p_makeTextureAnimation_1_) {
        String s = p_makeTextureAnimation_0_.getProperty("from");
        String s2 = p_makeTextureAnimation_0_.getProperty("to");
        final int i = Config.parseInt(p_makeTextureAnimation_0_.getProperty("x"), -1);
        final int j = Config.parseInt(p_makeTextureAnimation_0_.getProperty("y"), -1);
        final int k = Config.parseInt(p_makeTextureAnimation_0_.getProperty("w"), -1);
        final int l = Config.parseInt(p_makeTextureAnimation_0_.getProperty("h"), -1);
        if (s == null || s2 == null) {
            Config.warn("TextureAnimation: Source or target texture not specified");
            return null;
        }
        if (i < 0 || j < 0 || k < 0 || l < 0) {
            Config.warn("TextureAnimation: Invalid coordinates");
            return null;
        }
        s = s.trim();
        s2 = s2.trim();
        final String s3 = TextureUtils.getBasePath(p_makeTextureAnimation_1_.getResourcePath());
        s = TextureUtils.fixResourcePath(s, s3);
        s2 = TextureUtils.fixResourcePath(s2, s3);
        final byte[] abyte = getCustomTextureData(s, k);
        if (abyte == null) {
            Config.warn("TextureAnimation: Source texture not found: " + s2);
            return null;
        }
        final ResourceLocation resourcelocation = new ResourceLocation(s2);
        if (!Config.hasResource(resourcelocation)) {
            Config.warn("TextureAnimation: Target texture not found: " + s2);
            return null;
        }
        final TextureAnimation textureanimation = new TextureAnimation(s, abyte, s2, resourcelocation, i, j, k, l, p_makeTextureAnimation_0_, 1);
        return textureanimation;
    }
    
    public static byte[] getCustomTextureData(final String p_getCustomTextureData_0_, final int p_getCustomTextureData_1_) {
        byte[] abyte = loadImage(p_getCustomTextureData_0_, p_getCustomTextureData_1_);
        if (abyte == null) {
            abyte = loadImage("/anim" + p_getCustomTextureData_0_, p_getCustomTextureData_1_);
        }
        return abyte;
    }
    
    private static byte[] loadImage(final String p_loadImage_0_, final int p_loadImage_1_) {
        final GameSettings gamesettings = Config.getGameSettings();
        try {
            final ResourceLocation resourcelocation = new ResourceLocation(p_loadImage_0_);
            final InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return null;
            }
            BufferedImage bufferedimage = readTextureImage(inputstream);
            inputstream.close();
            if (bufferedimage == null) {
                return null;
            }
            if (p_loadImage_1_ > 0 && bufferedimage.getWidth() != p_loadImage_1_) {
                final double d0 = bufferedimage.getHeight() / bufferedimage.getWidth();
                final int j = (int)(p_loadImage_1_ * d0);
                bufferedimage = scaleBufferedImage(bufferedimage, p_loadImage_1_, j);
            }
            final int k2 = bufferedimage.getWidth();
            final int i = bufferedimage.getHeight();
            final int[] aint = new int[k2 * i];
            final byte[] abyte = new byte[k2 * i * 4];
            bufferedimage.getRGB(0, 0, k2, i, aint, 0, k2);
            for (int l = 0; l < aint.length; ++l) {
                final int m = aint[l] >> 24 & 0xFF;
                int i2 = aint[l] >> 16 & 0xFF;
                int j2 = aint[l] >> 8 & 0xFF;
                int k3 = aint[l] & 0xFF;
                if (gamesettings != null && gamesettings.anaglyph) {
                    final int l2 = (i2 * 30 + j2 * 59 + k3 * 11) / 100;
                    final int i3 = (i2 * 30 + j2 * 70) / 100;
                    final int j3 = (i2 * 30 + k3 * 70) / 100;
                    i2 = l2;
                    j2 = i3;
                    k3 = j3;
                }
                abyte[l * 4 + 0] = (byte)i2;
                abyte[l * 4 + 1] = (byte)j2;
                abyte[l * 4 + 2] = (byte)k3;
                abyte[l * 4 + 3] = (byte)m;
            }
            return abyte;
        }
        catch (FileNotFoundException var18) {
            return null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
    
    private static BufferedImage readTextureImage(final InputStream p_readTextureImage_0_) throws IOException {
        final BufferedImage bufferedimage = ImageIO.read(p_readTextureImage_0_);
        p_readTextureImage_0_.close();
        return bufferedimage;
    }
    
    public static BufferedImage scaleBufferedImage(final BufferedImage p_scaleBufferedImage_0_, final int p_scaleBufferedImage_1_, final int p_scaleBufferedImage_2_) {
        final BufferedImage bufferedimage = new BufferedImage(p_scaleBufferedImage_1_, p_scaleBufferedImage_2_, 2);
        final Graphics2D graphics2d = bufferedimage.createGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2d.drawImage(p_scaleBufferedImage_0_, 0, 0, p_scaleBufferedImage_1_, p_scaleBufferedImage_2_, null);
        return bufferedimage;
    }
}
