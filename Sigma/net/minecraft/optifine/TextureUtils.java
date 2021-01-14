package net.minecraft.optifine;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.nio.IntBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;

public class TextureUtils {
    public static String texGrassTop = "grass_top";
    public static String texStone = "stone";
    public static String texDirt = "dirt";
    public static String texGrassSide = "grass_side";
    public static String texStoneslabSide = "stone_slab_side";
    public static String texStoneslabTop = "stone_slab_top";
    public static String texBedrock = "bedrock";
    public static String texSand = "sand";
    public static String texGravel = "gravel";
    public static String texLogOak = "log_oak";
    public static String texLogOakTop = "log_oak_top";
    public static String texGoldOre = "gold_ore";
    public static String texIronOre = "iron_ore";
    public static String texCoalOre = "coal_ore";
    public static String texObsidian = "obsidian";
    public static String texGrassSideOverlay = "grass_side_overlay";
    public static String texSnow = "snow";
    public static String texGrassSideSnowed = "grass_side_snowed";
    public static String texMyceliumSide = "mycelium_side";
    public static String texMyceliumTop = "mycelium_top";
    public static String texDiamondOre = "diamond_ore";
    public static String texRedstoneOre = "redstone_ore";
    public static String texLapisOre = "lapis_ore";
    public static String texLeavesOak = "leaves_oak";
    public static String texLeavesOakOpaque = "leaves_oak_opaque";
    public static String texLeavesJungle = "leaves_jungle";
    public static String texLeavesJungleOpaque = "leaves_jungle_opaque";
    public static String texCactusSide = "cactus_side";
    public static String texClay = "clay";
    public static String texFarmlandWet = "farmland_wet";
    public static String texFarmlandDry = "farmland_dry";
    public static String texNetherrack = "netherrack";
    public static String texSoulSand = "soul_sand";
    public static String texLogSpruce = "log_spruce";
    public static String texLogBirch = "log_birch";
    public static String texLeavesSpruce = "leaves_spruce";
    public static String texLeavesSpruceOpaque = "leaves_spruce_opaque";
    public static String texLogJungle = "log_jungle";
    public static String texEndStone = "end_stone";
    public static String texSandstoneTop = "sandstone_top";
    public static String texSandstoneBottom = "sandstone_bottom";
    public static String texRedstoneLampOff = "redstone_lamp_off";
    public static String texRedstoneLampOn = "redstone_lamp_on";
    public static String texWaterStill = "water_still";
    public static String texWaterFlow = "water_flow";
    public static String texLavaStill = "lava_still";
    public static String texLavaFlow = "lava_flow";
    public static String texFireLayer0 = "fire_layer_0";
    public static String texFireLayer1 = "fire_layer_1";
    public static String texPortal = "portal";
    public static String texGlass = "glass";
    public static String texGlassPaneTop = "glass_pane_top";
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
    public static String SPRITE_LOCATION_PREFIX = "minecraft:blocks/";
    private static IntBuffer staticBuffer = GLAllocation.createDirectIntBuffer(256);

    public static void update() {
        TextureMap mapBlocks = TextureUtils.getTextureMapBlocks();

        if (mapBlocks != null) {
            String prefix = "minecraft:blocks/";
            TextureUtils.iconGrassTop = mapBlocks.getSpriteSafe(prefix + "grass_top");
            TextureUtils.iconGrassSide = mapBlocks.getSpriteSafe(prefix + "grass_side");
            TextureUtils.iconGrassSideOverlay = mapBlocks.getSpriteSafe(prefix + "grass_side_overlay");
            TextureUtils.iconSnow = mapBlocks.getSpriteSafe(prefix + "snow");
            TextureUtils.iconGrassSideSnowed = mapBlocks.getSpriteSafe(prefix + "grass_side_snowed");
            TextureUtils.iconMyceliumSide = mapBlocks.getSpriteSafe(prefix + "mycelium_side");
            TextureUtils.iconMyceliumTop = mapBlocks.getSpriteSafe(prefix + "mycelium_top");
            TextureUtils.iconWaterStill = mapBlocks.getSpriteSafe(prefix + "water_still");
            TextureUtils.iconWaterFlow = mapBlocks.getSpriteSafe(prefix + "water_flow");
            TextureUtils.iconLavaStill = mapBlocks.getSpriteSafe(prefix + "lava_still");
            TextureUtils.iconLavaFlow = mapBlocks.getSpriteSafe(prefix + "lava_flow");
            TextureUtils.iconFireLayer0 = mapBlocks.getSpriteSafe(prefix + "fire_layer_0");
            TextureUtils.iconFireLayer1 = mapBlocks.getSpriteSafe(prefix + "fire_layer_1");
            TextureUtils.iconPortal = mapBlocks.getSpriteSafe(prefix + "portal");
            TextureUtils.iconGlass = mapBlocks.getSpriteSafe(prefix + "glass");
            TextureUtils.iconGlassPaneTop = mapBlocks.getSpriteSafe(prefix + "glass_pane_top");
        }
    }

    public static BufferedImage fixTextureDimensions(String name, BufferedImage bi) {
        if (name.startsWith("/mob/zombie") || name.startsWith("/mob/pigzombie")) {
            int width = bi.getWidth();
            int height = bi.getHeight();

            if (width == height * 2) {
                BufferedImage scaledImage = new BufferedImage(width, height * 2, 2);
                Graphics2D gr = scaledImage.createGraphics();
                gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                gr.drawImage(bi, 0, 0, width, height, (ImageObserver) null);
                return scaledImage;
            }
        }

        return bi;
    }

    public static int ceilPowerOfTwo(int val) {
        int i;

        for (i = 1; i < val; i *= 2) {
            ;
        }

        return i;
    }

    public static int getPowerOfTwo(int val) {
        int i = 1;
        int po2;

        for (po2 = 0; i < val; ++po2) {
            i *= 2;
        }

        return po2;
    }

    public static int twoToPower(int power) {
        int val = 1;

        for (int i = 0; i < power; ++i) {
            val *= 2;
        }

        return val;
    }

    public static void refreshBlockTextures() {
    }

    public static ITextureObject getTexture(String path) {
        return TextureUtils.getTexture(new ResourceLocation(path));
    }

    public static ITextureObject getTexture(ResourceLocation loc) {
        ITextureObject tex = Config.getTextureManager().getTexture(loc);

        if (tex != null) {
            return tex;
        } else if (!Config.hasResource(loc)) {
            return null;
        } else {
            SimpleTexture tex1 = new SimpleTexture(loc);
            Config.getTextureManager().loadTexture(loc, tex1);
            return tex1;
        }
    }

    public static void resourcesReloaded(IResourceManager rm) {
        if (TextureUtils.getTextureMapBlocks() != null) {
            Config.dbg("*** Reloading custom textures ***");
            CustomSky.reset();
            TextureAnimations.reset();
            TextureUtils.update();
            NaturalTextures.update();
            BetterGrass.update();
            BetterSnow.update();
            TextureAnimations.update();
            CustomColorizer.update();
            CustomSky.update();
            RandomMobs.resetTextures();
            Config.updateTexturePackClouds();
            Config.getTextureManager().tick();
        }
    }

    public static TextureMap getTextureMapBlocks() {
        return Minecraft.getMinecraft().getTextureMapBlocks();
    }

    public static void registerResourceListener() {
        IResourceManager rm = Config.getResourceManager();

        if (rm instanceof IReloadableResourceManager) {
            IReloadableResourceManager tto = (IReloadableResourceManager) rm;
            IResourceManagerReloadListener ttol = new IResourceManagerReloadListener() {
                @Override
                public void onResourceManagerReload(IResourceManager var1) {
                    TextureUtils.resourcesReloaded(var1);
                }
            };
            tto.registerReloadListener(ttol);
        }

        ITickableTextureObject tto1 = new ITickableTextureObject() {
            @Override
            public void tick() {
                TextureAnimations.updateCustomAnimations();
            }

            @Override
            public void loadTexture(IResourceManager var1) throws IOException {
            }

            @Override
            public int getGlTextureId() {
                return 0;
            }

            @Override
            public void func_174936_b(boolean p_174936_1, boolean p_174936_2) {
            }

            @Override
            public void func_174935_a() {
            }
        };
        ResourceLocation ttol1 = new ResourceLocation("optifine/TickableTextures");
        Config.getTextureManager().loadTickableTexture(ttol1, tto1);
    }

    public static String fixResourcePath(String path, String basePath) {
        String strAssMc = "assets/minecraft/";

        if (path.startsWith(strAssMc)) {
            path = path.substring(strAssMc.length());
            return path;
        } else if (path.startsWith("./")) {
            path = path.substring(2);

            if (!basePath.endsWith("/")) {
                basePath = basePath + "/";
            }

            path = basePath + path;
            return path;
        } else {
            String strMcpatcher = "mcpatcher/";

            if (path.startsWith("~/")) {
                path = path.substring(2);
                path = strMcpatcher + path;
                return path;
            } else if (path.startsWith("/")) {
                path = strMcpatcher + path.substring(1);
                return path;
            } else {
                return path;
            }
        }
    }

    public static String getBasePath(String path) {
        int pos = path.lastIndexOf(47);
        return pos < 0 ? "" : path.substring(0, pos);
    }
}
