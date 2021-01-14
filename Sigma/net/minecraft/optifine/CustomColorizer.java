package net.minecraft.optifine;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStem;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class CustomColorizer {
    private static int[] grassColors = null;
    private static int[] waterColors = null;
    private static int[] foliageColors = null;
    private static int[] foliagePineColors = null;
    private static int[] foliageBirchColors = null;
    private static int[] swampFoliageColors = null;
    private static int[] swampGrassColors = null;
    private static int[][] blockPalettes = null;
    private static int[][] paletteColors = null;
    private static int[] skyColors = null;
    private static int[] fogColors = null;
    private static int[] underwaterColors = null;
    private static float[][][] lightMapsColorsRgb = null;
    private static int[] lightMapsHeight = null;
    private static float[][] sunRgbs = new float[16][3];
    private static float[][] torchRgbs = new float[16][3];
    private static int[] redstoneColors = null;
    private static int[] stemColors = null;
    private static int[] myceliumParticleColors = null;
    private static boolean useDefaultColorMultiplier = true;
    private static int particleWaterColor = -1;
    private static int particlePortalColor = -1;
    private static int lilyPadColor = -1;
    private static Vec3 fogColorNether = null;
    private static Vec3 fogColorEnd = null;
    private static Vec3 skyColorEnd = null;
    private static final int TYPE_NONE = 0;
    private static final int TYPE_GRASS = 1;
    private static final int TYPE_FOLIAGE = 2;
    private static Random random = new Random();

    public static void update() {
        CustomColorizer.grassColors = null;
        CustomColorizer.waterColors = null;
        CustomColorizer.foliageColors = null;
        CustomColorizer.foliageBirchColors = null;
        CustomColorizer.foliagePineColors = null;
        CustomColorizer.swampGrassColors = null;
        CustomColorizer.swampFoliageColors = null;
        CustomColorizer.skyColors = null;
        CustomColorizer.fogColors = null;
        CustomColorizer.underwaterColors = null;
        CustomColorizer.redstoneColors = null;
        CustomColorizer.stemColors = null;
        CustomColorizer.myceliumParticleColors = null;
        CustomColorizer.lightMapsColorsRgb = null;
        CustomColorizer.lightMapsHeight = null;
        CustomColorizer.lilyPadColor = -1;
        CustomColorizer.particleWaterColor = -1;
        CustomColorizer.particlePortalColor = -1;
        CustomColorizer.fogColorNether = null;
        CustomColorizer.fogColorEnd = null;
        CustomColorizer.skyColorEnd = null;
        CustomColorizer.blockPalettes = null;
        CustomColorizer.paletteColors = null;
        CustomColorizer.useDefaultColorMultiplier = true;
        String mcpColormap = "mcpatcher/colormap/";
        CustomColorizer.grassColors = CustomColorizer.getCustomColors("textures/colormap/grass.png", 65536);
        CustomColorizer.foliageColors = CustomColorizer.getCustomColors("textures/colormap/foliage.png", 65536);
        String[] waterPaths = new String[]{"water.png", "watercolorX.png"};
        CustomColorizer.waterColors = CustomColorizer.getCustomColors(mcpColormap, waterPaths, 65536);

        if (Config.isCustomColors()) {
            String[] pinePaths = new String[]{"pine.png", "pinecolor.png"};
            CustomColorizer.foliagePineColors = CustomColorizer.getCustomColors(mcpColormap, pinePaths, 65536);
            String[] birchPaths = new String[]{"birch.png", "birchcolor.png"};
            CustomColorizer.foliageBirchColors = CustomColorizer.getCustomColors(mcpColormap, birchPaths, 65536);
            String[] swampGrassPaths = new String[]{"swampgrass.png", "swampgrasscolor.png"};
            CustomColorizer.swampGrassColors = CustomColorizer.getCustomColors(mcpColormap, swampGrassPaths, 65536);
            String[] swampFoliagePaths = new String[]{"swampfoliage.png", "swampfoliagecolor.png"};
            CustomColorizer.swampFoliageColors = CustomColorizer.getCustomColors(mcpColormap, swampFoliagePaths, 65536);
            String[] sky0Paths = new String[]{"sky0.png", "skycolor0.png"};
            CustomColorizer.skyColors = CustomColorizer.getCustomColors(mcpColormap, sky0Paths, 65536);
            String[] fog0Paths = new String[]{"fog0.png", "fogcolor0.png"};
            CustomColorizer.fogColors = CustomColorizer.getCustomColors(mcpColormap, fog0Paths, 65536);
            String[] underwaterPaths = new String[]{"underwater.png", "underwatercolor.png"};
            CustomColorizer.underwaterColors = CustomColorizer.getCustomColors(mcpColormap, underwaterPaths, 65536);
            String[] redstonePaths = new String[]{"redstone.png", "redstonecolor.png"};
            CustomColorizer.redstoneColors = CustomColorizer.getCustomColors(mcpColormap, redstonePaths, 16);
            String[] stemPaths = new String[]{"stem.png", "stemcolor.png"};
            CustomColorizer.stemColors = CustomColorizer.getCustomColors(mcpColormap, stemPaths, 8);
            String[] myceliumPaths = new String[]{"myceliumparticle.png", "myceliumparticlecolor.png"};
            CustomColorizer.myceliumParticleColors = CustomColorizer.getCustomColors(mcpColormap, myceliumPaths, -1);
            int[][] lightMapsColors = new int[3][];
            CustomColorizer.lightMapsColorsRgb = new float[3][][];
            CustomColorizer.lightMapsHeight = new int[3];

            for (int i = 0; i < lightMapsColors.length; ++i) {
                String path = "mcpatcher/lightmap/world" + (i - 1) + ".png";
                lightMapsColors[i] = CustomColorizer.getCustomColors(path, -1);

                if (lightMapsColors[i] != null) {
                    CustomColorizer.lightMapsColorsRgb[i] = CustomColorizer.toRgb(lightMapsColors[i]);
                }

                CustomColorizer.lightMapsHeight[i] = CustomColorizer.getTextureHeight(path, 32);
            }

            CustomColorizer.readColorProperties("mcpatcher/color.properties");
            CustomColorizer.updateUseDefaultColorMultiplier();
        }
    }

    private static int getTextureHeight(String path, int defHeight) {
        try {
            InputStream e = Config.getResourceStream(new ResourceLocation(path));

            if (e == null) {
                return defHeight;
            } else {
                BufferedImage bi = ImageIO.read(e);
                return bi == null ? defHeight : bi.getHeight();
            }
        } catch (IOException var4) {
            return defHeight;
        }
    }

    private static float[][] toRgb(int[] cols) {
        float[][] colsRgb = new float[cols.length][3];

        for (int i = 0; i < cols.length; ++i) {
            int col = cols[i];
            float rf = (col >> 16 & 255) / 255.0F;
            float gf = (col >> 8 & 255) / 255.0F;
            float bf = (col & 255) / 255.0F;
            float[] colRgb = colsRgb[i];
            colRgb[0] = rf;
            colRgb[1] = gf;
            colRgb[2] = bf;
        }

        return colsRgb;
    }

    private static void readColorProperties(String fileName) {
        try {
            ResourceLocation e = new ResourceLocation(fileName);
            InputStream in = Config.getResourceStream(e);

            if (in == null) {
                return;
            }

            Config.log("Loading " + fileName);
            Properties props = new Properties();
            props.load(in);
            CustomColorizer.lilyPadColor = CustomColorizer.readColor(props, "lilypad");
            CustomColorizer.particleWaterColor = CustomColorizer.readColor(props, new String[]{"particle.water", "drop.water"});
            CustomColorizer.particlePortalColor = CustomColorizer.readColor(props, "particle.portal");
            CustomColorizer.fogColorNether = CustomColorizer.readColorVec3(props, "fog.nether");
            CustomColorizer.fogColorEnd = CustomColorizer.readColorVec3(props, "fog.end");
            CustomColorizer.skyColorEnd = CustomColorizer.readColorVec3(props, "sky.end");
            CustomColorizer.readCustomPalettes(props, fileName);
        } catch (FileNotFoundException var4) {
            return;
        } catch (IOException var5) {
            var5.printStackTrace();
        }
    }

    private static void readCustomPalettes(Properties props, String fileName) {
        CustomColorizer.blockPalettes = new int[256][1];

        for (int palettePrefix = 0; palettePrefix < 256; ++palettePrefix) {
            CustomColorizer.blockPalettes[palettePrefix][0] = -1;
        }

        String var18 = "palette.block.";
        HashMap map = new HashMap();
        Set keys = props.keySet();
        Iterator propNames = keys.iterator();
        String name;

        while (propNames.hasNext()) {
            String i = (String) propNames.next();
            name = props.getProperty(i);

            if (i.startsWith(var18)) {
                map.put(i, name);
            }
        }

        String[] var19 = ((String[]) map.keySet().toArray(new String[map.size()]));
        CustomColorizer.paletteColors = new int[var19.length][];

        for (int var20 = 0; var20 < var19.length; ++var20) {
            name = var19[var20];
            String value = props.getProperty(name);
            Config.log("Block palette: " + name + " = " + value);
            String path = name.substring(var18.length());
            String basePath = TextureUtils.getBasePath(fileName);
            path = TextureUtils.fixResourcePath(path, basePath);
            int[] colors = CustomColorizer.getCustomColors(path, 65536);
            CustomColorizer.paletteColors[var20] = colors;
            String[] indexStrs = Config.tokenize(value, " ,;");

            for (String indexStr : indexStrs) {
                String blockStr = indexStr;
                int metadata = -1;

                if (blockStr.contains(":")) {
                    String[] blockIndex = Config.tokenize(blockStr, ":");
                    blockStr = blockIndex[0];
                    String metadataStr = blockIndex[1];
                    metadata = Config.parseInt(metadataStr, -1);

                    if (metadata < 0 || metadata > 15) {
                        Config.log("Invalid block metadata: " + blockStr + " in palette: " + name);
                        continue;
                    }
                }

                int var21 = Config.parseInt(blockStr, -1);

                if (var21 >= 0 && var21 <= 255) {
                    if (var21 != Block.getIdFromBlock(Blocks.grass) && var21 != Block.getIdFromBlock(Blocks.tallgrass) && var21 != Block.getIdFromBlock(Blocks.leaves) && var21 != Block.getIdFromBlock(Blocks.vine)) {
                        if (metadata == -1) {
                            CustomColorizer.blockPalettes[var21][0] = var20;
                        } else {
                            if (CustomColorizer.blockPalettes[var21].length < 16) {
                                CustomColorizer.blockPalettes[var21] = new int[16];
                                Arrays.fill(CustomColorizer.blockPalettes[var21], -1);
                            }

                            CustomColorizer.blockPalettes[var21][metadata] = var20;
                        }
                    }
                } else {
                    Config.log("Invalid block index: " + var21 + " in palette: " + name);
                }
            }
        }
    }

    private static int readColor(Properties props, String[] names) {
        for (String name : names) {
            int col = CustomColorizer.readColor(props, name);

            if (col >= 0) {
                return col;
            }
        }

        return -1;
    }

    private static int readColor(Properties props, String name) {
        String str = props.getProperty(name);

        if (str == null) {
            return -1;
        } else {
            try {
                int e = Integer.parseInt(str, 16) & 16777215;
                Config.log("Custom color: " + name + " = " + str);
                return e;
            } catch (NumberFormatException var4) {
                Config.log("Invalid custom color: " + name + " = " + str);
                return -1;
            }
        }
    }

    private static Vec3 readColorVec3(Properties props, String name) {
        int col = CustomColorizer.readColor(props, name);

        if (col < 0) {
            return null;
        } else {
            int red = col >> 16 & 255;
            int green = col >> 8 & 255;
            int blue = col & 255;
            float redF = red / 255.0F;
            float greenF = green / 255.0F;
            float blueF = blue / 255.0F;
            return new Vec3(redF, greenF, blueF);
        }
    }

    private static int[] getCustomColors(String basePath, String[] paths, int length) {
        for (String path2 : paths) {
            String path = path2;
            path = basePath + path;
            int[] cols = CustomColorizer.getCustomColors(path, length);

            if (cols != null) {
                return cols;
            }
        }

        return null;
    }

    private static int[] getCustomColors(String path, int length) {
        try {
            ResourceLocation e = new ResourceLocation(path);
            InputStream in = Config.getResourceStream(e);

            if (in == null) {
                return null;
            } else {
                int[] colors = TextureUtil.readImageData(Config.getResourceManager(), e);

                if (colors == null) {
                    return null;
                } else if (length > 0 && colors.length != length) {
                    Config.log("Invalid custom colors length: " + colors.length + ", path: " + path);
                    return null;
                } else {
                    Config.log("Loading custom colors: " + path);
                    return colors;
                }
            }
        } catch (FileNotFoundException var5) {
            return null;
        } catch (IOException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static void updateUseDefaultColorMultiplier() {
        CustomColorizer.useDefaultColorMultiplier = CustomColorizer.foliageBirchColors == null && CustomColorizer.foliagePineColors == null && CustomColorizer.swampGrassColors == null && CustomColorizer.swampFoliageColors == null && CustomColorizer.blockPalettes == null && Config.isSwampColors() && Config.isSmoothBiomes();
    }

    public static int getColorMultiplier(BakedQuad quad, Block block, IBlockAccess blockAccess, BlockPos blockPos, RenderEnv renderEnv) {
        if (CustomColorizer.useDefaultColorMultiplier) {
            return -1;
        } else {
            int[] colors = null;
            int[] swampColors = null;
            int metadata;

            if (CustomColorizer.blockPalettes != null) {
                int useSwampColors = renderEnv.getBlockId();

                if (useSwampColors >= 0 && useSwampColors < 256) {
                    int[] smoothColors = CustomColorizer.blockPalettes[useSwampColors];
                    boolean type = true;
                    int type1;

                    if (smoothColors.length > 1) {
                        metadata = renderEnv.getMetadata();
                        type1 = smoothColors[metadata];
                    } else {
                        type1 = smoothColors[0];
                    }

                    if (type1 >= 0) {
                        colors = CustomColorizer.paletteColors[type1];
                    }
                }

                if (colors != null) {
                    if (Config.isSmoothBiomes()) {
                        return CustomColorizer.getSmoothColorMultiplier(block, blockAccess, blockPos, colors, colors, 0, 0, renderEnv);
                    }

                    return CustomColorizer.getCustomColor(colors, blockAccess, blockPos);
                }
            }

            if (!quad.func_178212_b()) {
                return -1;
            } else if (block == Blocks.waterlily) {
                return CustomColorizer.getLilypadColorMultiplier(blockAccess, blockPos);
            } else if (block instanceof BlockStem) {
                return CustomColorizer.getStemColorMultiplier(block, blockAccess, blockPos, renderEnv);
            } else {
                boolean useSwampColors1 = Config.isSwampColors();
                boolean smoothColors1 = false;
                byte type2 = 0;
                metadata = 0;

                if (block != Blocks.grass && block != Blocks.tallgrass) {
                    if (block == Blocks.leaves) {
                        type2 = 2;
                        smoothColors1 = Config.isSmoothBiomes();
                        metadata = renderEnv.getMetadata();

                        if ((metadata & 3) == 1) {
                            colors = CustomColorizer.foliagePineColors;
                        } else if ((metadata & 3) == 2) {
                            colors = CustomColorizer.foliageBirchColors;
                        } else {
                            colors = CustomColorizer.foliageColors;

                            if (useSwampColors1) {
                                swampColors = CustomColorizer.swampFoliageColors;
                            } else {
                                swampColors = colors;
                            }
                        }
                    } else if (block == Blocks.vine) {
                        type2 = 2;
                        smoothColors1 = Config.isSmoothBiomes();
                        colors = CustomColorizer.foliageColors;

                        if (useSwampColors1) {
                            swampColors = CustomColorizer.swampFoliageColors;
                        } else {
                            swampColors = colors;
                        }
                    }
                } else {
                    type2 = 1;
                    smoothColors1 = Config.isSmoothBiomes();
                    colors = CustomColorizer.grassColors;

                    if (useSwampColors1) {
                        swampColors = CustomColorizer.swampGrassColors;
                    } else {
                        swampColors = colors;
                    }
                }

                if (smoothColors1) {
                    return CustomColorizer.getSmoothColorMultiplier(block, blockAccess, blockPos, colors, swampColors, type2, metadata, renderEnv);
                } else {
                    if (swampColors != colors && blockAccess.getBiomeGenForCoords(blockPos) == BiomeGenBase.swampland) {
                        colors = swampColors;
                    }

                    return colors != null ? CustomColorizer.getCustomColor(colors, blockAccess, blockPos) : -1;
                }
            }
        }
    }

    private static int getSmoothColorMultiplier(Block block, IBlockAccess blockAccess, BlockPos blockPos, int[] colors, int[] swampColors, int type, int metadata, RenderEnv renderEnv) {
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        BlockPosM posM = renderEnv.getColorizerBlockPos();
        int r;
        int g;

        for (r = x - 1; r <= x + 1; ++r) {
            for (g = z - 1; g <= z + 1; ++g) {
                posM.setXyz(r, y, g);
                int[] b = colors;

                if (swampColors != colors && blockAccess.getBiomeGenForCoords(posM) == BiomeGenBase.swampland) {
                    b = swampColors;
                }

                boolean col = false;
                int var20;

                if (b == null) {
                    switch (type) {
                        case 1:
                            var20 = blockAccess.getBiomeGenForCoords(posM).func_180627_b(posM);
                            break;

                        case 2:
                            if ((metadata & 3) == 1) {
                                var20 = ColorizerFoliage.getFoliageColorPine();
                            } else if ((metadata & 3) == 2) {
                                var20 = ColorizerFoliage.getFoliageColorBirch();
                            } else {
                                var20 = blockAccess.getBiomeGenForCoords(posM).func_180625_c(posM);
                            }

                            break;

                        default:
                            var20 = block.colorMultiplier(blockAccess, posM);
                    }
                } else {
                    var20 = CustomColorizer.getCustomColor(b, blockAccess, posM);
                }

                sumRed += var20 >> 16 & 255;
                sumGreen += var20 >> 8 & 255;
                sumBlue += var20 & 255;
            }
        }

        r = sumRed / 9;
        g = sumGreen / 9;
        int var19 = sumBlue / 9;
        return r << 16 | g << 8 | var19;
    }

    public static int getFluidColor(Block block, IBlockAccess blockAccess, BlockPos blockPos) {
        return block.getMaterial() != Material.water ? block.colorMultiplier(blockAccess, blockPos) : (CustomColorizer.waterColors != null ? (Config.isSmoothBiomes() ? CustomColorizer.getSmoothColor(CustomColorizer.waterColors, blockAccess, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 3, 1) : CustomColorizer.getCustomColor(CustomColorizer.waterColors, blockAccess, blockPos)) : (!Config.isSwampColors() ? 16777215 : block.colorMultiplier(blockAccess, blockPos)));
    }

    private static int getCustomColor(int[] colors, IBlockAccess blockAccess, BlockPos blockPos) {
        BiomeGenBase bgb = blockAccess.getBiomeGenForCoords(blockPos);
        double temperature = MathHelper.clamp_float(bgb.func_180626_a(blockPos), 0.0F, 1.0F);
        double rainfall = MathHelper.clamp_float(bgb.getFloatRainfall(), 0.0F, 1.0F);
        rainfall *= temperature;
        int cx = (int) ((1.0D - temperature) * 255.0D);
        int cy = (int) ((1.0D - rainfall) * 255.0D);
        return colors[cy << 8 | cx] & 16777215;
    }

    public static void updatePortalFX(EntityFX fx) {
        if (CustomColorizer.particlePortalColor >= 0) {
            int col = CustomColorizer.particlePortalColor;
            int red = col >> 16 & 255;
            int green = col >> 8 & 255;
            int blue = col & 255;
            float redF = red / 255.0F;
            float greenF = green / 255.0F;
            float blueF = blue / 255.0F;
            fx.setRBGColorF(redF, greenF, blueF);
        }
    }

    public static void updateMyceliumFX(EntityFX fx) {
        if (CustomColorizer.myceliumParticleColors != null) {
            int col = CustomColorizer.myceliumParticleColors[CustomColorizer.random.nextInt(CustomColorizer.myceliumParticleColors.length)];
            int red = col >> 16 & 255;
            int green = col >> 8 & 255;
            int blue = col & 255;
            float redF = red / 255.0F;
            float greenF = green / 255.0F;
            float blueF = blue / 255.0F;
            fx.setRBGColorF(redF, greenF, blueF);
        }
    }

    public static void updateReddustFX(EntityFX fx, IBlockAccess blockAccess, double x, double y, double z) {
        if (CustomColorizer.redstoneColors != null) {
            IBlockState state = blockAccess.getBlockState(new BlockPos(x, y, z));
            int level = CustomColorizer.getRedstoneLevel(state, 15);
            int col = CustomColorizer.getRedstoneColor(level);

            if (col != -1) {
                int red = col >> 16 & 255;
                int green = col >> 8 & 255;
                int blue = col & 255;
                float redF = red / 255.0F;
                float greenF = green / 255.0F;
                float blueF = blue / 255.0F;
                fx.setRBGColorF(redF, greenF, blueF);
            }
        }
    }

    private static int getRedstoneLevel(IBlockState state, int def) {
        Block block = state.getBlock();

        if (!(block instanceof BlockRedstoneWire)) {
            return def;
        } else {
            Comparable val = state.getValue(BlockRedstoneWire.POWER);

            if (!(val instanceof Integer)) {
                return def;
            } else {
                Integer valInt = (Integer) val;
                return valInt.intValue();
            }
        }
    }

    public static int getRedstoneColor(int level) {
        return CustomColorizer.redstoneColors == null ? -1 : (level >= 0 && level <= 15 ? CustomColorizer.redstoneColors[level] & 16777215 : -1);
    }

    public static void updateWaterFX(EntityFX fx, IBlockAccess blockAccess, double x, double y, double z) {
        if (CustomColorizer.waterColors != null) {
            int col = CustomColorizer.getFluidColor(Blocks.water, blockAccess, new BlockPos(x, y, z));
            int red = col >> 16 & 255;
            int green = col >> 8 & 255;
            int blue = col & 255;
            float redF = red / 255.0F;
            float greenF = green / 255.0F;
            float blueF = blue / 255.0F;

            if (CustomColorizer.particleWaterColor >= 0) {
                int redDrop = CustomColorizer.particleWaterColor >> 16 & 255;
                int greenDrop = CustomColorizer.particleWaterColor >> 8 & 255;
                int blueDrop = CustomColorizer.particleWaterColor & 255;
                redF *= redDrop / 255.0F;
                greenF *= greenDrop / 255.0F;
                blueF *= blueDrop / 255.0F;
            }

            fx.setRBGColorF(redF, greenF, blueF);
        }
    }

    public static int getLilypadColorMultiplier(IBlockAccess blockAccess, BlockPos blockPos) {
        return CustomColorizer.lilyPadColor < 0 ? Blocks.waterlily.colorMultiplier(blockAccess, blockPos) : CustomColorizer.lilyPadColor;
    }

    public static Vec3 getFogColorNether(Vec3 col) {
        return CustomColorizer.fogColorNether == null ? col : CustomColorizer.fogColorNether;
    }

    public static Vec3 getFogColorEnd(Vec3 col) {
        return CustomColorizer.fogColorEnd == null ? col : CustomColorizer.fogColorEnd;
    }

    public static Vec3 getSkyColorEnd(Vec3 col) {
        return CustomColorizer.skyColorEnd == null ? col : CustomColorizer.skyColorEnd;
    }

    public static Vec3 getSkyColor(Vec3 skyColor3d, IBlockAccess blockAccess, double x, double y, double z) {
        if (CustomColorizer.skyColors == null) {
            return skyColor3d;
        } else {
            int col = CustomColorizer.getSmoothColor(CustomColorizer.skyColors, blockAccess, x, y, z, 7, 1);
            int red = col >> 16 & 255;
            int green = col >> 8 & 255;
            int blue = col & 255;
            float redF = red / 255.0F;
            float greenF = green / 255.0F;
            float blueF = blue / 255.0F;
            float cRed = (float) skyColor3d.xCoord / 0.5F;
            float cGreen = (float) skyColor3d.yCoord / 0.66275F;
            float cBlue = (float) skyColor3d.zCoord;
            redF *= cRed;
            greenF *= cGreen;
            blueF *= cBlue;
            return new Vec3(redF, greenF, blueF);
        }
    }

    public static Vec3 getFogColor(Vec3 fogColor3d, IBlockAccess blockAccess, double x, double y, double z) {
        if (CustomColorizer.fogColors == null) {
            return fogColor3d;
        } else {
            int col = CustomColorizer.getSmoothColor(CustomColorizer.fogColors, blockAccess, x, y, z, 7, 1);
            int red = col >> 16 & 255;
            int green = col >> 8 & 255;
            int blue = col & 255;
            float redF = red / 255.0F;
            float greenF = green / 255.0F;
            float blueF = blue / 255.0F;
            float cRed = (float) fogColor3d.xCoord / 0.753F;
            float cGreen = (float) fogColor3d.yCoord / 0.8471F;
            float cBlue = (float) fogColor3d.zCoord;
            redF *= cRed;
            greenF *= cGreen;
            blueF *= cBlue;
            return new Vec3(redF, greenF, blueF);
        }
    }

    public static Vec3 getUnderwaterColor(IBlockAccess blockAccess, double x, double y, double z) {
        if (CustomColorizer.underwaterColors == null) {
            return null;
        } else {
            int col = CustomColorizer.getSmoothColor(CustomColorizer.underwaterColors, blockAccess, x, y, z, 7, 1);
            int red = col >> 16 & 255;
            int green = col >> 8 & 255;
            int blue = col & 255;
            float redF = red / 255.0F;
            float greenF = green / 255.0F;
            float blueF = blue / 255.0F;
            return new Vec3(redF, greenF, blueF);
        }
    }

    public static int getSmoothColor(int[] colors, IBlockAccess blockAccess, double x, double y, double z, int samples, int step) {
        if (colors == null) {
            return -1;
        } else {
            int x0 = MathHelper.floor_double(x);
            int y0 = MathHelper.floor_double(y);
            int z0 = MathHelper.floor_double(z);
            int n = samples * step / 2;
            int sumRed = 0;
            int sumGreen = 0;
            int sumBlue = 0;
            int count = 0;
            BlockPosM blockPosM = new BlockPosM(0, 0, 0);
            int r;
            int g;
            int b;

            for (r = x0 - n; r <= x0 + n; r += step) {
                for (g = z0 - n; g <= z0 + n; g += step) {
                    blockPosM.setXyz(r, y0, g);
                    b = CustomColorizer.getCustomColor(colors, blockAccess, blockPosM);
                    sumRed += b >> 16 & 255;
                    sumGreen += b >> 8 & 255;
                    sumBlue += b & 255;
                    ++count;
                }
            }

            r = sumRed / count;
            g = sumGreen / count;
            b = sumBlue / count;
            return r << 16 | g << 8 | b;
        }
    }

    public static int mixColors(int c1, int c2, float w1) {
        if (w1 <= 0.0F) {
            return c2;
        } else if (w1 >= 1.0F) {
            return c1;
        } else {
            float w2 = 1.0F - w1;
            int r1 = c1 >> 16 & 255;
            int g1 = c1 >> 8 & 255;
            int b1 = c1 & 255;
            int r2 = c2 >> 16 & 255;
            int g2 = c2 >> 8 & 255;
            int b2 = c2 & 255;
            int r = (int) (r1 * w1 + r2 * w2);
            int g = (int) (g1 * w1 + g2 * w2);
            int b = (int) (b1 * w1 + b2 * w2);
            return r << 16 | g << 8 | b;
        }
    }

    private static int averageColor(int c1, int c2) {
        int r1 = c1 >> 16 & 255;
        int g1 = c1 >> 8 & 255;
        int b1 = c1 & 255;
        int r2 = c2 >> 16 & 255;
        int g2 = c2 >> 8 & 255;
        int b2 = c2 & 255;
        int r = (r1 + r2) / 2;
        int g = (g1 + g2) / 2;
        int b = (b1 + b2) / 2;
        return r << 16 | g << 8 | b;
    }

    public static int getStemColorMultiplier(Block blockStem, IBlockAccess blockAccess, BlockPos blockPos, RenderEnv renderEnv) {
        if (CustomColorizer.stemColors == null) {
            return blockStem.colorMultiplier(blockAccess, blockPos);
        } else {
            int level = renderEnv.getMetadata();

            if (level < 0) {
                level = 0;
            }

            if (level >= CustomColorizer.stemColors.length) {
                level = CustomColorizer.stemColors.length - 1;
            }

            return CustomColorizer.stemColors[level];
        }
    }

    public static boolean updateLightmap(World world, float torchFlickerX, int[] lmColors, boolean nightvision) {
        if (world == null) {
            return false;
        } else if (CustomColorizer.lightMapsColorsRgb == null) {
            return false;
        } else if (!Config.isCustomColors()) {
            return false;
        } else {
            int worldType = world.provider.getDimensionId();

            if (worldType >= -1 && worldType <= 1) {
                int lightMapIndex = worldType + 1;
                float[][] lightMapRgb = CustomColorizer.lightMapsColorsRgb[lightMapIndex];

                if (lightMapRgb == null) {
                    return false;
                } else {
                    int height = CustomColorizer.lightMapsHeight[lightMapIndex];

                    if (nightvision && height < 64) {
                        return false;
                    } else {
                        int width = lightMapRgb.length / height;

                        if (width < 16) {
                            Config.warn("Invalid lightmap width: " + width + " for: /environment/lightmap" + worldType + ".png");
                            CustomColorizer.lightMapsColorsRgb[lightMapIndex] = null;
                            return false;
                        } else {
                            int startIndex = 0;

                            if (nightvision) {
                                startIndex = width * 16 * 2;
                            }

                            float sun = 1.1666666F * (world.getSunBrightness(1.0F) - 0.2F);

                            if (world.func_175658_ac() > 0) {
                                sun = 1.0F;
                            }

                            sun = Config.limitTo1(sun);
                            float sunX = sun * (width - 1);
                            float torchX = Config.limitTo1(torchFlickerX + 0.5F) * (width - 1);
                            float gamma = Config.limitTo1(Config.getGameSettings().gammaSetting);
                            boolean hasGamma = gamma > 1.0E-4F;
                            CustomColorizer.getLightMapColumn(lightMapRgb, sunX, startIndex, width, CustomColorizer.sunRgbs);
                            CustomColorizer.getLightMapColumn(lightMapRgb, torchX, startIndex + 16 * width, width, CustomColorizer.torchRgbs);
                            float[] rgb = new float[3];

                            for (int is = 0; is < 16; ++is) {
                                for (int it = 0; it < 16; ++it) {
                                    int r;

                                    for (r = 0; r < 3; ++r) {
                                        float g = Config.limitTo1(CustomColorizer.sunRgbs[is][r] + CustomColorizer.torchRgbs[it][r]);

                                        if (hasGamma) {
                                            float b = 1.0F - g;
                                            b = 1.0F - b * b * b * b;
                                            g = gamma * b + (1.0F - gamma) * g;
                                        }

                                        rgb[r] = g;
                                    }

                                    r = (int) (rgb[0] * 255.0F);
                                    int var21 = (int) (rgb[1] * 255.0F);
                                    int var22 = (int) (rgb[2] * 255.0F);
                                    lmColors[is * 16 + it] = -16777216 | r << 16 | var21 << 8 | var22;
                                }
                            }

                            return true;
                        }
                    }
                }
            } else {
                return false;
            }
        }
    }

    private static void getLightMapColumn(float[][] origMap, float x, int offset, int width, float[][] colRgb) {
        int xLow = (int) Math.floor(x);
        int xHigh = (int) Math.ceil(x);

        if (xLow == xHigh) {
            for (int var14 = 0; var14 < 16; ++var14) {
                float[] var15 = origMap[offset + var14 * width + xLow];
                float[] var16 = colRgb[var14];

                for (int var17 = 0; var17 < 3; ++var17) {
                    var16[var17] = var15[var17];
                }
            }
        } else {
            float dLow = 1.0F - (x - xLow);
            float dHigh = 1.0F - (xHigh - x);

            for (int y = 0; y < 16; ++y) {
                float[] rgbLow = origMap[offset + y * width + xLow];
                float[] rgbHigh = origMap[offset + y * width + xHigh];
                float[] rgb = colRgb[y];

                for (int i = 0; i < 3; ++i) {
                    rgb[i] = rgbLow[i] * dLow + rgbHigh[i] * dHigh;
                }
            }
        }
    }

    public static Vec3 getWorldFogColor(Vec3 fogVec, WorldClient world, Entity renderViewEntity, float partialTicks) {
        int worldType = world.provider.getDimensionId();

        switch (worldType) {
            case -1:
                fogVec = CustomColorizer.getFogColorNether(fogVec);
                break;

            case 0:
                Minecraft mc = Minecraft.getMinecraft();
                fogVec = CustomColorizer.getFogColor(fogVec, mc.theWorld, renderViewEntity.posX, renderViewEntity.posY + 1.0D, renderViewEntity.posZ);
                break;

            case 1:
                fogVec = CustomColorizer.getFogColorEnd(fogVec);
        }

        return fogVec;
    }

    public static Vec3 getWorldSkyColor(Vec3 skyVec, WorldClient world, Entity renderViewEntity, float partialTicks) {
        int worldType = world.provider.getDimensionId();

        switch (worldType) {
            case 0:
                Minecraft mc = Minecraft.getMinecraft();
                skyVec = CustomColorizer.getSkyColor(skyVec, mc.theWorld, renderViewEntity.posX, renderViewEntity.posY + 1.0D, renderViewEntity.posZ);
                break;

            case 1:
                skyVec = CustomColorizer.getSkyColorEnd(skyVec);
        }

        return skyVec;
    }
}
