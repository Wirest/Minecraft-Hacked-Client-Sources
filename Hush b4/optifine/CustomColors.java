// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.entity.EntityList;
import net.minecraft.potion.Potion;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.BlockStem;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.Iterator;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Set;
import java.util.Map;
import org.apache.commons.lang3.tuple.ImmutablePair;
import java.util.HashMap;
import java.util.List;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.potion.PotionHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Vec3;

public class CustomColors
{
    private static String paletteFormatDefault;
    private static CustomColormap waterColors;
    private static CustomColormap foliagePineColors;
    private static CustomColormap foliageBirchColors;
    private static CustomColormap swampFoliageColors;
    private static CustomColormap swampGrassColors;
    private static CustomColormap[] colorsBlockColormaps;
    private static CustomColormap[][] blockColormaps;
    private static CustomColormap skyColors;
    private static CustomColorFader skyColorFader;
    private static CustomColormap fogColors;
    private static CustomColorFader fogColorFader;
    private static CustomColormap underwaterColors;
    private static CustomColorFader underwaterColorFader;
    private static CustomColormap[] lightMapsColorsRgb;
    private static int lightmapMinDimensionId;
    private static float[][] sunRgbs;
    private static float[][] torchRgbs;
    private static CustomColormap redstoneColors;
    private static CustomColormap xpOrbColors;
    private static int xpOrbTime;
    private static CustomColormap durabilityColors;
    private static CustomColormap stemColors;
    private static CustomColormap stemMelonColors;
    private static CustomColormap stemPumpkinColors;
    private static CustomColormap myceliumParticleColors;
    private static boolean useDefaultGrassFoliageColors;
    private static int particleWaterColor;
    private static int particlePortalColor;
    private static int lilyPadColor;
    private static int expBarTextColor;
    private static int bossTextColor;
    private static int signTextColor;
    private static Vec3 fogColorNether;
    private static Vec3 fogColorEnd;
    private static Vec3 skyColorEnd;
    private static int[] spawnEggPrimaryColors;
    private static int[] spawnEggSecondaryColors;
    private static float[][] wolfCollarColors;
    private static float[][] sheepColors;
    private static int[] textColors;
    private static int[] mapColorsOriginal;
    private static int[] potionColors;
    private static final IBlockState BLOCK_STATE_DIRT;
    private static final IBlockState BLOCK_STATE_WATER;
    public static Random random;
    private static final IColorizer COLORIZER_GRASS;
    private static final IColorizer COLORIZER_FOLIAGE;
    private static final IColorizer COLORIZER_FOLIAGE_PINE;
    private static final IColorizer COLORIZER_FOLIAGE_BIRCH;
    private static final IColorizer COLORIZER_WATER;
    
    static {
        CustomColors.paletteFormatDefault = "vanilla";
        CustomColors.waterColors = null;
        CustomColors.foliagePineColors = null;
        CustomColors.foliageBirchColors = null;
        CustomColors.swampFoliageColors = null;
        CustomColors.swampGrassColors = null;
        CustomColors.colorsBlockColormaps = null;
        CustomColors.blockColormaps = null;
        CustomColors.skyColors = null;
        CustomColors.skyColorFader = new CustomColorFader();
        CustomColors.fogColors = null;
        CustomColors.fogColorFader = new CustomColorFader();
        CustomColors.underwaterColors = null;
        CustomColors.underwaterColorFader = new CustomColorFader();
        CustomColors.lightMapsColorsRgb = null;
        CustomColors.lightmapMinDimensionId = 0;
        CustomColors.sunRgbs = new float[16][3];
        CustomColors.torchRgbs = new float[16][3];
        CustomColors.redstoneColors = null;
        CustomColors.xpOrbColors = null;
        CustomColors.xpOrbTime = -1;
        CustomColors.durabilityColors = null;
        CustomColors.stemColors = null;
        CustomColors.stemMelonColors = null;
        CustomColors.stemPumpkinColors = null;
        CustomColors.myceliumParticleColors = null;
        CustomColors.useDefaultGrassFoliageColors = true;
        CustomColors.particleWaterColor = -1;
        CustomColors.particlePortalColor = -1;
        CustomColors.lilyPadColor = -1;
        CustomColors.expBarTextColor = -1;
        CustomColors.bossTextColor = -1;
        CustomColors.signTextColor = -1;
        CustomColors.fogColorNether = null;
        CustomColors.fogColorEnd = null;
        CustomColors.skyColorEnd = null;
        CustomColors.spawnEggPrimaryColors = null;
        CustomColors.spawnEggSecondaryColors = null;
        CustomColors.wolfCollarColors = null;
        CustomColors.sheepColors = null;
        CustomColors.textColors = null;
        CustomColors.mapColorsOriginal = null;
        CustomColors.potionColors = null;
        BLOCK_STATE_DIRT = Blocks.dirt.getDefaultState();
        BLOCK_STATE_WATER = Blocks.water.getDefaultState();
        CustomColors.random = new Random();
        COLORIZER_GRASS = new IColorizer() {
            @Override
            public int getColor(final IBlockAccess p_getColor_1_, final BlockPos p_getColor_2_) {
                final BiomeGenBase biomegenbase = CustomColors.getColorBiome(p_getColor_1_, p_getColor_2_);
                return (CustomColors.swampGrassColors != null && biomegenbase == BiomeGenBase.swampland) ? CustomColors.swampGrassColors.getColor(biomegenbase, p_getColor_2_) : biomegenbase.getGrassColorAtPos(p_getColor_2_);
            }
            
            @Override
            public boolean isColorConstant() {
                return false;
            }
        };
        COLORIZER_FOLIAGE = new IColorizer() {
            @Override
            public int getColor(final IBlockAccess p_getColor_1_, final BlockPos p_getColor_2_) {
                final BiomeGenBase biomegenbase = CustomColors.getColorBiome(p_getColor_1_, p_getColor_2_);
                return (CustomColors.swampFoliageColors != null && biomegenbase == BiomeGenBase.swampland) ? CustomColors.swampFoliageColors.getColor(biomegenbase, p_getColor_2_) : biomegenbase.getFoliageColorAtPos(p_getColor_2_);
            }
            
            @Override
            public boolean isColorConstant() {
                return false;
            }
        };
        COLORIZER_FOLIAGE_PINE = new IColorizer() {
            @Override
            public int getColor(final IBlockAccess p_getColor_1_, final BlockPos p_getColor_2_) {
                return (CustomColors.foliagePineColors != null) ? CustomColors.foliagePineColors.getColor(p_getColor_1_, p_getColor_2_) : ColorizerFoliage.getFoliageColorPine();
            }
            
            @Override
            public boolean isColorConstant() {
                return CustomColors.foliagePineColors == null;
            }
        };
        COLORIZER_FOLIAGE_BIRCH = new IColorizer() {
            @Override
            public int getColor(final IBlockAccess p_getColor_1_, final BlockPos p_getColor_2_) {
                return (CustomColors.foliageBirchColors != null) ? CustomColors.foliageBirchColors.getColor(p_getColor_1_, p_getColor_2_) : ColorizerFoliage.getFoliageColorBirch();
            }
            
            @Override
            public boolean isColorConstant() {
                return CustomColors.foliageBirchColors == null;
            }
        };
        COLORIZER_WATER = new IColorizer() {
            @Override
            public int getColor(final IBlockAccess p_getColor_1_, final BlockPos p_getColor_2_) {
                final BiomeGenBase biomegenbase = CustomColors.getColorBiome(p_getColor_1_, p_getColor_2_);
                return (CustomColors.waterColors != null) ? CustomColors.waterColors.getColor(biomegenbase, p_getColor_2_) : (Reflector.ForgeBiome_getWaterColorMultiplier.exists() ? Reflector.callInt(biomegenbase, Reflector.ForgeBiome_getWaterColorMultiplier, new Object[0]) : biomegenbase.waterColorMultiplier);
            }
            
            @Override
            public boolean isColorConstant() {
                return false;
            }
        };
    }
    
    public static void update() {
        CustomColors.paletteFormatDefault = "vanilla";
        CustomColors.waterColors = null;
        CustomColors.foliageBirchColors = null;
        CustomColors.foliagePineColors = null;
        CustomColors.swampGrassColors = null;
        CustomColors.swampFoliageColors = null;
        CustomColors.skyColors = null;
        CustomColors.fogColors = null;
        CustomColors.underwaterColors = null;
        CustomColors.redstoneColors = null;
        CustomColors.xpOrbColors = null;
        CustomColors.xpOrbTime = -1;
        CustomColors.durabilityColors = null;
        CustomColors.stemColors = null;
        CustomColors.myceliumParticleColors = null;
        CustomColors.lightMapsColorsRgb = null;
        CustomColors.particleWaterColor = -1;
        CustomColors.particlePortalColor = -1;
        CustomColors.lilyPadColor = -1;
        CustomColors.expBarTextColor = -1;
        CustomColors.bossTextColor = -1;
        CustomColors.signTextColor = -1;
        CustomColors.fogColorNether = null;
        CustomColors.fogColorEnd = null;
        CustomColors.skyColorEnd = null;
        CustomColors.colorsBlockColormaps = null;
        CustomColors.blockColormaps = null;
        CustomColors.useDefaultGrassFoliageColors = true;
        CustomColors.spawnEggPrimaryColors = null;
        CustomColors.spawnEggSecondaryColors = null;
        CustomColors.wolfCollarColors = null;
        CustomColors.sheepColors = null;
        CustomColors.textColors = null;
        setMapColors(CustomColors.mapColorsOriginal);
        CustomColors.potionColors = null;
        PotionHelper.clearPotionColorCache();
        CustomColors.paletteFormatDefault = getValidProperty("mcpatcher/color.properties", "palette.format", CustomColormap.FORMAT_STRINGS, "vanilla");
        final String s = "mcpatcher/colormap/";
        final String[] astring = { "water.png", "watercolorX.png" };
        CustomColors.waterColors = getCustomColors(s, astring, 256, 256);
        updateUseDefaultGrassFoliageColors();
        if (Config.isCustomColors()) {
            final String[] astring2 = { "pine.png", "pinecolor.png" };
            CustomColors.foliagePineColors = getCustomColors(s, astring2, 256, 256);
            final String[] astring3 = { "birch.png", "birchcolor.png" };
            CustomColors.foliageBirchColors = getCustomColors(s, astring3, 256, 256);
            final String[] astring4 = { "swampgrass.png", "swampgrasscolor.png" };
            CustomColors.swampGrassColors = getCustomColors(s, astring4, 256, 256);
            final String[] astring5 = { "swampfoliage.png", "swampfoliagecolor.png" };
            CustomColors.swampFoliageColors = getCustomColors(s, astring5, 256, 256);
            final String[] astring6 = { "sky0.png", "skycolor0.png" };
            CustomColors.skyColors = getCustomColors(s, astring6, 256, 256);
            final String[] astring7 = { "fog0.png", "fogcolor0.png" };
            CustomColors.fogColors = getCustomColors(s, astring7, 256, 256);
            final String[] astring8 = { "underwater.png", "underwatercolor.png" };
            CustomColors.underwaterColors = getCustomColors(s, astring8, 256, 256);
            final String[] astring9 = { "redstone.png", "redstonecolor.png" };
            CustomColors.redstoneColors = getCustomColors(s, astring9, 16, 1);
            CustomColors.xpOrbColors = getCustomColors(String.valueOf(s) + "xporb.png", -1, -1);
            CustomColors.durabilityColors = getCustomColors(String.valueOf(s) + "durability.png", -1, -1);
            final String[] astring10 = { "stem.png", "stemcolor.png" };
            CustomColors.stemColors = getCustomColors(s, astring10, 8, 1);
            CustomColors.stemPumpkinColors = getCustomColors(String.valueOf(s) + "pumpkinstem.png", 8, 1);
            CustomColors.stemMelonColors = getCustomColors(String.valueOf(s) + "melonstem.png", 8, 1);
            final String[] astring11 = { "myceliumparticle.png", "myceliumparticlecolor.png" };
            CustomColors.myceliumParticleColors = getCustomColors(s, astring11, -1, -1);
            final Pair<CustomColormap[], Integer> pair = parseLightmapsRgb();
            CustomColors.lightMapsColorsRgb = pair.getLeft();
            CustomColors.lightmapMinDimensionId = pair.getRight();
            readColorProperties("mcpatcher/color.properties");
            CustomColors.blockColormaps = readBlockColormaps(new String[] { String.valueOf(s) + "custom/", String.valueOf(s) + "blocks/" }, CustomColors.colorsBlockColormaps, 256, 256);
            updateUseDefaultGrassFoliageColors();
        }
    }
    
    private static String getValidProperty(final String p_getValidProperty_0_, final String p_getValidProperty_1_, final String[] p_getValidProperty_2_, final String p_getValidProperty_3_) {
        try {
            final ResourceLocation resourcelocation = new ResourceLocation(p_getValidProperty_0_);
            final InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return p_getValidProperty_3_;
            }
            final Properties properties = new Properties();
            properties.load(inputstream);
            inputstream.close();
            final String s = properties.getProperty(p_getValidProperty_1_);
            if (s == null) {
                return p_getValidProperty_3_;
            }
            final List<String> list = Arrays.asList(p_getValidProperty_2_);
            if (!list.contains(s)) {
                warn("Invalid value: " + p_getValidProperty_1_ + "=" + s);
                warn("Expected values: " + Config.arrayToString(p_getValidProperty_2_));
                return p_getValidProperty_3_;
            }
            dbg(p_getValidProperty_1_ + "=" + s);
            return s;
        }
        catch (FileNotFoundException var9) {
            return p_getValidProperty_3_;
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
            return p_getValidProperty_3_;
        }
    }
    
    private static Pair<CustomColormap[], Integer> parseLightmapsRgb() {
        final String s = "mcpatcher/lightmap/world";
        final String s2 = ".png";
        final String[] astring = ResUtils.collectFiles(s, s2);
        final Map<Integer, String> map = new HashMap<Integer, String>();
        for (int i = 0; i < astring.length; ++i) {
            final String s3 = astring[i];
            final String s4 = StrUtils.removePrefixSuffix(s3, s, s2);
            final int j = Config.parseInt(s4, Integer.MIN_VALUE);
            if (j == Integer.MIN_VALUE) {
                warn("Invalid dimension ID: " + s4 + ", path: " + s3);
            }
            else {
                map.put(j, s3);
            }
        }
        final Set<Integer> set = map.keySet();
        final Integer[] ainteger = set.toArray(new Integer[set.size()]);
        Arrays.sort(ainteger);
        if (ainteger.length <= 0) {
            return new ImmutablePair<CustomColormap[], Integer>(null, 0);
        }
        final int j2 = ainteger[0];
        final int k1 = ainteger[ainteger.length - 1];
        final int l = k1 - j2 + 1;
        final CustomColormap[] acustomcolormap = new CustomColormap[l];
        for (int m = 0; m < ainteger.length; ++m) {
            final Integer integer = ainteger[m];
            final String s5 = map.get(integer);
            final CustomColormap customcolormap = getCustomColors(s5, -1, -1);
            if (customcolormap != null) {
                if (customcolormap.getWidth() < 16) {
                    warn("Invalid lightmap width: " + customcolormap.getWidth() + ", path: " + s5);
                }
                else {
                    final int i2 = integer - j2;
                    acustomcolormap[i2] = customcolormap;
                }
            }
        }
        return new ImmutablePair<CustomColormap[], Integer>(acustomcolormap, j2);
    }
    
    private static int getTextureHeight(final String p_getTextureHeight_0_, final int p_getTextureHeight_1_) {
        try {
            final InputStream inputstream = Config.getResourceStream(new ResourceLocation(p_getTextureHeight_0_));
            if (inputstream == null) {
                return p_getTextureHeight_1_;
            }
            final BufferedImage bufferedimage = ImageIO.read(inputstream);
            inputstream.close();
            return (bufferedimage == null) ? p_getTextureHeight_1_ : bufferedimage.getHeight();
        }
        catch (IOException var4) {
            return p_getTextureHeight_1_;
        }
    }
    
    private static void readColorProperties(final String p_readColorProperties_0_) {
        try {
            final ResourceLocation resourcelocation = new ResourceLocation(p_readColorProperties_0_);
            final InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return;
            }
            dbg("Loading " + p_readColorProperties_0_);
            final Properties properties = new Properties();
            properties.load(inputstream);
            inputstream.close();
            CustomColors.particleWaterColor = readColor(properties, new String[] { "particle.water", "drop.water" });
            CustomColors.particlePortalColor = readColor(properties, "particle.portal");
            CustomColors.lilyPadColor = readColor(properties, "lilypad");
            CustomColors.expBarTextColor = readColor(properties, "text.xpbar");
            CustomColors.bossTextColor = readColor(properties, "text.boss");
            CustomColors.signTextColor = readColor(properties, "text.sign");
            CustomColors.fogColorNether = readColorVec3(properties, "fog.nether");
            CustomColors.fogColorEnd = readColorVec3(properties, "fog.end");
            CustomColors.skyColorEnd = readColorVec3(properties, "sky.end");
            CustomColors.colorsBlockColormaps = readCustomColormaps(properties, p_readColorProperties_0_);
            CustomColors.spawnEggPrimaryColors = readSpawnEggColors(properties, p_readColorProperties_0_, "egg.shell.", "Spawn egg shell");
            CustomColors.spawnEggSecondaryColors = readSpawnEggColors(properties, p_readColorProperties_0_, "egg.spots.", "Spawn egg spot");
            CustomColors.wolfCollarColors = readDyeColors(properties, p_readColorProperties_0_, "collar.", "Wolf collar");
            CustomColors.sheepColors = readDyeColors(properties, p_readColorProperties_0_, "sheep.", "Sheep");
            CustomColors.textColors = readTextColors(properties, p_readColorProperties_0_, "text.code.", "Text");
            final int[] aint = readMapColors(properties, p_readColorProperties_0_, "map.", "Map");
            if (aint != null) {
                if (CustomColors.mapColorsOriginal == null) {
                    CustomColors.mapColorsOriginal = getMapColors();
                }
                setMapColors(aint);
            }
            CustomColors.potionColors = readPotionColors(properties, p_readColorProperties_0_, "potion.", "Potion");
            CustomColors.xpOrbTime = Config.parseInt(properties.getProperty("xporb.time"), -1);
        }
        catch (FileNotFoundException var5) {}
        catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    private static CustomColormap[] readCustomColormaps(final Properties p_readCustomColormaps_0_, final String p_readCustomColormaps_1_) {
        final List list = new ArrayList();
        final String s = "palette.block.";
        final Map map = new HashMap();
        for (final Object s2 : p_readCustomColormaps_0_.keySet()) {
            final String s3 = p_readCustomColormaps_0_.getProperty((String)s2);
            if (((String)s2).startsWith(s)) {
                map.put(s2, s3);
            }
        }
        final String[] astring = (String[])map.keySet().toArray(new String[map.size()]);
        for (int j = 0; j < astring.length; ++j) {
            final String s4 = astring[j];
            final String s5 = p_readCustomColormaps_0_.getProperty(s4);
            dbg("Block palette: " + s4 + " = " + s5);
            String s6 = s4.substring(s.length());
            final String s7 = TextureUtils.getBasePath(p_readCustomColormaps_1_);
            s6 = TextureUtils.fixResourcePath(s6, s7);
            final CustomColormap customcolormap = getCustomColors(s6, 256, 256);
            if (customcolormap == null) {
                warn("Colormap not found: " + s6);
            }
            else {
                final ConnectedParser connectedparser = new ConnectedParser("CustomColors");
                final MatchBlock[] amatchblock = connectedparser.parseMatchBlocks(s5);
                if (amatchblock != null && amatchblock.length > 0) {
                    for (int i = 0; i < amatchblock.length; ++i) {
                        final MatchBlock matchblock = amatchblock[i];
                        customcolormap.addMatchBlock(matchblock);
                    }
                    list.add(customcolormap);
                }
                else {
                    warn("Invalid match blocks: " + s5);
                }
            }
        }
        if (list.size() <= 0) {
            return null;
        }
        final CustomColormap[] acustomcolormap = list.toArray(new CustomColormap[list.size()]);
        return acustomcolormap;
    }
    
    private static CustomColormap[][] readBlockColormaps(final String[] p_readBlockColormaps_0_, final CustomColormap[] p_readBlockColormaps_1_, final int p_readBlockColormaps_2_, final int p_readBlockColormaps_3_) {
        final String[] astring = ResUtils.collectFiles(p_readBlockColormaps_0_, new String[] { ".properties" });
        Arrays.sort(astring);
        final List list = new ArrayList();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            dbg("Block colormap: " + s);
            try {
                final ResourceLocation resourcelocation = new ResourceLocation("minecraft", s);
                final InputStream inputstream = Config.getResourceStream(resourcelocation);
                if (inputstream == null) {
                    warn("File not found: " + s);
                }
                else {
                    final Properties properties = new Properties();
                    properties.load(inputstream);
                    final CustomColormap customcolormap = new CustomColormap(properties, s, p_readBlockColormaps_2_, p_readBlockColormaps_3_, CustomColors.paletteFormatDefault);
                    if (customcolormap.isValid(s) && customcolormap.isValidMatchBlocks(s)) {
                        addToBlockList(customcolormap, list);
                    }
                }
            }
            catch (FileNotFoundException var12) {
                warn("File not found: " + s);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (p_readBlockColormaps_1_ != null) {
            for (int j = 0; j < p_readBlockColormaps_1_.length; ++j) {
                final CustomColormap customcolormap2 = p_readBlockColormaps_1_[j];
                addToBlockList(customcolormap2, list);
            }
        }
        if (list.size() <= 0) {
            return null;
        }
        final CustomColormap[][] acustomcolormap = blockListToArray(list);
        return acustomcolormap;
    }
    
    private static void addToBlockList(final CustomColormap p_addToBlockList_0_, final List p_addToBlockList_1_) {
        final int[] aint = p_addToBlockList_0_.getMatchBlockIds();
        if (aint != null && aint.length > 0) {
            for (int i = 0; i < aint.length; ++i) {
                final int j = aint[i];
                if (j < 0) {
                    warn("Invalid block ID: " + j);
                }
                else {
                    addToList(p_addToBlockList_0_, p_addToBlockList_1_, j);
                }
            }
        }
        else {
            warn("No match blocks: " + Config.arrayToString(aint));
        }
    }
    
    private static void addToList(final CustomColormap p_addToList_0_, final List p_addToList_1_, final int p_addToList_2_) {
        while (p_addToList_2_ >= p_addToList_1_.size()) {
            p_addToList_1_.add(null);
        }
        List list = p_addToList_1_.get(p_addToList_2_);
        if (list == null) {
            list = new ArrayList();
            p_addToList_1_.set(p_addToList_2_, list);
        }
        list.add(p_addToList_0_);
    }
    
    private static CustomColormap[][] blockListToArray(final List p_blockListToArray_0_) {
        final CustomColormap[][] acustomcolormap = new CustomColormap[p_blockListToArray_0_.size()][];
        for (int i = 0; i < p_blockListToArray_0_.size(); ++i) {
            final List list = p_blockListToArray_0_.get(i);
            if (list != null) {
                final CustomColormap[] acustomcolormap2 = list.toArray(new CustomColormap[list.size()]);
                acustomcolormap[i] = acustomcolormap2;
            }
        }
        return acustomcolormap;
    }
    
    private static int readColor(final Properties p_readColor_0_, final String[] p_readColor_1_) {
        for (int i = 0; i < p_readColor_1_.length; ++i) {
            final String s = p_readColor_1_[i];
            final int j = readColor(p_readColor_0_, s);
            if (j >= 0) {
                return j;
            }
        }
        return -1;
    }
    
    private static int readColor(final Properties p_readColor_0_, final String p_readColor_1_) {
        String s = p_readColor_0_.getProperty(p_readColor_1_);
        if (s == null) {
            return -1;
        }
        s = s.trim();
        final int i = parseColor(s);
        if (i < 0) {
            warn("Invalid color: " + p_readColor_1_ + " = " + s);
            return i;
        }
        dbg(String.valueOf(p_readColor_1_) + " = " + s);
        return i;
    }
    
    private static int parseColor(String p_parseColor_0_) {
        if (p_parseColor_0_ == null) {
            return -1;
        }
        p_parseColor_0_ = p_parseColor_0_.trim();
        try {
            final int i = Integer.parseInt(p_parseColor_0_, 16) & 0xFFFFFF;
            return i;
        }
        catch (NumberFormatException var2) {
            return -1;
        }
    }
    
    private static Vec3 readColorVec3(final Properties p_readColorVec3_0_, final String p_readColorVec3_1_) {
        final int i = readColor(p_readColorVec3_0_, p_readColorVec3_1_);
        if (i < 0) {
            return null;
        }
        final int j = i >> 16 & 0xFF;
        final int k = i >> 8 & 0xFF;
        final int l = i & 0xFF;
        final float f = j / 255.0f;
        final float f2 = k / 255.0f;
        final float f3 = l / 255.0f;
        return new Vec3(f, f2, f3);
    }
    
    private static CustomColormap getCustomColors(final String p_getCustomColors_0_, final String[] p_getCustomColors_1_, final int p_getCustomColors_2_, final int p_getCustomColors_3_) {
        for (int i = 0; i < p_getCustomColors_1_.length; ++i) {
            String s = p_getCustomColors_1_[i];
            s = String.valueOf(p_getCustomColors_0_) + s;
            final CustomColormap customcolormap = getCustomColors(s, p_getCustomColors_2_, p_getCustomColors_3_);
            if (customcolormap != null) {
                return customcolormap;
            }
        }
        return null;
    }
    
    public static CustomColormap getCustomColors(final String p_getCustomColors_0_, final int p_getCustomColors_1_, final int p_getCustomColors_2_) {
        try {
            final ResourceLocation resourcelocation = new ResourceLocation(p_getCustomColors_0_);
            if (!Config.hasResource(resourcelocation)) {
                return null;
            }
            dbg("Colormap " + p_getCustomColors_0_);
            final Properties properties = new Properties();
            String s = StrUtils.replaceSuffix(p_getCustomColors_0_, ".png", ".properties");
            final ResourceLocation resourcelocation2 = new ResourceLocation(s);
            if (Config.hasResource(resourcelocation2)) {
                final InputStream inputstream = Config.getResourceStream(resourcelocation2);
                properties.load(inputstream);
                inputstream.close();
                dbg("Colormap properties: " + s);
            }
            else {
                properties.put("format", CustomColors.paletteFormatDefault);
                properties.put("source", p_getCustomColors_0_);
                s = p_getCustomColors_0_;
            }
            final CustomColormap customcolormap = new CustomColormap(properties, s, p_getCustomColors_1_, p_getCustomColors_2_, CustomColors.paletteFormatDefault);
            return customcolormap.isValid(s) ? customcolormap : null;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
    
    public static void updateUseDefaultGrassFoliageColors() {
        CustomColors.useDefaultGrassFoliageColors = (CustomColors.foliageBirchColors == null && CustomColors.foliagePineColors == null && CustomColors.swampGrassColors == null && CustomColors.swampFoliageColors == null && Config.isSwampColors() && Config.isSmoothBiomes());
    }
    
    public static int getColorMultiplier(final BakedQuad p_getColorMultiplier_0_, final Block p_getColorMultiplier_1_, final IBlockAccess p_getColorMultiplier_2_, BlockPos p_getColorMultiplier_3_, final RenderEnv p_getColorMultiplier_4_) {
        if (CustomColors.blockColormaps != null) {
            IBlockState iblockstate = p_getColorMultiplier_4_.getBlockState();
            if (!p_getColorMultiplier_0_.hasTintIndex()) {
                if (p_getColorMultiplier_1_ == Blocks.grass) {
                    iblockstate = CustomColors.BLOCK_STATE_DIRT;
                }
                if (p_getColorMultiplier_1_ == Blocks.redstone_wire) {
                    return -1;
                }
            }
            if (p_getColorMultiplier_1_ == Blocks.double_plant && p_getColorMultiplier_4_.getMetadata() >= 8) {
                p_getColorMultiplier_3_ = p_getColorMultiplier_3_.down();
                iblockstate = p_getColorMultiplier_2_.getBlockState(p_getColorMultiplier_3_);
            }
            final CustomColormap customcolormap = getBlockColormap(iblockstate);
            if (customcolormap != null) {
                if (Config.isSmoothBiomes() && !customcolormap.isColorConstant()) {
                    return getSmoothColorMultiplier(p_getColorMultiplier_2_, p_getColorMultiplier_3_, customcolormap, p_getColorMultiplier_4_.getColorizerBlockPosM());
                }
                return customcolormap.getColor(p_getColorMultiplier_2_, p_getColorMultiplier_3_);
            }
        }
        if (!p_getColorMultiplier_0_.hasTintIndex()) {
            return -1;
        }
        if (p_getColorMultiplier_1_ == Blocks.waterlily) {
            return getLilypadColorMultiplier(p_getColorMultiplier_2_, p_getColorMultiplier_3_);
        }
        if (p_getColorMultiplier_1_ == Blocks.redstone_wire) {
            return getRedstoneColor(p_getColorMultiplier_4_.getBlockState());
        }
        if (p_getColorMultiplier_1_ instanceof BlockStem) {
            return getStemColorMultiplier(p_getColorMultiplier_1_, p_getColorMultiplier_2_, p_getColorMultiplier_3_, p_getColorMultiplier_4_);
        }
        if (CustomColors.useDefaultGrassFoliageColors) {
            return -1;
        }
        final int i = p_getColorMultiplier_4_.getMetadata();
        IColorizer customcolors$icolorizer;
        if (p_getColorMultiplier_1_ != Blocks.grass && p_getColorMultiplier_1_ != Blocks.tallgrass && p_getColorMultiplier_1_ != Blocks.double_plant) {
            if (p_getColorMultiplier_1_ == Blocks.double_plant) {
                customcolors$icolorizer = CustomColors.COLORIZER_GRASS;
                if (i >= 8) {
                    p_getColorMultiplier_3_ = p_getColorMultiplier_3_.down();
                }
            }
            else if (p_getColorMultiplier_1_ == Blocks.leaves) {
                switch (i & 0x3) {
                    case 0: {
                        customcolors$icolorizer = CustomColors.COLORIZER_FOLIAGE;
                        break;
                    }
                    case 1: {
                        customcolors$icolorizer = CustomColors.COLORIZER_FOLIAGE_PINE;
                        break;
                    }
                    case 2: {
                        customcolors$icolorizer = CustomColors.COLORIZER_FOLIAGE_BIRCH;
                        break;
                    }
                    default: {
                        customcolors$icolorizer = CustomColors.COLORIZER_FOLIAGE;
                        break;
                    }
                }
            }
            else if (p_getColorMultiplier_1_ == Blocks.leaves2) {
                customcolors$icolorizer = CustomColors.COLORIZER_FOLIAGE;
            }
            else {
                if (p_getColorMultiplier_1_ != Blocks.vine) {
                    return -1;
                }
                customcolors$icolorizer = CustomColors.COLORIZER_FOLIAGE;
            }
        }
        else {
            customcolors$icolorizer = CustomColors.COLORIZER_GRASS;
        }
        return (Config.isSmoothBiomes() && !customcolors$icolorizer.isColorConstant()) ? getSmoothColorMultiplier(p_getColorMultiplier_2_, p_getColorMultiplier_3_, customcolors$icolorizer, p_getColorMultiplier_4_.getColorizerBlockPosM()) : customcolors$icolorizer.getColor(p_getColorMultiplier_2_, p_getColorMultiplier_3_);
    }
    
    protected static BiomeGenBase getColorBiome(final IBlockAccess p_getColorBiome_0_, final BlockPos p_getColorBiome_1_) {
        BiomeGenBase biomegenbase = p_getColorBiome_0_.getBiomeGenForCoords(p_getColorBiome_1_);
        if (biomegenbase == BiomeGenBase.swampland && !Config.isSwampColors()) {
            biomegenbase = BiomeGenBase.plains;
        }
        return biomegenbase;
    }
    
    private static CustomColormap getBlockColormap(final IBlockState p_getBlockColormap_0_) {
        if (CustomColors.blockColormaps == null) {
            return null;
        }
        if (!(p_getBlockColormap_0_ instanceof BlockStateBase)) {
            return null;
        }
        final BlockStateBase blockstatebase = (BlockStateBase)p_getBlockColormap_0_;
        final int i = blockstatebase.getBlockId();
        if (i < 0 || i >= CustomColors.blockColormaps.length) {
            return null;
        }
        final CustomColormap[] acustomcolormap = CustomColors.blockColormaps[i];
        if (acustomcolormap == null) {
            return null;
        }
        for (int j = 0; j < acustomcolormap.length; ++j) {
            final CustomColormap customcolormap = acustomcolormap[j];
            if (customcolormap.matchesBlock(blockstatebase)) {
                return customcolormap;
            }
        }
        return null;
    }
    
    private static int getSmoothColorMultiplier(final IBlockAccess p_getSmoothColorMultiplier_0_, final BlockPos p_getSmoothColorMultiplier_1_, final IColorizer p_getSmoothColorMultiplier_2_, final BlockPosM p_getSmoothColorMultiplier_3_) {
        int i = 0;
        int j = 0;
        int k = 0;
        final int l = p_getSmoothColorMultiplier_1_.getX();
        final int i2 = p_getSmoothColorMultiplier_1_.getY();
        final int j2 = p_getSmoothColorMultiplier_1_.getZ();
        final BlockPosM blockposm = p_getSmoothColorMultiplier_3_;
        for (int k2 = l - 1; k2 <= l + 1; ++k2) {
            for (int l2 = j2 - 1; l2 <= j2 + 1; ++l2) {
                blockposm.setXyz(k2, i2, l2);
                final int i3 = p_getSmoothColorMultiplier_2_.getColor(p_getSmoothColorMultiplier_0_, blockposm);
                i += (i3 >> 16 & 0xFF);
                j += (i3 >> 8 & 0xFF);
                k += (i3 & 0xFF);
            }
        }
        final int j3 = i / 9;
        final int k3 = j / 9;
        final int l3 = k / 9;
        return j3 << 16 | k3 << 8 | l3;
    }
    
    public static int getFluidColor(final IBlockAccess p_getFluidColor_0_, final IBlockState p_getFluidColor_1_, final BlockPos p_getFluidColor_2_, final RenderEnv p_getFluidColor_3_) {
        final Block block = p_getFluidColor_1_.getBlock();
        IColorizer customcolors$icolorizer = getBlockColormap(p_getFluidColor_1_);
        if (customcolors$icolorizer == null && block.getMaterial() == Material.water) {
            customcolors$icolorizer = CustomColors.COLORIZER_WATER;
        }
        return (customcolors$icolorizer == null) ? block.colorMultiplier(p_getFluidColor_0_, p_getFluidColor_2_) : ((Config.isSmoothBiomes() && !customcolors$icolorizer.isColorConstant()) ? getSmoothColorMultiplier(p_getFluidColor_0_, p_getFluidColor_2_, customcolors$icolorizer, p_getFluidColor_3_.getColorizerBlockPosM()) : customcolors$icolorizer.getColor(p_getFluidColor_0_, p_getFluidColor_2_));
    }
    
    public static void updatePortalFX(final EntityFX p_updatePortalFX_0_) {
        if (CustomColors.particlePortalColor >= 0) {
            final int i = CustomColors.particlePortalColor;
            final int j = i >> 16 & 0xFF;
            final int k = i >> 8 & 0xFF;
            final int l = i & 0xFF;
            final float f = j / 255.0f;
            final float f2 = k / 255.0f;
            final float f3 = l / 255.0f;
            p_updatePortalFX_0_.setRBGColorF(f, f2, f3);
        }
    }
    
    public static void updateMyceliumFX(final EntityFX p_updateMyceliumFX_0_) {
        if (CustomColors.myceliumParticleColors != null) {
            final int i = CustomColors.myceliumParticleColors.getColorRandom();
            final int j = i >> 16 & 0xFF;
            final int k = i >> 8 & 0xFF;
            final int l = i & 0xFF;
            final float f = j / 255.0f;
            final float f2 = k / 255.0f;
            final float f3 = l / 255.0f;
            p_updateMyceliumFX_0_.setRBGColorF(f, f2, f3);
        }
    }
    
    private static int getRedstoneColor(final IBlockState p_getRedstoneColor_0_) {
        if (CustomColors.redstoneColors == null) {
            return -1;
        }
        final int i = getRedstoneLevel(p_getRedstoneColor_0_, 15);
        final int j = CustomColors.redstoneColors.getColor(i);
        return j;
    }
    
    public static void updateReddustFX(final EntityFX p_updateReddustFX_0_, final IBlockAccess p_updateReddustFX_1_, final double p_updateReddustFX_2_, final double p_updateReddustFX_4_, final double p_updateReddustFX_6_) {
        if (CustomColors.redstoneColors != null) {
            final IBlockState iblockstate = p_updateReddustFX_1_.getBlockState(new BlockPos(p_updateReddustFX_2_, p_updateReddustFX_4_, p_updateReddustFX_6_));
            final int i = getRedstoneLevel(iblockstate, 15);
            final int j = CustomColors.redstoneColors.getColor(i);
            final int k = j >> 16 & 0xFF;
            final int l = j >> 8 & 0xFF;
            final int i2 = j & 0xFF;
            final float f = k / 255.0f;
            final float f2 = l / 255.0f;
            final float f3 = i2 / 255.0f;
            p_updateReddustFX_0_.setRBGColorF(f, f2, f3);
        }
    }
    
    private static int getRedstoneLevel(final IBlockState p_getRedstoneLevel_0_, final int p_getRedstoneLevel_1_) {
        final Block block = p_getRedstoneLevel_0_.getBlock();
        if (!(block instanceof BlockRedstoneWire)) {
            return p_getRedstoneLevel_1_;
        }
        final Object object = p_getRedstoneLevel_0_.getValue((IProperty<Object>)BlockRedstoneWire.POWER);
        if (!(object instanceof Integer)) {
            return p_getRedstoneLevel_1_;
        }
        final Integer integer = (Integer)object;
        return integer;
    }
    
    public static float getXpOrbTimer(final float p_getXpOrbTimer_0_) {
        if (CustomColors.xpOrbTime <= 0) {
            return p_getXpOrbTimer_0_;
        }
        final float f = 628.0f / CustomColors.xpOrbTime;
        return p_getXpOrbTimer_0_ * f;
    }
    
    public static int getXpOrbColor(final float p_getXpOrbColor_0_) {
        if (CustomColors.xpOrbColors == null) {
            return -1;
        }
        final int i = (int)Math.round((MathHelper.sin(p_getXpOrbColor_0_) + 1.0f) * (CustomColors.xpOrbColors.getLength() - 1) / 2.0);
        final int j = CustomColors.xpOrbColors.getColor(i);
        return j;
    }
    
    public static int getDurabilityColor(final int p_getDurabilityColor_0_) {
        if (CustomColors.durabilityColors == null) {
            return -1;
        }
        final int i = p_getDurabilityColor_0_ * CustomColors.durabilityColors.getLength() / 255;
        final int j = CustomColors.durabilityColors.getColor(i);
        return j;
    }
    
    public static void updateWaterFX(final EntityFX p_updateWaterFX_0_, final IBlockAccess p_updateWaterFX_1_, final double p_updateWaterFX_2_, final double p_updateWaterFX_4_, final double p_updateWaterFX_6_) {
        if (CustomColors.waterColors != null || CustomColors.blockColormaps != null) {
            final BlockPos blockpos = new BlockPos(p_updateWaterFX_2_, p_updateWaterFX_4_, p_updateWaterFX_6_);
            final RenderEnv renderenv = RenderEnv.getInstance(p_updateWaterFX_1_, CustomColors.BLOCK_STATE_WATER, blockpos);
            final int i = getFluidColor(p_updateWaterFX_1_, CustomColors.BLOCK_STATE_WATER, blockpos, renderenv);
            final int j = i >> 16 & 0xFF;
            final int k = i >> 8 & 0xFF;
            final int l = i & 0xFF;
            float f = j / 255.0f;
            float f2 = k / 255.0f;
            float f3 = l / 255.0f;
            if (CustomColors.particleWaterColor >= 0) {
                final int i2 = CustomColors.particleWaterColor >> 16 & 0xFF;
                final int j2 = CustomColors.particleWaterColor >> 8 & 0xFF;
                final int k2 = CustomColors.particleWaterColor & 0xFF;
                f *= i2 / 255.0f;
                f2 *= j2 / 255.0f;
                f3 *= k2 / 255.0f;
            }
            p_updateWaterFX_0_.setRBGColorF(f, f2, f3);
        }
    }
    
    private static int getLilypadColorMultiplier(final IBlockAccess p_getLilypadColorMultiplier_0_, final BlockPos p_getLilypadColorMultiplier_1_) {
        return (CustomColors.lilyPadColor < 0) ? Blocks.waterlily.colorMultiplier(p_getLilypadColorMultiplier_0_, p_getLilypadColorMultiplier_1_) : CustomColors.lilyPadColor;
    }
    
    private static Vec3 getFogColorNether(final Vec3 p_getFogColorNether_0_) {
        return (CustomColors.fogColorNether == null) ? p_getFogColorNether_0_ : CustomColors.fogColorNether;
    }
    
    private static Vec3 getFogColorEnd(final Vec3 p_getFogColorEnd_0_) {
        return (CustomColors.fogColorEnd == null) ? p_getFogColorEnd_0_ : CustomColors.fogColorEnd;
    }
    
    private static Vec3 getSkyColorEnd(final Vec3 p_getSkyColorEnd_0_) {
        return (CustomColors.skyColorEnd == null) ? p_getSkyColorEnd_0_ : CustomColors.skyColorEnd;
    }
    
    public static Vec3 getSkyColor(final Vec3 p_getSkyColor_0_, final IBlockAccess p_getSkyColor_1_, final double p_getSkyColor_2_, final double p_getSkyColor_4_, final double p_getSkyColor_6_) {
        if (CustomColors.skyColors == null) {
            return p_getSkyColor_0_;
        }
        final int i = CustomColors.skyColors.getColorSmooth(p_getSkyColor_1_, p_getSkyColor_2_, p_getSkyColor_4_, p_getSkyColor_6_, 3);
        final int j = i >> 16 & 0xFF;
        final int k = i >> 8 & 0xFF;
        final int l = i & 0xFF;
        float f = j / 255.0f;
        float f2 = k / 255.0f;
        float f3 = l / 255.0f;
        final float f4 = (float)p_getSkyColor_0_.xCoord / 0.5f;
        final float f5 = (float)p_getSkyColor_0_.yCoord / 0.66275f;
        final float f6 = (float)p_getSkyColor_0_.zCoord;
        f *= f4;
        f2 *= f5;
        f3 *= f6;
        final Vec3 vec3 = CustomColors.skyColorFader.getColor(f, f2, f3);
        return vec3;
    }
    
    private static Vec3 getFogColor(final Vec3 p_getFogColor_0_, final IBlockAccess p_getFogColor_1_, final double p_getFogColor_2_, final double p_getFogColor_4_, final double p_getFogColor_6_) {
        if (CustomColors.fogColors == null) {
            return p_getFogColor_0_;
        }
        final int i = CustomColors.fogColors.getColorSmooth(p_getFogColor_1_, p_getFogColor_2_, p_getFogColor_4_, p_getFogColor_6_, 3);
        final int j = i >> 16 & 0xFF;
        final int k = i >> 8 & 0xFF;
        final int l = i & 0xFF;
        float f = j / 255.0f;
        float f2 = k / 255.0f;
        float f3 = l / 255.0f;
        final float f4 = (float)p_getFogColor_0_.xCoord / 0.753f;
        final float f5 = (float)p_getFogColor_0_.yCoord / 0.8471f;
        final float f6 = (float)p_getFogColor_0_.zCoord;
        f *= f4;
        f2 *= f5;
        f3 *= f6;
        final Vec3 vec3 = CustomColors.fogColorFader.getColor(f, f2, f3);
        return vec3;
    }
    
    public static Vec3 getUnderwaterColor(final IBlockAccess p_getUnderwaterColor_0_, final double p_getUnderwaterColor_1_, final double p_getUnderwaterColor_3_, final double p_getUnderwaterColor_5_) {
        if (CustomColors.underwaterColors == null) {
            return null;
        }
        final int i = CustomColors.underwaterColors.getColorSmooth(p_getUnderwaterColor_0_, p_getUnderwaterColor_1_, p_getUnderwaterColor_3_, p_getUnderwaterColor_5_, 3);
        final int j = i >> 16 & 0xFF;
        final int k = i >> 8 & 0xFF;
        final int l = i & 0xFF;
        final float f = j / 255.0f;
        final float f2 = k / 255.0f;
        final float f3 = l / 255.0f;
        final Vec3 vec3 = CustomColors.underwaterColorFader.getColor(f, f2, f3);
        return vec3;
    }
    
    private static int getStemColorMultiplier(final Block p_getStemColorMultiplier_0_, final IBlockAccess p_getStemColorMultiplier_1_, final BlockPos p_getStemColorMultiplier_2_, final RenderEnv p_getStemColorMultiplier_3_) {
        CustomColormap customcolormap = CustomColors.stemColors;
        if (p_getStemColorMultiplier_0_ == Blocks.pumpkin_stem && CustomColors.stemPumpkinColors != null) {
            customcolormap = CustomColors.stemPumpkinColors;
        }
        if (p_getStemColorMultiplier_0_ == Blocks.melon_stem && CustomColors.stemMelonColors != null) {
            customcolormap = CustomColors.stemMelonColors;
        }
        if (customcolormap == null) {
            return -1;
        }
        final int i = p_getStemColorMultiplier_3_.getMetadata();
        return customcolormap.getColor(i);
    }
    
    public static boolean updateLightmap(final World p_updateLightmap_0_, final float p_updateLightmap_1_, final int[] p_updateLightmap_2_, final boolean p_updateLightmap_3_) {
        if (p_updateLightmap_0_ == null) {
            return false;
        }
        if (CustomColors.lightMapsColorsRgb == null) {
            return false;
        }
        final int i = p_updateLightmap_0_.provider.getDimensionId();
        final int j = i - CustomColors.lightmapMinDimensionId;
        if (j < 0 || j >= CustomColors.lightMapsColorsRgb.length) {
            return false;
        }
        final CustomColormap customcolormap = CustomColors.lightMapsColorsRgb[j];
        if (customcolormap == null) {
            return false;
        }
        final int k = customcolormap.getHeight();
        if (p_updateLightmap_3_ && k < 64) {
            return false;
        }
        final int l = customcolormap.getWidth();
        if (l < 16) {
            warn("Invalid lightmap width: " + l + " for dimension: " + i);
            CustomColors.lightMapsColorsRgb[j] = null;
            return false;
        }
        int i2 = 0;
        if (p_updateLightmap_3_) {
            i2 = l * 16 * 2;
        }
        float f = 1.1666666f * (p_updateLightmap_0_.getSunBrightness(1.0f) - 0.2f);
        if (p_updateLightmap_0_.getLastLightningBolt() > 0) {
            f = 1.0f;
        }
        f = Config.limitTo1(f);
        final float f2 = f * (l - 1);
        final float f3 = Config.limitTo1(p_updateLightmap_1_ + 0.5f) * (l - 1);
        final float f4 = Config.limitTo1(Config.getGameSettings().gammaSetting);
        final boolean flag = f4 > 1.0E-4f;
        final float[][] afloat = customcolormap.getColorsRgb();
        getLightMapColumn(afloat, f2, i2, l, CustomColors.sunRgbs);
        getLightMapColumn(afloat, f3, i2 + 16 * l, l, CustomColors.torchRgbs);
        final float[] afloat2 = new float[3];
        for (int j2 = 0; j2 < 16; ++j2) {
            for (int k2 = 0; k2 < 16; ++k2) {
                for (int l2 = 0; l2 < 3; ++l2) {
                    float f5 = Config.limitTo1(CustomColors.sunRgbs[j2][l2] + CustomColors.torchRgbs[k2][l2]);
                    if (flag) {
                        float f6 = 1.0f - f5;
                        f6 = 1.0f - f6 * f6 * f6 * f6;
                        f5 = f4 * f6 + (1.0f - f4) * f5;
                    }
                    afloat2[l2] = f5;
                }
                final int i3 = (int)(afloat2[0] * 255.0f);
                final int j3 = (int)(afloat2[1] * 255.0f);
                final int k3 = (int)(afloat2[2] * 255.0f);
                p_updateLightmap_2_[j2 * 16 + k2] = (0xFF000000 | i3 << 16 | j3 << 8 | k3);
            }
        }
        return true;
    }
    
    private static void getLightMapColumn(final float[][] p_getLightMapColumn_0_, final float p_getLightMapColumn_1_, final int p_getLightMapColumn_2_, final int p_getLightMapColumn_3_, final float[][] p_getLightMapColumn_4_) {
        final int i = (int)Math.floor(p_getLightMapColumn_1_);
        final int j = (int)Math.ceil(p_getLightMapColumn_1_);
        if (i == j) {
            for (int i2 = 0; i2 < 16; ++i2) {
                final float[] afloat3 = p_getLightMapColumn_0_[p_getLightMapColumn_2_ + i2 * p_getLightMapColumn_3_ + i];
                final float[] afloat4 = p_getLightMapColumn_4_[i2];
                for (int j2 = 0; j2 < 3; ++j2) {
                    afloat4[j2] = afloat3[j2];
                }
            }
        }
        else {
            final float f = 1.0f - (p_getLightMapColumn_1_ - i);
            final float f2 = 1.0f - (j - p_getLightMapColumn_1_);
            for (int k = 0; k < 16; ++k) {
                final float[] afloat5 = p_getLightMapColumn_0_[p_getLightMapColumn_2_ + k * p_getLightMapColumn_3_ + i];
                final float[] afloat6 = p_getLightMapColumn_0_[p_getLightMapColumn_2_ + k * p_getLightMapColumn_3_ + j];
                final float[] afloat7 = p_getLightMapColumn_4_[k];
                for (int l = 0; l < 3; ++l) {
                    afloat7[l] = afloat5[l] * f + afloat6[l] * f2;
                }
            }
        }
    }
    
    public static Vec3 getWorldFogColor(Vec3 p_getWorldFogColor_0_, final WorldClient p_getWorldFogColor_1_, final Entity p_getWorldFogColor_2_, final float p_getWorldFogColor_3_) {
        final int i = p_getWorldFogColor_1_.provider.getDimensionId();
        switch (i) {
            case -1: {
                p_getWorldFogColor_0_ = getFogColorNether(p_getWorldFogColor_0_);
                break;
            }
            case 0: {
                final Minecraft minecraft = Minecraft.getMinecraft();
                p_getWorldFogColor_0_ = getFogColor(p_getWorldFogColor_0_, minecraft.theWorld, p_getWorldFogColor_2_.posX, p_getWorldFogColor_2_.posY + 1.0, p_getWorldFogColor_2_.posZ);
                break;
            }
            case 1: {
                p_getWorldFogColor_0_ = getFogColorEnd(p_getWorldFogColor_0_);
                break;
            }
        }
        return p_getWorldFogColor_0_;
    }
    
    public static Vec3 getWorldSkyColor(Vec3 p_getWorldSkyColor_0_, final World p_getWorldSkyColor_1_, final Entity p_getWorldSkyColor_2_, final float p_getWorldSkyColor_3_) {
        final int i = p_getWorldSkyColor_1_.provider.getDimensionId();
        switch (i) {
            case 0: {
                final Minecraft minecraft = Minecraft.getMinecraft();
                p_getWorldSkyColor_0_ = getSkyColor(p_getWorldSkyColor_0_, minecraft.theWorld, p_getWorldSkyColor_2_.posX, p_getWorldSkyColor_2_.posY + 1.0, p_getWorldSkyColor_2_.posZ);
                break;
            }
            case 1: {
                p_getWorldSkyColor_0_ = getSkyColorEnd(p_getWorldSkyColor_0_);
                break;
            }
        }
        return p_getWorldSkyColor_0_;
    }
    
    private static int[] readSpawnEggColors(final Properties p_readSpawnEggColors_0_, final String p_readSpawnEggColors_1_, final String p_readSpawnEggColors_2_, final String p_readSpawnEggColors_3_) {
        final List<Integer> list = new ArrayList<Integer>();
        final Set set = p_readSpawnEggColors_0_.keySet();
        int i = 0;
        for (final Object s : set) {
            final String s2 = p_readSpawnEggColors_0_.getProperty((String)s);
            if (((String)s).startsWith(p_readSpawnEggColors_2_)) {
                final String s3 = StrUtils.removePrefix((String)s, p_readSpawnEggColors_2_);
                final int j = getEntityId(s3);
                final int k = parseColor(s2);
                if (j >= 0 && k >= 0) {
                    while (list.size() <= j) {
                        list.add(-1);
                    }
                    list.set(j, k);
                    ++i;
                }
                else {
                    warn("Invalid spawn egg color: " + s + " = " + s2);
                }
            }
        }
        if (i <= 0) {
            return null;
        }
        dbg(String.valueOf(p_readSpawnEggColors_3_) + " colors: " + i);
        final int[] aint = new int[list.size()];
        for (int l = 0; l < aint.length; ++l) {
            aint[l] = list.get(l);
        }
        return aint;
    }
    
    private static int getSpawnEggColor(final ItemMonsterPlacer p_getSpawnEggColor_0_, final ItemStack p_getSpawnEggColor_1_, final int p_getSpawnEggColor_2_, final int p_getSpawnEggColor_3_) {
        final int i = p_getSpawnEggColor_1_.getMetadata();
        final int[] aint = (p_getSpawnEggColor_2_ == 0) ? CustomColors.spawnEggPrimaryColors : CustomColors.spawnEggSecondaryColors;
        if (aint == null) {
            return p_getSpawnEggColor_3_;
        }
        if (i >= 0 && i < aint.length) {
            final int j = aint[i];
            return (j < 0) ? p_getSpawnEggColor_3_ : j;
        }
        return p_getSpawnEggColor_3_;
    }
    
    public static int getColorFromItemStack(final ItemStack p_getColorFromItemStack_0_, final int p_getColorFromItemStack_1_, final int p_getColorFromItemStack_2_) {
        if (p_getColorFromItemStack_0_ == null) {
            return p_getColorFromItemStack_2_;
        }
        final Item item = p_getColorFromItemStack_0_.getItem();
        return (item == null) ? p_getColorFromItemStack_2_ : ((item instanceof ItemMonsterPlacer) ? getSpawnEggColor((ItemMonsterPlacer)item, p_getColorFromItemStack_0_, p_getColorFromItemStack_1_, p_getColorFromItemStack_2_) : p_getColorFromItemStack_2_);
    }
    
    private static float[][] readDyeColors(final Properties p_readDyeColors_0_, final String p_readDyeColors_1_, final String p_readDyeColors_2_, final String p_readDyeColors_3_) {
        final EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
        final Map<String, EnumDyeColor> map = new HashMap<String, EnumDyeColor>();
        for (int i = 0; i < aenumdyecolor.length; ++i) {
            final EnumDyeColor enumdyecolor = aenumdyecolor[i];
            map.put(enumdyecolor.getName(), enumdyecolor);
        }
        final float[][] afloat1 = new float[aenumdyecolor.length][];
        int k = 0;
        for (final Object s : p_readDyeColors_0_.keySet()) {
            final String s2 = p_readDyeColors_0_.getProperty((String)s);
            if (((String)s).startsWith(p_readDyeColors_2_)) {
                String s3 = StrUtils.removePrefix((String)s, p_readDyeColors_2_);
                if (s3.equals("lightBlue")) {
                    s3 = "light_blue";
                }
                final EnumDyeColor enumdyecolor2 = map.get(s3);
                final int j = parseColor(s2);
                if (enumdyecolor2 != null && j >= 0) {
                    final float[] afloat2 = { (j >> 16 & 0xFF) / 255.0f, (j >> 8 & 0xFF) / 255.0f, (j & 0xFF) / 255.0f };
                    afloat1[enumdyecolor2.ordinal()] = afloat2;
                    ++k;
                }
                else {
                    warn("Invalid color: " + s + " = " + s2);
                }
            }
        }
        if (k <= 0) {
            return null;
        }
        dbg(String.valueOf(p_readDyeColors_3_) + " colors: " + k);
        return afloat1;
    }
    
    private static float[] getDyeColors(final EnumDyeColor p_getDyeColors_0_, final float[][] p_getDyeColors_1_, final float[] p_getDyeColors_2_) {
        if (p_getDyeColors_1_ == null) {
            return p_getDyeColors_2_;
        }
        if (p_getDyeColors_0_ == null) {
            return p_getDyeColors_2_;
        }
        final float[] afloat = p_getDyeColors_1_[p_getDyeColors_0_.ordinal()];
        return (afloat == null) ? p_getDyeColors_2_ : afloat;
    }
    
    public static float[] getWolfCollarColors(final EnumDyeColor p_getWolfCollarColors_0_, final float[] p_getWolfCollarColors_1_) {
        return getDyeColors(p_getWolfCollarColors_0_, CustomColors.wolfCollarColors, p_getWolfCollarColors_1_);
    }
    
    public static float[] getSheepColors(final EnumDyeColor p_getSheepColors_0_, final float[] p_getSheepColors_1_) {
        return getDyeColors(p_getSheepColors_0_, CustomColors.sheepColors, p_getSheepColors_1_);
    }
    
    private static int[] readTextColors(final Properties p_readTextColors_0_, final String p_readTextColors_1_, final String p_readTextColors_2_, final String p_readTextColors_3_) {
        final int[] aint = new int[32];
        Arrays.fill(aint, -1);
        int i = 0;
        for (final Object s : p_readTextColors_0_.keySet()) {
            final String s2 = p_readTextColors_0_.getProperty((String)s);
            if (((String)s).startsWith(p_readTextColors_2_)) {
                final String s3 = StrUtils.removePrefix((String)s, p_readTextColors_2_);
                final int j = Config.parseInt(s3, -1);
                final int k = parseColor(s2);
                if (j >= 0 && j < aint.length && k >= 0) {
                    aint[j] = k;
                    ++i;
                }
                else {
                    warn("Invalid color: " + s + " = " + s2);
                }
            }
        }
        if (i <= 0) {
            return null;
        }
        dbg(String.valueOf(p_readTextColors_3_) + " colors: " + i);
        return aint;
    }
    
    public static int getTextColor(final int p_getTextColor_0_, final int p_getTextColor_1_) {
        if (CustomColors.textColors == null) {
            return p_getTextColor_1_;
        }
        if (p_getTextColor_0_ >= 0 && p_getTextColor_0_ < CustomColors.textColors.length) {
            final int i = CustomColors.textColors[p_getTextColor_0_];
            return (i < 0) ? p_getTextColor_1_ : i;
        }
        return p_getTextColor_1_;
    }
    
    private static int[] readMapColors(final Properties p_readMapColors_0_, final String p_readMapColors_1_, final String p_readMapColors_2_, final String p_readMapColors_3_) {
        final int[] aint = new int[MapColor.mapColorArray.length];
        Arrays.fill(aint, -1);
        int i = 0;
        for (final Object s : p_readMapColors_0_.keySet()) {
            final String s2 = p_readMapColors_0_.getProperty((String)s);
            if (((String)s).startsWith(p_readMapColors_2_)) {
                final String s3 = StrUtils.removePrefix((String)s, p_readMapColors_2_);
                final int j = getMapColorIndex(s3);
                final int k = parseColor(s2);
                if (j >= 0 && j < aint.length && k >= 0) {
                    aint[j] = k;
                    ++i;
                }
                else {
                    warn("Invalid color: " + s + " = " + s2);
                }
            }
        }
        if (i <= 0) {
            return null;
        }
        dbg(String.valueOf(p_readMapColors_3_) + " colors: " + i);
        return aint;
    }
    
    private static int[] readPotionColors(final Properties p_readPotionColors_0_, final String p_readPotionColors_1_, final String p_readPotionColors_2_, final String p_readPotionColors_3_) {
        final int[] aint = new int[Potion.potionTypes.length];
        Arrays.fill(aint, -1);
        int i = 0;
        for (final Object s : p_readPotionColors_0_.keySet()) {
            final String s2 = p_readPotionColors_0_.getProperty((String)s);
            if (((String)s).startsWith(p_readPotionColors_2_)) {
                final int j = getPotionId((String)s);
                final int k = parseColor(s2);
                if (j >= 0 && j < aint.length && k >= 0) {
                    aint[j] = k;
                    ++i;
                }
                else {
                    warn("Invalid color: " + s + " = " + s2);
                }
            }
        }
        if (i <= 0) {
            return null;
        }
        dbg(String.valueOf(p_readPotionColors_3_) + " colors: " + i);
        return aint;
    }
    
    private static int getPotionId(final String p_getPotionId_0_) {
        if (p_getPotionId_0_.equals("potion.water")) {
            return 0;
        }
        final Potion[] apotion = Potion.potionTypes;
        for (int i = 0; i < apotion.length; ++i) {
            final Potion potion = apotion[i];
            if (potion != null && potion.getName().equals(p_getPotionId_0_)) {
                return potion.getId();
            }
        }
        return -1;
    }
    
    public static int getPotionColor(final int p_getPotionColor_0_, final int p_getPotionColor_1_) {
        if (CustomColors.potionColors == null) {
            return p_getPotionColor_1_;
        }
        if (p_getPotionColor_0_ >= 0 && p_getPotionColor_0_ < CustomColors.potionColors.length) {
            final int i = CustomColors.potionColors[p_getPotionColor_0_];
            return (i < 0) ? p_getPotionColor_1_ : i;
        }
        return p_getPotionColor_1_;
    }
    
    private static int getMapColorIndex(final String p_getMapColorIndex_0_) {
        return (p_getMapColorIndex_0_ == null) ? -1 : (p_getMapColorIndex_0_.equals("air") ? MapColor.airColor.colorIndex : (p_getMapColorIndex_0_.equals("grass") ? MapColor.grassColor.colorIndex : (p_getMapColorIndex_0_.equals("sand") ? MapColor.sandColor.colorIndex : (p_getMapColorIndex_0_.equals("cloth") ? MapColor.clothColor.colorIndex : (p_getMapColorIndex_0_.equals("tnt") ? MapColor.tntColor.colorIndex : (p_getMapColorIndex_0_.equals("ice") ? MapColor.iceColor.colorIndex : (p_getMapColorIndex_0_.equals("iron") ? MapColor.ironColor.colorIndex : (p_getMapColorIndex_0_.equals("foliage") ? MapColor.foliageColor.colorIndex : (p_getMapColorIndex_0_.equals("clay") ? MapColor.clayColor.colorIndex : (p_getMapColorIndex_0_.equals("dirt") ? MapColor.dirtColor.colorIndex : (p_getMapColorIndex_0_.equals("stone") ? MapColor.stoneColor.colorIndex : (p_getMapColorIndex_0_.equals("water") ? MapColor.waterColor.colorIndex : (p_getMapColorIndex_0_.equals("wood") ? MapColor.woodColor.colorIndex : (p_getMapColorIndex_0_.equals("quartz") ? MapColor.quartzColor.colorIndex : (p_getMapColorIndex_0_.equals("gold") ? MapColor.goldColor.colorIndex : (p_getMapColorIndex_0_.equals("diamond") ? MapColor.diamondColor.colorIndex : (p_getMapColorIndex_0_.equals("lapis") ? MapColor.lapisColor.colorIndex : (p_getMapColorIndex_0_.equals("emerald") ? MapColor.emeraldColor.colorIndex : (p_getMapColorIndex_0_.equals("podzol") ? MapColor.obsidianColor.colorIndex : (p_getMapColorIndex_0_.equals("netherrack") ? MapColor.netherrackColor.colorIndex : ((!p_getMapColorIndex_0_.equals("snow") && !p_getMapColorIndex_0_.equals("white")) ? ((!p_getMapColorIndex_0_.equals("adobe") && !p_getMapColorIndex_0_.equals("orange")) ? (p_getMapColorIndex_0_.equals("magenta") ? MapColor.magentaColor.colorIndex : ((!p_getMapColorIndex_0_.equals("light_blue") && !p_getMapColorIndex_0_.equals("lightBlue")) ? (p_getMapColorIndex_0_.equals("yellow") ? MapColor.yellowColor.colorIndex : (p_getMapColorIndex_0_.equals("lime") ? MapColor.limeColor.colorIndex : (p_getMapColorIndex_0_.equals("pink") ? MapColor.pinkColor.colorIndex : (p_getMapColorIndex_0_.equals("gray") ? MapColor.grayColor.colorIndex : (p_getMapColorIndex_0_.equals("silver") ? MapColor.silverColor.colorIndex : (p_getMapColorIndex_0_.equals("cyan") ? MapColor.cyanColor.colorIndex : (p_getMapColorIndex_0_.equals("purple") ? MapColor.purpleColor.colorIndex : (p_getMapColorIndex_0_.equals("blue") ? MapColor.blueColor.colorIndex : (p_getMapColorIndex_0_.equals("brown") ? MapColor.brownColor.colorIndex : (p_getMapColorIndex_0_.equals("green") ? MapColor.greenColor.colorIndex : (p_getMapColorIndex_0_.equals("red") ? MapColor.redColor.colorIndex : (p_getMapColorIndex_0_.equals("black") ? MapColor.blackColor.colorIndex : -1)))))))))))) : MapColor.lightBlueColor.colorIndex)) : MapColor.adobeColor.colorIndex) : MapColor.snowColor.colorIndex)))))))))))))))))))));
    }
    
    private static int[] getMapColors() {
        final MapColor[] amapcolor = MapColor.mapColorArray;
        final int[] aint = new int[amapcolor.length];
        Arrays.fill(aint, -1);
        for (int i = 0; i < amapcolor.length && i < aint.length; ++i) {
            final MapColor mapcolor = amapcolor[i];
            if (mapcolor != null) {
                aint[i] = mapcolor.colorValue;
            }
        }
        return aint;
    }
    
    private static void setMapColors(final int[] p_setMapColors_0_) {
        if (p_setMapColors_0_ != null) {
            final MapColor[] amapcolor = MapColor.mapColorArray;
            boolean flag = false;
            for (int i = 0; i < amapcolor.length && i < p_setMapColors_0_.length; ++i) {
                final MapColor mapcolor = amapcolor[i];
                if (mapcolor != null) {
                    final int j = p_setMapColors_0_[i];
                    if (j >= 0 && mapcolor.colorValue != j) {
                        mapcolor.colorValue = j;
                        flag = true;
                    }
                }
            }
            if (flag) {
                Minecraft.getMinecraft().getTextureManager().reloadBannerTextures();
            }
        }
    }
    
    private static int getEntityId(final String p_getEntityId_0_) {
        if (p_getEntityId_0_ == null) {
            return -1;
        }
        final int i = EntityList.getIDFromString(p_getEntityId_0_);
        if (i < 0) {
            return -1;
        }
        final String s = EntityList.getStringFromID(i);
        return Config.equals(p_getEntityId_0_, s) ? i : -1;
    }
    
    private static void dbg(final String p_dbg_0_) {
        Config.dbg("CustomColors: " + p_dbg_0_);
    }
    
    private static void warn(final String p_warn_0_) {
        Config.warn("CustomColors: " + p_warn_0_);
    }
    
    public static int getExpBarTextColor(final int p_getExpBarTextColor_0_) {
        return (CustomColors.expBarTextColor < 0) ? p_getExpBarTextColor_0_ : CustomColors.expBarTextColor;
    }
    
    public static int getBossTextColor(final int p_getBossTextColor_0_) {
        return (CustomColors.bossTextColor < 0) ? p_getBossTextColor_0_ : CustomColors.bossTextColor;
    }
    
    public static int getSignTextColor(final int p_getSignTextColor_0_) {
        return (CustomColors.signTextColor < 0) ? p_getSignTextColor_0_ : CustomColors.signTextColor;
    }
    
    public interface IColorizer
    {
        int getColor(final IBlockAccess p0, final BlockPos p1);
        
        boolean isColorConstant();
    }
}
