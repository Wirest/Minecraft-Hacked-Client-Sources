package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import shadersmod.client.MultiTexID;
import shadersmod.client.Shaders;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Iterator;

public class TextureUtils {
    public static final String texGrassTop = "grass_top";
    public static final String texStone = "stone";
    public static final String texDirt = "dirt";
    public static final String texCoarseDirt = "coarse_dirt";
    public static final String texGrassSide = "grass_side";
    public static final String texStoneslabSide = "stone_slab_side";
    public static final String texStoneslabTop = "stone_slab_top";
    public static final String texBedrock = "bedrock";
    public static final String texSand = "sand";
    public static final String texGravel = "gravel";
    public static final String texLogOak = "log_oak";
    public static final String texLogBigOak = "log_big_oak";
    public static final String texLogAcacia = "log_acacia";
    public static final String texLogSpruce = "log_spruce";
    public static final String texLogBirch = "log_birch";
    public static final String texLogJungle = "log_jungle";
    public static final String texLogOakTop = "log_oak_top";
    public static final String texLogBigOakTop = "log_big_oak_top";
    public static final String texLogAcaciaTop = "log_acacia_top";
    public static final String texLogSpruceTop = "log_spruce_top";
    public static final String texLogBirchTop = "log_birch_top";
    public static final String texLogJungleTop = "log_jungle_top";
    public static final String texLeavesOak = "leaves_oak";
    public static final String texLeavesBigOak = "leaves_big_oak";
    public static final String texLeavesAcacia = "leaves_acacia";
    public static final String texLeavesBirch = "leaves_birch";
    public static final String texLeavesSpuce = "leaves_spruce";
    public static final String texLeavesJungle = "leaves_jungle";
    public static final String texGoldOre = "gold_ore";
    public static final String texIronOre = "iron_ore";
    public static final String texCoalOre = "coal_ore";
    public static final String texObsidian = "obsidian";
    public static final String texGrassSideOverlay = "grass_side_overlay";
    public static final String texSnow = "snow";
    public static final String texGrassSideSnowed = "grass_side_snowed";
    public static final String texMyceliumSide = "mycelium_side";
    public static final String texMyceliumTop = "mycelium_top";
    public static final String texDiamondOre = "diamond_ore";
    public static final String texRedstoneOre = "redstone_ore";
    public static final String texLapisOre = "lapis_ore";
    public static final String texCactusSide = "cactus_side";
    public static final String texClay = "clay";
    public static final String texFarmlandWet = "farmland_wet";
    public static final String texFarmlandDry = "farmland_dry";
    public static final String texNetherrack = "netherrack";
    public static final String texSoulSand = "soul_sand";
    public static final String texGlowstone = "glowstone";
    public static final String texLeavesSpruce = "leaves_spruce";
    public static final String texLeavesSpruceOpaque = "leaves_spruce_opaque";
    public static final String texEndStone = "end_stone";
    public static final String texSandstoneTop = "sandstone_top";
    public static final String texSandstoneBottom = "sandstone_bottom";
    public static final String texRedstoneLampOff = "redstone_lamp_off";
    public static final String texRedstoneLampOn = "redstone_lamp_on";
    public static final String texWaterStill = "water_still";
    public static final String texWaterFlow = "water_flow";
    public static final String texLavaStill = "lava_still";
    public static final String texLavaFlow = "lava_flow";
    public static final String texFireLayer0 = "fire_layer_0";
    public static final String texFireLayer1 = "fire_layer_1";
    public static final String texPortal = "portal";
    public static final String texGlass = "glass";
    public static final String texGlassPaneTop = "glass_pane_top";
    public static final String texCompass = "compass";
    public static final String texClock = "clock";
    public static final String SPRITE_PREFIX_BLOCKS = "minecraft:blocks/";
    public static final String SPRITE_PREFIX_ITEMS = "minecraft:items/";
    public static TextureAtlasSprite iconGrassTop;
    public static TextureAtlasSprite iconGrassSide;
    public static TextureAtlasSprite iconGrassSideOverlay;
    public static TextureAtlasSprite iconSnow;
    public static TextureAtlasSprite iconGrassSideSnowed;
    public static TextureAtlasSprite iconMyceliumSide;
    public static TextureAtlasSprite iconMyceliumTop;
    public static TextureAtlasSprite iconWaterStill;
    public static TextureAtlasSprite iconWaterFlow;
    public static TextureAtlasSprite iconLavaStill;
    public static TextureAtlasSprite iconLavaFlow;
    public static TextureAtlasSprite iconPortal;
    public static TextureAtlasSprite iconFireLayer0;
    public static TextureAtlasSprite iconFireLayer1;
    public static TextureAtlasSprite iconGlass;
    public static TextureAtlasSprite iconGlassPaneTop;
    public static TextureAtlasSprite iconCompass;
    public static TextureAtlasSprite iconClock;
    private static IntBuffer staticBuffer = GLAllocation.createDirectIntBuffer(256);

    public static void update() {
        TextureMap localTextureMap = getTextureMapBlocks();
        if (localTextureMap != null) {
            String str1 = "minecraft:blocks/";
            iconGrassTop = localTextureMap.getSpriteSafe(str1 + "grass_top");
            iconGrassSide = localTextureMap.getSpriteSafe(str1 + "grass_side");
            iconGrassSideOverlay = localTextureMap.getSpriteSafe(str1 + "grass_side_overlay");
            iconSnow = localTextureMap.getSpriteSafe(str1 + "snow");
            iconGrassSideSnowed = localTextureMap.getSpriteSafe(str1 + "grass_side_snowed");
            iconMyceliumSide = localTextureMap.getSpriteSafe(str1 + "mycelium_side");
            iconMyceliumTop = localTextureMap.getSpriteSafe(str1 + "mycelium_top");
            iconWaterStill = localTextureMap.getSpriteSafe(str1 + "water_still");
            iconWaterFlow = localTextureMap.getSpriteSafe(str1 + "water_flow");
            iconLavaStill = localTextureMap.getSpriteSafe(str1 + "lava_still");
            iconLavaFlow = localTextureMap.getSpriteSafe(str1 + "lava_flow");
            iconFireLayer0 = localTextureMap.getSpriteSafe(str1 + "fire_layer_0");
            iconFireLayer1 = localTextureMap.getSpriteSafe(str1 + "fire_layer_1");
            iconPortal = localTextureMap.getSpriteSafe(str1 + "portal");
            iconGlass = localTextureMap.getSpriteSafe(str1 + "glass");
            iconGlassPaneTop = localTextureMap.getSpriteSafe(str1 + "glass_pane_top");
            String str2 = "minecraft:items/";
            iconCompass = localTextureMap.getSpriteSafe(str2 + "compass");
            iconClock = localTextureMap.getSpriteSafe(str2 + "clock");
        }
    }

    public static BufferedImage fixTextureDimensions(String paramString, BufferedImage paramBufferedImage) {
        if ((paramString.startsWith("/mob/zombie")) || (paramString.startsWith("/mob/pigzombie"))) {
            int i = paramBufferedImage.getWidth();
            int j = paramBufferedImage.getHeight();
            if (i == j * 2) {
                BufferedImage localBufferedImage = new BufferedImage(i, j * 2, 2);
                Graphics2D localGraphics2D = localBufferedImage.createGraphics();
                localGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                localGraphics2D.drawImage(paramBufferedImage, 0, 0, i, j, (ImageObserver) null);
                return localBufferedImage;
            }
        }
        return paramBufferedImage;
    }

    public static int ceilPowerOfTwo(int paramInt) {
        int i = 1;
        while (i < paramInt) {
            i *= 2;
        }
        return i;
    }

    public static int getPowerOfTwo(int paramInt) {
        int i = 1;
        for (int j = 0; i < paramInt; j++) {
            i *= 2;
        }
        return j;
    }

    public static int twoToPower(int paramInt) {
        int i = 1;
        for (int j = 0; j < paramInt; j++) {
            i *= 2;
        }
        return i;
    }

    public static ITextureObject getTexture(ResourceLocation paramResourceLocation) {
        ITextureObject localITextureObject = Config.getTextureManager().getTexture(paramResourceLocation);
        if (localITextureObject != null) {
            return localITextureObject;
        }
        if (!Config.hasResource(paramResourceLocation)) {
            return null;
        }
        SimpleTexture localSimpleTexture = new SimpleTexture(paramResourceLocation);
        Config.getTextureManager().loadTexture(paramResourceLocation, localSimpleTexture);
        return localSimpleTexture;
    }

    public static void resourcesReloaded(IResourceManager paramIResourceManager) {
        if (getTextureMapBlocks() != null) {
            Config.dbg("*** Reloading custom textures ***");
            CustomSky.reset();
            TextureAnimations.reset();
            update();
            NaturalTextures.update();
            BetterGrass.update();
            BetterSnow.update();
            TextureAnimations.update();
            CustomColors.update();
            CustomSky.update();
            RandomMobs.resetTextures();
            CustomItems.updateModels();
            Shaders.resourcesReloaded();
            Lang.resourcesReloaded();
            Config.updateTexturePackClouds();
            SmartLeaves.updateLeavesModels();
            Config.getTextureManager().tick();
        }
    }

    public static TextureMap getTextureMapBlocks() {
        return Minecraft.getMinecraft().getTextureMapBlocks();
    }

    public static void registerResourceListener() {
        IResourceManager localIResourceManager = Config.getResourceManager();
        if ((localIResourceManager instanceof IReloadableResourceManager)) {
            localObject1 = (IReloadableResourceManager) localIResourceManager;
            localObject2 = new IResourceManagerReloadListener() {
                public void onResourceManagerReload(IResourceManager paramAnonymousIResourceManager) {
                    TextureUtils.resourcesReloaded(paramAnonymousIResourceManager);
                }
            };
            ((IReloadableResourceManager) localObject1).registerReloadListener((IResourceManagerReloadListener) localObject2);
        }
        Object localObject1 = new ITickableTextureObject() {
            public void tick() {
            }

            public void loadTexture(IResourceManager paramAnonymousIResourceManager)
                    throws IOException {
            }

            public int getGlTextureId() {
                return 0;
            }

            public void setBlurMipmap(boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2) {
            }

            public void restoreLastBlurMipmap() {
            }

            public MultiTexID getMultiTexID() {
                return null;
            }
        };
        Object localObject2 = new ResourceLocation("optifine/TickableTextures");
        Config.getTextureManager().loadTickableTexture((ResourceLocation) localObject2, (ITickableTextureObject) localObject1);
    }

    public static String fixResourcePath(String paramString1, String paramString2) {
        String str1 = "assets/minecraft/";
        if (paramString1.startsWith(str1)) {
            paramString1 = paramString1.substring(str1.length());
            return paramString1;
        }
        if (paramString1.startsWith("./")) {
            paramString1 = paramString1.substring(2);
            if (!paramString2.endsWith("/")) {
                paramString2 = paramString2 + "/";
            }
            paramString1 = paramString2 + paramString1;
            return paramString1;
        }
        if (paramString1.startsWith("/~")) {
            paramString1 = paramString1.substring(1);
        }
        String str2 = "mcpatcher/";
        if (paramString1.startsWith("~/")) {
            paramString1 = paramString1.substring(2);
            paramString1 = str2 + paramString1;
            return paramString1;
        }
        if (paramString1.startsWith("/")) {
            paramString1 = str2 + paramString1.substring(1);
            return paramString1;
        }
        return paramString1;
    }

    public static String getBasePath(String paramString) {
        int i = paramString.lastIndexOf('/');
        return i < 0 ? "" : paramString.substring(0, i);
    }

    public static void applyAnisotropicLevel() {
        if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
            float f1 = GL11.glGetFloat(34047);
            float f2 = Config.getAnisotropicFilterLevel();
            f2 = Math.min(f2, f1);
            GL11.glTexParameterf(3553, 34046, f2);
        }
    }

    public static void bindTexture(int paramInt) {
        GlStateManager.bindTexture(paramInt);
    }

    public static boolean isPowerOfTwo(int paramInt) {
        int i = MathHelper.roundUpToPowerOfTwo(paramInt);
        return i == paramInt;
    }

    public static BufferedImage scaleImage(BufferedImage paramBufferedImage, int paramInt) {
        int i = paramBufferedImage.getWidth();
        int j = paramBufferedImage.getHeight();
        int k = -i;
        BufferedImage localBufferedImage = new BufferedImage(paramInt, k, 2);
        Graphics2D localGraphics2D = localBufferedImage.createGraphics();
        Object localObject = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        if ((paramInt < i) || (paramInt << i != 0)) {
            localObject = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        }
        localGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, localObject);
        localGraphics2D.drawImage(paramBufferedImage, 0, 0, paramInt, k, (ImageObserver) null);
        return localBufferedImage;
    }

    public static BufferedImage scaleToPowerOfTwo(BufferedImage paramBufferedImage, int paramInt) {
        if (paramBufferedImage == null) {
            return paramBufferedImage;
        }
        int i = paramBufferedImage.getWidth();
        int j = paramBufferedImage.getHeight();
        int k = Math.max(i, paramInt);
        k = MathHelper.roundUpToPowerOfTwo(k);
        if (k == i) {
            return paramBufferedImage;
        }
        int m = -i;
        BufferedImage localBufferedImage = new BufferedImage(k, m, 2);
        Graphics2D localGraphics2D = localBufferedImage.createGraphics();
        Object localObject = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        if (k << i != 0) {
            localObject = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
        }
        localGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, localObject);
        localGraphics2D.drawImage(paramBufferedImage, 0, 0, k, m, (ImageObserver) null);
        return localBufferedImage;
    }

    public static BufferedImage scaleMinTo(BufferedImage paramBufferedImage, int paramInt) {
        if (paramBufferedImage == null) {
            return paramBufferedImage;
        }
        int i = paramBufferedImage.getWidth();
        int j = paramBufferedImage.getHeight();
        if (i >= paramInt) {
            return paramBufferedImage;
        }
        int k = i;
        while (k < paramInt) {
            k *= 2;
        }
        int m = -i;
        BufferedImage localBufferedImage = new BufferedImage(k, m, 2);
        Graphics2D localGraphics2D = localBufferedImage.createGraphics();
        Object localObject = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        localGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, localObject);
        localGraphics2D.drawImage(paramBufferedImage, 0, 0, k, m, (ImageObserver) null);
        return localBufferedImage;
    }

    public static Dimension getImageSize(InputStream paramInputStream, String paramString) {
        Iterator localIterator = ImageIO.getImageReadersBySuffix(paramString);
        Dimension localDimension;
        for (; ; ) {
            if (!localIterator.hasNext()) {
                break label97;
            }
            ImageReader localImageReader = (ImageReader) localIterator.next();
            try {
                ImageInputStream localImageInputStream = ImageIO.createImageInputStream(paramInputStream);
                localImageReader.setInput(localImageInputStream);
                int i = localImageReader.getWidth(localImageReader.getMinIndex());
                int j = localImageReader.getHeight(localImageReader.getMinIndex());
                localDimension = new Dimension(i, j);
            } catch (IOException localIOException) {
                localImageReader.dispose();
            } finally {
                localImageReader.dispose();
            }
        }
        return localDimension;
        label97:
        return null;
    }

    public static void dbgMipmaps(TextureAtlasSprite paramTextureAtlasSprite) {
        int[][] arrayOfInt = paramTextureAtlasSprite.getFrameTextureData(0);
        for (int i = 0; i < arrayOfInt.length; i++) {
            int[] arrayOfInt1 = arrayOfInt[i];
            if (arrayOfInt1 == null) {
                Config.dbg("" + i + ": " + arrayOfInt1);
            } else {
                Config.dbg("" + i + ": " + arrayOfInt1.length);
            }
        }
    }

    public static void saveGlTexture(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        bindTexture(paramInt1);
        GL11.glPixelStorei(3333, 1);
        GL11.glPixelStorei(3317, 1);
        File localFile1 = new File(paramString);
        File localFile2 = localFile1.getParentFile();
        if (localFile2 != null) {
            localFile2.mkdirs();
        }
        File localFile3;
        for (int i = 0; i < 16; i++) {
            localFile3 = new File(paramString + "_" + i + ".png");
            localFile3.delete();
        }
        for (i = 0; i <= paramInt2; i++) {
            localFile3 = new File(paramString + "_" + i + ".png");
            int j = paramInt3 & i;
            int k = paramInt4 & i;
            int m = j * k;
            IntBuffer localIntBuffer = BufferUtils.createIntBuffer(m);
            int[] arrayOfInt = new int[m];
            GL11.glGetTexImage(3553, i, 32993, 33639, localIntBuffer);
            localIntBuffer.get(arrayOfInt);
            BufferedImage localBufferedImage = new BufferedImage(j, k, 2);
            localBufferedImage.setRGB(0, 0, j, k, arrayOfInt, 0, j);
            try {
                ImageIO.write(localBufferedImage, "png", localFile3);
                Config.dbg("Exported: " + localFile3);
            } catch (Exception localException) {
                Config.warn("Error writing: " + localFile3);
                Config.warn("" + localException.getClass().getName() + ": " + localException.getMessage());
            }
        }
    }

    public static int getGLMaximumTextureSize() {
        int i = 65536;
        while (i > 0) {
            GL11.glTexImage2D(32868, 0, 6408, i, i, 0, 6408, 5121, (IntBuffer) null);
            int j = GL11.glGetError();
            int k = GL11.glGetTexLevelParameteri(32868, 0, 4096);
            if (k != 0) {
                return i;
            }
            i &= 0x1;
        }
        return -1;
    }
}




