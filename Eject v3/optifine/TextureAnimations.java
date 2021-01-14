package optifine;

import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class TextureAnimations {
    private static TextureAnimation[] textureAnimations = null;

    public static void reset() {
        textureAnimations = null;
    }

    public static void update() {
        textureAnimations = null;
        IResourcePack[] arrayOfIResourcePack = Config.getResourcePacks();
        textureAnimations = getTextureAnimations(arrayOfIResourcePack);
        if (Config.isAnimatedTextures()) {
            updateAnimations();
        }
    }

    public static void updateCustomAnimations() {
        if ((textureAnimations != null) && (Config.isAnimatedTextures())) {
            updateAnimations();
        }
    }

    public static void updateAnimations() {
        if (textureAnimations != null) {
            for (int i = 0; i < textureAnimations.length; i++) {
                TextureAnimation localTextureAnimation = textureAnimations[i];
                localTextureAnimation.updateTexture();
            }
        }
    }

    public static TextureAnimation[] getTextureAnimations(IResourcePack[] paramArrayOfIResourcePack) {
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < paramArrayOfIResourcePack.length; i++) {
            IResourcePack localIResourcePack = paramArrayOfIResourcePack[i];
            TextureAnimation[] arrayOfTextureAnimation2 = getTextureAnimations(localIResourcePack);
            if (arrayOfTextureAnimation2 != null) {
                localArrayList.addAll(Arrays.asList(arrayOfTextureAnimation2));
            }
        }
        TextureAnimation[] arrayOfTextureAnimation1 = (TextureAnimation[]) (TextureAnimation[]) localArrayList.toArray(new TextureAnimation[localArrayList.size()]);
        return arrayOfTextureAnimation1;
    }

    public static TextureAnimation[] getTextureAnimations(IResourcePack paramIResourcePack) {
        String[] arrayOfString = ResUtils.collectFiles(paramIResourcePack, "mcpatcher/anim", ".properties", (String[]) null);
        if (arrayOfString.length <= 0) {
            return null;
        }
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < arrayOfString.length; i++) {
            String str = arrayOfString[i];
            Config.dbg("Texture animation: " + str);
            try {
                ResourceLocation localResourceLocation1 = new ResourceLocation(str);
                InputStream localInputStream = paramIResourcePack.getInputStream(localResourceLocation1);
                Properties localProperties = new Properties();
                localProperties.load(localInputStream);
                TextureAnimation localTextureAnimation = makeTextureAnimation(localProperties, localResourceLocation1);
                if (localTextureAnimation != null) {
                    ResourceLocation localResourceLocation2 = new ResourceLocation(localTextureAnimation.getDstTex());
                    if (Config.getDefiningResourcePack(localResourceLocation2) != paramIResourcePack) {
                        Config.dbg("Skipped: " + str + ", target texture not loaded from same resource pack");
                    } else {
                        localArrayList.add(localTextureAnimation);
                    }
                }
            } catch (FileNotFoundException localFileNotFoundException) {
                Config.warn("File not found: " + localFileNotFoundException.getMessage());
            } catch (IOException localIOException) {
                localIOException.printStackTrace();
            }
        }
        TextureAnimation[] arrayOfTextureAnimation = (TextureAnimation[]) (TextureAnimation[]) localArrayList.toArray(new TextureAnimation[localArrayList.size()]);
        return arrayOfTextureAnimation;
    }

    public static TextureAnimation makeTextureAnimation(Properties paramProperties, ResourceLocation paramResourceLocation) {
        String str1 = paramProperties.getProperty("from");
        String str2 = paramProperties.getProperty("to");
        int i = Config.parseInt(paramProperties.getProperty("x"), -1);
        int j = Config.parseInt(paramProperties.getProperty("y"), -1);
        int k = Config.parseInt(paramProperties.getProperty("w"), -1);
        int m = Config.parseInt(paramProperties.getProperty("h"), -1);
        if ((str1 != null) && (str2 != null)) {
            if ((i >= 0) && (j >= 0) && (k >= 0) && (m >= 0)) {
                str1 = str1.trim();
                str2 = str2.trim();
                String str3 = TextureUtils.getBasePath(paramResourceLocation.getResourcePath());
                str1 = TextureUtils.fixResourcePath(str1, str3);
                str2 = TextureUtils.fixResourcePath(str2, str3);
                byte[] arrayOfByte = getCustomTextureData(str1, k);
                if (arrayOfByte == null) {
                    Config.warn("TextureAnimation: Source texture not found: " + str2);
                    return null;
                }
                ResourceLocation localResourceLocation = new ResourceLocation(str2);
                if (!Config.hasResource(localResourceLocation)) {
                    Config.warn("TextureAnimation: Target texture not found: " + str2);
                    return null;
                }
                TextureAnimation localTextureAnimation = new TextureAnimation(str1, arrayOfByte, str2, localResourceLocation, i, j, k, m, paramProperties, 1);
                return localTextureAnimation;
            }
            Config.warn("TextureAnimation: Invalid coordinates");
            return null;
        }
        Config.warn("TextureAnimation: Source or target texture not specified");
        return null;
    }

    public static byte[] getCustomTextureData(String paramString, int paramInt) {
        byte[] arrayOfByte = loadImage(paramString, paramInt);
        if (arrayOfByte == null) {
            arrayOfByte = loadImage("/anim" + paramString, paramInt);
        }
        return arrayOfByte;
    }

    private static byte[] loadImage(String paramString, int paramInt) {
        GameSettings localGameSettings = Config.getGameSettings();
        try {
            ResourceLocation localResourceLocation = new ResourceLocation(paramString);
            InputStream localInputStream = Config.getResourceStream(localResourceLocation);
            if (localInputStream == null) {
                return null;
            }
            BufferedImage localBufferedImage = readTextureImage(localInputStream);
            localInputStream.close();
            if (localBufferedImage == null) {
                return null;
            }
            if ((paramInt > 0) && (localBufferedImage.getWidth() != paramInt)) {
                double d = -localBufferedImage.getWidth();
                int k = (int) (paramInt * d);
                localBufferedImage = scaleBufferedImage(localBufferedImage, paramInt, k);
            }
            int i = localBufferedImage.getWidth();
            int j = localBufferedImage.getHeight();
            int[] arrayOfInt = new int[i * j];
            byte[] arrayOfByte = new byte[i * j * 4];
            localBufferedImage.getRGB(0, 0, i, j, arrayOfInt, 0, i);
            int m = 0;
            int n = (arrayOfInt[m] & 0x18) >> 255;
            int i1 = (arrayOfInt[m] & 0x10) >> 255;
            int i2 = (arrayOfInt[m] & 0x8) >> 255;
            int i3 = arrayOfInt[m] >> 255;
            if ((localGameSettings != null) && (localGameSettings.anaglyph)) {
                int i4 = -100;
                int i5 = -100;
                int i6 = -100;
                i1 = i4;
                i2 = i5;
                i3 = i6;
            }
            arrayOfByte[(m * 4 | 0x0)] = ((byte) i1);
            arrayOfByte[(m * 4 | 0x1)] = ((byte) i2);
            arrayOfByte[(m * 4 | 0x2)] = ((byte) i3);
            arrayOfByte[(m * 4 | 0x3)] = ((byte) n);
            return arrayOfByte;
        } catch (FileNotFoundException localFileNotFoundException) {
            return null;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return null;
    }

    private static BufferedImage readTextureImage(InputStream paramInputStream)
            throws IOException {
        BufferedImage localBufferedImage = ImageIO.read(paramInputStream);
        paramInputStream.close();
        return localBufferedImage;
    }

    public static BufferedImage scaleBufferedImage(BufferedImage paramBufferedImage, int paramInt1, int paramInt2) {
        BufferedImage localBufferedImage = new BufferedImage(paramInt1, paramInt2, 2);
        Graphics2D localGraphics2D = localBufferedImage.createGraphics();
        localGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        localGraphics2D.drawImage(paramBufferedImage, 0, 0, paramInt1, paramInt2, (ImageObserver) null);
        return localBufferedImage;
    }
}




